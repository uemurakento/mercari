/**
 * ページング用のjs
 */

$(function(){
	//prev,nextのリンク先をページごとに変更する
    var arg  = new Object;
    url = location.search.substring(1).split('&');
    for(i=0; url[i]; i++) {
        var k = url[i].split('=');
        arg[k[0]] = k[1];
    }
    var page = arg.page;
    var pageMax = $("#pageMax").val();
    if(page == null || page == 1){
    	page = 1;
    	$(".previous").addClass("disabled");
    	$(".previous a").addClass("disabledlink");
    }
    $("#page-jump-text").val(parseInt(page));
    if(page == pageMax){
    	$(".next").addClass("disabled");
    	$(".next a").addClass("disabledlink");
    }
    //hrefを置き換える
    if(page > 1){
    	var prevpage = parseInt(page) - 1;
    	var url = "";
    	var firstTimeFlag = true;
    	for(var item in arg){
    		if(item != "page"){
    			if(firstTimeFlag){
    				url += "?";
    				firstTimeFlag = false;
    			}else{
    				url += "&";
    			}
    			url += item + "=" + arg[item];
    		}
    	}
    	if(firstTimeFlag){
    		url += "?";
    		firstTimeFlag = false;
    	}else{
    		url += "&";
    	}
    	url += "page="+prevpage;
    	
    	$(".previous a").attr("href","/"+url);
    }
    if(page < pageMax){
    	var nextpage = parseInt(page) + 1;
    	var url = "";
    	var firstTimeFlag = true;
    	for(var item in arg){
    		if(item != "page"){
    			if(firstTimeFlag){
    				url += "?";
    				firstTimeFlag = false;
    			}else{
    				url += "&";
    			}
    			url += item + "=" + arg[item];
    		}
    	}
    	if(firstTimeFlag){
    		url += "?";
    		firstTimeFlag = false;
    	}else{
    		url += "&";
    	}
    	url += "page="+nextpage;
    	$(".next a").attr("href","/"+url);
    }
});