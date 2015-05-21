package com.ipassistat.ipa.util;

import com.ipassistat.ipa.R;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class DialogUtil {
	/**显示对话框
	 * @param context
	 * @param btn_no
	 * @param btn_now
	 * @param title
	 * @param text
	 * @param btn_left
	 * @param btn_right
	 * @return
	 */
	public static AlertDialog showUpdateDialog(Context context,
			OnClickListener btn_no, OnClickListener btn_now,String title, String text,String btn_left,String btn_right) {
		AlertDialog dlg = new AlertDialog.Builder(context).create();
		dlg.show();
		Window window = dlg.getWindow();
		// *** 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		window.setContentView(R.layout.item_dialog_update);
		TextView content = (TextView) window.findViewById(R.id.content);
		TextView titleView = (TextView) window.findViewById(R.id.title);
		Button no = (Button) window.findViewById(R.id.btn_no);
		Button yes = (Button) window.findViewById(R.id.btn_yes);
		titleView.setText(title);
		content.setText(text);
		no.setText(btn_left);
		yes.setText(btn_right);
		// 为确认按钮添加事件,执行退出应用操作
		if (btn_no == null) {
			no.setVisibility(View.GONE);
		} else {
			no.setOnClickListener(btn_no); // 点击取消更新
		}
		yes.setOnClickListener(btn_now); // 现在立即更新
		return dlg;
	}
}
