package com.alan.clients.protocol.hyt.germmod.util;

import com.alan.clients.util.interfaces.InstanceAccess;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.compress.utils.IOUtils;

import javax.crypto.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Enumeration;
import java.util.Scanner;

public class UuidUtil implements InstanceAccess {
    private static String uuid;

    private static Key desPassword(String s) {
        try {
            KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
            SecureRandom securerandom = SecureRandom.getInstance("SHA1PRNG");
            securerandom.setSeed(s.getBytes());
            keygenerator.init(securerandom);
            return keygenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] desEncode(String string, byte[] pw) {
        try {
            Key key = desPassword(string);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(1, key);
            return cipher.doFinal(pw);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String base32Encode(String s, String s1)
    {
        return (new Base32()).encodeAsString(desEncode(s, s1.getBytes())).replaceAll("=", "");
    }

    private static String base64Encode(String pw) {
        return org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(desEncode("1qaz2wsx3edc4ds6g4f4g65a7ujm8ik,9ol.0p;/", pw.getBytes()));
    }

    private static String getArray(String string) {
        return base32Encode("!QAZ@WSX#EDC$RFV%TGB^YHN&UJM*IK<(OL>)P:?", base64Encode(string));
    }

    private static ArrayList<String> getHwid() {
        ArrayList<String> arrayList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            while (enumeration.hasMoreElements()) {
                for (InterfaceAddress interfaceAddress : enumeration.nextElement().getInterfaceAddresses()) {
                    byte[] byArray;
                    NetworkInterface networkInterface;
                    InetAddress inetAddress = interfaceAddress.getAddress();
                    if (inetAddress.isLinkLocalAddress() || (networkInterface = NetworkInterface.getByInetAddress(inetAddress)) == null || (byArray = networkInterface.getHardwareAddress()) == null)
                        continue;
                    stringBuilder.delete(0, stringBuilder.length());
                    for (int i = 0; i < byArray.length; ++i) {
                        stringBuilder.append(String.format("%02X%s", byArray[i], i < byArray.length - 1 ? "-" : ""));
                    }
                    if (arrayList.contains(stringBuilder.toString())) continue;
                    arrayList.add(stringBuilder.toString());
                }
            }
            return arrayList;
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    private static String runCmd(String command) {
        String code = null;
        Scanner scanner;
        try {
            Process process = Runtime.getRuntime().exec(command);
            process.getOutputStream().close();
            scanner = new Scanner(process.getInputStream());
            int line = 0;
            while (scanner.hasNextLine()) {
                String text = scanner.nextLine();
                ++line;
                if (line != 2) continue;
                code = text.trim();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        IOUtils.closeQuietly(scanner);
        scanner.close();
        return code;
    }

    private static String getCpuId() {
        return runCmd("wmic cpu get ProcessorId");
    }

    private static String getSerialNumber() {
        return runCmd("wmic path win32_physicalmedia get serialnumber");
    }

    private static char[] connectChars(char[][] ac) {
        int i = 0;
        int j = ac.length;
        for(int k = 0; k < j; k++) {
            char[] ac2 = ac[k];
            i += ac2.length;
        }

        final char[] finish = new char[i];
        j = 0;
        for (char[] ac3 : ac) {
            System.arraycopy(ac3, 0, finish, j, ac3.length);
            j += ac3.length;
        }

        return finish;
    }

    private static char[] getBaseUuid() {
        char[] ac = connectChars(new char[][]{getArray(getHwid().toString()).toCharArray(), getArray(getCpuId()).toCharArray(), getArray(getSerialNumber()).toCharArray()});
        for(int i = 0; i < ac.length; i++)
            ac[i] = Character.toUpperCase(ac[i]);

        return ac;
    }

    public static String getUuid() {
        if (uuid == null)
            uuid = Base64.getEncoder().encodeToString((new String(getBaseUuid())).replace("\r\n", "").getBytes(StandardCharsets.UTF_8));
        return uuid;
    }
}
