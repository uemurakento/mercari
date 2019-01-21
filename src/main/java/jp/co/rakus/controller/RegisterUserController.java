package jp.co.rakus.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.rakus.domain.User;
import jp.co.rakus.form.RegisterUserForm;
import jp.co.rakus.service.UserService;

/**
 * ユーザ登録をするコントローラ.
 * 
 * @author kento.uemura
 *
 */
@Controller
@RequestMapping("/registeruser")
public class RegisterUserController {
	@Autowired
	UserService userService;
	
	@ModelAttribute
	public RegisterUserForm setUpForm() {
		return new RegisterUserForm();
	}
	
	/**
	 * ユーザ登録画面を表示する.
	 * 
	 * @return ユーザ登録画面
	 */
	@RequestMapping("/view")
	public String viewRegisterUser() {
		return "/register";
	}
	
	/**
	 * ユーザ登録を行う.
	 * 
	 * @param form ユーザ情報
	 * @param result 入力値のエラー情報
	 * @return エラーがなければログイン画面
	 * エラーがあると登録画面
	 */
	@RequestMapping("/register")
	public String registerUser(@Validated RegisterUserForm form,BindingResult result) {
		User user = userService.findByEmail(form.getEmail());
		if(user != null) {
			result.rejectValue("email", "","The mail address has already been registered");
		}
		
		if(result.hasErrors()) {
			return viewRegisterUser();
		}
		user = new User();
		BeanUtils.copyProperties(form, user);
		userService.insert(user);
		return "redirect:/";
	}
}
