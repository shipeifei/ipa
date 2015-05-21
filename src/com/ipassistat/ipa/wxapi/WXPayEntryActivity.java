package com.ipassistat.ipa.wxapi;



import com.ipassistat.ipa.constant.Constant;
import com.ipassistat.ipa.ui.activity.OrderConfirmActivity;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.util.pay.WeiXinPay;
import com.ipassistat.ipa.util.pay.PayUitl.PayInterface;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	@SuppressWarnings("unused")
	private  final String TAG = "AAA";
	
    private IWXAPI api;
    
    @SuppressWarnings("unused")
	private PayInterface mPayInterface;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.pay_result);
    	api = WXAPIFactory.createWXAPI(this, WeiXinPay.AppID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
		
	}

	@Override
	public void onResp(BaseResp resp) {
		PayResp pay=(PayResp) resp;
		String str = pay.extData;
		String[] s = str.split(",");
		String orderCode=s[0];
		String flag=s[1];
		
		if (pay.errCode==0) {
			IntentUtil.startOrderSubmitSuccessActivity(this, orderCode);
		}else{
			if(flag.equals(Constant.ORDER_LIST)){
				//无逻辑
			}else{
				IntentUtil.startOrderDetailActivity(this, orderCode, flag);
			}
			
		}
		finish();
		if(OrderConfirmActivity.instanse!=null){
			OrderConfirmActivity.instanse.finish();
		}
		
	}

}