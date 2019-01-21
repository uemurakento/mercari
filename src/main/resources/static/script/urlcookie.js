/**
 * ページのurlをcookieに保存しておくjs
 */

$(function(){
	$("#item-detail").click(function(){
		console.log(location.href);
		$.cookie("BACK_URL", location.href,{path:'/'});
	});
});