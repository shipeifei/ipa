package com.ipassistat.ipa.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ipassistat.ipa.bean.request.entity.ContactPerson;




public class RemoveSameUtils {

	/**
	 * 传入存储联系人的list 返回去重后的list  
	 * @param list
	 * @return
	 */
	public static void removeSame(List<ContactPerson>list){
		List<ContactPerson> newList;//存储去重后的新的list
		Map<String, String> map;//用于去除重复
		boolean isSelected = false;//记录复选框是否被勾选
		boolean isSend = false;//记录发送状态是否被钩选
		String TAG="removeSame";
		
		newList = new ArrayList<ContactPerson>();
		map = new HashMap<String, String>();
		
		if(list.size()>0)
		{
			
			//将list中的联系人存入map中 进行去重
			for (ContactPerson contactPerson : list) {
				map.put(contactPerson.getPhoneNum(), contactPerson.getName());
			}
			//遍历map生成新的list:需遍历list 确定条目的选中和发送状态 两个属性的状态
			for (Map.Entry<String, String> entry : map.entrySet()) {
				String num = entry.getKey().toString();
				String name = entry.getValue().toString();
				for (ContactPerson person : list) {
					//只要有一条选中或者是已发送 就设为是true
					//遍历确定复选框是否被钩选
					if (!isSelected) {
						
						if(person.getPhoneNum().equals(num))
						{
							if(person.isSelected())
								isSelected = true;
						}
					}
					//遍历确定发送状态
					if(!isSend){
						if(person.getPhoneNum().equals(num))
						{
							if(person.isHavesend())
							isSend = true;
						}
					}
					//是否跳出循环
					if(isSelected&&isSend)
					{
						break;
					}
					
				}
				//转化成bean 存储
				ContactPerson contact = new ContactPerson();
				contact.setPhoneNum(num);
				contact.setName(name);
				contact.setSelected(isSelected);
				contact.setHavesend(isSend);
				newList.add(contact);
				isSelected=false;
				isSend=false;
			}
		}
		list.clear();
		list.addAll(newList);
		
	}
}
