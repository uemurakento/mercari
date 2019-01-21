package jp.co.rakus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.rakus.domain.Category;
import jp.co.rakus.domain.Item;
import jp.co.rakus.form.ItemForm;
import jp.co.rakus.service.CategoryService;
import jp.co.rakus.service.ItemService;

/**
 * 商品追加機能を操作するコントローラ.
 * 
 * @author kento.uemura
 *
 */
@Controller
@RequestMapping("/add")
public class AddItemController {
	@Autowired
	CategoryService categoryService;
	@Autowired
	ItemService itemService;
	@ModelAttribute
	public ItemForm setUpForm() {
		return new ItemForm();
	}
	/**
	 * 商品追加画面を表示する.
	 * 
	 * @return 商品追加画面
	 */
	@RequestMapping("/view")
	public String viewAddItem(Model model) {
		// カテゴリを調べてモデルにセット
		List<Category> parentCategories = categoryService.findParentCategory();
		model.addAttribute("parents", parentCategories);
		List<Category> childCategories = categoryService.findChildCategory();
		model.addAttribute("children", childCategories);
		List<Category> grandchildCategories = categoryService.findGrandchildCategory();
		model.addAttribute("grandchildren", grandchildCategories);
		return "/add";
	}
	
	/**
	 * 商品を追加する.
	 * 
	 * @param model モデル
	 * @param form 入力フォームの値
	 * @param result 入力値チェックに使用
	 * @return 商品一覧画面
	 */
	@RequestMapping("/additem")
	public String addItem(Model model,@Validated ItemForm form,BindingResult result) {
		if(result.hasErrors()) {
			return viewAddItem(model);
		}
		Item item = new Item();
		item.setId(form.getId());
		item.setName(form.getName());
		item.setPrice(Integer.valueOf(form.getPrice()));
		if(form.getParent() != 0 && form.getChild() != 0 && form.getGrandchild() != 0) {
			item.setCategory(form.getGrandchild());
		}else {
			item.setCategory(null);
		}
		item.setBrand(form.getBrand());
		item.setCondition(Integer.valueOf(form.getCondition()));
		item.setDescription(form.getDescription());
		itemService.insert(item);

		return "redirect:/";
	}
}
