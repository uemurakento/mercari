//package jp.co.rakus.controller;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import jp.co.rakus.domain.OriginalItem;
//import jp.co.rakus.service.CategoryService;
//import jp.co.rakus.service.ItemService;
//import jp.co.rakus.service.OriginalItemService;
//
//@RequestMapping("/insert")
//@Controller
//public class InsertController {
////	private static final Integer LIMIT_LINE = Integer.valueOf(100000);
//	private static final List<Integer> TEXT_FIELD_VALUE = Arrays.asList(1, 3, 4, 7);
//	@Autowired
//	private OriginalItemService originalItemService;
//	@Autowired
//	private CategoryService categoryService;
//	@Autowired
//	private ItemService itemService;
//	@RequestMapping("/original")
//	public void insert() {
//		List<OriginalItem> originalItems = originalItemService.findAll();
//		System.out.println("originalから情報取得完了");
//		itemService.insert(originalItems);
//	}
////	@RequestMapping("/")
////	public void insert() {
////		List<String> categories = originalItemService.findCategoryAll();
////		System.out.println(categories.size());
////		categoryService.insert(categories);
////	}
//
////	@RequestMapping("/")
////	public void insert() {
////		String path = "C:\\Users\\rakus\\Desktop\\3.商品データ管理システム研修\\20.設計\\DB\\train.tsv";
////		insertCsv(path);
////	}
//
//	//	@RequestMapping("/")
////	public void insert() {
////		String path = "C:\\Users\\rakus\\Desktop\\3.商品データ管理システム研修\\20.設計\\DB\\train.tsv";
////		for (int i = 1; i <= 1500000; i += LIMIT_LINE) {
////			String originalItemAll = readCsv(path, i);
////			service.InsertOriginalItemAll(originalItemAll);
////		}
////	}
////
////	public static String readCsv(String path, Integer offset) {
////		StringBuilder result = new StringBuilder();
////		try {
////			// ファイルを読み込む
////			FileReader fr = new FileReader(path);
////			BufferedReader br = new BufferedReader(fr);
////
////			// 読み込んだファイルを１行ずつ処理する
////			String line;
////			Integer i = 1;
////			while ((line = br.readLine()) != null && i < offset + LIMIT_LINE) {
////				if (i > offset) {
////					// 区切り文字"\t"で分割する
////					List<String> fields = Arrays.asList(line.split("\t", 0));
////					if (i == 1) {
////						i++;
////						continue;
////					}
////					Integer fieldCount = 0;
////					result.append("(");
////					for (String field : fields) {
////						field = field.replace("\'", "\'\'");
////						// System.out.println(token.nextToken());
////						if (fieldCount == 0) {
////							fieldCount++;
////							continue;
////						}
////						if (TEXT_FIELD_VALUE.contains(fieldCount)) {
////							if (!"".equals(field)) {
////								result.append("'");
////								result.append(field);
////								result.append("'");
////							}else {
////								result.append("null");
////							}
////						} else {
////							result.append(field);
////						}
////						result.append(",");
////						fieldCount++;
////					}
////					result.setLength(Math.max(result.length() - 1, 0));
////					result.append("),\n");
////				}
////				i++;
////			}
////			result.setLength(Math.max(result.length() - 2, 0));
////
////			// 終了処理
////			br.close();
////
////		} catch (IOException ex) {
////			// 例外発生時処理
////			ex.printStackTrace();
////		}
////
////		return result.toString();
////	}
//	
//	public void insertCsv(String path) {
//		List<String> lines = new ArrayList<String>();
//		try {
//			// ファイルを読み込む
//			FileReader fr = new FileReader(path);
//			BufferedReader br = new BufferedReader(fr);
//
//			// 読み込んだファイルを１行ずつ処理する
//			String line;
//			while ((line = br.readLine()) != null) {
//				lines.add(line);
//			}
//			// 終了処理
//			br.close();
//
//		} catch (IOException ex) {
//			// 例外発生時処理
//			ex.printStackTrace();
//		}
//		lines.remove(0);
//		insertOriginal(lines);
//	}
//	
//	public void insertOriginal(List<String> lines) {
//		Integer lineCount = 1;
//		for(String line:lines) {
//			if(lineCount % 1000 == 0) {
//				System.out.println(lineCount);
//			}
//			lineCount++;
//			List<String> fields = Arrays.asList(line.split("\t", -1));
//			StringBuilder result = new StringBuilder();
//			Integer fieldCount = 0;
//			result.append("(");
//			for (String field : fields) {
//				field = field.replace("\'", "\'\'");
//				if (fieldCount == 0) {
//					fieldCount++;
//					continue;
//				}
//				if (TEXT_FIELD_VALUE.contains(fieldCount)) {
//					if (!"".equals(field)) {
//						result.append("'");
//						result.append(field);
//						result.append("'");
//					}else {
//						result.append("null");
//					}
//				} else {
//					result.append(field);
//				}
//				result.append(",");
//				fieldCount++;
//			}
//			result.setLength(Math.max(result.length() - 1, 0));
//			result.append(")");
//			originalItemService.InsertOriginalItemAll(result.toString());
//		}
//	}
//}