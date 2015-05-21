package com.ipassistat.ipa.ui.activity;

import java.io.File;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.hp.hpl.sparta.xpath.ThisNodeTest;
import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.id;
import com.ipassistat.ipa.bean.local.CityLocalEntity;
import com.ipassistat.ipa.bean.request.UserInfoRequest;
import com.ipassistat.ipa.bean.response.UpdateUserInfoResponse;
import com.ipassistat.ipa.bean.response.UserInfoResponse;
import com.ipassistat.ipa.bean.response.entity.BeautyAddress;
import com.ipassistat.ipa.bean.response.entity.MemberInfo;
import com.ipassistat.ipa.business.UserModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.constant.Constant;
import com.ipassistat.ipa.constant.IntentFlag;
import com.ipassistat.ipa.constant.RequestCodeConstant;
import com.ipassistat.ipa.util.AreaUtil;
import com.ipassistat.ipa.util.GlobalUtil;
import com.ipassistat.ipa.util.LogUtil;
import com.ipassistat.ipa.util.PhotoPickerHandler;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.util.eventbus.MessageEvent;
import com.ipassistat.ipa.view.ActionSheet;
import com.ipassistat.ipa.view.AlertDialog;
import com.ipassistat.ipa.view.CircularImageView;
import com.ipassistat.ipa.view.DateSelectView;
import com.ipassistat.ipa.view.TitleBar;
import com.ipassistat.ipa.view.ActionSheet.ActionSheetListener;
import com.ipassistat.ipa.view.DateSelectView.OnDateSelectListener;
import com.ipassistat.ipa.view.TitleBar.TitleBarButton;
import com.umeng.analytics.MobclickAgent;


/**
 * 个人资料页面
 * 
 * @author maoyn
 * @update :任恒
 * 
 */
public class PersonalInfoActivity extends BaseActivity implements OnClickListener, ActionSheetListener, OnDateSelectListener {
	// 请求服务器相关变量
	private UserModule mModel;
	private UserInfoResponse mResponseGetData;
	private BeautyAddress mAddress;

	// 布局相关控件
	private TitleBar mTitlebar;
	private CircularImageView mPhoto;
	private TextView mAddrsContent, mCityContent, mSexContent, mBirthdayContent, mNickNameContent;
	private FrameLayout mFrameDate; // 存放日期的group

	// 其他相关变量
	private ActionSheet show;
	private Activity mActivity;
	private String mSexId = ""; // 用户选择的数据
	private String mAvatar = "";
	private String mAvatarOld;
	private String mNickname;
	/** 生日 */
	private String mBirthdayStr = "";
	/** 用户头像 */
	private String mUserPhoto = "";
	/** 地区 */
	private String mCityStr = ""; // 城市名称
	private String mCityCode = ""; // 城市id

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_info);

		// 注册EventBus
		//EventBus.getDefault().register(this);
		registerEventBus();

		PhotoPickerHandler.deleteAllCameraCache();
		mActivity = this;
		initWidgets();
		registerListeners();
		showAvatarFromLocal();
		setAddressFromLocal();
		mModel.postUserInfo(mActivity); // 从网络获取个人资料数据
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		setAddressFromLocal();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("1008"); // 统计页面
		MobclickAgent.onResume(this);

	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("1008"); // 保证 onPageEnd 在onPause 之前调用,因为
		MobclickAgent.onPause(this);
	}

	public void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 接触EventBus
		//EventBus.getDefault().unregister(this);
	}

	/**
	 * 初始化试图控件 时培飞 createAt:2015-01-13
	 */
	private void initWidgets() {

		// 标题栏
		mTitlebar = (TitleBar) findViewById(R.id.titlebar);
		mTitlebar.setTitleText(getResources().getString(R.string.personal_info));
		// 昵称
		mNickNameContent = (TextView) findViewById(id.nickname);
		// 性别
		mSexContent = (TextView) findViewById(R.id.sex);
		// 生日
		mBirthdayContent = (TextView) findViewById(R.id.birthday_type);
		// 城市
		mCityContent = (TextView) findViewById(R.id.city);
		// 收货地址
		mAddrsContent = (TextView) findViewById(R.id.address);
		mFrameDate = (FrameLayout) findViewById(R.id.frame_date);
		//mPhoto = (CircularImageView) findViewById(R.id.info_photo);
		mModel = new UserModule(this);
		mResponseGetData = new UserInfoResponse();
		// 生日选择控件
		initDateFrame();
	}

	private void initDateFrame() {
		mFrameDate = (FrameLayout) findViewById(R.id.frame_date);
		DateSelectView dateSelectView = new DateSelectView(this, this);
		mFrameDate.addView(dateSelectView);
		mFrameDate.setVisibility(View.GONE);
	}

	/**
	 * 注册监听器 时培飞 createAt:2015-01-13
	 */
	private void registerListeners() {

		// 头部返回
		mTitlebar.setButtonClickListener(TitleBarButton.leftImgv, new OnClickListener() {
			@Override
			public void onClick(View v) {
				back();
			}
		});

		findListeners(this,  id.nicktitle, R.id.info_sex, R.id.info_birthday, R.id.info_city, R.id.info_addrs);
	}

	

	private void setAddressFromLocal() {
		// 读取本地地址信息
		mAddress = mModel.getUserDefaultAddres(mActivity);
		if (mAddress != null) {
			mAddrsContent.setText(mAddress.getProvinces() + mAddress.getStreet());
		} else {
			mAddrsContent.setText(R.string.please_choose_address);
		}
	}

	/**
	 * 向服务器提交修改个人资料
	 */
	private void postDatas() {

		if (TextUtils.isEmpty(mAvatar)) {
			mAvatar = mAvatarOld;
		}
		if (mAddress == null || mAddress.getStreet() == null || "".equals(mAddress.getStreet())) {
			mAddress = null;
		}

		mModel.updateUserInfo(mActivity, mSexId, mBirthdayStr, mCityCode, mAddress, mAvatar, mNickname);
	}

	private UserInfoRequest getUserInfo() {
		UserInfoRequest info = new UserInfoRequest();

		if (!"".equals(mSexId)) {
			info.setSex("" + mSexId);
		} else {
			info.setSex("");
		}
		if (mAddress != null) {
			info.setAdress(mAddress);
		}
		info.setAvatar(mAvatar);
		info.setNickname(mNickNameContent.getText().toString());
		info.setArea_code(mCityCode);
		info.setBirthday(mBirthdayStr);

		return info;
	}

	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		if (isFinishing()) {
			return;
		}
		super.onMessageSucessCalledBack(url, object);
		if (ConfigInfo.API_USERINFO_GET.equals(url)) { // 查看个人资料
			mResponseGetData = (UserInfoResponse) object;
			if (mResponseGetData.getResultCode() == ConfigInfo.RESULT_OK) {
				fillDatas();
			} else {
				ToastUtil.showToast(mActivity, getString(R.string.global_message_server_error));
			}
		} else if (ConfigInfo.API_USERINFO_UPDATA.equals(url)) { // 修改个人资料

			UpdateUserInfoResponse mResponse_updateData = (UpdateUserInfoResponse) object;
			if (mResponse_updateData.getResultCode() == ConfigInfo.RESULT_OK) {
				mModel.saveDatas(mActivity, mSexId, mBirthdayStr, mCityCode, mUserPhoto, mNickname, mResponse_updateData.getUserInfo().getCentury());
			}
			finish();
			mResponse_updateData = null;
		}
	}

	@Override
	public void onMessageFailedCalledBack(String url, Object object) {
		super.onMessageFailedCalledBack(url, object);
		if (isFinishing()) {
			return;
		}
		if (ConfigInfo.API_USERINFO_UPDATA.equals(url)) {
			finish();

		}
	}

	/**
	 * 通过服务器返回的数据改变界面
	 */
	private void fillDatas() {
		mNickname = mResponseGetData.getNickname();
		mNickNameContent.setText(mNickname);
		mAvatarOld = mResponseGetData.getAvatar();
		mBirthdayStr = mResponseGetData.getBirthday();
		mCityCode = mResponseGetData.getArea_code();
		mCityStr = AreaUtil.getCityNameByCode(this, mCityCode);

		// 性别判断
		if (!TextUtils.isEmpty(mResponseGetData.getSex())) {
			mSexId = mResponseGetData.getSex();
			if (mSexId.equals(Constant.PERSON_MAN)) {
				mSexContent.setText(getString(R.string.man));
			} else if (mSexId.equals(Constant.PERSON_WOMAN)) {
				mSexContent.setText(getString(R.string.women));
			}
		}

		mBirthdayContent.setText(mBirthdayStr);
		mCityContent.setText(mCityStr);

		// 设置地址
		if (TextUtils.isEmpty(mResponseGetData.getAdress().getId())) {
			mAddrsContent.setText(R.string.please_choose_address);
		} else {
			mAddrsContent.setText(mResponseGetData.getAdress().getProvinces() + mResponseGetData.getAdress().getStreet());
		}

		// 设置头像
		if (!TextUtils.isEmpty(mResponseGetData.getAvatar())) {
			new AQuery(mPhoto).image(mResponseGetData.getAvatar(), true, true);
		} else {
			showAvatarFromLocal();
		}
	}

	// 从本地缓存中读取头像
	private void showAvatarFromLocal() {
		MemberInfo info = UserModule.getMemberInfo(mActivity);
		if (!TextUtils.isEmpty(info.getPhoto())) {
			new AQuery(mPhoto).image(info.getPhoto(), true, true);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			back();
			return true;
		} else
			return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
	/*	case R.id.info_photo: // 头像
			MobclickAgent.onEvent(mActivity, "1026");
			showPopUpWindow();
			break;*/

		// 修改昵称使用EventBus进行消息传递
		case id.nicktitle:
			intent.putExtra(NickNameEditActivity.TITLE_TEXT, getString(R.string.editnick));
			intent.putExtra(NickNameEditActivity.TITLE_MODEL, getUserInfo());
			intent.putExtra(NickNameEditActivity.DEFAULT_TEXT, mNickNameContent.getText().toString());
			intent.setClass(this, NickNameEditActivity.class);
			startActivityForResult(intent, NickNameEditActivity.RESULT_TEXT_CODE);
			break;

		case R.id.info_sex: // 性别
			MobclickAgent.onEvent(mActivity, "1027");
			intent.putExtra(ConfigInfo.PERSONALINFOWHICH, R.array.sex);
			intent.putExtra(ConfigInfo.PERSONALINFOCHOOSE, mSexId);
			intent.putExtra(ConfigInfo.PERSONALINFOORGIN, ConfigInfo.SEX);// 判断选中项的基数
			intent.putExtra(ConfigInfo.PERSONALINFOTITLE, getString(R.string.editsex));
			intent.putExtra(ConfigInfo.PERSONALINFOFLAG, "info_sex");
			intent.setClass(this, PersonalInfoChoActivity.class);
			startActivityForResult(intent, RequestCodeConstant.CHOOSESEX);
			break;
		case R.id.info_birthday: // 选择生日
			mFrameDate.setVisibility(View.VISIBLE);
			break;
		case R.id.info_city: // 城市选择
			intent.setClass(this, CityChoiceActivity.class);
			startActivityForResult(intent, RequestCodeConstant.CHOOSCITY);
			break;
		case R.id.info_addrs: // 收货地址
			MobclickAgent.onEvent(mActivity, "1030");
			intent.setClass(mActivity, AddressReceiveActivity.class);
			intent.putExtra(IntentFlag.ADDRESS_SELECT_REQUEST_KEY, IntentFlag.ADDRESS_SELECT_REQUEST_VALUE);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	/**
	 * 在主线程中处理接收EventBus发送的消息
	 * 
	 * @author 时培飞 Create at 2015-4-8 上午11:26:00
	 */
	public void onEventMainThread(MessageEvent event) {

		// 昵称修改
		if (event.getKey() == "NickNameEditActivity") {

			LogUtil.outLogDetail(getClass().getName(), event.getKey().toString());
			mNickname = event.getMsg().toString();
			mNickNameContent.setText(mNickname);
			return;
		}

		// 地区选择
		if (event.getKey() == "CityChoiceActivity") {
			CityLocalEntity module = (CityLocalEntity) event.getMsg();
			mCityCode = module.getCityID();
			mCityStr = module.getCityName();
			mCityContent.setText(mCityStr);
			return;

		}

	}

	/**
	 * 个人头像拍照调用方法
	 */
	public void showPopUpWindow() {
		show = ActionSheet.createBuilder(this, getSupportFragmentManager()).setCancelableOnTouchOutside(true).setPanelLayout(R.layout.item_popupwindow_photo).setListener(this).show();
	}

	/*
	 * set the listeners in the popWindow
	 */
	@Override
	public void onSetPanelListener(LinearLayout panel) {
		Button button1 = (Button) panel.findViewById(R.id.button1);
		Button button2 = (Button) panel.findViewById(R.id.button2);
		Button button3 = (Button) panel.findViewById(R.id.button3);
		// 拍照
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				// 是否存在sd卡
				if (GlobalUtil.isExistSDCard()) {
					Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "hmlphoto.jpg"));
					// 指定照片保存路径（SD卡），hmlphoto.jpg为一个临时文件，每次拍照后这个图片都会被替换
					intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				}
				startActivityForResult(intent, RequestCodeConstant.CAMERA);
				show.dismiss(); // 然弹出框消失，否则，会报错
			}
		});
		// 选择系统相册
		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent picture = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(picture, RequestCodeConstant.PICTURE);
				show.dismiss(); // 然弹出框消失，否则，会报错
			}
		});
		// 取消
		button3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				show.dismiss();
			}
		});
	}

	@Override
	public void onDismiss(ActionSheet actionSheet, boolean isCancle) {
		// Toast.makeText(getApplicationContext(),
		// "dismissed isCancle = " + isCancle, 0).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 针对手机拍照做单做处理
		if (resultCode == Activity.RESULT_OK && requestCode == RequestCodeConstant.CAMERA && data == null) {
			Intent intent = new Intent();
			intent.putExtra("from", RequestCodeConstant.CAMERA);
			intent.setClass(this, PhotoOperateActivity.class);
			startActivityForResult(intent, RequestCodeConstant.PHOTO_OPERATE);
		}
		if (resultCode == Activity.RESULT_OK && null != data) {
			int choice = data.getIntExtra("choice", 0);

			String[] stringArray;
			switch (requestCode) {

			case RequestCodeConstant.CHOOSESEX:// 性别
				stringArray = getResources().getStringArray(R.array.sex);
				mSexContent.setText(stringArray[choice]);
				if (stringArray[choice].equals(getString(R.string.man))) {
				
					mSexId = Constant.PERSON_MAN;
				} else {
					
					mSexId = Constant.PERSON_WOMAN;
				}
				break;

			case RequestCodeConstant.CHOOSEADDR:// 收货地址
				mAddress = (BeautyAddress) data.getSerializableExtra(IntentFlag.ADDRESS_SELECT_RESULT);
				mAddrsContent.setText(mAddress.getProvinces() + mAddress.getStreet());
				break;
			case RequestCodeConstant.CAMERA:// 调用系统相机
				data.putExtra("from", RequestCodeConstant.CAMERA);
				data.setClass(this, PhotoOperateActivity.class);
				startActivityForResult(data, RequestCodeConstant.PHOTO_OPERATE);
				break;
			case RequestCodeConstant.PICTURE:
				data.putExtra("from", RequestCodeConstant.PICTURE);
				data.setClass(this, PhotoOperateActivity.class);
				startActivityForResult(data, RequestCodeConstant.PHOTO_OPERATE);
				break;
			case RequestCodeConstant.PHOTO_OPERATE:
				mAvatar = data.getStringExtra(IntentFlag.PHOTO_URL);
				new AQuery(mPhoto).image(mAvatar, true, true);
				break;
			default:
				break;
			}
		}
		if (requestCode == PhotoPickerHandler.RESULT_PHOTO_GRAPH) {
			Intent intent = new Intent();
			intent.setClass(this, PhotoOperateActivity.class);
			intent.putExtra("from", RequestCodeConstant.CAMERA);
			LogUtil.outLogDetail("----------调用拍照完成跳转到 处理--------------");
			startActivityForResult(intent, RequestCodeConstant.PHOTO_OPERATE);
		}
	}

	@Override
	public void getDateByConfirm(String YearDate, String MonthDate, String DayDate, Boolean isClosed) {
		mBirthdayStr = YearDate + "-" + MonthDate + "-" + DayDate;
		mBirthdayContent.setText(mBirthdayStr);
		mFrameDate.setVisibility(View.GONE);

	}

	@Override
	public void cancel() {
		mFrameDate.setVisibility(View.GONE);
	}

	private void back() {
		if (TextUtils.isEmpty(mBirthdayStr)) {
			AlertDialog dialog = new AlertDialog(this);
			dialog.setTitle(getString(R.string.messagetitle));
			dialog.setMessage(getString(R.string.messagecontent));
			dialog.setNegativeButton(getString(R.string.messagecancel), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					postDatas();
				}
			});

			dialog.setPositiveButton(getString(R.string.messageok), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});

			dialog.show();
		} else {
			postDatas();
		}
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void bindEvents() {
		// TODO Auto-generated method stub
		
	}

}
