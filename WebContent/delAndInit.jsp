<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>删除数据并初始化数据库</title>
</head>
<body>
	init database....
	<script type="text/javascript">
		window.setTimeout(function() {
			window.location.replace('${pageContext.request.contextPath}/repairController/delAndRepair.action');
		}, 1000);
	</script>
</body>
</html>
