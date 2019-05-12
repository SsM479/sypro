/*package sy.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sy.pageModel.Bug;
import sy.pageModel.DataGrid;
import sy.pageModel.Json;
import sy.service.BugServiceI;
import sy.util.ExceptionUtil;
import sy.util.ResourceUtil;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/bugController")
public class BugController {

	private BugServiceI bugService;

	public BugServiceI getBugService() {
		return bugService;
	}

	@Autowired
	public void setBugService(BugServiceI bugService) {
		this.bugService = bugService;
	}

	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	@RequestMapping("/datagrid")
	@ResponseBody
	public DataGrid datagrid(Bug bug) {
		return bugService.datagrid(bug);
	}

	@RequestMapping("/add")
	@ResponseBody
	public Json add(Bug bug) {
		Json j = new Json();
		try {
			Bug r = bugService.save(bug);
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
	public Json remove(Bug bug) {
		bugService.remove(bug.getIds());
		Json j = new Json();
		j.setSuccess(true);
		j.setMsg("删除成功！");
		return j;
	}

	@RequestMapping("/bugglEdit")
	public String bugglEdit(String id, HttpServletRequest request) {
		Bug b = bugService.getBug(id);
		request.setAttribute("bug", b);
		return "admin/bugglEdit";
	}

	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(Bug bug) {
		Json j = new Json();
		try {
			Bug r = bugService.edit(bug);
			j.setSuccess(true);
			j.setMsg("编辑成功！");
			j.setObj(r);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@RequestMapping("/showNote")
	public String showNote(String id, HttpServletRequest request) {
		Bug b = bugService.getBug(id);
		request.setAttribute("bug", b);
		return "admin/bugglShowNote";
	}

	@RequestMapping("/upload")
	public void upload(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String savePath = session.getServletContext().getRealPath("/") + ResourceUtil.getUploadDirectory() + "/";// 文件保存目录路径
		String saveUrl = "/" + ResourceUtil.getUploadDirectory() + "/";// 要返回给xhEditor的文件保存目录URL

		SimpleDateFormat yearDf = new SimpleDateFormat("yyyy");
		SimpleDateFormat monthDf = new SimpleDateFormat("MM");
		SimpleDateFormat dateDf = new SimpleDateFormat("dd");
		Date date = new Date();
		String ymd = yearDf.format(date) + "/" + monthDf.format(date) + "/" + dateDf.format(date) + "/";
		savePath += ymd;
		saveUrl += ymd;

		File uploadDir = new File(savePath);// 创建要上传文件到指定的目录
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}

		String contentDisposition = request.getHeader("Content-Disposition");// 如果是HTML5上传文件，那么这里有相应头的
		if (contentDisposition != null) {// HTML5拖拽上传文件
			Long fileSize = Long.valueOf(request.getHeader("Content-Length"));// 上传的文件大小
			String fileName = contentDisposition.substring(contentDisposition.lastIndexOf("filename=\""));// 文件名称
			fileName = fileName.substring(fileName.indexOf("\"") + 1);
			fileName = fileName.substring(0, fileName.indexOf("\""));

			ServletInputStream inputStream;
			try {
				inputStream = request.getInputStream();
			} catch (IOException e) {
				this.uploadError("上传文件出错！", response);
				ExceptionUtil.getExceptionMessage(e);
				return;
			}

			if (inputStream == null) {
				this.uploadError("您没有上传任何文件！", response);
				return;
			}

			if (fileSize > ResourceUtil.getUploadFileMaxSize()) {
				this.uploadError("上传文件超出限制大小！", fileName, response);
				return;
			}

			// 检查文件扩展名
			String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
			if (!Arrays.<String> asList(ResourceUtil.getUploadFileExts().split(",")).contains(fileExt)) {
				this.uploadError("上传文件扩展名是不允许的扩展名。\n只允许" + ResourceUtil.getUploadFileExts() + "格式！", response);
				return;
			}

			String newFileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;// 新的文件名称
			File uploadedFile = new File(savePath, newFileName);

			try {
				FileCopyUtils.copy(inputStream, new FileOutputStream(uploadedFile));
			} catch (FileNotFoundException e) {
				this.uploadError("上传文件出错！", response);
				ExceptionUtil.getExceptionMessage(e);
				return;
			} catch (IOException e) {
				this.uploadError("上传文件出错！", response);
				ExceptionUtil.getExceptionMessage(e);
				return;
			}

			this.uploadSuccess(request.getContextPath() + saveUrl + newFileName, fileName, 0, response);// 文件上传成功
			return;
		} else {// 不是HTML5拖拽上传,普通上传
			if (ServletFileUpload.isMultipartContent(request)) {// 判断表单是否存在enctype="multipart/form-data"
				DiskFileItemFactory dfif = new DiskFileItemFactory();
				dfif.setSizeThreshold(1 * 1024 * 1024);// 设定当上传文件超过1M时，将产生临时文件用于缓冲
				dfif.setRepository(new File(savePath));// 存放临时文件的目录
				ServletFileUpload sfu = new ServletFileUpload(dfif);
				try {
					List<FileItem> fis = sfu.parseRequest(request);
					for (FileItem fi : fis) {
						if (fi.isFormField()) {
							// 是表单项，不是上传项
						} else {
							String fileName = fi.getName();// 上传的文件名称，如果是欧鹏浏览器，这个会带路径

							if (fi.getSize() > ResourceUtil.getUploadFileMaxSize()) {
								this.uploadError("上传文件超出限制大小！", fileName, response);
								return;
							}

							try {
								// 检查文件扩展名
								String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
								if (!Arrays.<String> asList(ResourceUtil.getUploadFileExts().split(",")).contains(fileExt)) {
									this.uploadError("上传文件扩展名是不允许的扩展名。\n只允许" + ResourceUtil.getUploadFileExts() + "格式！", response);
									return;
								}
								String newFileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;// 新的文件名称
								fi.write(new File(savePath, newFileName));

								this.uploadSuccess(request.getContextPath() + saveUrl + newFileName, fileName, 0, response);// 文件上传成功
								return;
							} catch (Exception e) {
								this.uploadError("上传文件出错！", response);
								ExceptionUtil.getExceptionMessage(e);
								return;
							}
						}
					}
				} catch (FileUploadException e) {
					this.uploadError("上传文件出错！", response);
					ExceptionUtil.getExceptionMessage(e);
					return;
				}
			} else {
				// 不是multipart/form-data表单
				this.uploadError("您没有上传任何文件！", response);
				return;
			}
			return;
		}

	}

	private void uploadError(String err, String msg, HttpServletResponse response) {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("err", err);
		m.put("msg", msg);
		this.writeJson(m, response);
	}

	private void uploadError(String err, HttpServletResponse response) {
		this.uploadError(err, "", response);
	}

	private void uploadSuccess(String newFileName, String fileName, int id, HttpServletResponse response) {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("err", "");
		Map<String, Object> nm = new HashMap<String, Object>();
		nm.put("url", newFileName);
		nm.put("localfile", fileName);
		nm.put("id", id);
		m.put("msg", nm);
		this.writeJson(m, response);
	}

	*//**
	 * 将对象转换成JSON字符串，并响应回前台
	 * 
	 * @param object
	 * @throws IOException
	 *//*
	private void writeJson(Object object, HttpServletResponse response) {
		try {
			String json = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss");
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(json);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
*/