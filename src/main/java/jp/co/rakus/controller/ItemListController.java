package jp.co.rakus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.rakus.domain.Category;
import jp.co.rakus.domain.Item;
import jp.co.rakus.service.CategoryService;
import jp.co.rakus.service.ItemService;

/**
 * 一覧画面に関する処理を行うコントローラー.
 * 
 * @author kento.uemura
 * 
 */
@Controller
@RequestMapping("/list")
public class ItemListController {
	@Autowired
	private ItemService service;
	@Autowired
	private CategoryService categoryService;

	/**
	 * 一覧画面の表示
	 * 
	 * @param model
	 *            モデル
	 * @param page
	 *            現在のページ
	 * @return 一覧画面
	 */
	@RequestMapping("/view")
	public String index(Model model, String name, Integer parent, Integer child, Integer grandchild, String brand,
			Integer page, @RequestParam(name = "brandmatch", required = false) String brandMatch) {
		// カテゴリを調べてモデルにセット
		List<Category> parentCategories = categoryService.findParentCategory();
		model.addAttribute("parents", parentCategories);
		List<Category> childCategories = categoryService.findChildCategory();
		model.addAttribute("children", childCategories);
		List<Category> grandchildCategories = categoryService.findGrandchildCategory();
		model.addAttribute("grandchildren", grandchildCategories);

		// brandの完全一致検索
		if (brandMatch == null || "".equals(brandMatch)) {
			if (brand != null && !"".equals(brand)) {
				if ("\"".equals(brand.substring(0, 1))
						&& "\"".equals(brand.substring(brand.length() - 1, brand.length()))) {
					brandMatch = brand.substring(1, brand.length() - 1);
				}
			}
		}

		// pageのパラメータが無いか、0未満なら1にする,最大値より多い場合最大値にする
		Integer pageMax = service.pageByNameAndCategoryAndBrandAndPage(name, parent, child, grandchild, brand,
				brandMatch);
		model.addAttribute("pageMax", pageMax);
		if (page == null || page <= 0) {
			page = 1;
		}
		if (page > pageMax) {
			page = pageMax;
		}

		List<Item> items = service.findByNameAndCategoryAndBrandAndPage(name, parent, child, grandchild, brand,
				brandMatch, page);
		model.addAttribute("items", items);
		return "/list";
	}
}
