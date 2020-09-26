package com.wsk.life.config;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES 是一种可逆加密算法，对用户的敏感信息加密处理
 * 对原始数据进行AES加密后，在进行Base64编码转化；
 * @author Antoneo
 * @create 2018-06-28 22:16
 **/
public class AesCBCUtils {
    /**
     * 加密用的Key 可以用26个字母和数字组成
     * 此处使用AES-128-CBC加密模式，key需要为16位。
     */



    /**
     * 加密
     * @param sSrc              明文
     * @param encodingFormat    字符集
     * @param sKey              秘钥
     * @param ivParameter       向量iv
     * @return
     * @throws Exception
     */
    public static String encrypt(String sSrc, String encodingFormat, String sKey, String ivParameter) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes(encodingFormat));
        return new BASE64Encoder().encode(encrypted);//此处使用BASE64做转码。
    }


    /**
     * 解密
     * @param sSrc              明文
     * @param encodingFormat    字符集
     * @param sKey              秘钥
     * @param ivParameter       iv向量
     * @return
     * @throws Exception
     */
    public static String decrypt(String sSrc, String encodingFormat, String sKey, String ivParameter) throws Exception {
        try {
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);//先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original,encodingFormat);
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }


    public static void main(String[] args) throws Exception {
//        String a=encrypt("{\"id\":\"536622115\",\"lv\":-1,\"tv\":-1,\"csrf_token\":\"\"}","utf-8","0CoJUm6Qyw8W8jud","0102030405060708");
//        System.out.println(a);
    }

}