require.config(confJS.requireConf);
require([ "domReady", "autoInclude", "gs" ],
		function(domReady, autoInclude, gs) {

			domReady(function() {
				autoInclude.include();
				gs.getGameList();
			});
		});