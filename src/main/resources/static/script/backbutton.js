/**
 * 詳細画面の戻るボタン用のjs
 */

$(function(){
	var url = $.cookie("BACK_URL");
	$("#back-button").attr("href",url);
});