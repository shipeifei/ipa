package com.ipassistat.ipa.bean.response.entity;
/**
 * 积分变动
 * author:lxc
 */
public class ScoredChange {
	private String level_name;//	 String	等级名称	
	private String score_unit;//	 String	积分单位	
	private int level;//	 int	等级编号	
	private int score;//	 int	获得积分	
	public String getLevel_name() {
		return level_name;
	}
	public void setLevel_name(String level_name) {
		this.level_name = level_name;
	}
	public String getScore_unit() {
		return score_unit;
	}
	public void setScore_unit(String score_unit) {
		this.score_unit = score_unit;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	

}
