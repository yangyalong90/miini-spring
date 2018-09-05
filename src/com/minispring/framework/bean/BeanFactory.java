package com.minispring.framework.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author 	杨亚龙
 * @date	2018-2-10
 * 存储bean，获取所产生的bean对象
 *
 */
public class BeanFactory {

	private static Map<String,Object> beanNameMap = new HashMap<>();
	private static Map<Class<?>,Object> beanTypeMap = new HashMap<>();
	
	
	
	public static Map<String, Object> getBeanNameMap() {
		return beanNameMap;
	}

	public static void setBeanNameMap(Map<String, Object> beanNameMap) {
		BeanFactory.beanNameMap = beanNameMap;
	}

	public static Map<Class<?>, Object> getBeanTypeMap() {
		return beanTypeMap;
	}

	public static void setBeanTypeMap(Map<Class<?>, Object> beanTypeMap) {
		BeanFactory.beanTypeMap = beanTypeMap;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name,Class<T> cla){
		return (T)beanNameMap.get(name);
	}
	
	public static Object getBean(String name){
		return beanNameMap.get(name);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> cla){
		return (T)beanTypeMap.get(cla);
	}
	
	public static void putBean(String objName,Object obj){
		beanNameMap.put(objName, obj);
	}
	
	public static void putBean(Class<?> cla,Object obj){
		beanTypeMap.put(cla, obj);
	}
	
}
