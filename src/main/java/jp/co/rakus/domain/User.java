package jp.co.rakus.domain;

/**
 * ユーザ情報を扱うドメイン.
 * 
 * @author kento.uemura
 *
 */
public class User {
	/** 主キー */
	private Integer id;
	/** メールアドレス */
	private String email;
	/** パスワード */
	private String password;
	
	public User() {}
	public User(Integer id, String email, String password) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
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
