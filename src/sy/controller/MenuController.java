package sy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sy.pageModel.Json;
import sy.pageModel.Menu;
import sy.service.MenuServiceI;

@Controller
@RequestMapping("/menuController")
public class MenuController {

	private MenuServiceI menuService;

	public MenuServiceI getMenuService() {
		return menuService;
	}

	@Autowired
	public void setMenuService(MenuServiceI menuService) {
		this.menuService = menuService;
	}

	@RequestMapping("/allTreeNode")
	@ResponseBody
	public List<Menu> allTreeNode() {
		return menuService.allTreeNode();
	}

	@RequestMapping("/treegrid")
	@ResponseBody
	public List<Menu> treegrid() {
		return menuService.treegrid();
	}

	@RequestMapping("/remove")
	@ResponseBody
	public Json remove(String id) {
		Json j = new Json();
		menuService.remove(id);
		j.setSuccess(true);
		j.setObj(id);
		j.setMsg("删除成功!");
		return j;
	}

	@RequestMapping("/add")
	@ResponseBody
	public Json add(Menu menu) {
		Json j = new Json();
		j.setSuccess(true);
		j.setObj(menuService.add(menu));
		j.setMsg("添加成功!");
		return j;
	}

	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(Menu menu) {
		Json j = new Json();
		j.setSuccess(true);
		j.setObj(menuService.edit(menu));
		j.setMsg("编辑成功!");
		return j;
	}

}
