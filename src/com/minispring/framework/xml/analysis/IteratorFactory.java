package com.minispring.framework.xml.analysis;

import com.minispring.framework.xml.analysis.Impl.IteratorNodeImpl;
import com.minispring.framework.xml.analysis.dom.Node;

/**
 * 
 * @author 	杨亚龙
 * @date	2018-3-7
 * 迭代器工厂
 *
 */
public class IteratorFactory {

	/**
	 * 生成主节点的迭代器
	 * @param root	主节点
	 * @return		节点迭代器
	 */
	public static Iterator<Node> getIterator(Node root){
		return new IteratorNodeImpl(root);
	}
	
	
}
