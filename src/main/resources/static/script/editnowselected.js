/**
 * edit画面の現在の値表示のjs
 */

$(function(){
	//カテゴリ
	var parentId = $("#parent-id").val();
	$("#parent-category").val(parentId);
	$("#parent-category").change();
	var childId = $("#child-id").val();
	$("#child-category").val(childId);
	$("#child-category").change();
	var grandchildId = $("#grandchild-id").val();
	$("#grandchild-category").val(grandchildId);
	$("#grandchild-category").change();
	
	//コンディション
	var condition = $("#item-condition").val();
	$('input[name=condition]').val([condition]);
});