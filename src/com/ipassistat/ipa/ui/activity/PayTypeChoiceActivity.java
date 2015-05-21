package com.ipassistat.ipa.ui.activity;

import java.util.ArrayList;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.constant.IntentFlag;
import com.ipassistat.ipa.view.ChoiceView;
import com.ipassistat.ipa.view.TitleBar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

/**
 * 支付方式选择页面
 * @author RenHeng
 *
 */
public class PayTypeChoiceActivity extends BaseActivity{
	
	private TitleBar mBar;
	private FrameLayout mFrame;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_type_choice);
		mFrame=(FrameLayout) findViewById(R.id.frame);
		initTitleBar();
		Intent intent =getIntent();
		ArrayList<String> list=intent.getStringArrayListExtra(IntentFlag.PAY_TYPE_CONTENT);
		int checkPos=intent.getIntExtra(IntentFlag.PAY_TYPE_CHOICE, -1);
		ChoiceView view=new ChoiceView(this, list, checkPos);
		mFrame.addView(view);
	}
	
	private void initTitleBar(){
		mBar=(TitleBar) findViewById(R.id.title_bar);
		mBar.setTitleText("支付方式");
		mBar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				PayTypeChoiceActivity.this.finish();
			}
		});
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
