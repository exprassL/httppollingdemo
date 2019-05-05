var confJS = {};
confJS.requireConf = {
	baseUrl: "/gs/js",
	paths : {
		"jq" : "lib/jquery-2.1.4.min",
		"domReady" : "lib/domReady",
		"autoInclude" : "common/autoInclude",
		"commonDialog" : "common/commonDialog",
		"gs" : "common/gs",
		"animalchecker" : "animalchecker/animalchecker",
		"jsencrypt" : "lib/jsencrypt.min",
		"rsaencrypter" : "common/RSAEncrypter"
	},
	shim : {
		"jsencrypt": {
			exports: "JSEncrypt"
		}
	}
};