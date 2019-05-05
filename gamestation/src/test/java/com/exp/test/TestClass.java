package com.exp.test;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.Test;
import org.springframework.beans.propertyeditors.PatternEditor;

import com.exp.common.pojo.gamepojo.GamePlayer;
import com.exp.common.util.AESEncryption;
import com.exp.common.util.ArrayUtil;
import com.exp.common.util.RSAEncryption;

@SuppressWarnings("unused")
public class TestClass {

	public static void main(String[] args) throws Exception {
//		System.out.println(AESEncryption.encrypt("kms_user"));
//		System.out.println(AESEncryption.encrypt("kms_user1234"));
//		System.out.println(AESEncryption.encrypt("Abc!23"));
//		System.out.println(AESEncryption.encrypt("SiTech-KMS"));
//		System.out.println(AESEncryption.encrypt("SiTech-10000"));
//		System.out.println(AESEncryption.decrypt("a618c174b66da2d911e5357962760875"));// user
//		System.out.println(AESEncryption.decrypt("a9e1ff72dff199e825f9bd5a24698332"));// pwd
//		System.out.println(AESEncryption.encrypt("root")); //$NON-NLS-1$
//		System.out.println(AESEncryption.encrypt("rootroot")); //$NON-NLS-1$
		System.out.println(AESEncryption.encrypt("exp")); //$NON-NLS-1$
		System.out.println(AESEncryption.encrypt("Qwert@13")); //$NON-NLS-1$
//		System.out.println(AESEncryption.decrypt("13b22bfa8d6a9a7c54e25b42a84dc0ba")); //$NON-NLS-1$
//		System.out.println(RSAEncryption.encrypt("1234"));
//		System.out.println(RSAEncryption.decrypt("XjQGdybgl5o/bjsIIFrCCNvKBRFldbqWggYYiflKGfOuobuoj+tahEw7SxMrj27VEXwnPdbLsCE8YJIlpIvkeVe+TZ2djVIRW/93hfba0fWYtkIDcQLjUMjomZv3fNSiAhnHAN3n8zDvauHqBSlisgCm/QJzG3gL/CvYK7kjddY="));
//		System.out.println(RSAEncryption.decrypt("bHUDPLL+Qr1kkZj0RByyxM4mr4/epko7Rg2m5TNs0eEkzdvzLWs1RpgGLKCrp9zIXks/QzBM+TTnN97jYVylphxVeb4p0T85gKFIDuVCKhikgHiL+ImQsKc1BFF5PVZ/MldlEm5CIFP834baV5bpzyNWFF9+towff/1QH+IvSWI="));
//		System.out.println(RSAEncryption.decrypt("hhIOuYH1CWVG9t9eV45V9ywx1fiNOtSAv5qaY5bX63ypMHjJfVh+eXy2pNXrV1F7ibmpjhUP3tpQ2+gclpwkemG+3gKZYgOKxy1KDHTuNrcOGuW8G9CANA3edxMSlIFOTuBwpWCU5W4vqF0kOvkqtlACIqR4nOYKYGGdU36pjrI="));
//		System.out.println(AESEncryption.decrypt("d501ea99fe7f4de04c904c18a78c48f2"));
		
//		GamePlayer[] arr = new GamePlayer[1];
//		GamePlayer p1 = new GamePlayer().asDefense();
//		arr[0] = p1;
//		System.out.println(arr[0].getSide());
//		GamePlayer[] copy = ArrayUtil.disorderRandomly(arr);
//		System.out.println(copy[0].getSide());
//		copy[0].asOffense();
//		System.out.println(copy[0].getSide());
//		System.out.println(arr[0].getSide());
//		System.out.println(copy);
//		System.out.println(arr);
		
//		Pattern p = Pattern.compile("^1[3-9][0-9]{9}$"); //$NON-NLS-1$
//		System.out.println(p.matcher("11605563925").matches()); //$NON-NLS-1$
	}
}
