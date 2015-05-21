package com.ipassistat.ipa.ui.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.adapter.ContactPersonAdapter;
import com.ipassistat.ipa.bean.request.ContactRequest;
import com.ipassistat.ipa.bean.request.entity.ContactPerson;
import com.ipassistat.ipa.bean.response.ContactsResponse;
import com.ipassistat.ipa.business.ContactModule;
import com.ipassistat.ipa.business.UserModule;
import com.ipassistat.ipa.util.ContactsUtil;
import com.ipassistat.ipa.util.NetUtil;
import com.ipassistat.ipa.util.StringHelper;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.util.ViewUtil;
import com.ipassistat.ipa.view.TitleBar;

public class MiniContactPersonActivity extends BaseActivity {
	private HashMap<String, Integer> contacts;// 手机联系人--未排序
	private LinearLayout layoutIndex;
	private ListView listView;
	private TextView tv_show;
	private ContactPersonAdapter adapter; // listview的适配器
	private String[] indexStr = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z" };
	private Map<String, ContactPerson> persons = null;
	private Map<String, ContactPerson> introducepersons = null;
	private List<ContactPerson> newPersons = null;
	private List<ContactPerson> person;
	private Map<String,ContactPerson> personMap;
	private int height;
	private boolean flag = false;
	private boolean falg = false;
	private List<String> instroducedPhone;// 服务器返回的已经推荐过的好友
	private List<ContactPerson> instroduced;// 已经推荐过的好友，同时在当前的手机联系人中有保存的
	private List<ContactPerson> canInstroduce;// 可以推荐的
	private String userMobile;// 会员手机号
	private LayoutInflater inflater;
	//private ProgressDialog dialog;

	private Button btn_myContact;
	private Button btn_myReccomand;
	private TextView tv_seleteced;
	private CheckBox cb_selecteall;
	private CheckBox cb_selecteallrec;
	private ImageView iv = null;
	private boolean isMyContact = true; // 是否未推荐
	private boolean autoChange = true; // 是否未推荐
	private  int myContactCount = 0; // 未推荐已选计数器
	private  int myReccomandCount = 0; // 已推荐已选计数器
	private List<ContactPerson> returnContactList;// 已经勾选过的联系人。
	private boolean needRemoveAllContact = true;
	private boolean needRemoveAllReContact = true;
	/**
	 * LOADING 加载动画
	 */
	private ImageView loading;
	private Handler handler = new Handler()
	{
		public void handleMessage(Message msg) 
		{
			if(NetUtil.isNetworkConnected(MiniContactPersonActivity.this))
			{
				sendRequest();
			}else
			{
				progress();
			}
			
		}
	};

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mini_contact_persons);
		returnContactList = (List<ContactPerson>) getIntent().getSerializableExtra("returnContact");
		introducepersons = new HashMap<String, ContactPerson>();
		setTitleText("我的通讯录");
		init();
		setListener();
	}

	private void init() {
		
		layoutIndex = (LinearLayout) this.findViewById(R.id.layout);
		listView = (ListView) findViewById(R.id.listView);
		tv_show = (TextView) findViewById(R.id.tv);
		tv_show.setVisibility(View.GONE);
		btn_myContact = (Button) findViewById(R.id.btn_myContact);
		btn_myReccomand = (Button) findViewById(R.id.btn_myReccomand);//#dbdbdb
		btn_myContact.setText(Html.fromHtml("未推荐<font color='#ff7701'>(" + myContactCount + ")</font>"));
		cb_selecteall = (CheckBox) findViewById(R.id.cb_selecteall);
		cb_selecteallrec = (CheckBox) findViewById(R.id.cb_selecteallrec);
		tv_seleteced = (TextView) findViewById(R.id.tv_seleteced);
		// 各个集合的初始化
		person = new ArrayList<ContactPerson>();
		instroduced = new ArrayList<ContactPerson>();
		canInstroduce = new ArrayList<ContactPerson>();
		personMap = new HashMap<String, ContactPerson>();
		getApplicationContext();
		userMobile = UserModule.getUserAccount(MiniContactPersonActivity.this);
		// 确定键
		inflater = LayoutInflater.from(getApplicationContext());
		// 初始化adapter
		adapter = new ContactPersonAdapter(getApplicationContext(), inflater);
		loading = (ImageView) findViewById(R.id.iv_loading);
		ViewUtil.setViewVisiblityWithAnimation(View.VISIBLE, true, loading);
	}

	/**
	 * 对名字进行排序
	 * 
	 * @param allNames
	 *            姓名的英文数组
	 * @param contactPersons
	 *            排序的集合
	 */
	private void sortList(String[] allNames, List<ContactPerson> contactPersons) {
		if (newPersons != null) {
			newPersons.clear();
		}
		for (int i = 0; i < allNames.length; i++) {
			if (allNames[i].length() > 1) {
				for (int j = 0; j < contactPersons.size(); j++) {
					if (allNames[i].equals(contactPersons.get(j).getPinYinName())) {
						if(!IsContain(newPersons,contactPersons.get(j).getPhoneNum()))
						{
							newPersons.add(contactPersons.get(j));
						}
					}
				}
			} else {
				ContactPerson contactPerson = new ContactPerson(allNames[i]);
				contactPerson.setPerson(false);
				newPersons.add(contactPerson);
			}
		}
	}

	// 是否在list有值
	private static boolean IsContain(List<ContactPerson> list, String un) {
		int size = list.size();
		for (int i = 0; i < size; i++) {
			if (un.equals(list.get(i).getPhoneNum())) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (!flag) {
			height = layoutIndex.getMeasuredHeight() / indexStr.length;
			getIndexView();
			flag = true;
		}
	}

	/**
	 * 返回排完序的姓名数组
	 * 
	 * @param persons
	 * @return
	 */
	public String[] sortIndex(List<ContactPerson> persons) {
		TreeSet<String> set = new TreeSet<String>();
		for (ContactPerson person : persons) {
			if(person.getName()!=null && person.getName().length()>0)
			{
				set.add(StringHelper.getPinYinHeadChar(person.getName()).substring(0, 1));
			}
		}
		String[] names = new String[persons.size() + set.size()];// 包含了插入A-Z的数组
		int i = 0;
		for (String string : set) {
			names[i] = string;
			i++;
		}
		String[] pinYinNames = new String[persons.size()];
		for (int j = 0; j < persons.size(); j++) {
			if ((persons.get(j).getPinYinName() == null
					|| "".equals(persons.get(j).getPinYinName()))&& persons.get(j).getName()!=null) {
				persons.get(j).setPinYinName(
						StringHelper.getPingYin(persons.get(j).getName().toString()));
				pinYinNames[j] = StringHelper.getPingYin(persons.get(j)
						.getName().toString());
			} else {
				pinYinNames[j] = persons.get(j).getPinYinName();
			}
		}
		System.arraycopy(pinYinNames, 0, names, set.size(), pinYinNames.length);
		if (names!=null) {
			Arrays.sort(names, String.CASE_INSENSITIVE_ORDER);
		}
		return names;
	}

	// 快速查询栏
	public void getIndexView() {
		LinearLayout.LayoutParams params = new LayoutParams(
				LayoutParams.WRAP_CONTENT, height);
		for (int i = 0; i < indexStr.length; i++) {
			final TextView tv = new TextView(this);
			tv.setLayoutParams(params);
			tv.setText(indexStr[i]);
			tv.setPadding(10, 0, 5, 0);
			tv.setGravity(Gravity.CENTER);
			tv.setTextSize(11);
			tv.setTextColor(Color.parseColor("#666666"));
			layoutIndex.addView(tv);
			layoutIndex.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					float y = event.getY();
					int index = (int) (y / height);
					if (index > -1 && index < indexStr.length) {
						String key = indexStr[index];
						if (contacts.containsKey(key)) {
							int pos = contacts.get(key);
							if (listView.getHeaderViewsCount() > 0) {
								listView.setSelectionFromTop(
										pos + listView.getHeaderViewsCount(), 0);
							} else {
								listView.setSelectionFromTop(pos, 0);
							}
							tv_show.setVisibility(View.VISIBLE);
							tv_show.setText(indexStr[index]);
						}
					}
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						/*layoutIndex.setBackgroundColor(Color
								.parseColor("#495154"));*/
						break;

					case MotionEvent.ACTION_MOVE:

						break;
					case MotionEvent.ACTION_UP:
						/*layoutIndex.setBackgroundColor(Color
								.parseColor("#495154"));*/
						tv_show.setVisibility(View.GONE);
						break;
					}
					return true;
				}
			});
		}
	}

	// 点击事件的监听
	protected void setListener() {
		// listview 点击事件
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				iv = (ImageView) view.findViewById(R.id.tv_instroduce2fri_select);
				if (position < newPersons.size()) {
					if (newPersons.get(position).isSelected()) {
						falg = false;
						iv.setVisibility(View.INVISIBLE);
						personMap.remove(newPersons.get(position).getPhoneNum());
					} else {
						falg = true;
						iv.setVisibility(View.VISIBLE);
						personMap.put(newPersons.get(position).getPhoneNum(), newPersons.get(position));
					}
					newPersons.get(position).setSelected(falg);
				}
				setShowCount(isMyContact);
			};
		});
		// 未推荐全选按钮监听
		cb_selecteall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						
						if (isChecked) // 如果选中 就选择所有
						{
							for (ContactPerson per : newPersons) {
								if (per.isPerson()) {
									per.setSelected(true);
									personMap.put(per.getPhoneNum(),per);
								}
							}
						} else // 如果取消就全不选
						{
							if(needRemoveAllContact){
								if(autoChange)
								{
									for (ContactPerson per : newPersons) {
										if (per.isPerson()) {
											per.setSelected(false);
											personMap.remove(per.getPhoneNum());
										}
									}
								}
							}
						}
						setShowCount(isMyContact);
					}
				});
		// 已推荐全选按钮监听
		cb_selecteallrec.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							for (ContactPerson per : newPersons) {
								if (per.isPerson()) {
									per.setSelected(true);
									personMap.put(per.getPhoneNum(),per);
								}
							}
						} else {
							if(needRemoveAllReContact){
								for (ContactPerson per : newPersons) {
									if (per.isPerson()) {
										per.setSelected(false);
										personMap.remove(per.getPhoneNum());
									}
								}
							}
						}
						setShowCount(isMyContact);
					}
				});
		
		//返回键
		TitleBar bar = (TitleBar) findViewById(R.id.contact_back);
		View leftButton = bar.getLeftButton();
		leftButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				finish();
			}
		});
	}

	/**
	 * 设置要显示的计数器
	 * 
	 */
	private void setShowCount(Boolean isMyContact) {
		if(canInstroduce==null || instroduced == null) return;
		if (isMyContact) {
			myContactCount = personMap.size() - myReccomandCount;
			btn_myContact.setText(Html.fromHtml("未推荐<font color='#ff7701'>(" + myContactCount + ")</font>"));
			if(canInstroduce.size()>0 && myContactCount==canInstroduce.size() )
			{
				cb_selecteall.setChecked(true);
			}else
			{
				needRemoveAllContact = false;
				cb_selecteall.setChecked(false);
			}
		} else {
			myReccomandCount = personMap.size() - myContactCount;
			btn_myReccomand.setText("已推荐(" + myReccomandCount + ")");
			if(instroduced.size()>0 && myReccomandCount==instroduced.size())
			{
				cb_selecteallrec.setChecked(true);
			}else
			{
				needRemoveAllReContact = false;
				cb_selecteallrec.setChecked(false);
			}
		}
		needRemoveAllContact = true;
		needRemoveAllReContact = true;
		tv_seleteced.setText(" (" + personMap.size() + ")");
		adapter.notifyDataSetChanged();
	}
	// 发送请求获取数据
	protected void sendRequest() {
		ContactModule module = new ContactModule(this);
		ViewUtil.setViewVisiblityWithAnimation(View.VISIBLE, true, loading);
		ContactRequest request = new ContactRequest();
		request.setMobile(userMobile);
		module.getMsgShareList(getApplicationContext(), request);
	}

	private void progress() {
		// 判断 筛选
		if (instroducedPhone != null && instroducedPhone.size() > 0) {
			// 筛选
			for (int i = 0; i < instroducedPhone.size(); i++) {
				ContactPerson p = persons.get(instroducedPhone.get(i));
				if (p != null) {
					p.setRec(true);
					introducepersons.put(p.getPhoneNum(), p);//添加到推荐过的人的列表中
					persons.remove(instroducedPhone.get(i)); // 移除已推荐过的人
				}
			}
		}
		/*
		 * 处理保存状态
		 */
		if (returnContactList != null && returnContactList.size() > 0) {
			for (int i = 0; i < returnContactList.size(); i++) {
				if(!returnContactList.get(i).isRec())
				{
					persons.put(returnContactList.get(i).getPhoneNum(), returnContactList.get(i));
					personMap.put(returnContactList.get(i).getPhoneNum(), returnContactList.get(i));
					myContactCount++;
				}else
				{
					introducepersons.put(returnContactList.get(i).getPhoneNum(), returnContactList.get(i));
					personMap.put(returnContactList.get(i).getPhoneNum(), returnContactList.get(i));
					myReccomandCount++;
				}
			}
			setShowCount(isMyContact);
			setShowCount(!isMyContact);
		}
		iteratorMap(persons,canInstroduce);
		iteratorMap(introducepersons,instroduced);
		//设置全选按钮
		if (returnContactList != null) {
			if (canInstroduce.size()!=0 && canInstroduce.size() == myContactCount) {
				cb_selecteall.setChecked(true);
			}else{
				cb_selecteall.setChecked(false);
			}
			if (instroduced.size()!=0 && instroduced.size() == myReccomandCount) {
				cb_selecteallrec.setChecked(true);
				
			}else{
				cb_selecteallrec.setChecked(false);
			}
		}
		setAdatper(canInstroduce);
	}

	/**
	 * 排序后添加数据进适配器
	 * 
	 * @param listPerson
	 *            要排序的联系人列表
	 * @param list
	 *            要加入适配器的联系人列表
	 */
	private void addAdatper(List<ContactPerson> listPerson,
			List<ContactPerson> list) {
		sortContact(listPerson); // 排序
		
		if(list!=null && list.size()>0)  //该方法能避免三星手机调用不到adapter的addAll()方法
		{
			for (ContactPerson contactPerson : list) {
				adapter.add(contactPerson);
			}
		}
		adapter.notifyDataSetChanged();
		listView.setAdapter(adapter);
	}

	/**
	 * 排序联系人
	 * 
	 * @param listPerson
	 *            要排序的数组
	 */
	private void sortContact(List<ContactPerson> listPerson) // 排序联系人
	{
		String[] allNames = sortIndex(listPerson); // 获得姓名英文列表
		sortList(allNames, listPerson);// 排序
		contacts = new HashMap<String, Integer>();
		for (int j = 0; j < indexStr.length; j++) {
			for (int i = 0; i < newPersons.size(); i++) {
				if (newPersons.get(i).getName().equals(indexStr[j])) {
					contacts.put(indexStr[j], i);
				}
			}

		}
	}
	/**
	 * 把Map转化成list
	 * @param map   需要转换的Map
	 * @param list   需要接受的list
	 */
	private void iteratorMap(Map<String, ContactPerson> map,List<ContactPerson> list)
	{
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next().toString();
			list.add(map.get(key));
		}
	}
	
	/**
	 * 改变按钮的背景图片
	 * @param imageId1  点击的按钮所设置的背景图片
	 * @param imageId2 每点击的按钮所设置的背景图片
	 */
	private void changeImage(int imageId1,int imageId2)
	{
		findViewById(R.id.btn_myContact).setBackgroundResource(imageId1);
		findViewById(R.id.btn_myReccomand).setBackgroundResource(imageId2);
	}
	//所有的点击事件
	public void myClick(View view) {
		switch (view.getId()) {
		case R.id.btn_sure: // 添加到发送列表
			iteratorMap(personMap,person);
			Intent intent = new Intent(); 
			intent.putExtra("contactPerson", (Serializable)person);
	        setResult(345, intent);// 放入回传的值,并添加一个Code,方便区分返回的数据 
			this.finish();
			personMap.clear();
			person.clear();
			break;
		case R.id.btn_myContact: // 我的通讯录联系人
			changeImage(R.drawable.choice_2_01,R.drawable.choice_2_02);
			btn_myContact.setTextColor(Color.WHITE);
			btn_myReccomand.setTextColor(Color.parseColor("#666666"));
			cb_selecteallrec.setVisibility(View.GONE);
			cb_selecteall.setVisibility(View.VISIBLE);
			isMyContact = true;
			setAdatper(canInstroduce);
			break;
		case R.id.btn_myReccomand: // 我的已推荐过的
			changeImage(R.drawable.choice_1_01,R.drawable.choice_1_02);
			btn_myContact.setTextColor(Color.parseColor("#666666"));
			btn_myReccomand.setTextColor(Color.WHITE);
			cb_selecteallrec.setVisibility(View.VISIBLE);
			cb_selecteall.setVisibility(View.GONE);
			isMyContact = false;
			setAdatper(instroduced);
			break;
		default:
			break;
		}
	}

	/**
	 * 设置listView的适配器
	 * 
	 * @param persons
	 *            需要排序的列表
	 */
	private void setAdatper(List<ContactPerson> persons) {
		adapter.clear();
		addAdatper(persons, newPersons);
		ViewUtil.setViewVisiblityWithAnimation(View.GONE, false, loading);
		//AppUtils.CloseProgressDialog(dialog);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ViewUtil.setViewVisiblityWithAnimation(View.GONE, false, loading);
		//AppUtils.CloseProgressDialog(dialog);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		ViewUtil.setViewVisiblityWithAnimation(View.GONE, false, loading);
		//AppUtils.CloseProgressDialog(dialog);
	}

	@Override
	protected void onResume() {
		super.onResume();
		ViewUtil.setViewVisiblityWithAnimation(View.VISIBLE, true, loading);
		//dialog.show();
		persons = null;
		// 手机联系人的排序筛选
		clearList(newPersons);
		clearList(person);
		clearList(canInstroduce);
		clearList(instroduced);
		if (newPersons == null) {
			newPersons = new ArrayList<ContactPerson>();
		}
		if (persons == null) {
			new	Thread(runTread).start();
		}
		
	}
	
	
	Runnable runTread = new Runnable() {
		
		@Override
		public void run() {
			persons = ContactsUtil.getSharedInstance().getPhoneContacts(getApplicationContext());// 读取本地通讯录
			persons.putAll(ContactsUtil.getSharedInstance().getSIMContacts(getApplicationContext()));// 读取SIM卡
			handler.sendEmptyMessage(0);
		}
	};

	private void clearList(List<ContactPerson> list) {
		if (list != null) {
			list.clear();
		}
	}
	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		super.onMessageSucessCalledBack(url, object);
		ViewUtil.setViewVisiblityWithAnimation(View.GONE, false, loading);
		ContactsResponse response = (ContactsResponse) object;
		if (instroducedPhone == null) {
			
			instroducedPhone = new ArrayList<String>();
		}
		if (response!=null && response.getMobile()!= null) {
			List<String> mobiles = response.getMobile();
			if (mobiles!=null) {
				
				for (int i = 0; i < mobiles.size(); i++) {
					instroducedPhone.add(mobiles.get(i));
				}
			}
		}
		progress();
	}
	@Override
	public void onMessageFailedCalledBack(String url, Object object) {
		super.onMessageFailedCalledBack(url, object);
		ViewUtil.setViewVisiblityWithAnimation(View.GONE, false, loading);
		ToastUtil.showToast(getApplicationContext(), "网络连接超时");
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
