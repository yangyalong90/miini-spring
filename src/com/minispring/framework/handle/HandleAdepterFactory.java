package com.minispring.framework.handle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minispring.framework.handle.impl.HandleAdepterImpl;

/**
 * 
 * @author 杨亚龙
 * @date	2018-2-12
 * 适配器工厂
 *
 */
public class HandleAdepterFactory {

	public static HandleAdepter getHandleAdepter(){
		return new HandleAdepterImpl();
	}
	
	public static HandleAdepter getHandleAdepter(HttpServletRequest req, HttpServletResponse resp){
		return new HandleAdepterImpl(req,resp);
	}
	
}
