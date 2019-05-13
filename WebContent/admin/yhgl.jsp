<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<script type="text/javascript">
	$(function() {

		$('#admin_yhgl_datagrid').datagrid({
			url : '${pageContext.request.contextPath}/userController/datagrid.action',
			fit : true,
			fitColumns : true,
			border : false,
			pagination : true,
			idField : 'id',
			pageSize : 10,
			pageList : [ 10, 20, 30, 40, 50 ],
			sortName : 'name',
			sortOrder : 'asc',
			checkOnSelect : false,
			selectOnCheck : false,
			nowrap : false,
			frozenColumns : [ [ {
				field : 'id',
				title : '编号',
				width : 150,
				checkbox : true
			}, {
				field : 'name',
				title : '登录名称',
				width : 80,
				sortable : true
			} ] ],
			columns : [ [ {
				field : 'pwd',
				title : '密码',
				width : 60,
				formatter : function(value, row, index) {
					return '******';
				}
			}, {
				field : 'createdatetime',
				title : '创建时间',
				width : 150,
				sortable : true
			}, {
				field : 'modifydatetime',
				title : '最后修改时间',
				width : 150,
				sortable : true
			}, {
				field : 'roleIds',
				title : '所属角色ID',
				width : 150,
				hidden : true
			}, {
				field : 'roleNames',
				title : '所属角色名称',
				width : 150
			}, {
				field : 'action',
				title : '动作',
				width : 100,
				formatter : function(value, row, index) {
					if (row.id == '0') {
						return '系统用户';
					} else {
						return formatString('<img onclick="admin_yhgl_editFun(\'{0}\');" src="{1}"/>&nbsp;<img onclick="admin_yhgl_deleteFun(\'{2}\');" src="{3}"/>&nbsp;<img onclick="admin_yhgl_modifyPwdFun(\'{4}\');" src="{5}"/>', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/pencil.png', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/cancel.png', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/lock/lock_edit.png');
					}
				}
			} ] ],
			toolbar : [ {
				text : '增加',
				iconCls : 'icon-add',
				handler : function() {
					admin_yhgl_appendFun();
				}
			}, '-', {
				text : '批量删除',
				iconCls : 'icon-remove',
				handler : function() {
					admin_yhgl_removeFun();
				}
			}, '-', {
				text : '批量设置角色',
				iconCls : 'icon-edit',
				handler : function() {
					admin_yhgl_modifyRoleFun();
				}
			}, '-' ]
		});

	});
	/* 过滤查找 */
	function admin_yhgl_searchFun() {
		$('#admin_yhgl_datagrid').datagrid('load', serializeObject($('#admin_yhgl_searchForm')));
	}
	/* 清空 */
	function admin_yhgl_cleanFun() {
		$('#admin_yhgl_searchForm input').val('');
		$('#admin_yhgl_datagrid').datagrid('load', {});
	}
	
	function admin_yhgl_editFun(id) {
		$('#admin_yhgl_datagrid').datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
		$('<div/>').dialog({
			href : '${pageContext.request.contextPath}/admin/yhglEdit.jsp',
			width : 520,
			height : 200,
			modal : true,
			title : '编辑用户',
			buttons : [ {
				text : '编辑',
				iconCls : 'icon-edit',
				handler : function() {
					var d = $(this).closest('.window-body');
					$('#admin_yhglEdit_editForm').form('submit', {
						url : '${pageContext.request.contextPath}/userController/edit.action',
						success : function(result) {
							try {
								var r = $.parseJSON(result);
								if (r.success) {
									$('#admin_yhgl_datagrid').datagrid('updateRow', {
										index : $('#admin_yhgl_datagrid').datagrid('getRowIndex', id),
										row : r.obj
									});
									d.dialog('destroy');
								}
								$.messager.show({
									title : '提示',
									msg : r.msg
								});
							} catch (e) {
								$.messager.alert('提示', result);
							}
						}
					});
				}
			} ],
			onClose : function() {
				$(this).dialog('destroy');
			},
			onLoad : function() {
				var index = $('#admin_yhgl_datagrid').datagrid('getRowIndex', id);
				var rows = $('#admin_yhgl_datagrid').datagrid('getRows');
				var o = rows[index];
				o.roleIds = stringToList(rows[index].roleIds);
				$('#admin_yhglEdit_editForm').form('load', o);
			}
		});
	}
	function admin_yhgl_appendFun() {
		$('#admin_yhgl_datagrid').datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
		$('<div/>').dialog({
			href : '${pageContext.request.contextPath}/admin/yhglAdd.jsp',
			width : 520,
			height : 200,
			modal : true,
			title : '添加用户',
			buttons : [ {
				text : '增加',
				iconCls : 'icon-add',
				handler : function() {
					var d = $(this).closest('.window-body');
					$('#admin_yhglAdd_addForm').form('submit', {
						url : '${pageContext.request.contextPath}/userController/add.action',
						success : function(result) {
							try {
								var r = $.parseJSON(result);
								if (r.success) {
									$('#admin_yhgl_datagrid').datagrid('insertRow', {
										index : 0,
										row : r.obj
									});
									d.dialog('destroy');
								}
								$.messager.show({
									title : '提示',
									msg : r.msg
								});
							} catch (e) {
								$.messager.alert('提示', result);
							}
						}
					});
				}
			} ],
			onClose : function() {
				$(this).dialog('destroy');
			}
		});
	}
	function admin_yhgl_removeFun() {
		var rows = $('#admin_yhgl_datagrid').datagrid('getChecked');
		var ids = [];
		if (rows.length > 0) {
			$.messager.confirm('确认', '您是否要删除当前选中的项目？', function(r) {
				if (r) {
					var currentUserId = '${sessionInfo.userId}';/*当前登录用户的ID*/
					var flag = false;
					for ( var i = 0; i < rows.length; i++) {
						if (currentUserId != rows[i].id) {
							ids.push(rows[i].id);
						} else {
							flag = true;
						}
					}
					$.ajax({
						url : '${pageContext.request.contextPath}/userController/remove.action',
						data : {
							ids : ids.join(',')
						},
						dataType : 'json',
						success : function(result) {
							if (result.success) {
								$('#admin_yhgl_datagrid').datagrid('load');
								$('#admin_yhgl_datagrid').datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
							}
							if (flag) {
								$.messager.show({
									title : '提示',
									msg : '不可以删除自己！'
								});
							} else {
								$.messager.show({
									title : '提示',
									msg : result.msg
								});
							}
						}
					});
				}
			});
		} else {
			$.messager.show({
				title : '提示',
				msg : '请勾选要删除的记录！'
			});
		}
	}
	function admin_yhgl_deleteFun(id) {
		$('#admin_yhgl_datagrid').datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
		$('#admin_yhgl_datagrid').datagrid('checkRow', $('#admin_yhgl_datagrid').datagrid('getRowIndex', id));
		admin_yhgl_removeFun();
	}
	function admin_yhgl_modifyPwdFun(id) {
		$('<div/>').dialog({
			href : '${pageContext.request.contextPath}/admin/yhglEditPwd.jsp',
			width : 300,
			height : 200,
			modal : true,
			title : '编辑用户密码',
			buttons : [ {
				text : '编辑',
				iconCls : 'icon-edit',
				handler : function() {
					var d = $(this).closest('.window-body');
					$('#admin_yhglEditPwd_editForm').form('submit', {
						url : '${pageContext.request.contextPath}/userController/modifyPwd.action',
						success : function(result) {
							try {
								var r = $.parseJSON(result);
								if (r.success) {
									d.dialog('destroy');
									$('#admin_yhgl_datagrid').datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
								}
								$.messager.show({
									title : '提示',
									msg : r.msg
								});
							} catch (e) {
								$.messager.alert('提示', result);
							}
						}
					});
				}
			} ],
			onClose : function() {
				$(this).dialog('destroy');
			},
			onLoad : function() {
				$('#admin_yhglEditPwd_editForm').form('load', {
					id : id
				});
			}
		});
	}
	function admin_yhgl_modifyRoleFun() {
		var rows = $('#admin_yhgl_datagrid').datagrid('getChecked');
		var ids = [];
		if (rows.length > 0) {
			for ( var i = 0; i < rows.length; i++) {
				ids.push(rows[i].id);
			}
			$('<div/>').dialog({
				href : '${pageContext.request.contextPath}/admin/yhglEditRole.jsp',
				width : 300,
				height : 200,
				modal : true,
				title : '批量编辑用户角色',
				buttons : [ {
					text : '编辑',
					iconCls : 'icon-edit',
					handler : function() {
						var d = $(this).closest('.window-body');
						$('#admin_yhglEditRole_editForm').form('submit', {
							url : '${pageContext.request.contextPath}/userController/modifyRole.action',
							success : function(result) {
								try {
									var r = $.parseJSON(result);
									if (r.success) {
										$('#admin_yhgl_datagrid').datagrid('load');
										$('#admin_yhgl_datagrid').datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
										d.dialog('destroy');
									}
									$.messager.show({
										title : '提示',
										msg : r.msg
									});
								} catch (e) {
									$.messager.alert('提示', result);
								}
							}
						});
					}
				} ],
				onClose : function() {
					$(this).dialog('destroy');
				},
				onLoad : function() {
					$('#admin_yhglEditRole_editForm').form('load', {
						ids : ids
					});
				}
			});
		} else {
			$.messager.show({
				title : '提示',
				msg : '请勾选要编辑的记录！'
			});
		}
	}
</script>
<div class="easyui-layout" data-options="fit : true,border : false">
	<div data-options="region:'north',title:'查询条件',border:false" style="height: 165px;overflow: hidden;" align="center">
		<form id="admin_yhgl_searchForm">
			<table class="tableForm">
				<tr>
					<th style="width: 170px;">检索用户名称(可模糊查询)</th>
					<td><input name="name" style="width: 315px;" /></td>
				</tr>
				<tr>
					<th>创建时间</th>
					<td><input name="createdatetimeStart" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" />至<input name="createdatetimeEnd" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" /></td>
				</tr>
				<tr>
					<th>最后修改时间</th>
					<td><input name="modifydatetimeStart" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" />至<input name="modifydatetimeEnd" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" /></td>
				</tr>
			</table>
			<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="admin_yhgl_searchFun();">过滤条件</a> <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="admin_yhgl_cleanFun();">清空条件</a>
		</form>
	</div>
	<div data-options="region:'center',border:false">
		<table id="admin_yhgl_datagrid"></table>
	</div>
</div>