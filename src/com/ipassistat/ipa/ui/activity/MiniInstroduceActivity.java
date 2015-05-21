package com.ipassistat.ipa.ui.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.adapter.InstroduceAdapter;
import com.ipassistat.ipa.adapter.viewholder.InstroduceViewHolder;
import com.ipassistat.ipa.bean.request.SendInstroduceRequest;
import com.ipassistat.ipa.bean.request.entity.ContactPerson;
import com.ipassistat.ipa.bean.response.MsgShareResponse;
import com.ipassistat.ipa.business.InstroduceModul;
import com.ipassistat.ipa.business.UserModule;
import com.ipassistat.ipa.util.NetUtil;
import com.ipassistat.ipa.util.RemoveSameUtils;
import com.ipassistat.ipa.util.StringUtil;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.view.TitleBar;

public class MiniInstroduceActivity extends BaseActivity {

	private EditText phoneNum; // 手机号
	private RelativeLayout addContact;// 添加联系人按钮
	private LinearLayout addPhone;// 添加手机号按钮
	private ListView lv;// 可选的发送人手机号列表
	private Button send;// 发送短信按钮
	private InstroduceAdapter adapter;// listview 的适配器
	private List<ContactPerson> contacts;// 将要发送短信的联系人
	private String nums;// 要发送的联系人的手机号
	private List<ContactPerson> returnContact;// 存储 联系人界面返回来联系人列表 可操作
	private List<ContactPerson> writeContact;// 存储手动添加的联系人
	private List<ContactPerson> returnContacts;// 存储联系人界面返回来的联系人列表 不可操作
	private String mobiles;// 会员编号
	private String status;// 向服务器发送推荐人后返回码
	private boolean isFirst = true;// 判断是否是第一次进入 如果是的话就检查网络发送历史数据
	private CheckBox selectedAll;// 全选框
	private TextView contactCount;// 选择联系人的个数显示框
	private String contactsStr;// 联系人串 格式：姓名：手机号
	private boolean isContacts;// 标记是否是从联系人界面返回的
	private List<String> failureNumsList;// 发送失败的手机号
	private boolean isFailure = false;// 是否发送短信失败 onFailure
	// private DataDBHelper helper;//
	// private ContactsAccess access;//
	private Handler hanlder = new Handler() {
		public void handleMessage(Message msg) {
			updateStatus();
			// 更新状态
			// 通讯录
			String str = "";

			for (int i = 0; i < contacts.size(); i++) {

				if (contacts.get(i).isSelected()) {
					str = contacts.get(i).getPhoneNum();
					contacts.get(i).setSuccess(true);
					contacts.get(i).setSelected(false);
					// havesend 这个状态是为了控制后面短信发送成功与否的状态的文本框的显隐 所以此处应该设置为true
					contacts.get(i).setHavesend(true);
					// 修改 发送成功与否的状态
					if (failureNumsList.size() > 0) {
						// 发送请求成功 且有短信发送失败情况
						for (int j = 0; j < failureNumsList.size(); j++) {
							if (str.equals(failureNumsList.get(j))) {
								// 修改发送状态
								// 修改成功状态并修改选中状态
								contacts.get(i).setSuccess(false);
								contacts.get(i).setSelected(true);
							}
						}
						//判断选中状态 如果是true 说明此条目发送失败 false说明发送成功 发送成功则移除
						// false则遍历returnContacts 找到 则删除 
						if (!contacts.get(i).isSelected()&&contacts.get(i).isHavesend()) {
							
							for (int k = 0; k < returnContacts.size(); k++) {
								if (contacts.get(i).getPhoneNum().equals(returnContacts.get(k).getPhoneNum())) {
									returnContacts.remove(k);
									break;
								}
							}
						}
					}
					// 发送失败的两种情况 1. 连接服务器失败 2. 全部发送成功
					else if (isFailure) {

						// 1.连接服务器失败 修改发送成功状态
						contacts.get(i).setSuccess(false);
						
					}else{
						// 2. 全部发送成功
						if (returnContacts!=null&&returnContacts.size()>0) {
							returnContacts.clear();
						}
					}

				}
			}

			// 更新UI
			adapter.notifyDataSetChanged();
			contactCount.setText("(0人)");
			selectedAll.setChecked(false);
			// 将已推荐的联系人发送到通讯录
			// sendRequest(contactsStr, false);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_miniinstroduce);
//		MemberInfo memberInfo = UserModule.getMemberInfo(getApplicationContext());
//		if (memberInfo!=null ) {
//			
//			mobiles = memberInfo.getMobile();
//		}
		mobiles = 	UserModule.getUserAccount(MiniInstroduceActivity.this);
		setTitleText("推荐给好友");
		init();

	}

	// 改变选择联系人数量的显示框
	void showSelectedContactCount() {
		int count = 0;
		for (int i = 0; i < contacts.size(); i++) {
			if (contacts.get(i).isSelected()) {
				count++;
			}
		}
		if (contacts.size()>0 && count == contacts.size()) {
			selectedAll.setChecked(true);
		} else {
			selectedAll.setChecked(false);
		}
		contactCount.setText("(" + count + "人)");
	}

	// 界面的初始化
	void init() {
		contacts = new ArrayList<ContactPerson>();
		returnContacts = new ArrayList<ContactPerson>();
		returnContact = new ArrayList<ContactPerson>();
		writeContact = new ArrayList<ContactPerson>();
		// 获取句柄
		phoneNum = (EditText) findViewById(R.id.et_miniinstroduce_phonenum);
		phoneNum.setInputType(EditorInfo.TYPE_CLASS_PHONE);
		addContact = (RelativeLayout) findViewById(R.id.rl_miniinstroduce_addnumfromcontact);
		addPhone = (LinearLayout) findViewById(R.id.ll_miniinstroduce_addnum);
		lv = (ListView) findViewById(R.id.lv_miniinstroduce_contacts);
		send = (Button) findViewById(R.id.btn_miniinstroduce_send);
		selectedAll = (CheckBox) findViewById(R.id.cb_miniinstroduce_selectall);
		selectedAll.setChecked(false);

		contactCount = (TextView) findViewById(R.id.tv_miniinstroduce_selectedcount);
		// 给listview设置适配器
		LayoutInflater inflater = getLayoutInflater();
		adapter = new InstroduceAdapter(contacts, getApplicationContext(),
				inflater);
		lv.setAdapter(adapter);
		// 设置监听
		setListener();

	}

	// 设置监听事件
	void setListener() {
		// 发送联系人列表的点击事件
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				InstroduceViewHolder holder = (InstroduceViewHolder) parent
						.getAdapter().getItem(position);
				if (contacts.get(position).isSelected()) {
					holder.cb.setBackgroundResource(R.drawable.not);
					contacts.get(position).setSelected(false);
				} else {
					holder.cb.setBackgroundResource(R.drawable.selected);
					contacts.get(position).setSelected(true);
				}
				adapter.notifyDataSetChanged();
				// 改变选中人数
				showSelectedContactCount();
			}
		});
		// 选择全部的复选框的点击事件
		selectedAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (selectedAll.isChecked()) {
					// 没有选中 再次点击 全选
					for (int i = 0; i < contacts.size(); i++) {
						contacts.get(i).setSelected(true);
						contactCount.setText("(" + contacts.size() + ")");
						// selectedAll.setBackgroundResource(R.drawable.selected);
					}
				} else {
					// 已经选中了再次点击 取消全选
					// selectedAll.setBackgroundResource(R.drawable.not);
					for (int i = 0; i < contacts.size(); i++) {
						contacts.get(i).setSelected(false);
						contactCount.setText("(0人)");
					}
				}
				// 更新UI
				adapter.notifyDataSetChanged();// 复选框
			}
		});
		// 从联系人里添加
		addContact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 由于每次要使展示的是联系人界面传来的最新的数据+手动添加的联系人 所以需要清空contacts
				// 在onactivtyresult（）中重新给contacts赋值
				// contacts = writeContacts+returnContact
				// TODO 跳转到联系人界面
				Intent intent = new Intent(getApplicationContext(),
						MiniContactPersonActivity.class);
				intent.putExtra("returnContact", (Serializable) returnContacts);
				startActivityForResult(intent, 123);
				updateStatus();
			}
		});

		// 手动添加手机号的按钮的点击事件
		addPhone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String number = phoneNum.getText().toString().trim();
				if (StringUtil.isPhoneNo(number)) {
					ContactPerson con = new ContactPerson();
					con.setPhoneNum(number);
					con.setName(number);
					con.setSelected(true);
					con.setHavesend(false);
					writeContact.add(con);
					contacts.add(con);
					RemoveSameUtils.removeSame(contacts);
					adapter.notifyDataSetChanged();
					phoneNum.setText("");
					showSelectedContactCount();
				} else {
					ToastUtil.showToast(getApplicationContext(), "请填写正确的手机号");
				}
			}
		});
		// 发送按钮的点击事件
		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 获取选中联系人手机号字符串
				getInstroudcer();
				if (NetUtil.isNetworkConnected(getApplicationContext())) {

					if (nums.length() == 0) {
						ToastUtil.showToast(getApplicationContext(), "请选择联系人");
					} else {
						// 调用接口 发送短信
						sendSendMsgRequest(nums);

					}

				} else {
					ToastUtil.showToast(getApplicationContext(), "网络连接失败");
				}
			}
		});
		
		//返回键
				TitleBar bar = (TitleBar) findViewById(R.id.titleBar_instroduce_back);
				View leftButton = bar.getLeftButton();
				leftButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						finish();
					}
				});
	}

	// 获取被选中的联系人
	private void getInstroudcer() {
		// 置空选中联系人的字符串 防止重复发送情况
		nums = "";
		contactsStr = "";
		// 将要发送短信的联系人传给服务
		switch (contacts.size()) {
		case 0:
			// 没有选择联系人
			ToastUtil.showToast(getApplicationContext(), "请选择联系人");
			break;
		case 1:
			// 推荐人只选择了一个人
			if (contacts.get(0).isSelected()) {
				nums = contacts.get(0).getPhoneNum();
				contactsStr = contactsStr + contacts.get(0).getName() + ":"
						+ contacts.get(0).getPhoneNum();
			}
			break;

		default:
			// 要推荐多个联系人
			for (int i = 0; i < contacts.size(); i++) {
				// 判断 是否被选中
				if (contacts.get(i).isSelected()) {
					// 判断是否是最后一个
					if (i != (contacts.size() - 1)) {
						nums = nums + contacts.get(i).getPhoneNum() + ",";
						contactsStr = contactsStr + contacts.get(i).getName()
								+ ":" + contacts.get(i).getPhoneNum() + ",";
					} else {
						nums = nums + contacts.get(i).getPhoneNum();
						contactsStr = contactsStr + contacts.get(i).getName()
								+ ":" + contacts.get(i).getPhoneNum();

					}
				}
			}
			break;
		}
	}

	@Override
	protected void onResume() {
		if (isContacts) {
			// 界面刷新
			if (contacts == null) {

				contacts = new ArrayList<ContactPerson>();
			}
			// 更新UI
			adapter.notifyDataSetChanged();
			showSelectedContactCount();
			isContacts = false;
		}

		super.onResume();
	}


	// 调用发送短信接口
	void sendSendMsgRequest(String contactsStr) {
		InstroduceModul instroduceModul = new InstroduceModul(this);
		SendInstroduceRequest request = new SendInstroduceRequest();
		request.setMobile(mobiles);
		request.setTels(contactsStr);
		instroduceModul.postMsgShareList(getApplicationContext(), request);
		}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (data != null && requestCode == 123 && resultCode == 345) {
			isContacts = true;
			ContactPerson con = null;
			if (returnContacts != null) {
				
				returnContacts.clear();
			}
			// 得到联系人界面返回的联系人
			returnContacts = (List<ContactPerson>) data
					.getSerializableExtra("contactPerson");
			if (returnContact == null) {
				returnContact = new ArrayList<ContactPerson>();
			}
			returnContact.clear();
			for (int i = 0; i < returnContacts.size(); i++) {
				con = new ContactPerson();
				con.setSelected(true);
				con.setName(returnContacts.get(i).getName());
				con.setPhoneNum(returnContacts.get(i).getPhoneNum());
				returnContact.add(con);
			}
			// 改变选中状态存入contacts中
			// 清空
			contacts.clear();
			// 重新添加
			for (int i = 0; i < returnContact.size(); i++) {
				con = returnContact.get(i);
				con.setSelected(true);
				contacts.add(con);
			}
			for (int i = 0; i < writeContact.size(); i++) {
				ContactPerson person = new ContactPerson();
				person = writeContact.get(i);
				contacts.add(person);

			}
			// 去重
			RemoveSameUtils.removeSame(contacts);
			// contacts.addAll(writeContact);
		}

	}

	// 跳转通讯录界面前 更新writeContact returnContact中联系人的状态
	void updateStatus() {
		for (int i = 0; i < contacts.size(); i++) {
			ContactPerson person = new ContactPerson();
			person = contacts.get(i);
			// 更新手动添加的联系人的状态
			for (int j = 0; j < writeContact.size(); j++) {
				ContactPerson person1 = new ContactPerson();
				person1 = writeContact.get(j);
				if (person.getPhoneNum().equals(person1.getPhoneNum())) {
					writeContact.get(j).setSelected(
							contacts.get(i).isSelected());
				}
			}
			// 更新通讯录添加的联系人的状态
			for (int p = 0; p < returnContact.size(); p++) {
				ContactPerson person1 = new ContactPerson();
				person1 = returnContact.get(p);
				if (person.getPhoneNum().equals(person1.getPhoneNum())) {
					returnContact.get(p).setSelected(
							contacts.get(i).isSelected());
				}
			}
		}
	}
	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		
		super.onMessageSucessCalledBack(url, object);
		MsgShareResponse response = (MsgShareResponse) object;
		// 更新界面同时发送推荐人到服务器
		if (response != null && response.getError_tel()!= null) {
			failureNumsList = response.getError_tel();
			isFailure = false;
			hanlder.sendEmptyMessage(0);
		}else{
			failureNumsList = new ArrayList<String>();
			isFailure = false;
			hanlder.sendEmptyMessage(0);
		}
	}
	@Override
	public void onMessageFailedCalledBack(String url, Object object) {
		
		super.onMessageFailedCalledBack(url, object);
		failureNumsList = new ArrayList<String>();
		isFailure = true;
		hanlder.sendEmptyMessage(0);
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
