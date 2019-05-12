package sy.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sy.pageModel.DataGrid;
import sy.pageModel.Json;
import sy.pageModel.SessionInfo;
import sy.pageModel.User;
import sy.service.UserServiceI;
import sy.util.IpUtil;
import sy.util.ResourceUtil;

@Controller
@RequestMapping("/userController")
public class UserController {

	private UserServiceI userService;

	public UserServiceI getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(UserServiceI userService) {
		this.userService = userService;
	}

	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	@RequestMapping("/reg")
	@ResponseBody
	public Json reg(User user) {
		Json j = new Json();
		try {
			User u = userService.save(user);
			j.setSuccess(true);
			j.setMsg("注册成功！");
			j.setObj(u);
		} catch (Exception e) {
			j.setMsg("注册失败！用户名已存在！");
		}
		return j;
	}

	@RequestMapping("/login")
	@ResponseBody
	public Json login(User user, HttpServletRequest request, HttpSession session) {
		Json j = new Json();
		User u = userService.find(user);
		if (u != null) {
			j.setSuccess(true);
			j.setMsg("登录成功！");

			SessionInfo sessionInfo = new SessionInfo();
			sessionInfo.setUserId(u.getId());
			sessionInfo.setLoginName(u.getName());
			sessionInfo.setIp(IpUtil.getIpAddr(request));
			sessionInfo.setRoleIds(u.getRoleIds());
			sessionInfo.setRoleNames(u.getRoleNames());
			sessionInfo.setResourceIds(u.getResourceIds());
			sessionInfo.setResourceNames(u.getResourceNames());
			sessionInfo.setResourceUrls(u.getResourceUrls());
			session.setAttribute(ResourceUtil.getSessionInfoName(), sessionInfo);

			j.setObj(sessionInfo);
		} else {
			j.setMsg("登录失败！");
		}
		return j;
	}

	@RequestMapping("/logout")
	@ResponseBody
	public Json logout(HttpSession session) {
		if (session != null) {
			session.invalidate();
		}
		Json j = new Json();
		j.setSuccess(true);
		return j;
	}

	@RequestMapping("/combogrid")
	@ResponseBody
	public DataGrid combogrid(User user) {
		return userService.datagrid(user);
	}

	@RequestMapping("/combobox")
	@ResponseBody
	public List<User> combobox(User user) {
		return userService.combobox(user);
	}

	@RequestMapping("/datagrid")
	@ResponseBody
	public DataGrid datagrid(User user) {
		return userService.datagrid(user);
	}

	@RequestMapping("/remove")
	@ResponseBody
	public Json remove(User user) {
		userService.remove(user.getIds());
		Json j = new Json();
		j.setSuccess(true);
		j.setMsg("删除成功！");
		return j;
	}

	@RequestMapping("/add")
	@ResponseBody
	public Json add(User user) {
		Json j = new Json();
		try {
			User u = userService.save(user);
			j.setSuccess(true);
			j.setMsg("添加成功！");
			j.setObj(u);
		} catch (Exception e) {
			j.setMsg("添加失败！用户名已存在！");
		}
		return j;
	}

	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(User user) {
		Json j = new Json();
		try {
			User u = userService.edit(user);
			j.setSuccess(true);
			j.setMsg("编辑成功！");
			j.setObj(u);
		} catch (Exception e) {
			j.setMsg("编辑失败！用户名已存在！");
		}
		return j;
	}

	@RequestMapping("/modifyRole")
	@ResponseBody
	public Json modifyRole(User user) {
		Json j = new Json();
		userService.modifyRole(user);
		j.setSuccess(true);
		j.setMsg("编辑成功！");
		return j;
	}

	@RequestMapping("/modifyPwd")
	@ResponseBody
	public Json modifyPwd(User user) {
		Json j = new Json();
		userService.modifyPwd(user);
		j.setSuccess(true);
		j.setMsg("编辑成功！");
		return j;
	}

	@RequestMapping("/modifyCurrentUserPwd")
	@ResponseBody
	public Json modifyCurrentUserPwd(User user) {
		Json j = new Json();
		userService.modifyPwd(user);
		j.setSuccess(true);
		j.setMsg("修改成功！");
		return j;
	}

	@RequestMapping("/userInfo")
	public String userInfo() {
		return "user/userInfo";
	}

}
