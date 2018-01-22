/**
 * Created by .JA
 * Company 北京众信金融
 */

$(function() {
	initConfig();// 加载配置
});

function initConfig(){
	$("#form").validate();
	setDefaultValues();//加载cookie值
}

// 增加一列表名
function addItem(){
	var item = "";
	item += "<div class='form-group'>"
 		+"<label class='col-lg-2 control-label'>表名</label>"
 		+"<div class='col-xs-3'><input type='text' name='tableNames' class='form-control' /></div>"
 		+"<label class='col-lg-2 control-label'>模型名</label>"
 		+"<div class='col-xs-3'><input type='text' name='modelNames' class='form-control' /></div>"
 		+"<a class='btn btn-success btn-xs' onclick='addItem()' title='增加'><span class='fui-check'>&nbsp;增加</span></a>&nbsp;"
 		+"<a class='btn btn-danger btn-xs' onclick='redItem(this)' title='删除'><span class='fui-cross'>&nbsp;删除</span></a>"
 		+"</div>";
	$("#form").append(item);
}

//删除一列表名
function redItem(para){
	$(para).parent().remove();
}

//生成并下载
function doSubmit(){
	if($("#form").valid(this,'填写信息不完整。') == false){
		return;
	}
	if (typeof($("#submitBtn")) != "undefined") { 
		$("#submitBtn").attr("disabled", "disabled");
	}
	var url =  basePath +'/generatorServlet.do?' + Math.random();
	$.ajax({
		type : 'post',
		url : url,
		dataType : 'text',
		data : $("#form").serialize(),
		success : function (data, textStatus) {
			$("#submitBtn").removeAttr("disabled");
			data = JSON.parse(data);
			if(data.rspCode == "000001"){
				alert("数据库连接失败，请检查您的数据库地址和数据库名");
			}else if(data.rspCode == "000002"){
				alert("数据库连接错误");
			}else if(data.rspCode == "000003"){
				alert("数据库连接错误");
			}else if(data.rspCode == "000004"){
				alert("发生错误，请检查您的用户名或密码");
			}else if(data.rspCode == "000005"){
				alert("操作失败");
			}else{
				//alert("操作成功");
				doStorageCookies();//存储到cookie
				window.open(basePath + "/tmp" +data.zipName);
			}
	    },
		error: function(data, textStatus){
			alert('操作失败');
			$("#submitBtn").removeAttr("disabled");
		}
	});
}

//加载上一次存储的cookie值
function setDefaultValues(){
	$("#connection").val(getCookie("connection"));
	$("#port").val(getCookie("port") != "" ? getCookie("port") : "3306");
	$("#dataBase").val(getCookie("dataBase"));
	$("#userId").val(getCookie("userId") != "" ? getCookie("userId") : "root");
	$("#userPass").val(getCookie("userPass"));
	$("#modelPath").val(getCookie("modelPath"));
	$("#daoPath").val(getCookie("daoPath"));
	$("#mappingPath").val(getCookie("mappingPath"));
}

//将值存储到cookie，下次加载时调取
function doStorageCookies(){
	var connection = $("#connection").val();//数据库连接
	var port = $("#port").val();//端口号
	var dataBase = $("#dataBase").val();//数据库
	var userId = $("#userId").val();//用户名
	var userPass = $("#userPass").val();//密码
	var modelPath = $("#modelPath").val();//模型的包名路径
	var daoPath = $("#daoPath").val();//DAO的包名路径
	var mappingPath = $("#mappingPath").val();//映射文件的路径
	addCookie("connection",connection,180);
	addCookie("port",port,180);
	addCookie("dataBase",dataBase,180);
	addCookie("userId",userId,180);
	addCookie("userPass",userPass,180);
	addCookie("modelPath",modelPath,180);
	addCookie("daoPath",daoPath,180);
	addCookie("mappingPath",mappingPath,180);
}

//存储cookie值
//name:cookie键
//value:cookie值
//expiresDays:过期时间（单位：天）
function addCookie(name, value, expiresDays) {
	var cookieString = name + "=" + escape(value);
	// 判断是否设置过期时间
	if (expiresDays > 0) {
		var date = new Date();
		date.setTime(date.getTime + expiresDays * 24 * 3600 * 1000);
		cookieString = cookieString + "; expires=" + date.toGMTString();
	}
	document.cookie = cookieString;
} 

//获取cookie值
function getCookie(name){ 
	var strCookie=document.cookie; 
	var arrCookie=strCookie.split("; "); 
	for(var i=0;i<arrCookie.length;i++){ 
		var arr=arrCookie[i].split("="); 
		if(arr[0]==name)return unescape(arr[1]); 
	} 
	return "";
}

//删除cookie值
function deleteCookie(name){ 
	var date=new Date(); 
	date.setTime(date.getTime()-99999999*24*3600*1000); 
	document.cookie=name+"=; expires="+date.toGMTString(); 
}