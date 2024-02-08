package cn.hackedmc.apotheosis.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;

@SuppressWarnings("All")
public class CryptUtil {
    public static class Base64Crypt {
        public static String decrypt(String message) {
            return new String(Base64.getDecoder().decode(message));
        }
        public static String encrypt(String message) {
            return Base64.getEncoder().encodeToString(message.getBytes(StandardCharsets.UTF_8));
        }
    }

    public static class DES {
        public static byte[] encrypt(String message, String secretKey, byte[] iv) {
            try {
                Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                KeySpec keySpec = new DESKeySpec(secretKey.getBytes());
                SecretKey key = keyFactory.generateSecret(keySpec);
                IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
                cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
                byte[] encryptedBytes = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
                return Base64.getEncoder().encode(encryptedBytes);
            } catch (Exception ignored) {}

            return new byte[]{0};
        }

        public static String decrypt(byte[] encryptedMessage, String secretKey, byte[] iv) {
            try {
                byte[] decodedMessage = Base64.getDecoder().decode(encryptedMessage);

                Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                KeySpec keySpec = new DESKeySpec(secretKey.getBytes());
                SecretKey key = keyFactory.generateSecret(keySpec);
                IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
                cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
                byte[] decryptedBytes = cipher.doFinal(decodedMessage);
                return new String(decryptedBytes);
            } catch (Exception ignored) {}

            return "";
        }
    }
}
