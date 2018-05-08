package com.rainyalley.architecture.util;

import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class DESedeTest {


    String base64EncodedDesedeStr = "cxh2LP0BPtwuUU6k9yNdT1rH7pDDSUXIzSzJXxR6ETGi2tEBOPps0CKE98KL6z0Q";


    @Test
    public void decrypt() throws Exception{
        byte[] src = Base64.getDecoder().decode(base64EncodedDesedeStr);

        SecretKey deskey = new SecretKeySpec(DESedeBuildKey("chinapnr"), "DESede");
        // 实例化Cipher
        Cipher c1 = Cipher.getInstance("DESede");
        c1.init(Cipher.DECRYPT_MODE, deskey);

        // 解密
        byte[] decoded = c1.doFinal(src);
        System.out.println(new String(decoded, "UTF-8"));
    }


    /**
     * 3DES-根据字符串生成密钥24位的字节数组
     *
     * @param keyStr
     *
     * @return
     *
     * @throws UnsupportedEncodingException
     */
    public static byte[] DESedeBuildKey(String keyStr) throws UnsupportedEncodingException {
        byte[] key = new byte[24];
        byte[] temp = keyStr.getBytes("UTF-8");

        if (key.length > temp.length) {
            System.arraycopy(temp, 0, key, 0, temp.length);

        } else {
            System.arraycopy(temp, 0, key, 0, key.length);
        }

        return key;
    }
}
