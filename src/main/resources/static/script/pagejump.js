/**
 * ページング(ページジャンプ)用のjs
 */

$(function(){
    $("#page-jump-button").click(function(){
    	var page = $("#page-jump-text").val();
        var arg  = new Object;
        url = location.search.substring(1).split('&');
        for(i=0; url[i]; i++) {
            var k = url[i].split('=');
            arg[k[0]] = k[1];
        }
        var jumpUrl = "";
    	var firstTimeFlag = true;
    	for(var item in arg){
    		console.log(item);
    		if(item != "page"){
    			if(firstTimeFlag){
    				jumpUrl += "?";
    				firstTimeFlag = false;
    			}else{
    				jumpUrl += "&";
    			}
    			jumpUrl += item + "=" + arg[item];
    		}
    	}
    	if(firstTimeFlag){
    		jumpUrl += "?";
    		firstTimeFlag = false;
    	}else{
    		jumpUrl += "&";
    	}
    	jumpUrl += "page="+page;

    	window.location.href = '/'+jumpUrl;
    });
});