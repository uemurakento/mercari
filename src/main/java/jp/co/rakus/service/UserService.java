package jp.co.rakus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jp.co.rakus.domain.User;
import jp.co.rakus.repository.UserRepository;

/**
 * User情報を操作するサービス.
 * 
 * @author kento.uemura
 *
 */
@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/**
	 * User情報を登録する.
	 * 
	 * @param user user情報を格納したドメイン
	 * @return 自動採番されたidを追加した情報を格納したドメイン
	 */
	public User insert(User user) {
		user.setPassword(encodePassword(user.getPassword()));
		return userRepository.insert(user);
	}
	
	/**
	 * パスワードのエンコードをする.
	 * 
	 * @param rawPassword 生のパスワード
	 * @return エンコードしたパスワード
	 */
	public String encodePassword(String rawPassword) {
		String encodedPassword = passwordEncoder.encode(rawPassword);
		return encodedPassword;
	}

	/**
	 * メールアドレスからユーザーを検索する.
	 * 
	 * @param email　メールアドレス
	 * @return　Userオブジェクト.該当データがなければnullを返す
	 */
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
}
