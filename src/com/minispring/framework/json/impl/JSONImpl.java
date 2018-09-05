package com.minispring.framework.json.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.minispring.framework.json.JSON;
import com.minispring.framework.xml.ReaderXml;

/**
 * 
 * @author 杨亚龙
 * @date	2018-3-7
 * 实现JSON数据接口，设置对应JSON数据
 *
 */
public class JSONImpl implements JSON{

	private Object obj;
	private String jsonData;
	
	public JSONImpl(){
		
	}
	
	public JSONImpl(Object obj){
		this.obj=obj;
		setJsonData();
	}
	
	@Override
	public String getJsonData(){
		return jsonData;
	}
	
	@Override
	public String toString() {
		return getJsonData();
	}
	
	@Override
	public void setJsonObj(Object obj){
		this.obj=obj;
		setJsonData();
	}
	
	@SuppressWarnings("unchecked")
	private void setJsonData(){
		Class<?> cla=obj.getClass();
		String classSimpleName=cla.getSimpleName();
		
		if(classSimpleName.equalsIgnoreCase("ArrayList")
				|| classSimpleName.equalsIgnoreCase("LinkList")){
			jsonData=listToJson((List<Object>)obj);
		}else if(classSimpleName.equalsIgnoreCase("HashMap")
				|| classSimpleName.equalsIgnoreCase("TreeMap")){
			jsonData=mapToJson((Map<Object,Object>)obj);
		}else if(classSimpleName.equalsIgnoreCase("String")){
			jsonData=obj.toString();
		}else{
			jsonData=objectToJson(obj);
		}

	}
	
	private String objectToJson(Object obj){
		if(obj==null){
			return null;
		}
		String beanpackage=ReaderXml.getbeanPackage();
		String claName=obj.getClass().getName();
		StringBuffer json=new StringBuffer("{");
		if(claName.indexOf(beanpackage)!=-1){
			Field[] fields=obj.getClass().getDeclaredFields();	
			String t_="\"";
			String t_t="\" : ";
			String t_t_="\" ,";
			for(Field field:fields){
				try {
					String fieldName=field.getName();
					field.setAccessible(true);
					Object fieldValue=field.get(obj);
					json.append(t_).append(fieldName).append(t_t);
					if(fieldValue==null){
						json.append("null ,");
					}else{
						json.append(t_+fieldValue+t_t_);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}else{
			json.append(obj.toString());
			json.append(" ");
		}
		String objectJsonValue=json.length()>1?json.substring(0, json.length()-1):json.toString();
		objectJsonValue+="}";
		return objectJsonValue;
	}
	
	private String listToJson(List<Object> list){
		StringBuffer json=new StringBuffer("[");
		String objectJsonValue=null;
		String t_=",";
		for(Object obj:list){
			objectJsonValue=objectToJson(obj);
			json.append(objectJsonValue);
			json.append(t_);
		}
		objectJsonValue=json.length()>1?json.substring(0, json.length()-1):json.toString();
		objectJsonValue+="]";
		return objectJsonValue;
	}
	
	private String mapToJson(Map<Object,Object> map){
		StringBuffer json=new StringBuffer("{");
		String objectJsonValue=null;
		String t_="\"";
		String t_t="\" : ";
		String t_t_=",";
		for(Entry<Object, Object> entry:map.entrySet()){
			String key=entry.getKey().toString();
			Object value=entry.getValue();
			json.append(t_).append(key).append(t_t);
			json.append(objectToJson(value)).append(t_t_);
		}
		objectJsonValue=json.length()>1?json.substring(0, json.length()-1):json.toString();
		objectJsonValue+="}";
		return objectJsonValue;
	}
	
	public String upperFirst(String word){
		char[] words=word.toCharArray();
		words[0]-=32;
		word=String.copyValueOf(words);
		return word;
	}
	
}
