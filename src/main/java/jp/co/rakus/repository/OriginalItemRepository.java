package jp.co.rakus.repository;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.rakus.domain.OriginalItem;

/**
 * originalテーブルを操作するリポジトリ.
 * 
 * @author kento.uemura
 *
 */
@Repository
public class OriginalItemRepository {
	/** テーブル名の定数 */
	public static final String TABLE_NAME = "original";
	@Autowired
	private NamedParameterJdbcTemplate template;
	/** 取得したoriginalテーブルの情報をドメインに格納するRowMapper */
	private RowMapper<OriginalItem> ORIGINAL_ITEM_ROW_MAPPER = (rs,i) -> {
		OriginalItem originalItem = new OriginalItem();
		originalItem.setName(			rs.getString(	"name"));
		originalItem.setCondition_id(	rs.getInt(		"condition_id"));
		originalItem.setCategory_name(	rs.getString(	"category_name"));
		originalItem.setBrand(			rs.getString(	"brand"));
		originalItem.setPrice(			rs.getInt(		"price"));
		originalItem.setShipping(		rs.getInt(		"shipping"));
		originalItem.setDescription(	rs.getString(	"description"));
		return originalItem;
	};

	/**
	 * insertメソッド.
	 * 
	 * @param originalItemAll フォーマットを整えた情報
	 */
	public void InsertOriginalItemAll(String originalItemAll) {
		SqlParameterSource param = new MapSqlParameterSource();
		String sql = "INSERT INTO "+TABLE_NAME+" (name,condition_id,category_name,brand,price,shipping,description) VALUES "+originalItemAll+";";
		template.update(sql, param);
	}
	
	/**
	 * カテゴリを全件検索する.
	 * 
	 * @return 重複を省いたカテゴリ
	 */
	public List<String> findCategoryAll(){
		String sql = "SELECT category_name FROM "+TABLE_NAME+";";
		SqlParameterSource param = new MapSqlParameterSource();
		List<String> categories = template.queryForList(sql,param,String.class);
        List<String> noOrverlapCategories = new ArrayList<String>(new LinkedHashSet<>(categories));
        return noOrverlapCategories;
	}
	
	/**
	 * 全件検索する.
	 * 
	 * @return 取得したoriginalItemの情報を格納したドメインのリスト
	 * 何も取得できなかった場合null
	 */
	public List<OriginalItem> findAll(){
		String sql = "SELECT id,name,condition_id,category_name,brand,price,shipping,description FROM "+TABLE_NAME+";";
		SqlParameterSource param = new MapSqlParameterSource();
		List<OriginalItem> originalItems = template.query(sql,param,ORIGINAL_ITEM_ROW_MAPPER);
		if(originalItems.size() == 0) {
			return null;
		}
		return originalItems;
	}
}
