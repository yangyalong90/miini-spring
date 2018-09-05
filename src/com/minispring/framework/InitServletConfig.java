package com.minispring.framework;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.minispring.framework.annotation.QAutowired;
import com.minispring.framework.annotation.QController;
import com.minispring.framework.annotation.QDao;
import com.minispring.framework.annotation.QRequestMapping;
import com.minispring.framework.annotation.QResource;
import com.minispring.framework.annotation.QService;
import com.minispring.framework.bean.BeanFactory;
import com.minispring.framework.bean.XmlBean;
import com.minispring.framework.handle.Handle;
import com.minispring.framework.handle.HandleFactory;
import com.minispring.framework.xml.ReaderXml;

/**
 * 
 * @author 	杨亚龙
 * @date 	2018-2-10
 * 初始化MiniDispatcherServlet配置信息
 *
 */
public class InitServletConfig {
	
	private static ReaderXml readerXml;

	public static void doLoadConfig(String configPath){
		readerXml=new ReaderXml(configPath);
	}
	
	public static void doXmlLoader(){
		try {
			readerXml.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<XmlBean> xmlBeans=readerXml.getXmlBeans();
		loadXmlBean(xmlBeans);
	}
	
	private static void loadXmlBean(List<XmlBean> xmlBeans){
		for(XmlBean xmlBean:xmlBeans){
			String id=xmlBean.getId();
			String className=xmlBean.getClassName();
			Class<?> cla=null;
			Object obj=null;
			try {
				cla =  Class.forName(className);
				obj =cla.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
			BeanFactory.putBean(id, obj);
			Class<?>[] interfaces=null;
			try {
				interfaces=cla.getInterfaces();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(interfaces!=null&&interfaces.length!=0){
				for(Class<?> inter:interfaces){
					BeanFactory.putBean(inter, obj);
				}
			}
		}
	}
	
	public static void doScannerAnno(){
		String scannerPage=readerXml.getScanPackage();
		scannerPage=scannerPage.replaceAll("\\.", "/");
		doScanner(scannerPage);
	}
	
	
	public static void doInjection(){
		Map<String, Object> beanMap=BeanFactory.getBeanNameMap();
		Map<String, Handle> handleMap=HandleFactory.getHandleMap();
		doBeanInjection(beanMap);
		doConteollerInjection(handleMap);
	}
	
	public static Handle patternHandler(String url,Map<String, Handle> handlerMapping){
		Handle handler=handlerMapping.get(url);
		return handler;
	}
	
	public static void doScanner(String scannerPage){
		URL url=InitServletConfig.class.getClassLoader().getResource("/"+scannerPage);
		File file=new File(url.getFile());
		if (!file.exists()) {
			return;
		}
		if(file.isDirectory()){
			File[] files = file.listFiles();
			for(int i=0;i<files.length;i++){
				String fileName=files[i].getName();
				doScanner(scannerPage+"/"+fileName);
			}
		}else{
			String classPath=scannerPage.replaceAll("/", "\\.");
			classPath=classPath.replaceAll("\\.class", "");
			try {
				Class<?> clazz=Class.forName(classPath);
				if(clazz.isAnnotationPresent(QController.class)){
					setControllerAnno(clazz);
				}else if(clazz.isAnnotationPresent(QService.class)||clazz.isAnnotationPresent(QDao.class)){
					setBeanAnno(clazz);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static String firstLower(String str){
		char[] chars=str.toCharArray();
		chars[0]+=32;
		str=String.copyValueOf(chars);
		return str;
	}
	
	public static void doBeanInjection(Map<String, Object> beanMap){
		for(Entry<String, Object> entry:beanMap.entrySet()){
			Object obj=entry.getValue();
			setObjectFieldValueAnno(obj);
		}
	}
	
	public static void doConteollerInjection(Map<String, Handle> handleMap){
		for(Entry<String, Handle> entry:handleMap.entrySet()){
			Handle handle=entry.getValue();
			Object obj=handle.getInstance();
			setObjectFieldValueAnno(obj);
		}
	}
	
	public static void setObjectFieldValueAnno(Object obj){
		Class<?> cla=obj.getClass();
		Field[] fields=cla.getDeclaredFields();
		for(Field field:fields){
			field.setAccessible(true);
			if(field.isAnnotationPresent(QResource.class)){
				setReauorceAnno(obj, field);
			}else if(field.isAnnotationPresent(QAutowired.class)){
				setAutowiredAnno(obj, field);
			}
			
		}
	}
	
	public static void setControllerAnno(Class<?> cla){
		String url="";
		if(cla.isAnnotationPresent(QRequestMapping.class)){
			QRequestMapping requestMapping=cla.getAnnotation(QRequestMapping.class);
			url=requestMapping.value();
		}
		Method[] methods=cla.getMethods();
		Object instance=null;
		try {
			instance = cla.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		for(Method method:methods){
			if(method.isAnnotationPresent(QRequestMapping.class)){
				QRequestMapping requestMapping=method.getAnnotation(QRequestMapping.class);
				String specificUrl=url+requestMapping.value();
				Handle handle=new Handle();
				handle.setInstance(instance);
				handle.setHandlerMethor(method);
				HandleFactory.putHandle(specificUrl, handle);
			}
		}
	}
	
	public static void setBeanAnno(Class<?> cla){
		Object obj=null;
		Class<?>[] interfaces=null;
		try {
			obj = cla.newInstance();
			interfaces=cla.getInterfaces();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		if(interfaces!=null&&interfaces.length!=0){
			for(Class<?> inter:interfaces){
				String beanName=inter.getSimpleName();
				beanName=firstLower(beanName);
				BeanFactory.putBean(beanName, obj);
				BeanFactory.putBean(inter, obj);
			}
		}else{
			String beanName=cla.getSimpleName();
			beanName=firstLower(beanName);
			BeanFactory.putBean(beanName, obj);
			BeanFactory.putBean(cla, obj);
		}
		
	}
	
	
	public static void setReauorceAnno(Object obj,Field field){
		QResource resource=field.getAnnotation(QResource.class);
		try {
			field.set(obj, BeanFactory.getBean(resource.name()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void setAutowiredAnno(Object obj,Field field){
		Class<?> cla=field.getType();
		try {
			field.set(obj, BeanFactory.getBean(cla));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
