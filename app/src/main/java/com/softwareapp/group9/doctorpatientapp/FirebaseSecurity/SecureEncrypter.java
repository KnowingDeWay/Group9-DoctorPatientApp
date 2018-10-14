package com.softwareapp.group9.doctorpatientapp.FirebaseSecurity;

import android.provider.SyncStateContract;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.softwareapp.group9.doctorpatientapp.FirebaseSecurity.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SecureEncrypter {

    private static final String secretKey = "]@q-k`[h$4BX=:##";
    private static final String initVector = "cVwd/lPBi3#zzDyL";

    private SecureEncrypter(){

    }

    public static SecureEncrypter getInstance(){
        return new SecureEncrypter();
    }

    public ArrayList<String> getEncryptedStrings(ArrayList<String> toEncrypt){
        ArrayList<String> secureArrayList = new ArrayList<>();
        for (String unencrypted: toEncrypt) {
            try {
                secureArrayList.add(encryptData(unencrypted));
            } catch(Exception e){
                e.printStackTrace();
            }

        }
        return secureArrayList;
    }

    public ArrayList<String> getDecryptedStrings(ArrayList<String> toDecrypt){
        ArrayList<String> secureArrayList = new ArrayList<>();
        for (String unencrypted: toDecrypt) {
            try {
                secureArrayList.add(decryptData(unencrypted));
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        return secureArrayList;
    }

    public String encryptData(String text){

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            byte[] static_key = secretKey.getBytes();

            SecretKeySpec keySpec = new SecretKeySpec(static_key, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(initVector.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            byte[] results = cipher.doFinal(text.getBytes());

            String result = Base64.encodeToString(results, Base64.NO_WRAP|Base64.DEFAULT);
            return result;
        } catch(Exception e){
            return "";
        }
    }


    public String decryptData(String text){

        try{
            byte[] encryted_bytes = Base64.decode(text, Base64.DEFAULT);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            byte[] static_key = secretKey.getBytes();

            SecretKeySpec keySpec = new SecretKeySpec(static_key, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(initVector.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            byte[] decrypted = cipher.doFinal(encryted_bytes);
            String result = new String(decrypted);

            return result;
        } catch(Exception e){
            return "";
        }
    }
}
