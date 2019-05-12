<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" charset="utf-8">
	$(function() {
		$('#user_userInfo_tree').tree({
			url : '${pageContext.request.contextPath}/resourceController/allTreeNode.action',
			parentField : 'pid',
			lines : true,
			checkbox : true,
			onLoadSuccess : function(node, data) {
				var idList = stringToList($('#user_userInfo_form input[name=resourceIds]').val());
				if (idList.length > 0) {
					for ( var i = 0; i < idList.length; i++) {
						var n = $('#user_userInfo_tree').tree('find', idList[i]);
						$('#user_userInfo_tree').tree('check', n.target);
					}
				}
				$('#user_userInfo_tree').unbind();
			}
		});
	});
</script>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'east',title:'可访问资源',split:true" style="width: 200px;">
		<ul id="user_userInfo_tree"></ul>
	</div>
	<div data-options="region:'center',title:'个人信息'" style="overflow: hidden;padding: 5px;" align="center">
		<form id="user_userInfo_form" method="post">
			<input name="id" type="hidden" value="${sessionInfo.userId}" />
			<table class="tableForm">
				<tr>
					<th style="width: 55px;">登录名</th>
					<td><input readonly="readonly" value="${sessionInfo.loginName}" /></td>
				</tr>
				<tr>
					<th>登录IP</th>
					<td><input readonly="readonly" value="${sessionInfo.ip}" /></td>
				</tr>
				<tr>
					<th>修改密码</th>
					<td><input name="pwd" type="password" class="easyui-validatebox" data-options="required:'true',missingMessage:'请填写登录密码'" /></td>
				</tr>
				<tr>
					<th>所属角色</th>
					<td><textarea readonly="readonly" style="height: 40px;width: 150px;">${sessionInfo.roleNames}</textarea></td>
				</tr>
				<tr style="display: none;">
					<th>权限ID</th>
					<td><input name="resourceIds" value="${sessionInfo.resourceIds}" /></td>
				</tr>
			</table>
		</form>
	</div>
</div>