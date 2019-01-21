package jp.co.rakus.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.rakus.domain.Category;
import jp.co.rakus.domain.Item;
import jp.co.rakus.domain.OriginalItem;
import jp.co.rakus.repository.CategoryRepository;
import jp.co.rakus.repository.ItemRepository;

/**
 * Itemを操作するサービス.
 * 
 * @author kento.uemura
 *
 */
@Service
public class ItemService {
	@Autowired
	private ItemRepository repository;
	@Autowired
	private CategoryRepository categoryRepository;

	/**
	 * insertメソッド
	 * 
	 * @param item insertする情報を格納したドメイン
	 * @return 自動採番された情報が追加されたドメイン
	 */
	public Item insert(OriginalItem originalItem) {
		Item item = new Item();
		item.setName(originalItem.getName());
		item.setCondition(originalItem.getCondition_id());
		item.setBrand(originalItem.getBrand());
		item.setPrice(originalItem.getPrice());
		item.setShipping(originalItem.getShipping());
		item.setDescription(originalItem.getDescription());
		
		//category名に対応するcategoryテーブルの主キーを取得する
		String categoryNameAll = originalItem.getCategory_name();
		if(categoryNameAll != null) {
			List<String> categoryNames = Arrays.asList(categoryNameAll.split("/", 0));
			String categoryNameThirdGeneration = String.join("/", categoryNames.subList(0,3));
			Category category = categoryRepository.findByNameAll(categoryNameThirdGeneration);
			item.setCategory(category.getId());
		}
		return repository.insert(item);
	}
	
	/**
	 * 複数insertする.
	 * 
	 * @param originalItems insertするoriginalItemドメインのリスト
	 * @return 自動採番された番号の情報が追加されたドメインのリスト
	 */
	public List<Item> insert(List<OriginalItem> originalItems){
		List<Item> items = new ArrayList<>();
		Integer count = 1;
		for(OriginalItem originalItem:originalItems) {
			if(count % 10000 == 0) {
				System.out.println(count);
			}
			items.add(insert(originalItem));
			count++;
		}
		return items;
	}
	
	/**
	 * ページ番号を指定して、Item情報を調べる.
	 * 
	 * @param pageNum 指定するページ番号
	 * @return 取得したItem情報が格納されたドメインのリスト
	 * 見つからなかった場合はnull
	 */
	public List<Item> findByPage(Integer pageNum){
		return repository.findByPage(pageNum);
	}
	
	/**
	 * Itemsの全件数を調べる.
	 * 
	 * @return 見つかった件数
	 */
	public Integer countItems() {
		return repository.countItems();
	}

	/**
	 * 一覧表示の最大ページ数を調べる.
	 * 
	 * @return ページ数
	 */
	public Integer maxPage() {
		return repository.maxpage();
	}
	
	/**
	 * 条件指定時の一覧表示の最大ページ数を調べる.
	 * 
	 * @param name 名前
	 * @param parentCategoryId 親カテゴリのID
	 * @param childCategoryId 子カテゴリのID
	 * @param grandchildCategoryId 孫カテゴリのID
	 * @param brand ブランド
	 * @param brandMatch ブランドの完全一致検索用
	 * @param pageNum 
	 * @return
	 */
	public Integer pageByNameAndCategoryAndBrandAndPage(String name,Integer parentCategoryId,Integer childCategoryId,Integer grandchildCategoryId,String brand,String brandMatch){
		return repository.pageByNameAndCategoryAndBrandAndPage(name, parentCategoryId, childCategoryId, grandchildCategoryId, brand,brandMatch);
	}
	
	/**
	 * 条件を指定して、item情報を取得する.
	 * 
	 * @param name 名前
	 * @param parentCategoryId 親カテゴリのID
	 * @param childCategoryId 子カテゴリのID
	 * @param grandchildCategoryId 孫カテゴリのID
	 * @param brand ブランド
	 * @param brandMatch ブランドの完全一致検索用
	 * @param pageNum ページ番号
	 * @return 取得したitem情報を格納したドメインのリスト
	 * 空ならnullを返す
	 */
	public List<Item> findByNameAndCategoryAndBrandAndPage(String name,Integer parentCategoryId,Integer childCategoryId,Integer grandchildCategoryId,String brand,String brandMatch,Integer pageNum){
		return repository.findByNameAndCategoryAndBrandAndPage(name, parentCategoryId, childCategoryId, grandchildCategoryId, brand, brandMatch, pageNum);
	}
	
	/**
	 * idを指定してitem情報を取得する.
	 * 
	 * @param id 主キー
	 * @return 取得したitem情報を格納したドメイン
	 * 見つからなければnull
	 */
	public Item findById(Integer id) {
		return repository.findById(id);
	}
	
	/**
	 * itmesテーブルのupdateメソッド.
	 * 
	 * @param item 更新する情報
	 * @return　引数
	 */
	public Item update(Item item) {
		return repository.update(item);
	}
	
	/**
	 * itemsテーブルのinsertメソッド.
	 * 
	 * @param item insertする情報
	 * @return 自動採番されたidを含むitem
	 */
	public Item insert(Item item) {
		return repository.insert(item);
	}
}
