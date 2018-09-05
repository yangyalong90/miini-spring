package com.minispring.framework.json;

import com.minispring.framework.json.impl.JSONImpl;

/**
 * 
 * @author 	杨亚龙
 * @date	2018-3-4
 * JSON工厂
 *
 */
public class JsonFactory {

	public static JSON getJson(){
		return new JSONImpl();
	}
	
	public static JSON getJson(Object obj){
		return new JSONImpl(obj);
	}
	
}
