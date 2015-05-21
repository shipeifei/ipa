package com.ipassistat.ipa.ui.activity;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.bean.response.BaseResponse;
import com.ipassistat.ipa.business.GoodsModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.constant.IntentKey;
import com.ipassistat.ipa.util.BitmapOptionsFactory;
import com.ipassistat.ipa.util.ProgressHub;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.util.UpdateUtil;
import com.ipassistat.ipa.util.UpdateUtil.ImageUpdateListener;
import com.ipassistat.ipa.view.PhotoPickerView;
import com.ipassistat.ipa.view.TitleBar;
import com.ipassistat.ipa.view.TitleBar.TitleBarButton;
import com.umeng.analytics.MobclickAgent;

/**
 * 编辑评价
 * @author renheng
 *
 */

public class GoodsCommentActivity extends BaseActivity implements OnClickListener{
	private TextView mTime;  //交易时间
	private TextView mProductName;  //商品名字
	private TextView mProductContent;  //商品内容
	private EditText mCommentContent;  //编辑商品内容
	private TitleBar bar;  
	private ImageView mProductImage;  //商品缩略图
	private Button mAddComm; //添加商品评论
	private String orderCodeStr=null;
	private PhotoPickerView mPhotoPickerView;
	private List<String> mBitmapList; //存放选择的图片路径的集合
	
	private String skuCode; //商品标签
	private UpdateUtil mUpdateUtil = UpdateUtil.getInstance();
	
	private GoodsModule module = new GoodsModule(this);
	private Context mContext=this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_comment);
		init();
		
		show();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("1035"); //统计页面
	    MobclickAgent.onResume(this); //统计时长
	}
	

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("1035"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
	    MobclickAgent.onPause(this);
	}
	
	private void init(){
		initTitleBar();
		mTime=(TextView) findViewById(R.id.transaction_time);
		mProductName=(TextView) findViewById(R.id.product_name);
		mProductContent=(TextView) findViewById(R.id.product_content);
		mCommentContent=(EditText) findViewById(R.id.comment_content);
		mProductImage=(ImageView) findViewById(R.id.product_image);
		mAddComm=(Button) findViewById(R.id.comment_send);
		mPhotoPickerView=(PhotoPickerView) findViewById(R.id.picker);
		mPhotoPickerView.hideProduct(true);
		mPhotoPickerView.setMaxPhotoNumber(4);
		mAddComm.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_imgv:
			showWarn();
			break;
		case R.id.comment_send:
			String s = mCommentContent.getText().toString();
			if(s==null||s.length()==0){
				ToastUtil.showToast(this, "亲亲,不要懒,至少一个字的评论哦~");
			}else if(s.length()>500){
				ToastUtil.showToast(this, "亲亲,你太勤快了,超过500字啦~");
			}
			else{
				if(mBitmapList!=null){
					updateImage();
				}else{
					addGoodsComm(null);
				}
				MobclickAgent.onEvent(this, "1102");
			}
			break;
		default:
			break;
		}
		
	}
	
	private void initTitleBar(){
		bar=(TitleBar) findViewById(R.id.titlebar);
		String name="评价宝贝";
		bar.setTitleText(name);
		bar.setButtonClickListener(TitleBarButton.leftImgv, this);
	}

	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		super.onMessageSucessCalledBack(url, object);
		
		if(url.equals(ConfigInfo.API_GOODS_COMMENT_ADD)){
			
			BaseResponse resp=(BaseResponse) object;
			if(resp.getResultCode()==1){
				ToastUtil.showToast(this, "评价添加成功");
				//IntentUtil.startOrderDetailActivity(this, orderCodeStr);
				finish();
			}else if(resp.getResultCode() == 934205133){
				ToastUtil.showToast(this, "商品已经评论");
				finish();
			}
		}
	}
	
	/**
	 * 添加商品评论
	 */
	private void addGoodsComm(String bitmapUrlStr){
		String content = mCommentContent.getText().toString();
		String label="";
		module.addGoodsComment(this, content , label, skuCode,orderCodeStr, bitmapUrlStr);
	}
	
	/**
	 * 上传图片
	 */
	private void updateImage(){
		mUpdateUtil.updateImages(mBitmapList, new ImageUpdateListener() {
			
			@Override
			public void onUpdateComplate(List<String> bitmapUrls) {
				mHub.dismiss();
				mHub.showWithNoTouch("正在提交数据");
				addGoodsComm(UpdateUtil.formatImageString(bitmapUrls));
			}
			
			@Override
			public void onProgress(int max, int position) {
				
			}
			
			@Override
			public void onError(String error) {
				ToastUtil.showToast(mContext, "上传图片失败");
			}
			
			@Override
			public void onBegin() {
				mHub.showWithNoTouch("正在上传图片");
			}
		});
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			showWarn();
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 关闭页面提示
	 */
	private void showWarn(){
		AlertDialog.Builder builder=new Builder(this);
		builder.setTitle("返回后将不保存内容哦，确认放弃编辑？");
		
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		
		builder.setNegativeButton("放弃", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				MobclickAgent.onEvent(GoodsCommentActivity.this, "1101");
				GoodsCommentActivity.this.finish();
			}
		});
		
		builder.create().show();
	}
	
	/**
	 * 显示商品和订单信息 
	 */
	private void show(){
		Intent intent = getIntent();
		skuCode = intent.getStringExtra(IntentKey.GOODS_COMMENT_SKU_CODE);
		mProductName.setText(intent.getStringExtra(IntentKey.GOODS_COMMENT_NAME));
		String contentStr=intent.getStringExtra(IntentKey.GOODS_COMMENT_DESCRIBE);
		mProductContent.setText(contentStr);
		BitmapOptionsFactory.getInstance(getApplicationContext()).display(mProductImage, intent.getStringExtra(IntentKey.GOODS_COMMENT_THUMB));
		
		mTime.setText(intent.getStringExtra(IntentKey.GOODS_COMMENT_ORDER_TIME));
		orderCodeStr=intent.getStringExtra(IntentKey.GOODS_COMMENT_ORDER_CODE);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mPhotoPickerView.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
		mBitmapList = mPhotoPickerView.getBitmapPath();
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
