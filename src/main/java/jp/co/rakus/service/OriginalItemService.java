package jp.co.rakus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.rakus.domain.OriginalItem;
import jp.co.rakus.repository.OriginalItemRepository;

/**
 * OriginalItemを操作するサービス.
 * 
 * @author kento.uemura
 *
 */
@Service
public class OriginalItemService {
	@Autowired
	private OriginalItemRepository repository;
	
	/**
	 * insertを行う.
	 * 
	 * @param originalItemAll フォーマットされた情報
	 */
	public void InsertOriginalItemAll(String originalItemAll) {
		repository.InsertOriginalItemAll(originalItemAll);
	}
	
	/**
	 * カテゴリの全件検索を行う.
	 * 
	 * @return 検索されたカテゴリ
	 */
	public List<String> findCategoryAll(){
		return repository.findCategoryAll();
	}
	
	/**
	 * 全件検索を行う.
	 * 
	 * @return 検索された情報が格納されたドメインのリスト
	 * 見つからなかった場合null
	 */
	public List<OriginalItem> findAll(){
		return repository.findAll();
	}
}
