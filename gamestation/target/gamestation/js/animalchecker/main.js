require.config(confJS.requireConf);
require(["domReady", "commonDialog"], function(domReady, commonDialog){
	domReady(function() {
		$(box).width($("body").width() * 0.98);
		$(box).css("margin-top", $("body").height() * 0.1);
		$("#box > div").width(Math.floor((box.offsetWidth - 12) / 6));
		$("#box > div").height(Math.floor((box.offsetWidth - 12) / 6));
		$("#box > div").css("line-height", Math.floor((box.offsetWidth - 14) / 6) + "px");
		$(box).css("margin-left", ($("body").width() - box.offsetWidth) / 2 + "px");
		commonDialog.show("startbtn", "modules/start.html");
	})
});