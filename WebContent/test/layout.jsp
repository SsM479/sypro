<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
	<title>Insert title here</title>
	<jsp:include page="inc.jsp"></jsp:include>
</head>
<body id="blayout">
	<div region="north" title="North Title" split="true" href="north.html" style="height:100px;"></div>
	<div region="south" title="South Title" split="true" style="height:100px">
		<input type="button" value="展开" onclick="cc.layout('collapse','west')" name="" id="" />
	</div>
	<div region="east" iconcls="icon-reload" title="East" split="true" style="width:100px;"></div>
	<div region="west" split="true" title="功能导航" style="width:200px;"></div>
	<div region="center" iconCls="icon-save" title="欢迎" href="center.html" style="overflow: hidden;"></div>
</body>
<script type="text/javascript">
	var cc;
	$(function(){
		cc = $('#blayout').layout();
		cc.layout('expand', 'west');
		cc.layout('collapse','south');
	})
	
	function getCenterPanel(){
		var centerPanel = $('#blayout').layout('panel','center');
		console.info(centerPanel.panel('options').title);
	}
</script>

</html>