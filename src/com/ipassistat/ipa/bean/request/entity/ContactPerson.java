package com.ipassistat.ipa.bean.request.entity;

import java.io.Serializable;

import android.graphics.Bitmap;

public class ContactPerson implements Serializable{  
	
	private static final long serialVersionUID = 1L;
	private String p_id;
    private String name;  //姓名
    private boolean isPerson = true ;
    private String pinYinName;  //拼音
    private String phoneNum;  //电话号码
    private Bitmap headImag;   //联系人头像
    private boolean isSelected = false; //是否被选中
    private boolean isRec = false;    //是否推荐过
    private int itemType;//0是字母 1是条目
    
    private String emailType;
    private String numberType; 
  	private boolean havesend;//发送状态
  	private boolean isSuccess;//是否成功
  	private String letter;//首字母
  	
  	private String email;
    public int getItemType() {
		return itemType;
	}
	public boolean isRec() {
		return isRec;
	}

	public boolean isHavesend() {
		return havesend;
	}
	public void setHavesend(boolean havesend) {
		this.havesend = havesend;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public void setRec(boolean isRec) {
		this.isRec = isRec;
	}

	public void setItemType(int itemType) {
		this.itemType = itemType;
	}



	public ContactPerson(){} 
   
    
    
    @Override
	public String toString() {
		return "Person [name=" + name + ", isPerson=" + isPerson
				+ ", pinYinName=" + pinYinName + ", phoneNum=" + phoneNum
				+ ", headImag=" + headImag + ", isRecommend=" + isSelected
				+ "]";
	}



	public ContactPerson(String name){
    	
    	this.name = name ;
    } 
    public ContactPerson(boolean isPerson){
    	
    	this.isPerson = isPerson ;
    } 
    
    public ContactPerson(String name,String pinYinName,int itemType,String phoneNum)
    {
    	this.name = name;
    	this.pinYinName = pinYinName;
    	this.phoneNum = phoneNum ;
    	this.itemType =itemType;
    }
    
    public String getName() {  
        return name;  
    }  
  
    public void setName(String name) {  
        this.name = name;  
    }  
  
    public String getPinYinName() {  
        return pinYinName;  
    }  
  
    public void setPinYinName(String pinYinName) {  
        this.pinYinName = pinYinName;  
    }

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public Bitmap getHeadImag() {
		return headImag;
	}

	public void setHeadImag(Bitmap headImag) {
		this.headImag = headImag;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isRecommend) {
		this.isSelected = isRecommend;
	}

	public boolean isPerson() {
		return isPerson;
	}

	public void setPerson(boolean isPerson) {
		this.isPerson = isPerson;
	}

	public String getP_id() {
		return p_id;
	}


	public void setP_id(String p_id) {
		this.p_id = p_id;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the emailType
	 */
	public String getEmailType() {
		return emailType;
	}
	/**
	 * @param emailType the emailType to set
	 */
	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}
	/**
	 * @return the numberType
	 */
	public String getNumberType() {
		return numberType;
	}
	/**
	 * @param numberType the numberType to set
	 */
	public void setNumberType(String numberType) {
		this.numberType = numberType;
	}
	/**
	 * @return the letter
	 */
	public String getLetter() {
		return letter;
	}
	/**
	 * @param letter the letter to set
	 */
	public void setLetter(String letter) {
		this.letter = letter;
	}  
	
	
}  

