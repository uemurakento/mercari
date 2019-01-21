package jp.co.rakus.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * ユーザ登録情報を扱うフォーム.
 * 
 * @author kento.uemura
 *
 */
public class RegisterUserForm {
	/** メールアドレス */
	@NotBlank ( message = "Please enter your e-mail address")
	@Pattern(regexp = "^([\\w])+([\\w\\._-])*\\@([\\w])+([\\w\\._-])*\\.([a-zA-Z])+$",message="The format of the e-mail address is incorrect")
	private String email;
	/** パスワード */
	@NotBlank ( message = "Please enter your password")
	private String password;

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
