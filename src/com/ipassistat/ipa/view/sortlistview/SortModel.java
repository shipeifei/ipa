package com.ipassistat.ipa.view.sortlistview;

import java.io.Serializable;

public class SortModel<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;  
	private String sortLetters;  
	private T t;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
	public T getT() {
		return t;
	}
	public void setT(T t) {
		this.t = t;
	}
	
	
}
