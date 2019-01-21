/**
 * カテゴリ表示用のjs
 */

$(function(){
	childWrap();
	grandchildWrap();
	//現在の親カテゴリを保持する変数
	var nowParent;
	//現在の子カテゴリを保持する変数
	var nowChild;
	
	//子カテゴリの非表示
	function childWrap(){
		$(".child").wrap('<span>');
		$(".child-parent").wrap('<span>');
	}
	
	//孫カテゴリの非表示
	function grandchildWrap(){
		$(".grandchild").wrap('<span>');
		$(".grandchild-parent").wrap('<span>');
	}
	
	//親カテゴリが選ばれたら対応する子カテゴリを表示する
	$("#parent-category").change(function(){
		$("#child-category").val(0);
		$("#grandchild-category").val(0);
		if(nowParent != null){
			$(".child").each(function(index,element){
				var child = $(element).val();
				var parent =  $("#"+child).val();
				if(nowParent == parent){
					$(element).wrap('<span>');
				}
			});
		}
		if(nowChild != null){
			$(".grandchild").each(function(index,element){
				var grandchild = $(element).val();
				var parent =  $("#"+grandchild).val();
				if(nowChild == parent){
					$(element).wrap('<span>');
				}
			});
			nowChild = null;
		}
		var selected = $("#parent-category option:selected").val();
		nowParent = selected;
		//ここで選ばれている親カテゴリselected,の子カテゴリをselectboxに入れる
		$(".child").each(function(index,element){
			var child = $(element).val();
			var parent =  $("#"+child).val();
			if(selected == parent){
				$(element).unwrap('<span>');
			}
		});
	});

	//子カテゴリが選ばれたら対応する孫カテゴリを表示する
	$("#child-category").change(function(){
		$("#grandchild-category").val(0);
		if(nowChild != null){
			$(".grandchild").each(function(index,element){
				var grandchild = $(element).val();
				var parent =  $("#"+grandchild).val();
				if(nowChild == parent){
					$(element).wrap('<span>');
				}
			});
		}
		
		var selected = $("#child-category option:selected").val();
		nowChild = selected;
		//ここで選ばれている親カテゴリselected,の子カテゴリをselectboxに入れる
		$(".grandchild").each(function(index,element){
			var grandchild = $(element).val();
			var parent =  $("#"+grandchild).val();
			if(selected == parent){
				$(element).unwrap('<span>');
			}
		});
	});
});