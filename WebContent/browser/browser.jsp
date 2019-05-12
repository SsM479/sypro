<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE HTML >
<html>
<head>
<jsp:include page="../inc.jsp"></jsp:include>
<link rel="stylesheet" href="${pageContext.request.contextPath}/browser/styles/style.css" type="text/css"></link>
<script type="text/javascript" src="${pageContext.request.contextPath}/browser/js/device.js"></script>
<style type="text/css">
img {
	cursor: pointer;
}
</style>
<script type="text/javascript">
	$(function() {
		$('#sypro').click(function() {
			location.replace('${pageContext.request.contextPath}/index.jsp');
		});
		$('#sshe').click(function() {
			$.messager.alert('提示', 'SSHE已经演示了好久了，目前暂时不演示SSHE示例项目，请看SyPro示例项目', 'info', function() {
				location.replace('${pageContext.request.contextPath}/index.jsp');
			});
		});

		$.messager.alert('提示', 'SyPro示例项目并未完全写完，大家先凑合预览一下吧，我会尽量抽出业余时间完成所有功能。<br/><div align="center"><h1>请点击SyPro图片进入演示系统！</h1><div>', 'info');
	});
</script>
</head>
<body>
	<div id="deviceInfo">
		<div id="deviceInfoBox"></div>
		<p></p>
		<div style="text-align: center;">
			<img id="sypro" alt="进入SyPro示例项目" src="${pageContext.request.contextPath}/style/images/sypro.png" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img id="sshe" alt="进入SSHE示例项目" src="${pageContext.request.contextPath}/style/images/sshe.png" />
		</div>
	</div>
</body>
</html>
