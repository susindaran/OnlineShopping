package com.betadevels.onlineshopping.util;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class PasswordEncipher
{
    private static MessageDigest messageDigest;
    public static String encryptWithMD5(String password)
    {
        try
        {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();

            byte[] bytes = password.getBytes();
            byte[] digested = messageDigest.digest(bytes);

            StringBuilder sb = new StringBuilder();
            for( byte b : digested )
            {
                sb.append(Integer.toHexString(0xff & b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
