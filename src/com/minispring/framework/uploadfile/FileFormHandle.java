package com.minispring.framework.uploadfile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.minispring.framework.xml.ReaderXml;

/**
 * 
 * @author 	杨亚龙
 * @date	2018-3-1
 * 上传文件所对应的处理器
 *
 */
public class FileFormHandle {

	private HttpServletRequest request;
	
	private Map<String, String[]> requestParameterMap=new HashMap<>();
	
	private Map<String, MultipartFile> multipartFileMap=new HashMap<>();
	
	public FileFormHandle(HttpServletRequest request){
		this.request=request;
		try {
			setMultipartFileMap();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public MultipartFile getMultipartFile(String parameterName){
		return multipartFileMap.get(parameterName);
	}
	
	public Map<String, String[]> getRequestParameterMap(){
		return requestParameterMap;
	}
	
	public void setMultipartFileMap() throws Exception{
		// 获得磁盘文件条目工厂
		FileItemFactory factory = new DiskFileItemFactory();
		// 高水平的API文件上传处理，将文件保存在服务器硬盘
		ServletFileUpload upload = new ServletFileUpload(factory);
		//设置上传的单个文件的大小10M
		upload.setSizeMax(10*1024*1024);
		//设置上传的总文件的大小40M
		upload.setFileSizeMax(40*1024*1024);
		//http中的复杂表单元素都被看作是FileItem,所以可以上传多个文件
		List<FileItem> fileItemList = upload.parseRequest(request);
		//如果表单内容不为空
		if (fileItemList != null) {
			//遍历内容
			for (FileItem fileItem: fileItemList) {		
			
				boolean isFile=!fileItem.isFormField();
				if (isFile) {
					InputStream in=fileItem.getInputStream();
					String fileName=fileItem.getName();
					MultipartFile multipartFile=new MultipartFile();
					multipartFile.setInputStream(in);
					multipartFile.setFileName(fileName);
					multipartFileMap.put(fileItem.getFieldName(), multipartFile);
					//清除应用服务器下的临时文件
					fileItem.delete();
				}else{
					String parameterName=fileItem.getFieldName();
					String value=fileItem.getString(ReaderXml.getCharacterEncoding());
					String[] values={value};
					requestParameterMap.put(parameterName, values);
				}
			}
		}
	}
	
}
