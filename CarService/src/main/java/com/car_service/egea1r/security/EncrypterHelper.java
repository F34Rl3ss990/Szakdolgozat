package com.car_service.egea1r.security;

import lombok.extern.slf4j.Slf4j;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

@Slf4j
public final class EncrypterHelper {
    private static final String KEY = "#sma#key#Ra_3weqZ3owgPT9Y6Bcx";
    private static final String PBE_WITH_MD_5_AND_DES_MODE = "PBEWithMD5AndDES";
    private static Cipher ecipher;
    private static Cipher dcipher;
    private static EncrypterHelper instance = new EncrypterHelper();

    private EncrypterHelper() {
        final byte[] salt = {(byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x34, (byte) 0xE3, (byte) 0x03};
        int iterationCount = 19;
        try {
            final KeySpec keySpec = new PBEKeySpec(KEY.toCharArray(), salt, iterationCount);
            final SecretKey key = SecretKeyFactory.getInstance(PBE_WITH_MD_5_AND_DES_MODE).generateSecret(keySpec);
            ecipher = Cipher.getInstance(PBE_WITH_MD_5_AND_DES_MODE);
            dcipher = Cipher.getInstance(PBE_WITH_MD_5_AND_DES_MODE);

            final AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        } catch (InvalidAlgorithmParameterException e) {
            log.error("EXCEPTION: InvalidAlgorithmParameterException");
        } catch (InvalidKeySpecException e) {
            log.error("EXCEPTION: InvalidKeySpecException");
        } catch (NoSuchPaddingException e) {
            log.error("EXCEPTION: NoSuchPaddingException");
        } catch (NoSuchAlgorithmException e) {
            log.error("EXCEPTION: NoSuchAlgorithmException");
        } catch (InvalidKeyException e) {
            log.error("EXCEPTION: InvalidKeyException");
        }
    }

    private static String encodeHasField(String hashField) {
        return String.format("##%s##", hashField);
    }

    public static String encrypt(String str, String hashField) {
        return instance.encrypt(String.format("%s%s%s", encodeHasField(hashField), str, KEY));
    }

    public static String decrypt(String encodeStr, String hashField) {
        String decrypt = instance.decrypt(encodeStr);
        if (decrypt == null) {
            return null;
        }
        decrypt = decrypt.replace(KEY, "");
        final String result = decrypt.replace(encodeHasField(hashField), "");
        if (result.equals(decrypt)) {
            return null;
        }
        return result;
    }
    public static String encrypt(String str) {
        try {
            final byte[] utf8 = str.getBytes("UTF8");
            final byte[] enc = ecipher.doFinal(utf8);
            return javax.xml.bind.DatatypeConverter.printBase64Binary(enc);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static String decrypt(String str) {
        try {
            final byte[] dec = javax.xml.bind.DatatypeConverter.parseBase64Binary(str);
            final byte[] utf8 = dcipher.doFinal(dec);
            return new String(utf8, "UTF8");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
