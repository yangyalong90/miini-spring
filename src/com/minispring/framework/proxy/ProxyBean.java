package com.minispring.framework.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class ProxyBean implements InvocationHandler,MethodInterceptor{

	private Object target;
	
	private Map<String, Method> adviceMap;
	
	public ProxyBean(Object target){
		this.target=target;
	}
	
	public ProxyBean(Object target,Map<String, Method> adviceMap){
		this.target=target;
		setMethodMap(adviceMap);
	}
	
	public void setMethodMap(Map<String, Method> methodMap){
		this.adviceMap=methodMap;
	}
	
	
	public Object getProxy(){
		int interfaceLem=target.getClass().getInterfaces().length;
		Object proxy=null;
		if(interfaceLem==0){
			proxy=getCGlibProxy();
		}else{
			proxy=getJDKProxy();
		}
		return proxy;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		beforeMethod(args);
		Object result=method.invoke(target, args);
		afterMethod(args);
		return result;
	}
	
	@Override
	public Object intercept(Object arg0, Method arg1, Object[] arg2, MethodProxy arg3) throws Throwable {
		beforeMethod(arg2);
		Object result=arg3.invoke(target, arg2);
		afterMethod(arg2);
		return result;
	}
	
	public Object getJDKProxy(){
		ClassLoader classLoader=target.getClass().getClassLoader();
		Class<?>[] interfaces=target.getClass().getInterfaces();
		Object obj=Proxy.newProxyInstance(classLoader, interfaces, this);
		return obj;
	}
	
	public Object getCGlibProxy(){
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(target.getClass());
		enhancer.setCallback(this);
		Object proxy=enhancer.create();
		return proxy;
	}
	
	public void beforeMethod(Object[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException{
		
		Method method=adviceMap.get("before");
		Class<?>[] parameterTypes= method.getParameterTypes();
		Object parameter=null;
		if(parameterTypes.length!=0){
			parameter=args;
		}
		Class<?> adviceClass=method.getDeclaringClass();
		method.invoke(adviceClass.newInstance(), parameter);
		
	}
	
	public void afterMethod(Object[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException{
		
		Method method=adviceMap.get("after");
		Class<?>[] parameterTypes= method.getParameterTypes();
		Object parameter=null;
		if(parameterTypes.length!=0){
			parameter=args;
		}
		Class<?> adviceClass=method.getDeclaringClass();
		method.invoke(adviceClass.newInstance(), parameter);
		
	}
	
	public void arroundMethod(Object[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException{
		
		Method method=adviceMap.get("arround");
		Class<?>[] parameterTypes= method.getParameterTypes();
		Object parameter=null;
		if(parameterTypes.length!=0){
			parameter=args;
		}
		Class<?> adviceClass=method.getDeclaringClass();
		method.invoke(adviceClass.newInstance(), parameter);
		
	}

	
	
}
