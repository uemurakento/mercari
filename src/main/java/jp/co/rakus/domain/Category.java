package jp.co.rakus.domain;

/**
 * カテゴリ情報を格納するドメイン
 * 
 * @author kento.uemura
 *
 */
public class Category {
	/** 主キー */
	private Integer id;
	/** 親カテゴリの主キー */
	private Integer parent;
	/** 名前 */
	private String name;
	/** 一番下のカテゴリが持つカテゴリ全体の名前 */
	private String nameAll;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getParent() {
		return parent;
	}
	public void setParent(Integer parent) {
		this.parent = parent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameAll() {
		return nameAll;
	}
	public void setNameAll(String nameAll) {
		this.nameAll = nameAll;
	}
}
