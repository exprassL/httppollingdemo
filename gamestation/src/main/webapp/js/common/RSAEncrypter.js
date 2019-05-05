define(["jsencrypt"], function(JSEncrypt) {
	var publicKey = 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCRz/TumoqRoOpXAWEUEjPJyR0sqQ28ssOkiLtaeNe5DrbwQv7Z4sdWOR/4SSC2SorIYKVATacOIHMWUodL2XTh2xQIIq+7xCON2nwRWOGF+FEZaNTP+fq9TKoCiDSGAmd89zisoB94ynGtw5Od+4y3KOTfdBHygZf/e+5/RDv2vwIDAQAB';
	var encrypt = function (str) {
		
		var encrypter = new JSEncrypt();
        encrypter.setPublicKey(publicKey);
        var data = encrypter.encrypt(str);
        return data;
	}
	
	return{
		encrypt: encrypt  
    }
});