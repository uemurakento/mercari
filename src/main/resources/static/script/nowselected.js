/**
 * urlのgetパラメータから今選択されている検索項目をformに反映するjs
 */

$(function(){
    var arg  = new Object;
    url = location.search.substring(1).split('&');
    for(i=0; url[i]; i++) {
        var k = url[i].split('=');
        arg[k[0]] = k[1];
    }
    
    var name = arg.name;
    if(name != null && name != ""){
    	name = name.replace(/\+/g, '%20');
    	$("#name").val(decodeURIComponent(name));
    }
    
    var brand = arg.brand;
    if(brand != null && brand != ""){
    	brand = brand.replace(/\+/g, '%20');
    	$("#brand").val(decodeURIComponent(brand));
    }else{
    	var brandMatch = arg.brandmatch;
    	if(brandMatch != null && brandMatch != ""){
    		$("#brand").val("\""+decodeURIComponent(brandMatch)+"\"");
    	}
    }
    
    var parent = arg.parent;
    var child = arg.child;
    var grandchild = arg.grandchild;
    if(parent != null && parent != 0){
    	$("#parent-category").val(parent);
    	$("#parent-category").change();
    }
    if(child != null && child != 0){
    	$("#child-category").val(child);
    	$("#child-category").change();
    }
    if(grandchild != null && grandchild != 0){
    	$("#grandchild-category").val(grandchild);
    	$("#grandchild-category").change();
    }
});
