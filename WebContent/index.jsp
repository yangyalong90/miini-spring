<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<script type="text/javascript">

/**
 * 创建httpRequest对象
 * @param url
 * @param type
 * @param parameter
 * @param resultFunction
 * @returns {Boolean}
 */
function createHttpRequest(url,type,parameter,resultFunction){		
	var httpRequest=null;
	//创建XMLHttpRequest对象
	if(window.XMLHttpRequest){
		//非IE浏览器
		httpRequest=new XMLHttpRequest();
	}else if(window.ActiveXObject){
		//IE浏览器，IE浏览器有两种创建方式(根据IE版本的不同)
		try{
			httRequest=new ActiveXObject("Msxml2.XMLHTTP");	
		}catch(e){
			httRequest=new ActiveXObject("Microsoft.XMLHTTP");	
		}		
	}
	//如果创建失败（httpRequest==null）
	if(!httpRequest){
		alert("不能创建XMLHttpRequest实例");
		return false;
	}
	httpRequest.onreadystatechange=function(){
		if(httpRequest.readyState==4){	
			if(httpRequest.status==200){			
				resultFunction(httpRequest.responseText);
			}
		}
	}
	httpRequest.open(type,url,true);
	if(type=="POST"){
		httpRequest.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	}
	httpRequest.send(parameter);
	return httpRequest;
}



/**
 * 将json串转化为js对象串
 * @param json
 * @returns
 */
function getJsOfJson(json){
	return JSON.parse(json);
}

/**
 * 提交form表单(默认以post提交)
 * @param formId
 * @param url
 */
function submitForm(formId,url,type){
	//使用jQuery序列化form表单数据
	var params=$("#"+formId).serialize();
	//将表单数据从url转换编码utf-8
	params = decodeURIComponent(params,true);
	createHttpRequest(url,type==null?'POST':type,params);
}


function result(data){
	console.log(data);
	var persons=getJsOfJson(data);
	console.log(persons["a"]);
}

function cli(){
	var inp=document.getElementById("name");
	var name=inp.value;
	console.log(inp);
	createHttpRequest("web/go_.action","GET",null,result);
}

</script>
<body>
<input id="name">
<a href="javascript:void(0)" onclick="cli()">test</a>
<form action="web/upload.action" enctype="multipart/form-data"  method="post">
	<input id="filename" name="name">
	<span id="span">上传文件</span>
   	<input id="file" type="file" name="file"><br>
  	<br>
   	<button class="but" type="submit">确定</button><br>
   	<button class="but"  type="button">取消</button>
</form>
</body>
</html>