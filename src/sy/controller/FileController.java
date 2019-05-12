package sy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/fileController")
public class FileController {

	@RequestMapping("/upload")
	@ResponseBody
	public String upload() {

		return "失败";
	}

}
