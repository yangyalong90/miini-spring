package com.minispring.framework;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minispring.framework.handle.Handle;
import com.minispring.framework.handle.HandleAdepter;
import com.minispring.framework.handle.HandleAdepterFactory;
import com.minispring.framework.handle.HandleFactory;
import com.minispring.framework.xml.ReaderXml;
/**
 * 
 * @author 杨亚龙
 * @date 	2018-2-10
 * 核心servlet
 *
 */
@SuppressWarnings("serial")
public class MiniDispatcherServlet extends HttpServlet {
	
	@Override
	public void init(ServletConfig config) throws ServletException {		
		super.init(config);
		String configPath=config.getInitParameter("contextConfigLocation");
		InitServletConfig.doLoadConfig(configPath);
		InitServletConfig.doXmlLoader();
		InitServletConfig.doScannerAnno();
		InitServletConfig.doInjection();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String encoding=ReaderXml.getCharacterEncoding();
		req.setCharacterEncoding(encoding);
		resp.setContentType("text/html;charset="+encoding);
		resp.setCharacterEncoding(encoding);
		String contextPath=req.getContextPath();
		String url=req.getRequestURI().replaceAll(contextPath, "");
		Handle handle=HandleFactory.getHandle(url);
		HandleAdepter adepter=HandleAdepterFactory.getHandleAdepter(req, resp);
		adepter.setHandle(handle);
		adepter.invokeHandle();
	}
	
	
	
}
