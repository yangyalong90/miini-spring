package com.minispring.framework.handle;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author 	杨亚龙
 * @date	2018-2-12
 * 处理器工厂，并存储处理器映射
 *
 */
public class HandleFactory {

	/**
	 * 处理器映射map，通过url映射handle
	 */
	private static Map<String, Handle> handleMap=new HashMap<>();
	
	public static Handle getHandle(String url){
		return handleMap.get(url);
	}
	
	public static void putHandle(String url,Handle handle){
		handleMap.put(url, handle);
	}

	public static Map<String, Handle> getHandleMap() {
		return handleMap;
	}

	public static void setHandleMap(Map<String, Handle> handleMap) {
		HandleFactory.handleMap = handleMap;
	}	
	
}
