package com.ipassistat.ipa.view;

import java.util.List;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.constant.IntentFlag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ChoiceView extends LinearLayout implements OnItemClickListener{
	
	private LayoutInflater mInflater;
	private ListView mListView;
	private List<String> mList;
	private int mCheckedPos; //选中的位置 
	private Context context;
	private Activity mActivity;
	
	public ChoiceView(Context context, AttributeSet attrs, int defStyle, List<String> list, int checkPos) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context, list, checkPos);
	}

	public ChoiceView(Context context, AttributeSet attrs, List<String> list, int checkPos) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context, list, checkPos);
	}

	public ChoiceView(Context context, List<String> list, int checkPos) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context, list, checkPos);
	}

	private void init(Context context, List<String> list, int checkPos){
		this.mList=list;
		this.mCheckedPos=checkPos;
		this.context=context;
		mActivity=(Activity) context;
		mInflater = LayoutInflater.from(context);
		mInflater.inflate(R.layout.view_list_choice, this);
		mListView = (ListView) findViewById(R.id.listview);
		mListView.setOnItemClickListener(this);
		updateUI(mCheckedPos);
	}
	
	private void updateUI(int checkPos){
		ChoiceListAdapter adapter=new ChoiceListAdapter(context, mList, checkPos);
		mListView.setAdapter(adapter);
	}
	
	 class ChoiceListAdapter extends BaseAdapter {

		private Context context;
		private List<String> list;
		private LayoutInflater inflater;
		private int checkedId;

		/**
		 * 
		 * @param context
		 * @param list
		 * @param checkId
		 *            选中的position，如果无选中，则写负数
		 */
		private ChoiceListAdapter(Context context, List<String> list, int checkedPos) {
			this.context = context;
			this.list = list;
			this.checkedId = checkedPos;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.adapter_choice_listview,
						null);
				holder.personal_info_item = (TextView) convertView
						.findViewById(R.id.personal_info_item);
				holder.choose = (ImageView) convertView.findViewById(R.id.choose);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.personal_info_item.setText(list.get(position));
			if (position == checkedId) {
				holder.choose.setVisibility(View.VISIBLE);
			} else {
				holder.choose.setVisibility(View.GONE);
			}
			return convertView;
		}

		class ViewHolder {
			TextView personal_info_item;
			ImageView choose;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		updateUI(position);
		Intent data=new Intent();
		data.putExtra(IntentFlag.PAY_TYPE, position);
		mActivity.setResult(mActivity.RESULT_OK, data);
		mActivity.finish();
	}
	
}
