package sy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import sy.service.RepairServiceI;

@Controller
@RequestMapping("/repairController")
public class RepairController {

	private RepairServiceI repairService;

	public RepairServiceI getRepairService() {
		return repairService;
	}

	@Autowired
	public void setRepairService(RepairServiceI repairService) {
		this.repairService = repairService;
	}

	@RequestMapping("/repair")
	public String repair() {
		repairService.repair();
		return "redirect:/index.jsp";
	}

	@RequestMapping("/delAndRepair")
	public String delAndRepair() {
		repairService.delAndRepair();
		return "redirect:/index.jsp";
	}

}
