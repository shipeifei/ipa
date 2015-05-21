package com.ipassistat.ipa.bean.response.entity;  

import java.io.Serializable;

/**  
=======
package com.ichsy.mboss.bean.response.entity;

/**
>>>>>>> .r19203
 * 
 * Package: com.ichsy.mboss.bean.response.entity
 * 
 * File: RecreationVo.java
 * 
 * @author: LiuYuHang Date: 2015年2月3日
 * 
 *          Modifier： Modified Date： Modify：
 * 
 *          Copyright @ 2015 Corpration CHSY
 * 
 */
public class RecreationVo implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1694883220227681120L;
	private int imgId;
	private String title;
	
	
	private int img;
	
	private String uplattime;

	private String imgname;

	private String playhour;
	
	private String playtime;
	
	

	public int getImg() {
		return img;
	}

	public String getImgname() {
		return imgname;
	}

	public void setImgname(String imgname) {
		this.imgname = imgname;
	}

	public String getUplattime() {
		return uplattime;
	}

	public void setUplattime(String uplattime) {
		this.uplattime = uplattime;
	}

	public String getPlayhour() {
		return playhour;
	}

	public void setPlayhour(String playhour) {
		this.playhour = playhour;
	}

	public String getPlaytime() {
		return playtime;
	}

	public void setPlaytime(String playtime) {
		this.playtime = playtime;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImg(int img) {
		this.imgId = img;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
