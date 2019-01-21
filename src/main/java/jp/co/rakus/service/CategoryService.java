package jp.co.rakus.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.rakus.domain.Category;
import jp.co.rakus.repository.CategoryRepository;

/**
 * カテゴリ情報を操作するサービス.
 * 
 * @author kento.uemura
 *
 */
@Service
public class CategoryService {
	@Autowired
	private CategoryRepository repository;
	
	/**
	 * "/"区切りのカテゴリを分割して１つのカテゴリーテーブルに挿入する.
	 * 
	 * @param categories 分割前のカテゴリ情報が複数入ったStringのリスト
	 */
	public void insert(List<String> categories) {
		for (String category : categories) {
			if (category != null) {
				//"/"区切りでカテゴリを分割
				List<String> splitCategory = Arrays.asList(category.split("/", 0));

				//親のカテゴリの処理
				String parentName = splitCategory.get(0);
				Category parentCategory = repository.findByNameAndParentIsNull(parentName);
				if(parentCategory == null) {
					parentCategory = new Category();
					parentCategory.setName(parentName);
					parentCategory = repository.insert(parentCategory);
				}
				
				//子のカテゴリの処理
				String childName = splitCategory.get(1);
				Integer parentId = parentCategory.getId();
				Category childCategory = repository.findByNameAndParent(childName,parentId);
				if(childCategory == null) {
					childCategory = new Category();
					childCategory.setName(childName);
					childCategory.setParent(parentId);
					childCategory = repository.insert(childCategory);
				}
				
				//孫のカテゴリの処理
				//４つ目以降のカテゴリは切り捨てる、孫のname_allは３つ目までを入れる
				String grandchildName = splitCategory.get(2);
				Integer childId = childCategory.getId();
				Category grandchildCategory = repository.findByNameAndParent(grandchildName, childId);
				if(grandchildCategory == null) {
					grandchildCategory = new Category();
					grandchildCategory.setName(grandchildName);
					grandchildCategory.setParent(childId);
					String nameAll = String.join("/", splitCategory.subList(0,3));
					grandchildCategory.setNameAll(nameAll);
					repository.insert(grandchildCategory);
				}
			}
		}
	}
	
	/**
	 * 親カテゴリ一覧を取得する.
	 * 
	 * @return 取得したカテゴリ情報を格納したドメインのリスト
	 * 空だった場合null
	 */
	public List<Category> findParentCategory() {
		return repository.findParentCategory();
	}
	
	/**
	 * 子カテゴリ一覧を取得する.
	 * 
	 * @return 取得したカテゴリ情報を格納したドメインのリスト
	 * 空だった場合null
	 */
	public List<Category> findChildCategory() {
		return repository.findChildCategory();
	}
	
	/**
	 * 孫カテゴリ一覧を取得する.
	 * 
	 * @return 取得したカテゴリ情報を格納したドメインのリスト
	 * 空だった場合null
	 */
	public List<Category> findGrandchildCategory() {
		return repository.findGrandchildCategory();
	}
}
