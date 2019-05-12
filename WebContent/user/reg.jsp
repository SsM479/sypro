<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<script type="text/javascript">
	$(function() {
		$('#user_reg_regForm').form({
			url : '${pageContext.request.contextPath}/userController/reg.action',
			success : function(result) {
				var r = $.parseJSON(result);
				if (r.success) {
					$('#user_reg_regDialog').dialog('close');
				}
				$.messager.show({
					title : '提示',
					msg : r.msg
				});
			}
		});

		$('#user_reg_regDialog').show().dialog({
			modal : true,
			title : '用户注册',
			closed : true,
			width : 250,
			height : 180,
			buttons : [ {
				text : '注册',
				handler : function() {
					$('#user_reg_regForm').submit();
				}
			} ]
		});
	});
</script>
<div id="user_reg_regDialog" style="display: none;overflow: hidden;">
	<div align="center">
		<form method="post" id="user_reg_regForm">
			<table class="tableForm">
				<tr>
					<th>登录名</th>
					<td><input name="name" class="easyui-validatebox" data-options="required:true" />
					</td>
				</tr>
				<tr>
					<th>密码</th>
					<td><input type="password" name="pwd" class="easyui-validatebox" data-options="required:true" style="width: 150px;" /></td>
				</tr>
				<tr>
					<th>重复密码</th>
					<td><input type="password" name="rePpwd" class="easyui-validatebox" data-options="required:true,validType:'eqPwd[\'#user_reg_regForm input[name=pwd]\']'" style="width: 150px;" /></td>
				</tr>
			</table>
		</form>
	</div>
</div>