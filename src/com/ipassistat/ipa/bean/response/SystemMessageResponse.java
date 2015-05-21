package com.ipassistat.ipa.bean.response;

import java.util.List;

import android.R.integer;

import com.ipassistat.ipa.bean.response.entity.PageResults;
import com.ipassistat.ipa.bean.response.entity.SystemMessageVo;

/**
 * 系统消息的返回
 * 
 * @author LiuYuHang
 *
 */
public class SystemMessageResponse extends BaseResponse {

	public List<SystemMessageVo> messages;
	
	public String is_read;
	public PageResults paged;
	/**消息数量 增加人：时培飞 增加时间:2014-12-09*/
	public int count;
	
}
