package sy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sy.pageModel.DataGrid;
import sy.pageModel.Json;
import sy.pageModel.Role;
import sy.service.RoleServiceI;

@Controller
@RequestMapping("/roleController")
public class RoleController {

	private RoleServiceI roleService;

	public RoleServiceI getRoleService() {
		return roleService;
	}

	@Autowired
	public void setRoleService(RoleServiceI roleService) {
		this.roleService = roleService;
	}

	@RequestMapping("/datagrid")
	@ResponseBody
	public DataGrid datagrid(Role role) {
		return roleService.datagrid(role);
	}

	@RequestMapping("/add")
	@ResponseBody
	public Json add(Role role) {
		Json j = new Json();
		try {
			Role r = roleService.save(role);
			j.setSuccess(true);
			j.setMsg("添加成功！");
			j.setObj(r);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@RequestMapping("/remove")
	@ResponseBody
	public Json remove(Role role) {
		roleService.remove(role.getIds());
		Json j = new Json();
		j.setSuccess(true);
		j.setMsg("删除成功！");
		return j;
	}

	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(Role role) {
		Json j = new Json();
		try {
			Role u = roleService.edit(role);
			j.setSuccess(true);
			j.setMsg("编辑成功！");
			j.setObj(u);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@RequestMapping("/combogrid")
	@ResponseBody
	public DataGrid combogrid(Role role) {
		return roleService.datagrid(role);
	}

}
