define(["jq"], function(){
	var autoInclude = function() {
		var common_modules = document.getElementsByClassName("common_module");
		for(var i = 0; i < common_modules.length; i++) {
			$(common_modules[i]).load(common_modules[i].getAttribute("data") + "?r=" + Math.random());
		}
	}
	return {
		include: autoInclude
	};
})