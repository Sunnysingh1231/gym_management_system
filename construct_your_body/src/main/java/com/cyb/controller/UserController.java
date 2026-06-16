package com.cyb.controller;

import java.security.SecureRandom;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Lettuce;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cyb.entity.MemberShipPaymentDetail;
import com.cyb.entity.Planrate;
import com.cyb.entity.User;
import com.cyb.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
//@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private InMemoryUserDetailsManager inMemoryUserDetailsManager;

	@Autowired
	private Planrate planrate;

	public UserController(Planrate planrate) {
		this.planrate = planrate;
	}

//GET method ------------------------------------------------------------------------

	@GetMapping("/user")
	public String index() {

		return "index";

	}

//	@GetMapping("/")
//	public String index() {
//		return "index";
//	}

	@GetMapping("/register")
	public String register() {
		return "/user/register";
	}

	@GetMapping("/login")
	public String login() {

		return "login";
	}

	@GetMapping("/header")
	public String header() {
		return "/user/header";
	}

	@GetMapping("/test")
	public String test(Model m1) {

		m1.addAttribute("user", userService.findAllUser());

		m1.addAttribute("mem", userService.findAllMemberShipDetail());

		return "test";
	}

	@GetMapping("/user/membership")
	public String membership(Model model) {

		model.addAttribute("plati", planrate.getPlatinum());
		model.addAttribute("gold", planrate.getGold());
		model.addAttribute("silv", planrate.getSilver());

		return "/user/membership";
	}

	@GetMapping("/user/payment")
	public String payment() {

		System.out.println("kjsghfbi");

		return "/user/";
	}

	@GetMapping("/user/dashboard")
	public String dashboard(Model model) {

		if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			User user = userService
					.findUserByUserName(SecurityContextHolder.getContext().getAuthentication().getName());

			MemberShipPaymentDetail memberShipPaymentDetail = userService.findMemberShipByUserName(user.getEmail());

			model.addAttribute("name", user.getFullName());

			// SET MEMBERSHIP PLAN, PAYMENT HOSTORY, TODAYS WORKOUT,MONTHLY ATTENDENCE
			if (memberShipPaymentDetail != null) {

				MemberShipPaymentDetail memberShipPaymentDetail2 = userService
						.findMemberShipByUserName(user.getEmail());

				List<String> lastPaymentList = userService.lastPayment(user.getEmail());

				// SET MEMBERSHIP PLAN DETAIL
				model.addAttribute("plan", lastPaymentList.get(0));
				model.addAttribute("planActivetill", lastPaymentList.get(3));

				// SET LAST PAYMENT DETAIL
				model.addAttribute("lastPaymentValue", "₹" + lastPaymentList.get(1));
				model.addAttribute("lastPaymentDate", lastPaymentList.get(2));

				// SET TODAYS WORKOUT DETAIL
				DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
				if (dayOfWeek == DayOfWeek.MONDAY) {
					model.addAttribute("hf", "Chest Focus (Main Muscle)");
					model.addAttribute("nf", "Shoulders");
					model.addAttribute("wf", "Triceps");
				} else if (dayOfWeek == DayOfWeek.TUESDAY) {
					model.addAttribute("hf", "Back Focus (Main Muscle)");
					model.addAttribute("nf", "Biceps");
					model.addAttribute("wf", "Rear Delts");
				} else if (dayOfWeek == DayOfWeek.WEDNESDAY) {
					model.addAttribute("hf", "Leg Focus (Main Muscle)");
					model.addAttribute("nf", "Calves");
					model.addAttribute("wf", "Core");
				} else if (dayOfWeek == DayOfWeek.THURSDAY) {
					model.addAttribute("hf", "Shoulder Focus (Main Muscle)");
					model.addAttribute("nf", "Chest");
					model.addAttribute("wf", "Triceps");
				} else if (dayOfWeek == DayOfWeek.FRIDAY) {
					model.addAttribute("hf", "Biceps (Main Muscle)");
					model.addAttribute("nf", "Triceps (Heavy Focus)");
					model.addAttribute("wf", "Forearms");
				} else if (dayOfWeek == DayOfWeek.SATURDAY) {
					model.addAttribute("hf", "Legs & Core");
					model.addAttribute("nf", "Hamstrings Focus");
					model.addAttribute("wf", "Quads");
				} else {
					model.addAttribute("hf", "REST DAY");
					model.addAttribute("nf", "REST DAY");
					model.addAttribute("wf", "REST DAY");
				}
				model.addAttribute("progressDays", memberShipPaymentDetail.getAttendence());

			}

			// SET HEALTH STATUS
			String bString = "";
			Float cbmi = userService.bmiCalculator(user.getWeight(), user.getHeight());
			if (cbmi < 18.5) {
				bString = "Under Weight";
			} else if (cbmi < 24.9) {
				bString = "Normal Weight";
			} else if (cbmi < 29.9) {
				bString = "Over Weight";
			} else {
				bString = "Obese";
			}
			model.addAttribute("bmi", cbmi + " (" + bString + ")");
			model.addAttribute("weight", user.getWeight() + "kg (" + user.getAge() + "y)");
		}

		return "/user/dashboard";
	}

	@GetMapping("/user/profile")
	public String profile(Model model) {

		User user = userService.findUserByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
		MemberShipPaymentDetail memberShipPaymentDetail = userService.findMemberShipByUserName(user.getEmail());

		model.addAttribute("fullName", user.getFullName());
		model.addAttribute("email", user.getEmail());
		model.addAttribute("age", user.getAge() + "(y)");
		model.addAttribute("phone", user.getPhone());
		model.addAttribute("userId", user.getId());
		model.addAttribute("gender", user.getGender());
		model.addAttribute("height", user.getHeight());
		model.addAttribute("weight", user.getWeight());

		if (memberShipPaymentDetail != null) {
			model.addAttribute("memberShipType", memberShipPaymentDetail.getMemberShipType());
			model.addAttribute("joinDate", memberShipPaymentDetail.getMemberShipStartDate());
			model.addAttribute("endDate", memberShipPaymentDetail.getMemberShipEndDate());
		}
		return "/user/profile";
	}

	@GetMapping("/user/about")
	public String about() {
		return "/user/about";
	}

	@GetMapping("/user/contact-us")
	public String contact() {
		return "/user/contact";
	}

	@GetMapping("/user/profile/edit")
	public String profile_form(Model model) {

		User user = userService.findUserByUserName(SecurityContextHolder.getContext().getAuthentication().getName());

		model.addAttribute("fullName", user.getFullName());
		model.addAttribute("phone", user.getPhone());
		model.addAttribute("height", user.getHeight());
		model.addAttribute("weight", user.getWeight());
		model.addAttribute("age", user.getAge());

		return "/user/profile_form";
	}

	@GetMapping("/user/plan_detail")
	public String fullPlan() {
		return "/user/workout_plan";
	}

	@GetMapping("/user/payment_detail")
	public String paymentDetail(Model model) {

		User user = userService.findUserByUserName(SecurityContextHolder.getContext().getAuthentication().getName());

		model.addAttribute("userPayment", userService.userAllPaymentDetail(user.getEmail()));

		return "/user/user_payment_detail";
	}

	@GetMapping("/user/alert")
	public String alert() {
		return "/user/alert";
	}

	// POST mentod-------------------------------------------

	// save new user into inMemoryDetaiManager
	// provide all users to lodeUserByUsename at time of login
	@PostMapping("/register")
	public String registerData(@ModelAttribute User user) {

		user.setId(Integer.parseInt(userService.generateUserId()));
		user.setHasRole("USER");
		this.userService.addUser(user);

		UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
				.password(passwordEncoder.encode(user.getPassword())).roles("USER").build();

		inMemoryUserDetailsManager.createUser(userDetails);

		return "/login";
	}

	// login using Username & Password
	@PostMapping("/login")
	public String loginUser() {

		return "redirect:/";
	}

	// membership
	// control-------------------------------------------------------------------------

	@PostMapping("/user/membership")
	public String membership(@RequestParam String value, Model model, HttpSession session) {

		String[] dataString = value.split(",");

		MemberShipPaymentDetail memberShipDetail = new MemberShipPaymentDetail();

		if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {

			String userName = (SecurityContextHolder.getContext().getAuthentication().getName());

			memberShipDetail.setEmail(userName);

			memberShipDetail.setMemberShipType(dataString[1]);

			memberShipDetail.setMemberShipPrice(dataString[0]);

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
			LocalDate startDate = LocalDate.now();
			LocalDate enDate = LocalDate.now().plusMonths(1);

			memberShipDetail.setMemberShipStartDate(startDate.format(formatter));

			memberShipDetail.setMemberShipEndDate(enDate.format(formatter));

			memberShipDetail.setPaymenDateTime(
					LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS")));

			memberShipDetail.setAttendence(ChronoUnit.DAYS.between(startDate, enDate));

			session.setAttribute("tempMemberShipDetail", memberShipDetail);

		}

		model.addAttribute("value", "₹" + dataString[0]);
		model.addAttribute("type", dataString[1]);

		return "/user/payment";
	}

	@PostMapping("/user/payment")
	public String paymentConform(HttpSession session) {

		MemberShipPaymentDetail memberShipPaymentDetail = (MemberShipPaymentDetail) session
				.getAttribute("tempMemberShipDetail");
		if (memberShipPaymentDetail != null) {
			userService.addMemberShip(memberShipPaymentDetail);
		}
		return "redirect:/";
	}

	@PostMapping("/user/profile/edit")
	public String setProfile_form(@RequestParam String fullName, String phone, String gender, int height, Float weight,
			int age) {

		if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			User user = userService
					.findUserByUserName(SecurityContextHolder.getContext().getAuthentication().getName());

			user.setFullName(fullName.toUpperCase());
			user.setPhone(phone);
			user.setGender(gender.toUpperCase());
			user.setHeight(height);
			user.setWeight(weight);
			user.setAge(age);

			System.out.println(fullName + " " + phone + " " + gender + " " + height + " " + weight + " " + age);

			return "redirect:/user/profile";
		}
		return "redirect:/user/profile/edit";

	}

}
