/**
 * 
 */
package com.ipassistat.ipa.bean.request;

import com.ipassistat.ipa.bean.request.entity.PageOption;

/**
 * @author wrj
 *  视频列表
 */
public class VideoListRequest extends BaseRequest {
	  public   String recreation_type;//频道ID
	  public   PageOption    paging;//翻页
	  public   Integer picWidth;
	
}
