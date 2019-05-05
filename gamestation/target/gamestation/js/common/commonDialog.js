define(/*["jq"],*/ function() {
	function showDialog(id, src) {
		$("body").append($("<div id='" + id + "' style='position:fixed;top:0;left:0;background-color:rgba(13,31,49,0.90);padding:1%;width:100%;height:100%;display:flex;align-items:center;justify-content:center;flex-wrap:wrap;align-content:center;'></div>"));
		$("#" + id).load(src + "?r=" + Math.random(), function() {
			$("#" + id).on("click", ".confirm", function() {
				var res = true;
				if(typeof confirm == "function") {
					res = confirm();
				}
				if(res) {
					$("#" + id).remove();
				}
			});
			$("#" + id).on("click", ".cancel", function() {
				var res = true;
				if(typeof cancel == "function") {
					res = cancel();
				}
				if(res) {
					$("#" + id).remove();
				}
			});
		});
	}
	
	return {
		show: showDialog
	};
})