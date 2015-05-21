package com.ipassistat.ipa.view;

import com.ipassistat.ipa.view.PopMenu.OnItemClickListener;

/**
 * PopMenu的菜单实体类
 * 
 * @author LiuYuHang
 * @date 2014年9月24日
 *
 */
public class PopMenuItem {
	private int id;
	private String title;
	private Integer color;
	private OnItemClickListener listener;

	public PopMenuItem() {}

	public PopMenuItem(String title, OnItemClickListener listener) {
		this.title = title;
		this.listener = listener;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getColor() {
		return color;
	}

	public void setColor(Integer color) {
		this.color = color;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public OnItemClickListener getListener() {
		return listener;
	}

	public void setListener(OnItemClickListener listener) {
		this.listener = listener;
	}
}
