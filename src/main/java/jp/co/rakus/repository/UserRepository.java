package jp.co.rakus.repository;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import jp.co.rakus.domain.User;

/**
 * usersテーブルの操作をするリポジトリ.
 * 
 * @author kento.uemura
 *
 */
@Repository
public class UserRepository {
	@Autowired
	private NamedParameterJdbcTemplate template;
	/** テーブル名の定数 */
	public static final String TABLE_NAME = "users";
	
	private SimpleJdbcInsert insert;
	
	/** 取得したuser情報をドメインに格納するローマッパー */
	private RowMapper<User> USER_ROW_MAPPER = (rs,i) -> {
		User user = new User();
		user.setId(rs.getInt("id"));
		user.setEmail(rs.getString("email"));
		user.setPassword(rs.getString("password"));
		return user;
	};

	/**
	 * initメソッド.
	 * 
	 * テーブルにinsertするためのメソッド
	 */
	@PostConstruct
	public void init() {
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert((JdbcTemplate) template.getJdbcOperations());
		SimpleJdbcInsert withTableName = simpleJdbcInsert.withTableName(TABLE_NAME);
		insert = withTableName.usingGeneratedKeyColumns("id");
	}

	/**
	 * itemsテーブルにinsertする.
	 * 
	 * @param user
	 *            insertするuser情報を格納したドメイン
	 * @return 引数に自動採番された値が追加されたドメイン
	 */
	public User insert(User user) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		Number key = insert.executeAndReturnKey(param);
		user.setId(key.intValue());
		return user;
	}

	/**
	 * メールアドレスからユーザーを検索する.
	 * 
	 * @param email　メールアドレス
	 * @return　Userオブジェクト.該当データがなければnullを返す
	 */
	public User findByEmail(String email) {
		String sql = "SELECT id, email, password FROM "+TABLE_NAME+" WHERE email=:email";
		SqlParameterSource param = new MapSqlParameterSource().addValue("email",email);
		
		List<User> userList = template.query(sql,param,USER_ROW_MAPPER);
		if(userList.size() == 0) {
			return null;
		}
		return userList.get(0);
	}

}
