package com.ipassistat.ipa.ui.contact.activity;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.adapter.ContactPersonAdapter;
import com.ipassistat.ipa.bean.request.ContactRequest;
import com.ipassistat.ipa.bean.request.entity.ContactPerson;
import com.ipassistat.ipa.bean.response.ContactsResponse;
import com.ipassistat.ipa.business.ContactModule;
import com.ipassistat.ipa.business.UserModule;
import com.ipassistat.ipa.ui.activity.BaseActivity;
import com.ipassistat.ipa.util.ContactsUtil;
import com.ipassistat.ipa.util.DeviceUtil;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.util.NetUtil;
import com.ipassistat.ipa.util.ProgressHub;
import com.ipassistat.ipa.util.StringHelper;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.util.ViewUtil;
import com.ipassistat.ipa.view.DialogView;
import com.ipassistat.ipa.view.TitleBar;
import com.lidroid.xutils.view.annotation.event.OnItemLongClick;

/***
 * 联系人
 * 
 * @author shipeifei
 * 
 */
public class ContactPersonActivity extends BaseActivity {

	private ListView listView;
	private TextView tv_show;
	private ContactPersonAdapter adapter; // listview的适配器

	private List<ContactPerson> person;
	private Map<String, ContactPerson> mapPerson;

	private SideBar indexBar;
	private WindowManager mWindowManager;
	private TextView mDialogText;
	private View head;

	private Handler progressHandler = new Handler() {
		public void handleMessage(Message msg) {
			progress();
		}
	};

	public class PinyinComparator implements Comparator<ContactPerson> {

		public int compare(ContactPerson o1, ContactPerson o2) {
			if (o1.getLetter().equals("@") || o2.getLetter().equals("#")) {
				return -1;
			} else if (o1.getLetter().equals("#") || o2.getLetter().equals("@")) {
				return 1;
			} else {
				return o1.getLetter().compareTo(o2.getLetter());
			}
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		setTitleText("通讯录");
		findViewById();
		bindEvents();
		ItemOnLongClick2();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

	}

	

	private void progress() {

		setAdatper(mapPerson);
	}


	/**
	 * 设置listView的适配器
	 * 
	 * @param persons
	 *            需要排序的列表
	 */
	private void setAdatper(Map<String, ContactPerson> persons) {

		if (persons.size() > 0) {
			
			if(person!=null&&person.size()>0)
			{
				person.clear();
				
			}
			
			person=iteratorMap(persons);
			
			// 排序
			Collections.sort(person, new PinyinComparator());

			adapter = new ContactPersonAdapter(getApplicationContext(), person);
			listView.setAdapter(adapter);
			indexBar.setListView(listView);

		}
	}
	/**
	 * 把Map转化成list
	 * 
	 * @param map
	 *            需要转换的Map
	 * @param list
	 *            需要接受的list
	 */
	private List<ContactPerson> iteratorMap(Map<String, ContactPerson> map) {

		List<ContactPerson> list = new ArrayList<ContactPerson>();
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next().toString();
			list.add(map.get(key));
		}

		return list;
	}

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// ViewUtil.setViewVisiblityWithAnimation(View.GONE, false, loading);
		// AppUtils.CloseProgressDialog(dialog);
	}

	@Override
	protected void onStop() {
		super.onStop();
		// ViewUtil.setViewVisiblityWithAnimation(View.GONE, false, loading);
		// AppUtils.CloseProgressDialog(dialog);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// ViewUtil.setViewVisiblityWithAnimation(View.VISIBLE, true, loading);

		// if (person == null) {
		if (mapPerson != null && mapPerson.size() > 0) {
			mapPerson.clear();
			mapPerson = null;
		}
		new Thread(runTread).start();
		// }

	}

	Runnable runTread = new Runnable() {

		@Override
		public void run() {
			mapPerson = ContactsUtil.getSharedInstance().getSIMContacts(getApplicationContext(),true);// 读取本地通讯录
			mapPerson.putAll(ContactsUtil.getSharedInstance().getPhoneContacts(getApplicationContext(),true));// 读取SIM卡
			progressHandler.sendEmptyMessage(0);
		}
	};

	@Override
	protected void findViewById() {

		listView = (ListView) this.findViewById(R.id.contact_list_view);
		indexBar = (SideBar) findViewById(R.id.sideBar);

		person = new ArrayList<ContactPerson>();
		mDialogText = (TextView) LayoutInflater.from(this).inflate(R.layout.list_position, null);
		head = LayoutInflater.from(this).inflate(R.layout.head, null);
		listView.addHeaderView(head);
		mDialogText.setVisibility(View.INVISIBLE);
		mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
		mWindowManager.addView(mDialogText, lp);
		indexBar.setTextView(mDialogText);

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	private void ItemOnLongClick2() {
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				final ContactPerson contactPerson = person.get(position);
				new AlertDialog.Builder(ContactPersonActivity.this).setTitle(contactPerson.getName()).setItems(R.array.contact_type, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String[] PK = getResources().getStringArray(R.array.contact_type);
						if (PK[which].equals("删除")) {
							DialogView.getAlertDialogWithTitle(ContactPersonActivity.this, getString(R.string.contact_dialog_title), getString(R.string.contact_dialog_delete),
									getString(R.string.contact_button_sure), new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog, int which) {
											ContactsUtil.deleteContact(contactPerson);
											//onResume();
											//listView.getAdapter().notify();
											adapter.notifyDataSetChanged();
										}
									}, getString(R.string.dialog_button_cancle), new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog, int which) {

										}
									});

						}

						else if (PK[which].equals("打电话")) {

							IntentUtil.telePhone(ContactPersonActivity.this, contactPerson.getPhoneNum());

						}
					}
				}).show();
				return true;
			}
		});

	}

	/***
	 * 长按事件 create at 2015-5-22 author 时培飞
	 */
	private void ItemOnLongClick() {
		// 注：setOnCreateContextMenuListener是与下面onContextItemSelected配套使用的
		listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View arg1, ContextMenuInfo arg2) {
				// TODO Auto-generated method stub
				menu.add(0, 0, 0, "购买");
				menu.add(0, 1, 0, "收藏");
				menu.add(0, 2, 0, "对比");
			}
		});
	}

	// 长按菜单响应函数
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

		switch (item.getItemId()) {
		case 0:
			// 添加操作
			Toast.makeText(ContactPersonActivity.this, "添加", Toast.LENGTH_SHORT).show();
			break;
		case 1:
			// 删除操作
			break;
		case 2:
			// 删除ALL操作
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected void bindEvents() {
		// listview 点击事件
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// iv = (ImageView)
				// view.findViewById(R.id.tv_instroduce2fri_select);
				if (position < person.size()) {
					if (person.get(position).isSelected()) {
						// falg = false;
						// iv.setVisibility(View.INVISIBLE);
						// personMap.remove(newPersons.get(position).getPhoneNum());
					} else {
						// falg = true;
						// iv.setVisibility(View.VISIBLE);
						// personMap.put(newPersons.get(position).getPhoneNum(),
						// newPersons.get(position));
					}
					// newPersons.get(position).setSelected(falg);
				}
				// setShowCount(isMyContact);
			};
		});
		// 未推荐全选按钮监听
		// cb_selecteall.setOnCheckedChangeListener(new
		// CompoundButton.OnCheckedChangeListener() {
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView, boolean
		// isChecked) {
		//
		// }
		// });
		// // 已推荐全选按钮监听
		// cb_selecteallrec.setOnCheckedChangeListener(new
		// CompoundButton.OnCheckedChangeListener() {
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView, boolean
		// isChecked) {
		//
		// }
		// });

		// 返回键
		TitleBar bar = (TitleBar) this.findViewById(R.id.contact_back);
		View leftButton = bar.getLeftButton();
		leftButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				finish();
			}
		});

	}

}
