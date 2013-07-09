package com.wordpress.roadtonerdvana.base64;

import java.util.HashMap;

/*
 * This is a homebrew base64 decoder/encoder, 
 * I did this for fun, use it at your own risk.
 * (I highly recommend using the one from apache)
 */
/**
 *
 * @author JSedano
 */
public class JcBase64 {

    private static final String BASE64REGEX = "[A-Za-z0-9+/]+[=]{0,2}";
    private static final char PADDINGCHAR = '=';
    private static final char[] ENCODEMAP = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
    };
    private static final HashMap DECODEMAP = new HashMap<Character, Integer>() {
        {
            put('A', 0);
            put('B', 1);
            put('C', 2);
            put('D', 3);
            put('E', 4);
            put('F', 5);
            put('G', 6);
            put('H', 7);
            put('I', 8);
            put('J', 9);
            put('K', 10);
            put('L', 11);
            put('M', 12);
            put('N', 13);
            put('O', 14);
            put('P', 15);
            put('Q', 16);
            put('R', 17);
            put('S', 18);
            put('T', 19);
            put('U', 20);
            put('V', 21);
            put('W', 22);
            put('X', 23);
            put('Y', 24);
            put('Z', 25);
            put('a', 26);
            put('b', 27);
            put('c', 28);
            put('d', 29);
            put('e', 30);
            put('f', 31);
            put('g', 32);
            put('h', 33);
            put('i', 34);
            put('j', 35);
            put('k', 36);
            put('l', 37);
            put('m', 38);
            put('n', 39);
            put('o', 40);
            put('p', 41);
            put('q', 42);
            put('r', 43);
            put('s', 44);
            put('t', 45);
            put('u', 46);
            put('v', 47);
            put('w', 48);
            put('x', 49);
            put('y', 50);
            put('z', 51);
            put('0', 52);
            put('1', 53);
            put('2', 54);
            put('3', 55);
            put('4', 56);
            put('5', 57);
            put('6', 58);
            put('7', 59);
            put('8', 60);
            put('9', 61);
            put('+', 62);
            put('/', 63);
        }
    };

    private int unsignedByte(int b) {
        return b & 255;
    }

    private String encodeThree(byte b1, byte b2, byte b3) {
        int byte1 = unsignedByte(b1);
        int byte2 = unsignedByte(b2);
        int byte3 = unsignedByte(b3);
        int int1 = (byte1 >> 2) & 0x3F;
        int int2 = (((byte1 << 6) | ((byte2 >> 2))) >> 2) & 0x3F;
        int int3 = (((byte2 << 2 | byte3 >> 6) & 0x3F));
        int int4 = ((byte3 & 0x3F));
        return ""+ENCODEMAP[int1] + ENCODEMAP[int2] + ENCODEMAP[int3] + ENCODEMAP[int4];
    }

    public byte[] decode(String encodedString) {
        if (encodedString == null || encodedString.length() == 0) {
            throw new IllegalArgumentException("String is null or empty");
        }
        if (!encodedString.matches(BASE64REGEX)) {
            throw new IllegalArgumentException("String does not match " + BASE64REGEX);
        }
        while (encodedString.charAt(encodedString.length() - 1) == PADDINGCHAR) {
            encodedString = encodedString.substring(0, encodedString.length() - 1);
        }
        byte decodedArray[] = null;
        switch (encodedString.length() % 4) {
            case 0:
                decodedArray = new byte[encodedString.length() / 4 * 3];
                break;
            case 2:
                decodedArray = new byte[((encodedString.length() + 2) / 4 * 3) - 2];
                break;
            case 3:
                decodedArray = new byte[((encodedString.length() + 1) / 4 * 3) - 1];
                break;
            default:
                throw new IllegalArgumentException("String of wrong lenght: " + encodedString.length());
        }
        int j = 0;
        int lastDigits = 0;
        for (int i=0; i < encodedString.length(); i += 4) {
            lastDigits = encodedString.length() - i;
            if (lastDigits > 3) {
                int char1 = (int) DECODEMAP.get(encodedString.charAt(i));
                int char2 = (int) DECODEMAP.get(encodedString.charAt(i + 1));
                int char3 = (int) DECODEMAP.get(encodedString.charAt(i + 2));
                int char4 = (int) DECODEMAP.get(encodedString.charAt(i + 3));
                decodedArray[j++] = (byte) unsignedByte(char1 << 2 | char2 >> 4);
                decodedArray[j++] = (byte) unsignedByte(char2 << 4 | char3 >> 2);
                decodedArray[j++] = (byte) unsignedByte(char3 << 6 | char4);
            }
        }
        switch (lastDigits) {
            case 2:
                int tail1Char1 = (int) DECODEMAP.get(encodedString.charAt(encodedString.length() - 2));
                int tail1Char2 = (int) DECODEMAP.get(encodedString.charAt(encodedString.length() - 1));
                decodedArray[j] = (byte) unsignedByte(tail1Char1 << 2 | tail1Char2 >> 4);
                break;
            case 3:
                int tail2char1 = (int) DECODEMAP.get(encodedString.charAt(encodedString.length() - 3));
                int tail2char2 = (int) DECODEMAP.get(encodedString.charAt(encodedString.length() - 2));
                int tail2char3 = (int) DECODEMAP.get(encodedString.charAt(encodedString.length() - 1));
                decodedArray[j++] = (byte) unsignedByte(tail2char1 << 2 | tail2char2 >> 4);
                decodedArray[j] = (byte) unsignedByte(tail2char2 << 4 | tail2char3 >> 2);
                break;
        }
        return decodedArray;
    }

    public String encode(byte[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("byte array is null or empty");
        }
        int lastDigits = 0;
        String encodedString = "";
        for (int i=0; i < arr.length; i += 3) {
            lastDigits = arr.length - i;
            if (lastDigits > 2) {
                encodedString += encodeThree(arr[i], arr[i + 1], arr[i + 2]);
            }
        }
        switch (lastDigits) {
            case 1:
                String tail1 = encodeThree(arr[arr.length - 1], (byte) (0), (byte) (0));
                encodedString += tail1.substring(0, 2) + PADDINGCHAR + PADDINGCHAR;
                break;
            case 2:
                String tail2 = encodeThree(arr[arr.length - 2], arr[arr.length - 1], (byte) (0));
                encodedString += tail2.substring(0, 3) + PADDINGCHAR;
                break;
        }
        return encodedString;
    }
}
