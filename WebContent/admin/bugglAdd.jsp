<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<script type="text/javascript">
	$(function() {
		$('#admin_bugglAdd_textarea').xheditor({
			tools : 'full',
			html5Upload : true,
			upMultiple : 4,
			upLinkUrl : '${pageContext.request.contextPath}/bugController/upload.action',
			upLinkExt : 'zip,rar,txt,doc,docx,xls,xlsx',
			upImgUrl : '${pageContext.request.contextPath}/bugController/upload.action',
			upImgExt : 'jpg,jpeg,gif,png'
		});
	});
</script>
<div align="center">
	<form id="admin_bugglAdd_addForm" method="post">
		<table class="tableForm">
			<tr>
				<th>编号</th>
				<td><input name="id" readonly="readonly" />
				</td>
				<th>上报BUG名称</th>
				<td><input name="name" class="easyui-validatebox" data-options="required:true" />
				</td>
			</tr>
			<tr>
				<th>BUG描述</th>
				<td colspan="3"><textarea id="admin_bugglAdd_textarea" name="note" style="height: 260px;width: 400px;"></textarea>
				</td>
			</tr>
		</table>
	</form>
</div>