package jp.co.rakus.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 商品詳細編集ページのフォーム情報を格納するフォーム.
 * 
 * @author kento.uemura
 *
 */
public class ItemForm {
	/** 主キー */
	private Integer id;
	/** 名前 */
	@NotBlank(message="Please enter the name")
	private String name;
	/** 値段 */
	@NotBlank(message="Please enter the price")
	@Pattern(regexp="^[1-9][0-9]*$",message="Must be 1 or more")
	private String price;
	/** 親カテゴリID */
	private Integer parent;
	/** 子カテゴリID */
	private Integer child;
	/** 孫カテゴリID */
	private Integer grandchild;
	/** ブランド */
	private String brand;
	/** 状態 */
	@NotBlank(message="Please enter the price")
	@Pattern(regexp="^[1-5]$",message="Must be 1-5")
	private String condition;
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
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public Integer getParent() {
		return parent;
	}
	public void setParent(Integer parent) {
		this.parent = parent;
	}
	public Integer getChild() {
		return child;
	}
	public void setChild(Integer child) {
		this.child = child;
	}
	public Integer getGrandchild() {
		return grandchild;
	}
	public void setGrandchild(Integer grandchild) {
		this.grandchild = grandchild;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
