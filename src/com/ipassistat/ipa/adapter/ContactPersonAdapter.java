package com.ipassistat.ipa.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.androidquery.AQuery;
import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.drawable;
import com.ipassistat.ipa.bean.request.entity.ContactPerson;
import com.ipassistat.ipa.util.GlobalUtil;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class ContactPersonAdapter extends BaseAdapter implements SectionIndexer {

	private List<ContactPerson> list = null;
	private Context mContext;
	private SectionIndexer mIndexer;
	private AQuery aQuery;

	public ContactPersonAdapter(Context mContext, List<ContactPerson> list) {
		this.mContext = mContext;
		this.list = list;
		aQuery = new AQuery(mContext);

	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;

		// if (person.getItemType() == 0) {
		// convertView = inflater.inflate(R.layout.contact_index, parent,
		// false);
		// initHeader(convertView, person);
		// //convertView=null;
		// return convertView;
		// }
		//
		// else {

		//if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.contact_item, parent, false);
			viewHolder = new ViewHolder();

			viewHolder.name = (TextView) convertView.findViewById(R.id.phone_name);

			viewHolder.headPhone = (com.ipassistat.ipa.view.CircularImageView) convertView.findViewById(R.id.contact_phone);
			viewHolder.tvLetter = (TextView) convertView.findViewById(R.id.catalog);
			convertView.setTag(viewHolder);
//		} else {
//			viewHolder = (ViewHolder) convertView.getTag();
//		}

		ContactPerson cv = list.get(position);
		if (cv.getName() != null) {
			viewHolder.name.setText(cv.getName());
		}
		if (cv.getHeadImag() != null) {
			aQuery.id(viewHolder.headPhone).image(cv.getHeadImag());
			//(, true, true, GlobalUtil.displayMetrics.widthPixels / 4, drawable.default_goods_noborder);

			//viewHolder.headPhone.setImageBitmap(cv.getHeadImag());
		}

		if (position == 0) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(cv.getLetter());
		} else {
			String lastCatalog = list.get(position - 1).getLetter();
			if (cv.getLetter().equals(lastCatalog)) {
				viewHolder.tvLetter.setVisibility(View.GONE);
			} else {
				viewHolder.tvLetter.setVisibility(View.VISIBLE);
				viewHolder.tvLetter.setText(cv.getLetter());
			}
		}
		return convertView;

	}

	public class ViewHolder {

		public TextView name;
		TextView tvLetter;
		public com.ipassistat.ipa.view.CircularImageView headPhone;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getSectionForPosition(int position) {

		return 0;
	}

	public int getPositionForSection(int section) {
		ContactPerson mContent;
		String l;
		if (section == '!') {
			return 0;
		} else {
			for (int i = 0; i < getCount(); i++) {
				mContent = (ContactPerson) list.get(i);
				l = mContent.getLetter();
				char firstChar = l.toUpperCase().charAt(0);
				if (firstChar == section) {
					return i + 1;
				}

			}
		}
		mContent = null;
		l = null;
		return -1;
	}

}
