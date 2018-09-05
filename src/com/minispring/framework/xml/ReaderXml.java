package com.minispring.framework.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import com.minispring.framework.bean.XmlBean;
import com.minispring.framework.xml.analysis.AnalysisFactory;
import com.minispring.framework.xml.analysis.AnalysisXml;
import com.minispring.framework.xml.analysis.Iterator;
import com.minispring.framework.xml.analysis.IteratorFactory;
import com.minispring.framework.xml.analysis.dom.Node;

/**
 * 
 * @author 	杨亚龙
 * @date	2018-3-2
 * 读取核心配置文件xml
 *
 */
public class ReaderXml {

	private String xmlPath;
	private List<XmlBean> xmlBeans=new ArrayList<>();
	private String scanPackage;
	private static String characterEncoding;
	private static String beanPackage;
	
	public ReaderXml(String xmlPath) {
		this.xmlPath=xmlPath;
	}
	
	public void read() throws IOException{
		read(xmlPath);
	}
	
	public void read(String xmlPath) throws IOException{
		InputStream inputStream=this.getClass().getClassLoader().getResourceAsStream(xmlPath);
		read(inputStream);
		
	}
	
	public void read(InputStream inputStream) throws IOException{
		AnalysisXml analysisXml=AnalysisFactory.getAnalysisXml();
		analysisXml.read(inputStream);
		Node root=analysisXml.getRoot();
		Iterator<Node> iterator=IteratorFactory.getIterator(root);
		while(iterator.hasNext()){
			Node node=iterator.next();
			String nodeName=node.getName();
			if("bean".equals(nodeName)){
				pushXmlBean(node);
			}else if("scan".equals(nodeName)){
				setScanPackage(node);
			}else if("characterEncoding".equals(nodeName)){
				setCharacterEncoding(node);
			}else if("beanpackage".equals(nodeName)){
				setBeanPackage(node);
			}
		}
	}
		
	
	
	
	public void pushXmlBean(Node node){
		XmlBean xmlBean=new XmlBean();
		String id=node.attributeValue("id");
		String className=node.attributeValue("class");
		xmlBean.setId(id);
		xmlBean.setClassName(className);
		xmlBeans.add(xmlBean);
	}
	
	public void setScanPackage(Node node){
		scanPackage=node.attributeValue("package");
	}
	
	public List<XmlBean> getXmlBeans(){
		return xmlBeans;
	}
	
	public String getScanPackage() {
		return scanPackage;
	}
	
	public void setCharacterEncoding(Node node){
		String encoding=node.attributeValue("encoding");
		setCharacterEncoding(encoding);
	}
	
	public static void setCharacterEncoding(String encoding){
		characterEncoding=encoding;
	}
	
	public void setBeanPackage(Node node){
		String package_=node.attributeValue("package");
		setBeanPackage(package_);
	}
	
	public static void setBeanPackage(String package_){
		beanPackage=package_;
	}
	
	
	public static String getCharacterEncoding(){
		if(characterEncoding==null){
			return "GBK";
		}
		return characterEncoding;
	}
	
	public static String getbeanPackage(){
		if(beanPackage==null){
			return "bean";
		}
		return beanPackage;
	}
	
}
