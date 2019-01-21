package jp.co.rakus.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import jp.co.rakus.domain.Item;

/**
 * Itemsテーブルを操作するリポジトリ.
 * 
 * @author kento.uemura
 *
 */
@Repository
public class ItemRepository {
	@Autowired
	private NamedParameterJdbcTemplate template;
	/** テーブル名の定数 */
	public static final String TABLE_NAME = "items";
	/** １ページの表示制限 */
	public static final Integer DISPLAY_LIMIT_ITEM = 30;
	/** Itemの情報をドメインに詰めるRowMapper */
	private static final RowMapper<Item> ITEM_ROWMAPPER = (rs, i) -> {
		Item item = new Item();
		item.setId(rs.getInt("item_id"));
		item.setName(rs.getString("item_name"));
		item.setCondition(rs.getInt("item_condition"));

		List<Category> categories = new ArrayList<>();
		item.setCategories(categories);
		// 親カテゴリ
		Category parentCategory = new Category();
		parentCategory.setId(rs.getInt("category_parent_id"));
		parentCategory.setName(rs.getString("category_parent_name"));
		categories.add(parentCategory);
		// 子カテゴリ
		Category childCategory = new Category();
		childCategory.setId(rs.getInt("category_child_id"));
		childCategory.setName(rs.getString("category_child_name"));
		childCategory.setParent(parentCategory.getId());
		categories.add(childCategory);
		// 孫カテゴリ
		Category grandchildCategory = new Category();
		grandchildCategory.setId(rs.getInt("category_grandchild_id"));
		grandchildCategory.setName(rs.getString("category_grandchild_name"));
		grandchildCategory.setParent(childCategory.getId());
		grandchildCategory.setNameAll(rs.getString("category_name"));
		categories.add(grandchildCategory);

		item.setCategory(grandchildCategory.getId());
		item.setCategoryAllName(grandchildCategory.getNameAll());
		item.setBrand(rs.getString("item_brand"));
		item.setPrice(rs.getInt("item_price"));
		item.setShipping(rs.getInt("item_shipping"));
		item.setDescription(rs.getString("item_description"));
		return item;
	};

	private SimpleJdbcInsert insert;

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
	 * @param item
	 *            insertするitem情報を格納したドメイン
	 * @return 引数に自動採番された値が追加されたドメイン
	 */
	public Item insert(Item item) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(item);
		Number key = insert.executeAndReturnKey(param);
		item.setId(key.intValue());
		return item;
	}

	/**
	 * ページ番号を指定して、Item情報を調べる.
	 * 
	 * @param pageNum
	 *            指定するページ番号
	 * @return 取得したItem情報が格納されたドメインのリスト 見つからなかった場合はnull
	 */
	public List<Item> findByPage(Integer pageNum) {
		String sql = "SELECT i.id item_id,i.name item_name,i.condition item_condition,cg.name_all category_name,cp.id category_parent_id,cp.name category_parent_name,cc.id category_child_id,cc.name category_child_name,cg.id category_grandchild_id,cg.name category_grandchild_name,i.brand item_brand,i.price item_price,i.shipping item_shipping,i.description item_description FROM "
				+ TABLE_NAME + " i LEFT OUTER JOIN " + CategoryRepository.TABLE_NAME
				+ " cg ON i.category = cg.id LEFT OUTER JOIN " + CategoryRepository.TABLE_NAME
				+ " cc ON cg.parent = cc.id LEFT OUTER JOIN " + CategoryRepository.TABLE_NAME
				+ " cp ON cc.parent = cp.id ORDER BY i.name OFFSET :offsetNum LIMIT :limitNum;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("offsetNum", (pageNum - 1) * DISPLAY_LIMIT_ITEM)
				.addValue("limitNum", DISPLAY_LIMIT_ITEM);
		List<Item> items = template.query(sql, param, ITEM_ROWMAPPER);
		if (items.size() == 0) {
			return null;
		}
		return items;
	}

	/**
	 * 条件を指定して、item情報を取得する.
	 * 
	 * @param name
	 *            名前
	 * @param parentCategoryId
	 *            親カテゴリのID
	 * @param childCategoryId
	 *            子カテゴリのID
	 * @param grandchildCategoryId
	 *            孫カテゴリのID
	 * @param brand
	 *            ブランド
	 * @param brandMatch
	 *            ブランドの完全一致検索用
	 * @param pageNum
	 *            ページ番号
	 * @return 取得したitem情報を格納したドメインのリスト 空ならnullを返す
	 */
	public List<Item> findByNameAndCategoryAndBrandAndPage(String name, Integer parentCategoryId,
			Integer childCategoryId, Integer grandchildCategoryId, String brand, String brandMatch, Integer pageNum) {
		MapSqlParameterSource param = new MapSqlParameterSource();

		List<String> conditions = new ArrayList<>();
		Map<String, String> params = new LinkedHashMap<>();
		Map<String, Integer> categoryParams = new LinkedHashMap<>();
		List<String> pageConditions = new ArrayList<>();
		Map<String, Integer> pageParams = new LinkedHashMap<>();

		StringBuilder sql = new StringBuilder();
		sql.append(
				"SELECT i.id item_id,i.name item_name,i.condition item_condition,cg.name_all category_name,cp.id category_parent_id,cp.name category_parent_name,cc.id category_child_id,cc.name category_child_name,cg.id category_grandchild_id,cg.name category_grandchild_name,i.brand item_brand,i.price item_price,i.shipping item_shipping,i.description item_description FROM "
						+ TABLE_NAME + " i LEFT OUTER JOIN " + CategoryRepository.TABLE_NAME
						+ " cg ON i.category = cg.id LEFT OUTER JOIN " + CategoryRepository.TABLE_NAME
						+ " cc ON cg.parent = cc.id LEFT OUTER JOIN " + CategoryRepository.TABLE_NAME
						+ " cp ON cc.parent = cp.id");
		// name
		if (name != null && !"".equals(name)) {
			conditions.add(" i.name LIKE :name");
			params.put("name", "%" + name + "%");
		}
		// parentCategory
		if (parentCategoryId != null && parentCategoryId != 0) {
			conditions.add(" cp.id=:parentCategoryId");
			categoryParams.put("parentCategoryId", parentCategoryId);
		}
		// childCategory
		if (childCategoryId != null && childCategoryId != 0) {
			conditions.add(" cc.id=:childCategoryId");
			categoryParams.put("childCategoryId", childCategoryId);
		}
		// grandChildCategory
		if (grandchildCategoryId != null && grandchildCategoryId != 0) {
			conditions.add(" cg.id=:grandchildCategoryId");
			categoryParams.put("grandchildCategoryId", grandchildCategoryId);
		}
		// brand
		if (brandMatch == null || "".equals(brandMatch)) {
			if (brand != null && !"".equals(brand)) {
				conditions.add(" i.brand LIKE :brand");
				params.put("brand", "%" + brand + "%");
			}
		} else {
			conditions.add(" i.brand LIKE :brand");
			params.put("brand", brandMatch);
		}
		// page
		if (pageNum != null) {
			pageConditions.add(" OFFSET :offset");
			pageParams.put("offset", (pageNum - 1) * DISPLAY_LIMIT_ITEM);
		}
		pageConditions.add(" LIMIT :limit");
		pageParams.put("limit", DISPLAY_LIMIT_ITEM);

		if (!conditions.isEmpty()) {
			sql.append(" WHERE");
		}
		boolean firstTimeNotDoFlag = false;
		for (int i = 0; i < conditions.size(); i++) {
			if (firstTimeNotDoFlag) {
				sql.append(" AND");
			}
			sql.append(conditions.get(i));
			if (!firstTimeNotDoFlag) {
				firstTimeNotDoFlag = true;
			}
		}
		sql.append(" ORDER BY i.name");
		param.addValues(params);
		param.addValues(categoryParams);
		for (int i = 0; i < pageConditions.size(); i++) {
			sql.append(pageConditions.get(i));
		}
		param.addValues(pageParams);
		SqlParameterSource sqlParam = param;
		List<Item> items = template.query(sql.toString(), sqlParam, ITEM_ROWMAPPER);
		if (items.size() == 0) {
			return null;
		}
		return items;
	}

	/**
	 * itemsテーブルの条件を指定した時の件数を取得する
	 * 
	 * @param name
	 *            名前
	 * @param parentCategoryId
	 *            親カテゴリのID
	 * @param childCategoryId
	 *            子カテゴリのID
	 * @param grandchildCategoryId
	 *            孫カテゴリのID
	 * @param brand
	 *            ブランド
	 * @param brandMatch
	 *            ブランドの完全一致検索用
	 * @return 件数
	 */
	public Integer countByNameAndCategoryAndBrandAndPage(String name, Integer parentCategoryId, Integer childCategoryId,
			Integer grandchildCategoryId, String brand, String brandMatch) {
		MapSqlParameterSource param = new MapSqlParameterSource();

		List<String> conditions = new ArrayList<>();
		Map<String, String> params = new LinkedHashMap<>();
		Map<String, Integer> categoryParams = new LinkedHashMap<>();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) FROM " + TABLE_NAME + " i LEFT OUTER JOIN " + CategoryRepository.TABLE_NAME
				+ " cg ON i.category = cg.id LEFT OUTER JOIN " + CategoryRepository.TABLE_NAME
				+ " cc ON cg.parent = cc.id LEFT OUTER JOIN " + CategoryRepository.TABLE_NAME
				+ " cp ON cc.parent = cp.id");
		// name
		if (name != null && !"".equals(name)) {
			conditions.add(" i.name LIKE :name");
			params.put("name", "%" + name + "%");
		}
		// parentCategory
		if (parentCategoryId != null && parentCategoryId != 0) {
			conditions.add(" cp.id=:parentCategoryId");
			categoryParams.put("parentCategoryId", parentCategoryId);
		}
		// childCategory
		if (childCategoryId != null && childCategoryId != 0) {
			conditions.add(" cc.id=:childCategoryId");
			categoryParams.put("childCategoryId", childCategoryId);
		}
		// grandChildCategory
		if (grandchildCategoryId != null && grandchildCategoryId != 0) {
			conditions.add(" cg.id=:grandchildCategoryId");
			categoryParams.put("grandchildCategoryId", grandchildCategoryId);
		}
		// brand
		if (brandMatch == null || "".equals(brandMatch)) {
			if (brand != null && !"".equals(brand)) {
				conditions.add(" i.brand LIKE :brand");
				params.put("brand", "%" + brand + "%");
			}
		} else {
			conditions.add(" i.brand LIKE :brand");
			params.put("brand", brandMatch);
		}

		if (!conditions.isEmpty()) {
			sql.append(" WHERE");
		}
		boolean firstTimeNotDoFlag = false;
		for (int i = 0; i < conditions.size(); i++) {
			if (firstTimeNotDoFlag) {
				sql.append(" AND");
			}
			sql.append(conditions.get(i));
			if (!firstTimeNotDoFlag) {
				firstTimeNotDoFlag = true;
			}
		}
		param.addValues(params);
		param.addValues(categoryParams);
		SqlParameterSource sqlParam = param;
		Integer count = template.queryForObject(sql.toString(), sqlParam, Integer.class);
		return count;
	}

	/**
	 * itemsテーブルの条件を指定した時の最大ページ数を取得する
	 * 
	 * @param name
	 *            名前
	 * @param parentCategoryId
	 *            親カテゴリのID
	 * @param childCategoryId
	 *            子カテゴリのID
	 * @param grandchildCategoryId
	 *            孫カテゴリのID
	 * @param brand
	 *            ブランド
	 * @param brandMatch
	 *            ブランドの完全一致検索用
	 * @return 最大ページ数
	 */
	public Integer pageByNameAndCategoryAndBrandAndPage(String name, Integer parentCategoryId, Integer childCategoryId,
			Integer grandchildCategoryId, String brand, String brandMatch) {
		return countByNameAndCategoryAndBrandAndPage(name, parentCategoryId, childCategoryId, grandchildCategoryId,
				brand, brandMatch) / DISPLAY_LIMIT_ITEM + 1;
	}

	/**
	 * Itemsの全件数を調べる.
	 * 
	 * @return 見つかった件数
	 */
	public Integer countItems() {
		String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + ";";
		SqlParameterSource param = new MapSqlParameterSource();
		Integer count = template.queryForObject(sql, param, Integer.class);
		return count;
	}

	/**
	 * 一覧表示の最大ページ数を調べる.
	 * 
	 * @return ページ数
	 */
	public Integer maxpage() {
		return countItems() / DISPLAY_LIMIT_ITEM + 1;
	}

	/**
	 * idを指定してitem情報を取得する.
	 * 
	 * @param id
	 *            主キー
	 * @return 取得したitem情報を格納したドメイン 見つからなければnull
	 */
	public Item findById(Integer id) {
		String sql = "SELECT i.id item_id,i.name item_name,i.condition item_condition,cg.name_all category_name,cp.id category_parent_id,cp.name category_parent_name,cc.id category_child_id,cc.name category_child_name,cg.id category_grandchild_id,cg.name category_grandchild_name,i.brand item_brand,i.price item_price,i.shipping item_shipping,i.description item_description FROM "
				+ TABLE_NAME + " i LEFT OUTER JOIN " + CategoryRepository.TABLE_NAME
				+ " cg ON i.category = cg.id LEFT OUTER JOIN " + CategoryRepository.TABLE_NAME
				+ " cc ON cg.parent = cc.id LEFT OUTER JOIN " + CategoryRepository.TABLE_NAME
				+ " cp ON cc.parent = cp.id WHERE i.id=:id;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		List<Item> items = template.query(sql, param, ITEM_ROWMAPPER);
		if (items.isEmpty()) {
			return null;
		}
		return items.get(0);
	}

	/**
	 * itmesテーブルのupdateメソッド.
	 * 
	 * @param item 更新する情報
	 * @return　引数
	 */
	public Item update(Item item) {
		String sql = "UPDATE " + TABLE_NAME
				+ " SET name=:name,condition=:condition,category=:category,brand=:brand,description=:description,price=:price WHERE id=:id";
		BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(item);
		template.update(sql, param);
		return item;
	}
}
