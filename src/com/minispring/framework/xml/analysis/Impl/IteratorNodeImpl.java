package com.minispring.framework.xml.analysis.Impl;

import com.minispring.framework.xml.analysis.Iterator;
import com.minispring.framework.xml.analysis.dom.Node;

/**
 * 
 * @author 	杨亚龙
 * @date	2018-3-7
 * 节点迭代器
 *
 */
public class IteratorNodeImpl implements Iterator<Node>{

	private Node root;
	private int index=-1;
	private int childsSize;
	
	public IteratorNodeImpl(Node node){
		root=node;
		childsSize=node.getChilds().size();
	}
	
	@Override
	public boolean hasNext() {
		boolean flag=false;
		if(index<childsSize-1){
			flag=true;
		}
		return flag;
	}

	@Override
	public Node next() {
		index++;
		Node node=null;
		if(index<childsSize){
			node=root.getChilds().get(index);
		}
		return node;
	}


}
