package cn.hackedmc.apotheosis.util.vantage;

import cn.hackedmc.apotheosis.util.CryptUtil;
import com.profesorfalken.wmi4java.WMI4Java;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Map;

public class HWIDUtil {
    public static String lastHWID = "";

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length == 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    private static String getSplitString(String str, String split, int length) {
        int len = str.length();
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < len; i++) {
            if (i % length == 0 && i > 0) {
                temp.append(split);
            }
            temp.append(str.charAt(i));
        }
        String[] attrs = temp.toString().split(split);
        StringBuilder finalMachineCode = new StringBuilder();
        for (String attr : attrs) {
            if (attr.length() == length) {
                finalMachineCode.append(attr).append(split);
            }
        }
        return finalMachineCode.substring(0,
                finalMachineCode.toString().length() - 1);
    }

    private static String md5Encoder(String str) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return getSplitString(new BigInteger(1, md.digest()).toString(16), "-", 5).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        lastHWID = CryptUtil.Base64Crypt.encrypt(str);
        return str;
    }

    public static String getUUID() {
        String name = System.getProperty("os.name").toLowerCase();

        try {
            if (name.contains("windows")) {
                StringBuilder raw = new StringBuilder();
                String main = System.getenv("PROCESS_IDENTIFIER") + System.getenv("COMPUTERNAME");
                byte[] bytes = main.getBytes(StandardCharsets.UTF_8);
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                byte[] md5 = messageDigest.digest(bytes);
                int i = 0;

                for(byte b : md5) {
                    raw.append(Integer.toHexString((b & 0xFF) | 0x300),0,3);
                    if(i != md5.length -1) {
                        raw.append("");
                    }
                    i++;
                }

                StringBuilder result = new StringBuilder((raw).substring(raw.length() - 20, raw.length()).toUpperCase());

                int index;

                for(index = 5; index < result.length(); index+=6) {
                    result.insert(index, '-');
                }
                return md5Encoder(result.toString());
            } else if (name.contains("mac")) {
                Enumeration<NetworkInterface> el = NetworkInterface.getNetworkInterfaces();
                while (el.hasMoreElements()) {
                    byte[] mac = el.nextElement().getHardwareAddress();
                    if (mac == null)
                        continue;

                    String hexStr = bytesToHexString(mac);
                    if (hexStr == null) return null;
                    return md5Encoder(getSplitString(hexStr, "-", 2).toUpperCase());
                }
                return null;
            } else if (name.contains("linux")) {
                String result = "";
                Process process = Runtime.getRuntime().exec("sudo dmidecode -s system-uuid");
                InputStream in;
                BufferedReader br;
                in = process.getInputStream();
                br = new BufferedReader(new InputStreamReader(in));
                while (in.read() != -1) {
                    result = br.readLine();
                }
                br.close();
                in.close();
                process.destroy();
                return md5Encoder(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}