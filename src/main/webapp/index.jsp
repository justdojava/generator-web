<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>Generator在线</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<link rel="stylesheet" type="text/css"
	href="css/vendor/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/flat-ui.css">
<link rel="stylesheet" type="text/css" href="css/demo.css">
<link rel="shortcut icon" href="img/favicon.ico">

</head>

<body style="background-color: #1abc9c;">
	<style>
      body {
        min-height: 800px;
        padding-top: 65px;
      }
    </style>

	<!-- Static navbar -->
    <div class="navbar navbar-default navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
          </button>
          <a class="navbar-brand" href="#">Generator-Web</a>
        </div>
        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
          </ul>
          <ul class="nav navbar-nav navbar-right">
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </div>
	<div class="container login-form">
		<form class="form-horizontal" role="form" id="form">
		
			<div class="form-group">
				<label for="connection" class="col-lg-2 control-label">数据库连接</label>
				<div class="col-lg-10">
					<input type="text" class="form-control" placeholder="192.168.0.90" id="connection" name="connection" required/>
				</div>
			</div>
			
			<div class="form-group">
				<label for="port" class="col-lg-2 control-label">端口号</label>
				<div class="col-xs-4">
					<input type="text" placeholder="3306" value="3306" id="port" name="port" class="form-control" required/>
				</div>
				<label for="dataBase" class="col-lg-2 control-label">数据库</label>
				<div class="col-xs-4">
					<input type="text" placeholder="databaseName" id="dataBase" name="dataBase" class="form-control" required/>
				</div>
			</div>
			
			<div class="form-group">
				<label for="userId" class="col-lg-2 control-label">用户名</label>
				<div class="col-xs-4">
					<input type="text" placeholder="root" id="userId" name="userId" class="form-control" required/>
				</div>
				<label for="userPass" class="col-lg-2 control-label">密码</label>
				<div class="col-xs-4">
					<input type="text" placeholder="password" id="userPass" name="userPass" class="form-control" required/>
				</div>
			</div>
			
			<div class="form-group">
				<label for="modelPath" class="col-lg-2 control-label">模型的包名路径</label>
				<div class="col-lg-10">
					<input type="text" class="form-control" placeholder="com.yourCompany.core.entity" id="modelPath" name="modelPath" required/>
				</div>
			</div>
			
			<div class="form-group">
				<label for="daoPath" class="col-lg-2 control-label">DAO的包名路径</label>
				<div class="col-lg-10">
					<input type="text" class="form-control" placeholder="com.yourCompany.core.dao" id="daoPath" name="daoPath" required/>
				</div>
			</div>
			
			<div class="form-group">
				<label for="mappingPath" class="col-lg-2 control-label">映射文件的路径</label>
				<div class="col-lg-10">
					<input type="text" class="form-control" placeholder="mybatis.mapper" id="mappingPath" name="mappingPath" required/>
				</div>
			</div>
			<div class="form-group">
				<label for="mappingPath" class="col-lg-2 control-label">模型是否驼峰命名</label>
				<div class="col-lg-10">
					<select id="isHump" name="isHump" class="form-control">
						<option value="0" >是</option>
						<option value="1" >否</option>
					</select>
				</div>
			</div>
			
			<div class="form-group">
				<label for="tableNames" class="col-lg-2 control-label">表名</label>
				<div class="col-xs-3">
					<input type="text" id="tableNames" name="tableNames" class="form-control" placeholder="table_name" required/>
				</div>
				<label for="modelNames" class="col-lg-2 control-label">模型名</label>
				<div class="col-xs-3">
					<input type="text" id="modelNames" name="modelNames" class="form-control" placeholder="TableName" required/>
				</div>
				<a class="btn btn-success btn-xs" onclick="addItem()" title="增加">
					<span class="fui-check">&nbsp;增加</span>
				</a>
			</div>

		</form>
		
		<div class="form-group">
			<div class="col-md-12" style="text-align: center;">
	        	<a id="submitBtn" href="javascript:void(0);" onclick="doSubmit();" style="width: 200px;" class="btn btn-lg btn-primary "><span class="fui-check-circle"></span>&nbsp;生成并下载</a>
	        </div>
		</div>
	
	</div>
	<!-- /container -->
	<footer> </footer>
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script src="js/jquery.min.js"></script>
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="js/flat-ui.min.js"></script>
	<script src="js/jquery.form.js"></script>
	<script src="js/jquery.validate.min.js"></script>
	<script src="js/index.js"></script>
	<script>
		var basePath = '<%=path%>';
	</script>
</body>
</html>