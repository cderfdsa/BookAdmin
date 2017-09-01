package com.example.bookadmin;

import com.example.bookadmin.tools.utils.CipherUtil;
import com.example.bookadmin.tools.utils.LogUtils;

import org.junit.Test;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private static final String AES_PASSWORD = "TR%an%^&1^G4LUCK";

    @Test
    public void addition_isCorrect() throws Exception {
        String info = "123";
        try {
            byte[] pwdByte = AES_PASSWORD.getBytes("utf-8");//80 80
            for (int i = 0; i < pwdByte.length; i++) {
                pwdByte[i] = (byte) (0xf1 & pwdByte[i]);
            }
            SecureRandom random = new SecureRandom(pwdByte);
            byte[] ivBytes = new byte[16];
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            keygen.init(128, random);
            Key key = keygen.generateKey();

            Cipher cipher = Cipher.getInstance("AES/OFB128/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

            byte[] byte_encode = info.getBytes("utf-8");
            byte[] cipherText = cipher.doFinal(byte_encode);//-112

            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

            byte[] plainText = cipher.doFinal(cipherText);//49

            String value = CipherUtil.byte2hex(plainText);
            LogUtils.i("密码原始：" + info + "   加密 ：" + value);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}