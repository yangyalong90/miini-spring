package com.minispring.framework.uploadfile;

import java.io.InputStream;

/**
 * 
 * @author 	杨亚龙
 * @date	2018-3-1
 * 上传文件所对应的类对象
 *
 */
public class MultipartFile {
	
	private InputStream inputStream;
	private String fileName;

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	

}
