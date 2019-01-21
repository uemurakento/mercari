package jp.co.rakus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.rakus.domain.Item;
import jp.co.rakus.service.ItemService;

/**
 * 詳細画面表示をするコントローラ.
 * 
 * @author kento.uemura
 *
 */
@Controller
@RequestMapping("/itemdetail")
public class ItemDetailController {
	@Autowired
	ItemService service;
	/**
	 * 詳細画面を表示する.
	 * 
	 * @param model モデル
	 * @param id 表示するitemの主キー
	 * @return 詳細画面
	 */
	@RequestMapping("/view")
	public String viewItemDetail(Model model,Integer id) {
		Item item = service.findById(id);
		model.addAttribute("item", item);
		return "/detail";
	}

}
