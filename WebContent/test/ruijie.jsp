<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html>
<head>
<title>Insert title here</title>
<style type="text/css">
	.pro:nth-child(odd){
		text-align: right;
	}
	.pro:nth-child(even){
		text-align: right;
	}
</style>
<jsp:include page="inc.jsp"></jsp:include>
</head>
<body style="font-size:30px;">
	<table id="tab" border="1" cellspacing="0" cellpadding="0">
		<tr class="pro">
			<td style="width:120px;">产品名称</td>
			<td><input type="text" name="" id="proName" /></td>
			<td style="width:120px;">产品编号</td>
			<td><input type="text" name="" id="proNum" /></td>
			<td><input type="button" name="" id="add" value="添加"/></td>
		</tr>
		<tr id="box2">
			<td style="width:120px;">生产类别:</td>
			<td><input type="checkbox" name="" id="evt" /></td>
			<td><input type="checkbox" name="" id="dvt" /></td>
		</tr>
	</table>
</body>
<script type="text/javascript">
setTr = function(){
	return '<tr class="pro">'
	+'<td style="width:120px;">产品名称</td>'
	+'<td><input type="text" name="" id="proName" /></td>'
	+'<td style="width:120px;">产品编号</td>'
	+'<td><input type="text" name="" id="proNum" /></td>'
	+'</tr>';
}
	
$(function(){
	$('#add').click(function(){
		$('#box2').before(setTr());
	})
})
</script>
</html>