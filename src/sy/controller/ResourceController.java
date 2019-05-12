package sy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sy.pageModel.Json;
import sy.pageModel.Resource;
import sy.service.ResourceServiceI;

@Controller
@RequestMapping("/resourceController")
public class ResourceController {

	private ResourceServiceI resourceService;

	public ResourceServiceI getResourceService() {
		return resourceService;
	}

	@Autowired
	public void setResourceService(ResourceServiceI resourceService) {
		this.resourceService = resourceService;
	}

	@RequestMapping("/treegrid")
	@ResponseBody
	public List<Resource> treegrid() {
		return resourceService.treegrid();
	}

	@RequestMapping("/allTreeNode")
	@ResponseBody
	public List<Resource> allTreeNode() {
		return resourceService.allTreeNode();
	}

	@RequestMapping("/add")
	@ResponseBody
	public Json add(Resource resource) {
		Json j = new Json();
		j.setSuccess(true);
		j.setObj(resourceService.add(resource));
		j.setMsg("添加成功!");
		return j;
	}

	@RequestMapping("/remove")
	@ResponseBody
	public Json remove(String id) {
		Json j = new Json();
		resourceService.remove(id);
		j.setSuccess(true);
		j.setObj(id);
		j.setMsg("删除成功!");
		return j;
	}

	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(Resource resource) {
		Json j = new Json();
		j.setSuccess(true);
		j.setObj(resourceService.edit(resource));
		j.setMsg("编辑成功!");
		return j;
	}

}
