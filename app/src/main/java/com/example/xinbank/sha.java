package com.example.xinbank;

import java.math.BigInteger;
import java.security.MessageDigest;

public class sha {
    public static String otuhashing(String inputdata){
        byte[]inputData = inputdata.getBytes();
        byte[]outputData = new byte[0];

        try {
            outputData = sha.encrypteSHA(inputData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BigInteger shaData = new BigInteger(1,outputData);
        return shaData.toString(16);
    }
    public static byte[] encrypteSHA(byte[]data)throws Exception{
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        sha.update(data);
        return sha.digest();
    }
}
