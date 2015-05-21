package com.ipassistat.ipa.bean.response.entity;

import java.io.Serializable;
import java.util.List;

import com.ipassistat.ipa.bean.response.BaseResponse;

/**
 * 姐妹圈 - 列表详情 - 条目实体类
 * 
 * @author LiuYuHang
 *
 */
public class SisterGroupPostVo extends BaseResponse implements Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserInfo postPublisherLists;
	/**
	 * 追贴，和跟帖用一个Vo
	 */
	public List<SisterGroupPostVo> postFollowLists;

	// post_label String 选择标签
	public String post_label;
	// sort String 排序
	public String sort;
	// post_code String 帖子ID
	public String post_code;
	// status String 状态
	public String status;
	// post_browse String 浏览量
	public String post_browse;
	// post_praise String 点赞量
	public String post_praise;
	// publisher_code String 用户ID
	public String publisher_code;
	// post_content String 正文
	public String post_content;
	// isofficial String 官方帖
	public String isofficial;
	// publish_time String 发布时间
	public String publish_time;

	// product_code String 商品
	public String product_code;
	public SaleProduct productinfo;

	// post_count int 回复量
	public int post_count, reply_acount;
	// issessence String 精华帖
	public String issessence;
	// post_img String 图片
	public String post_img;
	
	//新版本的图片实体类
	public ImageInfo[] picInfos;
	
	// post_title String 标题
	public String post_title;
	// ishot String 是否火
	public String ishot;
	
	//链接
	public String linkUrl;

	public String ispraise;
	public String iscollect;
	/***
	 * 帖子总点赞量(主帖和追帖点赞之和)
	 */
	public int post_praise_count = 0;
	public Object clone() {
		SisterGroupPostVo o = null;
		try {
			o = (SisterGroupPostVo) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}

	/**
	 * 发帖用户
	 */
	// public MemberInfo postPublisherLists;
	// // 帖子编号 string
	// public String post_code;
	// // 帖子标题 string
	// public String post_title;
	// // 帖子标签 string[] ??
	//
	// // 发帖时间 string
	// public String publish_time;
	// // 帖子内容 string
	// public String post_content;
	// // 阅览数 string
	// public String post_browse;
	// // 回复数 string
	// public int reply_acount;
	// // 赞同数
	// public String post_praise;
	//
	// /**
	// * 是否收藏 0:被收藏；空值代表未被收藏
	// */
	// public String iscollect;
	// /**
	// * 是否点赞 0:被点赞；空值代表未被点赞
	// */
	// public String ispraise;
	//
	// // 图片URL string[]
	// public String post_img;
	// //追贴
	// public List<PostFollowVo> postFollowLists;
	// 帖子tag string[]
	// public List<SisterGroupTagVo> tags;
}
