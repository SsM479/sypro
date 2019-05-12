package sy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/druidController")
public class DruidController {

	@RequestMapping("/druid")
	public String druid() {
		return "redirect:/druid/index.html";
	}

}
