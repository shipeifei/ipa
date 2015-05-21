package com.ipassistat.ipa.ui.activity;

import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.color;
import com.ipassistat.ipa.R.drawable;
import com.ipassistat.ipa.R.id;
import com.ipassistat.ipa.R.layout;
import com.ipassistat.ipa.R.string;
import com.ipassistat.ipa.bean.response.BaseResponse;
import com.ipassistat.ipa.bean.response.PostOperationTagsResponse;
import com.ipassistat.ipa.bean.response.entity.SisterGroupTagVo;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.ui.fragment.SisterGroupFragment;
import com.ipassistat.ipa.util.LogUtil;
import com.ipassistat.ipa.util.ProgressHub;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.util.UpdateUtil;
import com.ipassistat.ipa.util.ViewUtil;
import com.ipassistat.ipa.util.UpdateUtil.ImageUpdateListener;
import com.ipassistat.ipa.view.AlertDialog;
import com.ipassistat.ipa.view.PhotoPickerView;
import com.ipassistat.ipa.view.WrapLinearLayout;
import com.umeng.analytics.MobclickAgent;

/**
 * 姐妹圈-发帖
 * 
 * @author LiuYuHang
 *
 */
public class SisterGroupPostEditActivity extends BaseActivity implements OnClickListener {
	
	
	private PhotoPickerView mPhotoPickerView;
	private WrapLinearLayout mLablesViewGroup;

	private boolean sendPostEnable = false;// 是否允许发帖

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_post);

		//设置标题栏标题
		setTitleText(getString(string.sistergroup_post_send_title));

		findListeners(new int[] { R.id.left_imgv, id.right_textview }, this);

		initWidgets();
		disableSlideClose();
		
		
		MobclickAgent.onEvent(getApplicationContext(), "1042");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("1042"); // 统计页面
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("1042"); // 统计页面
	}

	/***
	 * 初始换页面控件
	 * @author 时培飞
	 * Create at 2015-4-20 下午1:29:50
	 */
	private void initWidgets() {
		mLablesViewGroup = (WrapLinearLayout) findViewById(id.group_tags);
		mPhotoPickerView = (PhotoPickerView) findViewById(id.photo_picker_view);
		
		//发布帖子按钮
		TextView postBtn = (TextView) findViewById(id.right_textview);
		postBtn.setText(string.issue);
		postBtn.setVisibility(View.VISIBLE);
       
		//请求发帖标签
		//new SisterGroupModule(this).postOperationPullTags(this);
	}

	@SuppressWarnings("deprecation")
	private void initLabelsView(SisterGroupTagVo[] tags) {
		if (tags == null) return;
		mLablesViewGroup.removeAllViews();

		for (int i = 0; i < tags.length; i++) {
			View contentView = View.inflate(getApplicationContext(), layout.view_post_tags, null);
			TextView tagView = (TextView) contentView.findViewById(id.textview_tag);

			tagView.setText(tags[i].label_name);
			tagView.setTextColor(getResources().getColor(color.black));
			tagView.setBackgroundDrawable(new BitmapDrawable());
			tagView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					MobclickAgent.onEvent(getApplicationContext(), "1122");

					int childCount = mLablesViewGroup.getChildCount();

					for (int j = 0; j < childCount; j++) {
						View childView = mLablesViewGroup.getChildAt(j);
						TextView tagView = (TextView) childView.findViewById(id.textview_tag);
						tagView.setBackgroundDrawable(new BitmapDrawable());
						tagView.setTextColor(getResources().getColor(color.black));
						tagView.setTag(false);
					}
					v.setTag(true);
					((TextView) v).setTextColor(getResources().getColor(color.white));
					v.setBackgroundResource(drawable.sistergroup_tag_background);
					// v.setBackgroundResource(color.global_main_text_color);
				}
			});
			mLablesViewGroup.addView(contentView);

			if (i == 0) {
				tagView.setTag(true);
				tagView.setTextColor(getResources().getColor(color.white));
				tagView.setBackgroundResource(drawable.sistergroup_tag_background);
				// v.setBackgroundResource(color.global_main_text_color);
			}
		}
	}

	/**
	 * 获取 当前选择的标签
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月26日
	 *
	 * @return
	 */
	private String getSelectTag() {
		String tag = null;
		int childCount = mLablesViewGroup.getChildCount();

		for (int i = 0; i < childCount; i++) {
			View childView = mLablesViewGroup.getChildAt(i);
			TextView tagView = (TextView) childView.findViewById(id.textview_tag);

			if ((Boolean) tagView.getTag()) {
				tag = tagView.getText().toString();
				break;
			}
		}
		return tag;
	}

	/**
	 * 发表帖子
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月23日
	 *
	 */
	private void sendPost() {
		// 点击发帖子之后，先上传照片
		UpdateUtil.getInstance().updateImages(mPhotoPickerView.getBitmapPath(), new ImageUpdateListener() {

			@Override
			public void onUpdateComplate(List<String> bitmapUrls) {
				EditText titleView = (EditText) findViewById(id.editview_title);
				EditText contentView = (EditText) findViewById(id.editview_content);
				String postTag = getSelectTag();
				String title = titleView.getText().toString();
				String content = contentView.getText().toString();
				String productId = mPhotoPickerView.getProduct() == null ? null : mPhotoPickerView.getProduct().getId();

				ProgressHub.getInstance(SisterGroupPostEditActivity.this).dismiss();
				//new SisterGroupModule(SisterGroupPostEditActivity.this).postOperationSend(getApplicationContext(), postTag, title, content, productId, UpdateUtil.formatImageString(bitmapUrls));
			}

			@Override
			public void onError(String error) {
				LogUtil.outLogDetail(error);
				ToastUtil.showToast(getApplicationContext(), error);
			}

			@Override
			public void onProgress(int max, int position) {
				LogUtil.outLogDetail("当前第:" + position);
			}

			@Override
			public void onBegin() {
				ProgressHub.getInstance(SisterGroupPostEditActivity.this).showWithNoTouch(getString(string.uploadingimages));
			}
		});
	}

	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		super.onMessageSucessCalledBack(url, object);
		
		//发帖回调函数
		if (url.equals(ConfigInfo.API_POST_OPERATION_SEND)) {
			BaseResponse response = (BaseResponse) object;
			if (response.getResultCode() == ConfigInfo.RESULT_OK) {
				setResult(SisterGroupFragment.RESULT_CODE_SEND_SUCCESS);
				finish();
			}
			sendPostEnable = true;
			return ;
		}
		
		if (url.equals(ConfigInfo.API_POST_OPERATION_SEND_TAGS)) {
			PostOperationTagsResponse response = (PostOperationTagsResponse) object;
			initLabelsView(response.postlabel);
			sendPostEnable = true;
			return ;
		}
	}

	@Override
	public void onNoNet() {
		super.onNoNet();
		sendPostEnable = true;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  
			AlertDialog dialog = new AlertDialog(this);
			dialog.setTitle(getString(string.gameover));
			dialog.setPositiveButton(getString(string.menu_ok), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			dialog.setNegativeButton(getString(string.menu_cancel), null);
			dialog.show();
            return true;  
        } else  
            return super.onKeyDown(keyCode, event);  
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		
		case R.id.left_imgv://放弃编辑
			MobclickAgent.onEvent(getApplicationContext(), "1126");
	
			AlertDialog dialog = new AlertDialog(this);
			dialog.setTitle(getString(string.gameover));
			dialog.setPositiveButton(getString(string.menu_ok), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			dialog.setNegativeButton(getString(string.menu_cancel), null);
			dialog.show();
			break;
		case id.right_textview://提交发帖
			if (!sendPostEnable) return;

			EditText titleView = (EditText) findViewById(id.editview_title);
			EditText contentView = (EditText) findViewById(id.editview_content);
			
			MobclickAgent.onEvent(getApplicationContext(), "1125");
			
			
			if (ViewUtil.isEdittextEmpty(titleView, contentView)) {
				ToastUtil.showToast(getApplicationContext(), getString(string.sistergroup_post_send_empty));
			} else if (titleView.getText().toString().length() < 3) {
				ToastUtil.showToast(getApplicationContext(), getString(string.sistergroup_post_send_title_short));
			} else if (contentView.getText().toString().length() < 10) {
				ToastUtil.showToast(getApplicationContext(), getString(string.sistergroup_post_send_content_short));
			} else {
				sendPost();
				sendPostEnable = false;
			}

			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mPhotoPickerView.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
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
