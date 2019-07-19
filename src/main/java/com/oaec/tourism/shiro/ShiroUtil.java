package com.oaec.tourism.shiro;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import java.util.UUID;

/**
 * 加盐加密
 */
public class ShiroUtil {
    /**
     * 生成32的随机盐值
     */
    public static String createSalt(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成加盐加密后的密码
     * @param password 明文密码
     * @param salt 盐
     * @return
     */
    public static String createPwdBySalt(String password, String salt){
        String hashAlgorithmName = "MD5";//加密方式
        ByteSource saltBytes = ByteSource.Util.bytes(salt);//以UUID作为盐值
        int hashIterations = 1024;//加密1024次
        SimpleHash hash = new SimpleHash(hashAlgorithmName, password, saltBytes,hashIterations);
        return hash.toString();
    }

    public static void main(String[] args) {
        String salt = createSalt();
        String pwdBySalt = createPwdBySalt("123456", salt);
        String pwdBySalt2= createPwdBySalt("123456", salt);

        System.out.println(pwdBySalt);
        System.out.println(pwdBySalt2);
    }
}
