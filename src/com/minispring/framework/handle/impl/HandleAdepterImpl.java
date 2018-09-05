package com.minispring.framework.handle.impl;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.minispring.framework.annotation.QRequestParameter;
import com.minispring.framework.annotation.QResponseBody;
import com.minispring.framework.handle.Handle;
import com.minispring.framework.handle.HandleAdepter;
import com.minispring.framework.json.JSON;
import com.minispring.framework.json.JsonFactory;
import com.minispring.framework.uploadfile.FileFormHandle;
import com.minispring.framework.uploadfile.MultipartFile;
import com.minispring.framework.xml.ReaderXml;

/**
 * 
 * @author 杨亚龙
 * @date	2018-3-7
 * 执行处理器handle的适配器adepter
 */
public class HandleAdepterImpl implements HandleAdepter{
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	private Handle handle;
	private Class<?>[] parameterTypes;
	
	public HandleAdepterImpl(){
		
	}
	
	public HandleAdepterImpl(HttpServletRequest req, HttpServletResponse resp){
		this.request=req;
		this.response=resp;
	}

	@Override
	public Object invokeHandle(){
		Object obj=null;
		if(handle==null){
			return null;
		}
		Method method=handle.getHandlerMethor();
		Object instance=handle.getInstance();
		Object[] args=handle.getArgs();
		try {
			obj=method.invoke(instance, args);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		if(method.isAnnotationPresent(QResponseBody.class)){
			obj=doJson(obj);
			try {
				response.getWriter().write((String)obj);
				response.getWriter().flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			if(obj!=null && obj.getClass().getSimpleName().equalsIgnoreCase("String")){
				try {
					doPageSkip(obj.toString());
				} catch (IOException | ServletException e) {
					e.printStackTrace();
				}
			}
			
		}
		return obj;
	}

	@Override
	public String doJson(Object obj){
		JSON json=JsonFactory.getJson(obj);
		return json.getJsonData();
	}

	@Override
	public void doPageSkip(String url) throws IOException, ServletException{
		int index=url.indexOf(":");
		if(index!=-1 && url.substring(0, index+1).equals(FORWARD)){
			url=url.substring(index+1);
			request.getRequestDispatcher(url).forward(request, response);
		}else if(index!=-1 && url.substring(0, index+1).equals(REDIRECT)){
			String contextPath=request.getContextPath();
			url=contextPath+"/"+url.substring(index+1);
			response.sendRedirect(url);
		}else{
			request.getRequestDispatcher(url).forward(request, response);
		}
	}

	@Override
	public Handle getHandle() {
		return handle;
	}

	@Override
	public void setHandle(Handle handle) {
		if(handle==null){
			return;
		}
		parameterTypes=handle.getHandlerMethor().getParameterTypes();
		if(parameterTypes.length!=0){
			handle.setArgs(new Object[parameterTypes.length]);
		}
		this.handle = handle;
		setRequestParameterMap();
	}

	@Override
	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	@Override
	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	@Override
	public void setRequestParameterMap() {
		Map<String, String[]> requestParameterMap = request.getParameterMap();
		Method method=handle.getHandlerMethor();
		Annotation[][] annotationss=method.getParameterAnnotations();
		//是否为上传文件
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		FileFormHandle fileHandle=null;
		if(isMultipart){
			fileHandle=new FileFormHandle(request);
			requestParameterMap=fileHandle.getRequestParameterMap();
		}
		parameterTypes:
		for (int i=0;i<parameterTypes.length;i++) {
			Annotation[] annotations=annotationss[i];
			for(Annotation annotation:annotations){
				if(annotation.annotationType().equals(QRequestParameter.class)){					
					if(isMultipart&&method.getParameterTypes()[i].equals(MultipartFile.class)){
						String parameterName = ((QRequestParameter)annotation).value();
						MultipartFile multipartFile=fileHandle.getMultipartFile(parameterName);
						handle.getArgs()[i]=multipartFile;
						continue parameterTypes;
					}
					String parameterName = ((QRequestParameter)annotation).value();
					String[] strs=requestParameterMap.get(parameterName);
					setRequestParamAnnoValue(i,parameterTypes[i],strs);
				}
			}
			if(annotations.length==0&&isBeanType(parameterTypes[i])){
				setBeanParameterValue(i,parameterTypes[i],requestParameterMap);
			}
			if(parameterTypes[i].getName().equals(HttpSession.class.getName())){
				handle.getArgs()[i]=request.getSession();
			}else if(parameterTypes[i].getName().equals(HttpServletRequest.class.getName())){
				handle.getArgs()[i]=request;
			}else if(parameterTypes[i].getName().equals(HttpServletResponse.class.getName())){
				handle.getArgs()[i]=response;
			}
		}
	}


	private boolean isBeanType(Class<?> cla){
		boolean flag=true;
		String className=cla.getName();
		if(!className.matches(ReaderXml.getbeanPackage())){
			flag=false;
		}
		return flag;
	}

	private void setBeanParameterValue(int parameterIndex,Class<?> cla,Map<String, String[]> requestParameterMap){
		Field[] fields=cla.getDeclaredFields();
		Object obj=null;
		Object objValue=null;
		try {
			obj=cla.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		for(Field field:fields){
			String fieldName=field.getName();
			String[] parameterValues=requestParameterMap.get(fieldName);
			if(field.getType().equals(String.class)){
				objValue=parameterValues[0];
			}else{
				String fieldType=field.getType().getName();
				switch(fieldType){
					case "int":
						objValue=Integer.valueOf(parameterValues[0]);
						break;
					case "Integer":
						objValue=Integer.valueOf(parameterValues[0]);
						break;
					case "double":
						objValue=Double.valueOf(parameterValues[0]);
						break;
					case "Double":
						objValue=Double.valueOf(parameterValues[0]);
						break;
				}
			}
			try {
				field.setAccessible(true);
				field.set(obj, objValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		handle.getArgs()[parameterIndex]=obj;
	}

	private void setRequestParamAnnoValue(int parameterIndex,Class<?> cla,String[] strs){
		if(parameterTypes[parameterIndex].equals(List.class)){
			List<String> list=new ArrayList<>();
			for(String str:strs){
				list.add(str);
			}
			handle.getArgs()[parameterIndex]=list;
		}else{
			if(strs==null){
				handle.getArgs()[parameterIndex]=null;
			}else{
				handle.getArgs()[parameterIndex]=strs[0];
			}
			
		}
	}
	
	
}
