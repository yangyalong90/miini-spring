package com.minispring.framework.proxy;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ProxyFactory {

	private static Method[] adviceMethods;
	
	public static Object getProxy(Object target,Class<?> adviceClass,Map<String,String> adviceMap){
		adviceMethods=adviceClass.getMethods();
		Map<String, Method> methodMap=getMethodMap(adviceMap);
		ProxyBean proxy=new ProxyBean(target, methodMap);
		Object obj=proxy.getProxy();
		return obj;
		
	}
	
	private static Map<String, Method> getMethodMap(Map<String,String> adviceMap){
		Map<String, Method> methodMap=new HashMap<>();
		for(Entry<String, String> entry:adviceMap.entrySet()){
			String methodName=entry.getValue();
			Method method=getMethodByName(methodName);
			if(method!=null){
				methodMap.put(entry.getKey(), method);
			}
		}
		return methodMap;
	}
	
	private static Method getMethodByName(String name){
		Method method=null;
		for(Method m:adviceMethods){
			String methodName=m.getName();
			if(methodName.equals(name)){
				method=m;
				break;
			}
		}
		return method;
	}
	
}
