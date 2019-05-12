<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<script type="text/javascript">
	$(function() {
		$('#admin_yhglAdd_combogrid').combogrid({
			multiple : true,
			nowrap : false,
			url : '${pageContext.request.contextPath}/roleController/combogrid.action',
			panelWidth : 450,
			panelHeight : 200,
			idField : 'id',
			textField : 'text',
			pagination : true,
			fitColumns : true,
			rownumbers : true,
			editable : true,
			mode : 'remote',
			delay : 500,
			pageSize : 5,
			pageList : [ 5, 10 ],
			columns : [ [ {
				field : 'id',
				title : '编号',
				width : 150,
				hidden : true
			}, {
				field : 'text',
				title : '角色名称',
				width : 150
			}, {
				title : '可访问资源ID',
				field : 'resourceIds',
				width : 300,
				hidden : true
			}, {
				title : '可访问资源',
				field : 'resourceNames',
				width : 300
			} ] ]
		});
	});
</script>
<div align="center">
	<form id="admin_yhglAdd_addForm" method="post">
		<table class="tableForm">
			<tr>
				<th style="width: 100px;">编号</th>
				<td><input name="id" readonly="readonly" />
				</td>
				<th style="width: 80px;">登录名称</th>
				<td><input name="name" class="easyui-validatebox" data-options="required:true" />
				</td>
			</tr>
			<tr>
				<th>密码</th>
				<td><input name="pwd" type="password" class="easyui-validatebox" data-options="required:true" style="width: 150px;" />
				</td>
				<th>创建时间</th>
				<td><input name="createdatetime" readonly="readonly" />
				</td>
			</tr>
			<tr>
				<th>最后修改时间</th>
				<td><input name="modifydatetime" readonly="readonly" />
				</td>
				<th>所属角色</th>
				<td><input id="admin_yhglAdd_combogrid" name="roleIds" style="width: 155px;" />
				</td>
			</tr>
		</table>
	</form>
</div>