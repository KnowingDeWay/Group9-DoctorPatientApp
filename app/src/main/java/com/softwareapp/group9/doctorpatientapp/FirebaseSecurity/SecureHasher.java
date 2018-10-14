package com.softwareapp.group9.doctorpatientapp.FirebaseSecurity;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecureHasher {
    private static final String HASHING_ALGORITHM = "MD5";
    private static final String SALT = "E_SIG$674zKa_MUM-Jg5%ypERx(8mwz01v83V$vlwJ0TGn#F1RGq267x!TQ5ojuD";
    private static final String PEPPER = "AXj%1S^@(4fC*t_%";

    private SecureHasher(){

    }

    public static SecureHasher getInstance() {
        return new SecureHasher();
    }

    public String generateHash(String stringToHash){
        try{
            MessageDigest digest = MessageDigest.getInstance("MD5");
            String seasonedString = SALT + stringToHash + PEPPER;
            digest.update(seasonedString.getBytes(), 0, seasonedString.length());
            BigInteger bigInteger = new BigInteger(1, digest.digest());
            return bigInteger.toString();
        } catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return "";
    }


}
