package com.minispring.framework.handle;

import java.io.IOException;

import javax.servlet.ServletException;

/**
 * 
 * @author 	杨亚龙
 * @date	2018-3-5
 * 执行处理器handle的适配器adepter
 *
 */
public interface HandleAdepter {
	
	static final String FORWARD="forward:";
	static final String REDIRECT="redirect:";
	
	
	public Object invokeHandle();
	
	public String doJson(Object obj);
	
	public void doPageSkip(String url) throws IOException, ServletException;
	
	public Handle getHandle();
	
	public void setHandle(Handle handle);
	
	public Class<?>[] getParameterTypes();
	
	public void setParameterTypes(Class<?>[] parameterTypes);
	
	public void setRequestParameterMap();


	
}
