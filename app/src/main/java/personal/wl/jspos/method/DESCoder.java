package personal.wl.jspos.method;

import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class DESCoder {
    // 密钥
    public static String FINAL_DESKEY = "Buynow20";


    //des加密后base64
    public static String Encrypt(String inputData)
            throws Exception {
        String inputKey = FINAL_DESKEY;
        inputData = new String(inputData.getBytes(), "UTF-8");
        Cipher enCipher = Cipher.getInstance("DES/ECB/NoPadding");// 得到加密对象Cipher
        int blockSize = enCipher.getBlockSize();
        byte[] bytes_inputKey = inputKey.getBytes();
        byte[] bytes_inputData = inputData.getBytes();
        int bytes_inputData_length = bytes_inputData.length;

        if (bytes_inputData_length % blockSize != 0) {
            bytes_inputData_length = bytes_inputData_length + (blockSize - (bytes_inputData_length % blockSize));
        }

        byte[] byte_inputData_new = new byte[bytes_inputData_length];
        //补0,相当于PaddingMode.Zeros;
        System.arraycopy(bytes_inputData, 0, byte_inputData_new, 0, bytes_inputData.length);

        Key key = new SecretKeySpec(bytes_inputKey, "DES");

        enCipher.init(Cipher.ENCRYPT_MODE, key);// 设置工作模式为加密模式，给出密钥和向量
        byte[] pasByte = enCipher.doFinal(byte_inputData_new);

//        BASE64 base64Encoder = new BASE64Encoder();
        String rt = new String(Base64.encode(pasByte, Base64.DEFAULT), "UTF-8");
        return rt;
    }

//    public static String Decrypt(String inputData)
//            throws Exception {
//        String inputKey = FINAL_DESKEY;
//        BASE64Decoder base64Decoder = new BASE64Decoder();
//        byte[] bytes_inputData = base64Decoder.decodeBuffer(inputData);
//        byte[] bytes_inputKey = inputKey.getBytes();
//
//        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
//        Key key = new SecretKeySpec(bytes_inputKey, "DES");
//
//        // 用密匙初始化Cipher对象
//        cipher.init(Cipher.DECRYPT_MODE, key);
//        byte decryptedData[] = cipher.doFinal(bytes_inputData);
//        String value = new String(decryptedData, "UTF-8");
//        return value;
//    }

    public static String Decrypt(String inputData)
            throws Exception {
        if (inputData == null)
            return null;
        byte[] buf = Base64.decode(inputData, Base64.DEFAULT);
        byte[] bt = decrypt(buf, FINAL_DESKEY.getBytes("UTF-8"));
        return new String(bt, "UTF-8").trim();
    }

    /**
     * Description 根据键值进行解密 加密键byte数组
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        SecretKey secureKey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, secureKey);

        return cipher.doFinal(data);
    }
}
