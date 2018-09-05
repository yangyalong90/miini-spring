package com.minispring.framework.xml.analysis;

/**
 * 
 * @author 	杨亚龙
 * @date	2018-3-7
 * 迭代器
 *
 * @param <T>
 */
public interface Iterator<T> {
	
	public boolean hasNext();
	
	public T next();
	
}
