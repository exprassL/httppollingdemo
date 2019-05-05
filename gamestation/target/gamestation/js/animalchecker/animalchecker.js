define(/*[ "jq" ],*/ function() {
	var animalchecker = {};
	var countingDown = 30;
	var interval;
	var selectedChessman = {};
	animalchecker.prepare = function() {
		$.ajax({
			url : "/gs/game/animalchecker/prepare?r=" + Math.random(),
			type : "GET",
			dataType : "json",
			success : function(data) {
				if(!data.success && data.code == 302) {
					location.href = "/gs" + data.data + "?r=" + Math.random();
					return;
				}
				$("#box > div").attr("act", "enable");
				$("#box > div").attr("class", "hidden");
				if(data.side == "OFFENSE") {
					$("p.side").html("你是<span class='OFFENSE'>攻方</span>，执<span class='OFFENSE'>红</span>色");
					$("p.result").text("挑战开始，请先行...");
				}else{
					$("p.side").html("你是<span class='DEFENSE'>守方</span>，执<span class='DEFENSE'>蓝</span>色");
					$("p.result").text("已建立，等待其他用户加入...");
					countingDown = 60;
				}
				$("p.side").show();
				interval = setInterval(function() {
					$("p.counting_down > b").text(--countingDown);
					if(countingDown <= 0) {
						$("p.result").text("等待超时，游戏结束。");
						animalchecker.runTimeout(data.data.gameId);
						clearInterval(interval);
					}
				}, 1000);
				$("p.result").show();
				$("p.counting_down").show();
				
				animalchecker.listen(data.data.gameId, data.data.status);
				
				$("#box > div").empty();
				$(box).on("click", "div[act=enable]", function() {
					var params = {};
					params.locationX = $(this).attr("locationX");
					params.locationY = $(this).attr("locationY");
					params.locationX0 = selectedChessman.locationX;
					params.locationY0 = selectedChessman.locationY;
					selectedChessman.locationX = $(this).attr("locationX");
					selectedChessman.locationY = $(this).attr("locationY");
					params.gameId = data.data.gameId;
					animalchecker.tackAction(params);
				});
			}
		});
	};

	animalchecker.listen = function(gameId, status) {
		$.ajax({
			url : "/gs/game/animalchecker/listen?gameId=" + gameId + "&status=" + status + "&r=" + Math.random(),
			type : "GET",
			dataType : "json",
			success : function(data) {
				if(!data.success && data.code == 302) {
					location.href = "/gs" + data.data + "?r=" + Math.random();
					return;
				}
				animalchecker.displayChessboard(data.data.changed);
				if(data.data.status == -4) {
					$(box).off("click");
					alert(data.data.winner + "\n点击返回开始页。");
					window.location.reload(true);
				}else{
					if(data.data.myturn) {
						$("p.result").text("轮到你了。");
					}else{
						$("p.result").text("轮到对方了。");
					}
					countingDown = 30;
					animalchecker.listen(data.data.gameId, data.data.status);
				}
			}
		});
	};
	
	animalchecker.tackAction = function(params) {
		$.ajax({
			url : "/gs/game/animalchecker/action?r=" + Math.random(),
			type : "POST",
			data: params,
			dataType : "json",
			success : function(data) {
				if(!data.success && data.code == 302) {
					location.href = "/gs" + data.data + "?r=" + Math.random();
					return;
				}
				if(data.success) {
					selectedChessman = {};
				}else{
					$("p.result").text(data.cause);
				}
			}
		})
	};

	var chessmen = {"象": "🐘", "虎": "🐅", "狼": "🐺", "狗": "🐕", "猫": "🐱", "鼠": "🐀", "球": "⚽"};
	var snds = {"象": "elephant", "虎": "tiger", "狼": "wolf", "狗": "dog", "猫": "cat", "鼠": "mouse", "球": "ball"};
	animalchecker.displayChessboard = function(changed) {
		if(!changed) {
			console.log("nothing returned");
			return;
		}
		changed.forEach(function(chessman, index, arr) {
			if(!chessman.chessmanName) {
				$("#box div[locationX=" + chessman.location[0] + "][locationY=" + chessman.location[1] + "]").removeClass().text("");
			}else{
				$("#box div[locationX=" + chessman.location[0] + "][locationY=" + chessman.location[1] + "]").attr("class", chessman.player.side).text(chessmen[chessman.chessmanName]);
				$("body audio").attr("src", "../css/snd/" + snds[chessman.chessmanName] + ".mp3");
			}
		});
	};

	animalchecker.runTimeout = function(gameId) {
		$.ajax({
			url : "/gs/game/animalchecker/runtimeout?r=" + Math.random(),
			type : "POST",
			data: {"gameId": gameId},
			dataType : "json",
			success : function(data) {
				if(!data.success && data.code == 302) {
					location.href = "/gs" + data.data + "?r=" + Math.random();
					return;
				}
			}
		})
	};

	return animalchecker;
});