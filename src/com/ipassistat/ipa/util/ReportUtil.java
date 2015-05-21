package com.ipassistat.ipa.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;

import com.ipassistat.ipa.R.string;
import com.ipassistat.ipa.view.AlertDialog;

/**
 * 举报的工具类
 * 
 * @author LiuYuHang
 * @date 2014年11月25日
 */
public class ReportUtil implements OnClickListener {
	private Context context;
	private String postCode;
	private String commentCode;

	public void report(Context context, String postCode) {
		report(context, postCode, null);
	}

	public void report(Context context, String postCode, String commentCode) {
		this.context = context;
		this.postCode = postCode;
		this.commentCode = commentCode;

		AlertDialog alertDialog = new AlertDialog(context);

		if (TextUtils.isEmpty(commentCode)) {
			alertDialog.setTitle(context.getString(string.global_message_report_content));
		} else {
			alertDialog.setTitle(context.getString(string.global_message_report_reply_content));
		}

		alertDialog.setPositiveButton(context.getString(string.menu_ok), this);
		alertDialog.setNegativeButton(context.getString(string.menu_cancel), null);

		alertDialog.show();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (IntentUtil.checkAndLogin(context)) {
			//new SystemModule(context).report(postCode, commentCode, null);
		}
	}

}
