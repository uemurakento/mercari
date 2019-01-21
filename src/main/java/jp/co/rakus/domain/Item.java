package jp.co.rakus.domain;

import java.util.List;

/**
 * 商品情報を格納するドメイン.
 * 
 * @author kento.uemura
 *
 */
public class Item {
	/** 主キー */
	private Integer id;
	/** 名前 */
	private String name;
	/** コンディション */
	private Integer condition;
	/** カテゴリ */
	private List<Category> categories;
	/** 孫カテゴリの主キー */
	private Integer category;
	/** カテゴリ名全体 */
	private String categoryAllName;
	/** ブランド */
	private String brand;
	/** 値段 */
	private Integer price;
	/** 配送状況 */
	private Integer shipping;
	/** 説明 */
	private String description;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getCondition() {
		return condition;
	}
	public void setCondition(Integer condition) {
		this.condition = condition;
	}
	public List<Category> getCategories() {
		return categories;
	}
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	public Integer getCategory() {
		return category;
	}
	public void setCategory(Integer category) {
		this.category = category;
	}
	public String getCategoryAllName() {
		return categoryAllName;
	}
	public void setCategoryAllName(String categoryAllName) {
		this.categoryAllName = categoryAllName;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Integer getShipping() {
		return shipping;
	}
	public void setShipping(Integer shipping) {
		this.shipping = shipping;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
