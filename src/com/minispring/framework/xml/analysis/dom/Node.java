package com.minispring.framework.xml.analysis.dom;

import java.util.ArrayList;
import java.util.List;

import com.minispring.framework.xml.analysis.Iterator;
import com.minispring.framework.xml.analysis.IteratorFactory;

/**
 * 
 * @author 	杨亚龙
 * @date	2018-3-7
 * xml对应的节点类
 * 可生成迭代器进行迭代
 *
 */
public class Node {

	private Node parent;
	private String name;
	private List<Attribute> attributes=new ArrayList<>();
	private List<Node> childs=new ArrayList<>();
	private String text;
	
	public void pushAttr(Attribute attribute){
		attributes.add(attribute);
	}
	
	public Attribute getAttr(String attrName){
		Attribute attribute_=null;
		for(Attribute attribute:attributes){
			if(attribute.getName().equals(attrName)){
				attribute_=attribute;
				break;
			}
		}
		return attribute_;
	}
	
	public Iterator<Node> getIterator(){
		Iterator<Node> iterator=IteratorFactory.getIterator(this);
		return iterator;
	}
	
	public String attributeValue(String attrName){
		String value=null;
		Attribute attribute=getAttr(attrName);
		if(attribute!=null){
			value=attribute.getValue();
		}
		return value;
	}
	
	public void pushChild(Node child){
		childs.add(child);
	}
	
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Attribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	public List<Node> getChilds() {
		return childs;
	}

	public void setChilds(List<Node> childs) {
		this.childs = childs;
	}
	
	
	
}
