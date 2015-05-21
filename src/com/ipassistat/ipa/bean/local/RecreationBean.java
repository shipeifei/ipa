package com.ipassistat.ipa.bean.local;

import java.io.Serializable;
import java.util.List;

/**
 * 精彩一刻和开怀一刻的Bean
 * @author Guanboren
 *
 */
public class RecreationBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int title;
	
	private List<RecreationItem> list;

	

	public int getTitle() {
		return title;
	}

	public void setTitle(int title) {
		this.title = title;
	}

	public List<RecreationItem> getList() {
		return list;
	}

	public void setList(List<RecreationItem> list) {
		this.list = list;
	}
	
	
	
}
