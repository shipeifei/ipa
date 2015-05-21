package com.ipassistat.ipa.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.array;
import com.ipassistat.ipa.R.drawable;
import com.ipassistat.ipa.R.id;
import com.ipassistat.ipa.R.layout;
import com.ipassistat.ipa.bean.response.entity.MemberInfo;
import com.ipassistat.ipa.bean.response.entity.UserMessageVo;
import com.ipassistat.ipa.business.UserModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.util.BitmapOptionsFactory;
import com.ipassistat.ipa.view.CircularImageView;
import com.lidroid.xutils.BitmapUtils;

public class SisterGroupMessageMineAdapter extends PaginationAdapter<UserMessageVo> {
	static String TAG = "SisterGroupMessageMineAdapter";
	// private List<UserMessageVo> mData;
	private MemberInfo mMemberInfo;
	// private ImageWorker mImageWorker;
	private BitmapUtils mBitmapUtils;

	public SisterGroupMessageMineAdapter(Context context, List<UserMessageVo> data) {
		super(context, data);

		mMemberInfo = UserModule.getMemberInfo(getContext());

		// mImageWorker = ImageWorker.newInstance(getContext());
		// mImageWorker.addParams(TAG,
		// ImageCacheParams.getDefaultParams(getContext(),
		// drawable.personal_photo));
		mBitmapUtils = BitmapOptionsFactory.newInstanceBitmapUtils(getContext(), drawable.personal_photo);
	}

	// public SisterGroupMessageMineAdapter(Context context, List<UserMessageVo>
	// list) {
	// mInflater = LayoutInflater.from(context);
	// mData = list;
	// }

	// @Override
	// public int getCount() {
	// return mData.size();
	// }

	// @Override
	// public Object getItem(int arg0) {
	// return null;
	// }

	// @Override
	// public long getItemId(int arg0) {
	// return 0;
	// }

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(layout.adapter_message_mine, null);

			holder.iconView = (CircularImageView) convertView.findViewById(id.imageview_icon);
			holder.nicknameView = (TextView) convertView.findViewById(id.textview_nickname);
			holder.actionView = (TextView) convertView.findViewById(id.textview_action);
			holder.contentView = (TextView) convertView.findViewById(R.id.textview_content);
			holder.timeView = (TextView) convertView.findViewById(R.id.textview_time);
			holder.actionNickNameView = (TextView) convertView.findViewById(id.textview_action_nickname);
			holder.oldCommentView = (TextView) convertView.findViewById(id.textview_old_comment);

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		UserMessageVo item = getItem(position);
		holder.nicknameView.setText(item.messageUser.getNickname());

		mBitmapUtils.display(holder.iconView, item.messageUser.getMember_avatar(), BitmapOptionsFactory.getOptionConfig(getContext(), drawable.personal_photo));
		// mImageWorker.loadBitmap(TAG, item.messageUser.getMember_avatar(),
		// holder.iconView);

		String actionStr[] = getContext().getResources().getStringArray(array.sistergroup_post_message_action);

		if (item.message_type.equals(ConfigInfo.TYPE_MESSAG_ACTION_COMMENT)) {
			holder.actionView.setText(actionStr[0]);
		} else if (item.message_type.equals(ConfigInfo.TYPE_MESSAG_ACTION_UP_POST)) {
			holder.actionView.setText(actionStr[1]);
		} else if (item.message_type.equals(ConfigInfo.TYPE_MESSAG_ACTION_UP_COMMENT)) {
			holder.actionView.setText(actionStr[2]);
		} else if (item.message_type.equals(ConfigInfo.TYPE_MESSAG_ACTION_REPLY_COMMENT)) {
			holder.actionView.setText(actionStr[3]);
		} else {
			holder.actionView.setText(actionStr[4]);
		}

		holder.contentView.setText(item.message_info);
		holder.contentView.setVisibility(TextUtils.isEmpty(item.message_info) ? View.GONE : View.VISIBLE);

		holder.timeView.setText(item.create_time);

		holder.actionNickNameView.setText(mMemberInfo.getNickname() + "：");
		holder.oldCommentView.setText(item.old_comment);

		return convertView;
	}

	private class Holder {
		CircularImageView iconView;
		TextView nicknameView, actionView, contentView, timeView, actionNickNameView, oldCommentView;
	}
}
