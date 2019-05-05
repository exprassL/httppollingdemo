define(function() {
	function showDialog(id, src, jsSrc) {
		$("body").append($("<div id='" + id + "' style='position:fixed;top:0;left:0;background-color:rgba(13,31,49,0.90);padding:1%;width:100%;height:100%;display:flex;align-items:center;justify-content:center;flex-wrap:wrap;align-content:center;'></div>"));
		$("#" + id).load(src + "?r=" + Math.random(), function() {
			if(jsSrc) {
				var js = document.createElement('script');
				js.type = 'text/javascript';
				js.async = true;
				js.src = jsSrc;
				var firstJs = document.getElementsByTagName('script')[0];
				firstJs.parentNode.insertBefore(js, firstJs);
			}
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