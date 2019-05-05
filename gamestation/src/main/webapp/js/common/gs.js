/**
 * gs系统的全局模块。
 */
define(function() {
	function getGameList() {
		$.ajax({
			url : "/gs/game/system/listall?r=" + Math.random(),
			type : "GET",
			dataType : "json",
			success : function(data) {
				if(!data.success && data.code == 302) {
					location.href = "/gs" + data.data + "?r=" + Math.random();
					return;
				}
				if(data.success && data.data.length > 0) {
					allgames.innerHTML = "";
					data.data.forEach(function(value, index, arr) {
						allgames.innerHTML += value.gameHTML;
					});
					$("#btn_login").hide();
					$("#btn_reg").hide();
					$("#btn_logout").css("display", "block");
				}
			}
		});
	}
	return {
		getGameList: getGameList
	}
})