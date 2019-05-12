<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<script type="text/javascript">
	$(function() {
		$('#admin_buggl_datagrid').datagrid({
			url : '${pageContext.request.contextPath}/bugController/datagrid.action',
			fit : true,
			fitColumns : true,
			border : false,
			pagination : true,
			idField : 'id',
			pageSize : 10,
			pageList : [ 10, 20, 30, 40, 50 ],
			sortName : 'createdatetime',
			sortOrder : 'desc',
			checkOnSelect : false,
			selectOnCheck : false,
			nowrap : false,
			frozenColumns : [ [ {
				title : '编号',
				field : 'id',
				width : 150,
				sortable : true,
				checkbox : true
			}, {
				title : 'BUG名称',
				field : 'name',
				width : 150,
				sortable : true
			} ] ],
			columns : [ [ {
				title : '添加时间',
				field : 'createdatetime',
				width : 150
			}, {
				title : 'BUG描述',
				field : 'note',
				width : 150,
				formatter : function(value, row, index) {
					return formatString('<span class="icon-search" style="display:inline-block;vertical-align:middle;width:16px;height:16px;"></span><a href="javascript:void(0);" onclick="admin_buggl_showNoteFun(\'{0}\');">查看详细</a>', index);
				}
			}, {
				field : 'action',
				title : '动作',
				width : 100,
				formatter : function(value, row, index) {
					return formatString('<img onclick="admin_buggl_editFun(\'{0}\');" src="{1}"/>&nbsp;<img onclick="admin_buggl_deleteFun(\'{2}\');" src="{3}"/>', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/pencil.png', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/cancel.png');
				}
			} ] ],
			toolbar : '#admin_buggl_toolbar'
		});
	});

	function admin_buggl_showNoteFun(index) {
		var rows = $('#admin_buggl_datagrid').datagrid('getRows');
		var row = rows[index];
		$('<div/>').dialog({
			title : 'BUG名称[' + row.name + ']',
			modal : true,
			maximizable : true,
			width : 640,
			height : 480,
			content : '<iframe src="${pageContext.request.contextPath}/bugController/showNote.action?id=' + row.id + '" frameborder="0" style="border:0;width:100%;height:99%;"></iframe>',
			onClose : function() {
				$(this).dialog('destroy');
			}
		});

		$('#admin_buggl_datagrid').datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
	}

	function admin_buggl_editFun(id) {
		$('#admin_buggl_datagrid').datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
		$('<div/>').dialog({
			href : '${pageContext.request.contextPath}/bugController/bugglEdit.action?id=' + id,
			width : 500,
			height : 450,
			modal : true,
			title : '编辑BUG',
			buttons : [ {
				text : '编辑',
				iconCls : 'icon-edit',
				handler : function() {
					var d = $(this).closest('.window-body');
					$('#admin_bugglEdit_editForm').form({
						url : '${pageContext.request.contextPath}/bugController/edit.action',
						success : function(result) {
							try {
								var r = $.parseJSON(result);
								if (r.success) {
									$('#admin_buggl_datagrid').datagrid('updateRow', {
										index : $('#admin_buggl_datagrid').datagrid('getRowIndex', id),
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
					$('#admin_bugglEdit_editForm').submit();
				}
			} ],
			onClose : function() {
				$(this).dialog('destroy');
			},
			onLoad : function() {
			}
		});
	}
	function admin_buggl_appendFun() {
		$('#admin_buggl_datagrid').datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
		$('<div/>').dialog({
			href : '${pageContext.request.contextPath}/admin/bugglAdd.jsp',
			width : 500,
			height : 450,
			modal : true,
			title : '添加BUG',
			buttons : [ {
				text : '增加',
				iconCls : 'icon-add',
				handler : function() {
					var d = $(this).closest('.window-body');
					$('#admin_bugglAdd_addForm').form({
						url : '${pageContext.request.contextPath}/bugController/add.action',
						success : function(result) {
							try {
								var r = $.parseJSON(result);
								if (r.success) {
									$('#admin_buggl_datagrid').datagrid('insertRow', {
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
					$('#admin_bugglAdd_addForm').submit();
				}
			} ],
			onClose : function() {
				$(this).dialog('destroy');
			}
		});
	}
	function admin_buggl_removeFun() {
		var rows = $('#admin_buggl_datagrid').datagrid('getChecked');
		var ids = [];
		if (rows.length > 0) {
			$.messager.confirm('确认', '您是否要删除当前选中的项目？', function(r) {
				if (r) {
					for ( var i = 0; i < rows.length; i++) {
						ids.push(rows[i].id);
					}
					$.ajax({
						url : '${pageContext.request.contextPath}/bugController/remove.action',
						data : {
							ids : ids.join(',')
						},
						dataType : 'json',
						success : function(result) {
							if (result.success) {
								$('#admin_buggl_datagrid').datagrid('load');
								$('#admin_buggl_datagrid').datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
							}
							$.messager.show({
								title : '提示',
								msg : result.msg
							});
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
	function admin_buggl_deleteFun(id) {
		$('#admin_buggl_datagrid').datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
		$('#admin_buggl_datagrid').datagrid('checkRow', $('#admin_buggl_datagrid').datagrid('getRowIndex', id));
		admin_buggl_removeFun();
	}
</script>
<table id="admin_buggl_datagrid"></table>
<div id="admin_buggl_toolbar" style="display: none;">
	<a href="javascript:void(0);" onclick="admin_buggl_appendFun();" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" style="float: left;">增加</a>
	<div class="datagrid-btn-separator"></div>
	<a href="javascript:void(0);" onclick="admin_buggl_removeFun();" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" style="float: left;">批量删除</a>
	<div class="datagrid-btn-separator"></div>
	<input id="admin_buggl_searchbox" class="easyui-searchbox" style="width:150px;" data-options="searcher:function(value,name){$('#admin_buggl_datagrid').datagrid('load',{name:value});},prompt:'可模糊查询BUG名称'"></input> <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="$('#admin_buggl_datagrid').datagrid('load',{});$('#admin_buggl_searchbox').searchbox('setValue','');">清空条件</a>
</div>