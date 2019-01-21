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
 * 商品詳細の編集に関するコントローラ.
 * 
 * @author kento.uemura
 *
 */
@Controller
@RequestMapping("/itemdetail")
public class ItemDetailEditController {
	@Autowired
	ItemService service;
	@Autowired
	private CategoryService categoryService;

	@ModelAttribute
	ItemForm setUpForm() {
		return new ItemForm();
	}

	/**
	 * 商品詳細を表示する.
	 * 
	 * @param model
	 *            モデル
	 * @param id
	 *            表示するitemの主キー
	 * @return 詳細画面
	 */
	@RequestMapping("/edit")
	public String viewEdit(Model model, Integer id) {
		// カテゴリを調べてモデルにセット
		List<Category> parentCategories = categoryService.findParentCategory();
		model.addAttribute("parents", parentCategories);
		List<Category> childCategories = categoryService.findChildCategory();
		model.addAttribute("children", childCategories);
		List<Category> grandchildCategories = categoryService.findGrandchildCategory();
		model.addAttribute("grandchildren", grandchildCategories);

		Item item = service.findById(id);
		model.addAttribute("item", item);
		return "/edit";
	}

	/**
	 * 商品情報を更新する.
	 * 
	 * @param model モデル
	 * @param form 入力フォームの値
	 * @param result 入力値チェックに使用
	 * @return 商品詳細ページにリダイレクト
	 */
	@RequestMapping("/update")
	public String updateDetail(Model model,@Validated ItemForm form,BindingResult result) {
		if(result.hasErrors()) {
			return viewEdit(model, form.getId());
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

		service.update(item);

		return "redirect:/itemdetail/view?id="+form.getId();
	}
}
