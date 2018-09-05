package com.minispring.framework.xml.analysis;

import com.minispring.framework.xml.analysis.Impl.AnalysisXmlImpl;

/**
 * 
 * @author 	杨亚龙
 * @date	2018-3-6
 * xml解析工厂
 *
 */
public class AnalysisFactory {

	public static AnalysisXml getAnalysisXml(){
		return new AnalysisXmlImpl();
	}
	
}
