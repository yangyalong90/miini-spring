package com.minispring.framework.handle;

import java.lang.reflect.Method;

/**
 * 
 * @author	 杨亚龙
 * @date	2018-2-11
 * 处理请求（执行controller）的处理器类
 *
 */
public class Handle {

	private Object instance;
	private Method handlerMethor;
	private Object[] args;
	
	
	public Method getHandlerMethor() {
		return handlerMethor;
	}
	public void setHandlerMethor(Method handlerMethor) {
		this.handlerMethor = handlerMethor;
	}
	public Object getInstance() {
		return instance;
	}
	public void setInstance(Object instance) {
		this.instance = instance;
	}
	public Object[] getArgs() {
		return args;
	}
	public void setArgs(Object[] args) {
		this.args = args;
	}
	
	
}
