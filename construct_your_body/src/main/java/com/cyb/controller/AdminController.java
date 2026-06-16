package com.cyb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cyb.entity.Planrate;
import com.cyb.entity.User;
import com.cyb.service.AdminService;
import com.cyb.service.UserService;
import com.cyb.service.impl.AdminServiceImpl;

@Controller
public class AdminController {
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	private Planrate planrate;

	@GetMapping("/admin/dashboard")
	public String dashboard(Model model) {
		
		model.addAttribute("activeMem",userService.findAllMemberShipDetail().size());
		model.addAttribute("totalMem",userService.findAllUser().size());

		
		model.addAttribute("users",adminService.findAllUsers());
		
		return "/admin/home";
	}
	
	@GetMapping("/admin/members")
	public String member(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, Model model) {
		
		int start = page*size;
		
		int end = Math.min(start+size, userService.findAllUser().size());
		
//		List<User> paginatedUsers = ((List<User>) userService.findAllUser()).subList(start,end);
		
		model.addAttribute("users",adminService.findAllUsers());
		
		return "/admin/member";
	}
	
	@GetMapping("/admin/classes")
	public String classes(Model model) {
		
		model.addAttribute("platinum", planrate.getPlatinum());
		model.addAttribute("gold", planrate.getGold());
		model.addAttribute("silver", planrate.getSilver());
		
		
		return "/admin/plan";
	}
	
	@GetMapping("/admin/reports")
	public String report() {
		return "/admin/report";
	}
	
	@GetMapping("/admin/setting")
	public String setting() {
		return "/admin/setting";
	}
	
	@PostMapping("/admin/update-plan-rate")
	public String setPlan(@RequestParam Integer platinum, int gold, int silver) {
		
		planrate.setPlatinum(platinum);
		planrate.setGold(gold);
		planrate.setSilver(silver);
		
		return "/admin/home";
	}
}
