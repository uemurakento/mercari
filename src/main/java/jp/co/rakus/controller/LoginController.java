package jp.co.rakus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class LoginController {
	@RequestMapping("/")
	public String index(Model model,@RequestParam(required = false) String error) {
		if (error != null) {
			model.addAttribute("loginError","The mail address has already been registered");
		}
	
		return "/login";
	}
}
