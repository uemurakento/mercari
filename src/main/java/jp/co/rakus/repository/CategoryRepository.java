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

import jp.co.rakus.domain.Category;

/**
 * categoryテーブルを操作するリポジトリ.
 * 
 * @author kento.uemura
 *
 */
@Repository
public class CategoryRepository {
	@Autowired
	private NamedParameterJdbcTemplate template;
	/** テーブル名の定数 */
	public static final String TABLE_NAME = "category";
	/** 取得したカテゴリ情報をドメインに詰めるRowMapper */
	private RowMapper<Category> CATEGORY_ROW_MAPPER = (rs,i) -> {
		Category category = new Category();
		category.setId(			rs.getInt(		"id"));
		category.setParent(		rs.getInt(		"parent"));
		category.setName(		rs.getString(	"name"));
		category.setNameAll(	rs.getString(	"name_all"));
		return category;
	};

	/**
	 * nameが一致するカテゴリをテーブルから検索する.
	 * 
	 * @param name 検索する名前
	 * @return 所得したカテゴリ情報を格納したドメイン
	 * 見つからなければnull
	 */
	public Category findByName(String name) {
		String sql = "SELECT id,parent,name,name_all FROM "+TABLE_NAME+" WHERE name=:name;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", name);
		List<Category> categories = template.query(sql, param, CATEGORY_ROW_MAPPER);
		if(categories.size() == 0) {
			return null;
		}
		return categories.get(0);
	}
	
	/**
	 * name,parentが一致するカテゴリをテーブルから検索する。
	 * 
	 * @param name 検索する名前
	 * @param parent 検索する親カテゴリのid
	 * @return 所得したカテゴリ情報を格納したドメイン
	 * 見つからなければnull
	 */
	public Category findByNameAndParent(String name,Integer parent) {
		String sql = "SELECT id,parent,name,name_all FROM "+TABLE_NAME+" WHERE name=:name AND parent=:parent　ORDER BY name;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", name).addValue("parent", parent);
		List<Category> categories = template.query(sql, param, CATEGORY_ROW_MAPPER);
		if(categories.size() == 0) {
			return null;
		}
		return categories.get(0);
	}

	/**
	 * nameが一致して、parentがnullのカテゴリをテーブルから検索する.
	 * @param name 検索する名前
	 * @return 取得してカテゴリ情報を格納したドメイン
	 * 見つからなければnull
	 */
	public Category findByNameAndParentIsNull(String name) {
		String sql = "SELECT id,parent,name,name_all FROM "+TABLE_NAME+" WHERE name=:name AND parent is null ORDER BY name;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", name);
		List<Category> categories = template.query(sql, param, CATEGORY_ROW_MAPPER);
		if(categories.size() == 0) {
			return null;
		}
		return categories.get(0);
	}

	private SimpleJdbcInsert insert;
	/**
	 * initメソッド.
	 * 
	 * テーブルにinsertするためのメソッド
	 */
	@PostConstruct
	public void init() {
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert((JdbcTemplate)template.getJdbcOperations());
		SimpleJdbcInsert withTableName = simpleJdbcInsert.withTableName(TABLE_NAME);
		insert = withTableName.usingGeneratedKeyColumns("id");
	}

	/**
	 * カテゴリ情報をinsertする.
	 * 
	 * @param category 挿入するカテゴリ情報
	 * @return　引数に自動採番情報を追加したもの
	 */
	public Category insert(Category category) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(category);
		Number key = insert.executeAndReturnKey(param);
		category.setId(key.intValue());
		return category;
	}
	
	/**
	 * カテゴリ全体の名前で検索する.
	 * 
	 * @param nameAll カテゴリ全体の名前
	 * @return 取得したカテゴリ情報を格納したドメイン
	 * 見つからなかった場合null
	 */
	public Category findByNameAll(String nameAll) {
		String sql = "SELECT id,parent,name,name_all FROM "+TABLE_NAME+" WHERE name_all=:nameAll;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("nameAll", nameAll);
		List<Category> categories = template.query(sql, param, CATEGORY_ROW_MAPPER);
		if(categories.size() == 0) {
			return null;
		}
		return categories.get(0);
	}
	
	/**
	 * 親カテゴリ一覧を取得する.
	 * 
	 * @return 取得したカテゴリ情報を格納したドメインのリスト
	 * 空だった場合null
	 */
	public List<Category> findParentCategory() {
		String sql = "SELECT id,parent,name,name_all FROM "+TABLE_NAME+" WHERE parent is null ORDER BY name;";
		SqlParameterSource param = new MapSqlParameterSource();
		List<Category> categories = template.query(sql, param, CATEGORY_ROW_MAPPER);
		if(categories.size() == 0) {
			return null;
		}
		return categories;
	}
	
	/**
	 * 子カテゴリ一覧を取得する.
	 * 
	 * @return 取得したカテゴリ情報を格納したドメインのリスト
	 * 空だった場合null
	 */
	public List<Category> findChildCategory() {
		String sql = "SELECT id,parent,name,name_all FROM "+TABLE_NAME+" WHERE parent is not null AND name_all is null ORDER BY name;";
		SqlParameterSource param = new MapSqlParameterSource();
		List<Category> categories = template.query(sql, param, CATEGORY_ROW_MAPPER);
		if(categories.size() == 0) {
			return null;
		}
		return categories;
	}
	
	/**
	 * 孫カテゴリ一覧を取得する.
	 * 
	 * @return 取得したカテゴリ情報を格納したドメインのリスト
	 * 空だった場合null
	 */
	public List<Category> findGrandchildCategory() {
		String sql = "SELECT id,parent,name,name_all FROM "+TABLE_NAME+" WHERE name_all is not null ORDER BY name;";
		SqlParameterSource param = new MapSqlParameterSource();
		List<Category> categories = template.query(sql, param, CATEGORY_ROW_MAPPER);
		if(categories.size() == 0) {
			return null;
		}
		return categories;
	}
}
