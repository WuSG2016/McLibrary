package com.wsg.mclib;


import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 工具类
 *
 * @author WuSG
 *         Create at 2018/12/5 9:35
 **/
public class ByteUtils {


    /**
     * 高低位反转
     *
     * @param data
     * @return
     */
    public static int reversal(int data) {
//        return ((a & 0xf) << 4 | ((a & 0xf0) >> 4));
        data = ((data << 4) | (data >> 4));
        data = (((data << 2) & 0xcc) | ((data >> 2) & 0x33));
        data = (((data << 1) & 0xaa) | ((data >> 1) & 0x55));
        return data;
    }

    /**
     * V3 校验和
     *
     * @param shorts
     * @return
     */
    public static int getCrc(byte[] shorts) {
        int i, j;
        int crc = 0x00;
        short current;
        for (i = 0; i < shorts.length; i++) {
            current = (short) (shorts[i] << 8);
            for (j = 0; j < 8; j++) {
                if ((short) (crc ^ current) < 0) {
                    crc = (short) ((crc << 1) ^ 0x1021);
                } else {
                    crc <<= 1;
                }
                current <<= 1;
            }
        }
        return crc & 0xfffff;
    }

    /**
     * long 转byte
     *
     * @param num
     * @return
     */
    public static byte[] longBytes(long num) {
        byte[] byteNum = new byte[8];
        for (int ix = 0; ix < 8; ++ix) {
            int offset = 64 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }

    /**
     * byte 转long
     *
     * @param byteNum
     * @return
     */
    public static long bytesLong(byte[] byteNum) {
        long num = 0;
        for (int ix = 0; ix < 8; ++ix) {
            num <<= 8;
            num |= (byteNum[ix] & 0xff);
        }
        return num;
    }

    /**
     * byte数组转成String
     *
     * @param buffer
     * @return
     */
    public static String byte2hex(byte[] buffer) {
        String h = "";
        for (int i = 0; i < buffer.length; i++) {
            String temp = Integer.toHexString(buffer[i] & 0xFF);
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            h = h + " " + temp;
        }
        return h.toUpperCase();

    }

    /**
     * List转成Byte数组
     *
     * @param list
     * @return
     */
    public static byte[] listTobyte(List<Byte> list) {
        Object[] boxedArray = list.toArray();
        int len = boxedArray.length;
        byte[] array = new byte[len];
        for (int i = 0; i < len; i++) {
            Object object = boxedArray[i];
            if (object == null) {
                throw new NullPointerException();
            }
            array[i] = ((Number) object).byteValue();
        }
        return array;
    }

    /**
     * int 类型数据转两个byte数组
     *
     * @param v
     * @return
     */
    public static byte[] makeByte2(int v) {
        byte[] bytes = {(byte) ((v & 0xff00) >> 8), (byte) (v & 0xff)};
        return bytes;
    }

    /**
     * int 类型数据转两个byte数组
     *
     * @param v
     * @return
     */
    public static byte[] makeByte2(int v, boolean var) {
        byte[] bytes = {(byte) (((v & 0xff00) >> 8) + 1), (byte) (v & 0xff)};
        return bytes;
    }

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。  和bytesToInt2（）配套使用
     */
    public static byte[] intToBytes2(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value >> 24) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * 冰山校验和
     *
     * @param msg
     * @return
     */
    public static byte[] blockFCC(final byte[] msg) {
        char i;
        int crc = msg[1];
        for (i = 2; i < msg.length; i++) {
            crc += (msg[i] & 0xff);
        }
        return makeByte2(crc);
    }


    /**
     * int 转成 一个byte数据
     *
     * @param v
     * @return
     */

    public static byte makeByte1(int v) {
        return (byte) (v & 0xff);
    }

    public static int makeUint(byte v) {

        return (0xff & v);
    }

    /**
     * 合并两个数组
     *
     * @param first
     * @param second
     * @return
     */
    public static byte[] concat(byte[] first, byte[] second) {
        byte[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    /**
     * @param b
     * @return
     */
    public static int makeUint8(byte b) {
        return (0xff & b);
    }

    /**
     * @param b
     * @param c
     * @return
     */
    public static int makeUint16(byte b, byte c) {
        return (0xff & b) << 8 | (0xff & c);
    }

    /**
     * @param b
     * @param c
     * @param d
     * @param e
     * @return
     */
    public static int makeUint32(byte b, byte c, byte d, byte e) {
        return (0xff & b) << 24 | (0xff & c) << 16 | (0xff & d) << 8 | (0xff & e);
    }

    /**
     * short 转成 byte[]
     *
     * @param s
     * @return
     */
    public static byte[] short2byte(short s) {
        byte[] b = new byte[2];
        for (int i = 0; i < 2; i++) {
            //因为byte占4个字节，所以要计算偏移量
            int offset = 16 - (i + 1) * 8;
            //把16位分为2个8位进行分别存储
            b[i] = (byte) ((s >> offset) & 0xff);
        }
        return b;
    }

    /**
     * 16进制的字符串表示转成字节数组
     *
     * @param hexString 16进制格式的字符串
     * @return 转换后的字节数组
     **/
    public static byte[] toByteArray(String hexString) {
//        if (!TextUtils.isEmpty(hexString)){
//            throw new IllegalArgumentException("this hexString must not be empty");
//        }
        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() / 2];
        int k = 0;
        for (int i = 0; i < byteArray.length; i++) {
            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
            byteArray[i] = (byte) (high << 4 | low);
            k += 2;
        }
        return byteArray;
    }



    /**
     * 2个字节返回一个int
     *
     * @param b
     * @return
     */
    public static int makeUint16(byte[] b) {
        return (0xff & b[0]) << 8 | (0xff & b[1]);
    }


    /**
     * 校验和
     *
     * @param bytes
     * @return
     */
    public static boolean isCRC(byte[] bytes) {
        byte[] tempByte = new byte[bytes.length - 2];
        byte[] crcByte = new byte[2];
        System.arraycopy(bytes, bytes.length - 2, crcByte, 0, crcByte.length);
        System.arraycopy(bytes, 0, tempByte, 0, bytes.length - 2);
        return ByteUtils.byte2hex(crcByte).equals(ByteUtils.byte2hex(ByteUtils.short2byte((short) ByteUtils.getCrc(tempByte))));
    }

    /**
     * 通过byte数组取到short
     *
     * @param b
     * @param index 第几位开始取
     * @return
     */
    public static short getShort(byte[] b, int index) {
        return (short) (((b[index + 1] << 8) | b[index + 0] & 0xff));
    }

    /**
     * 十六进制高低位转换，返回十进制数
     *
     * @param str
     * @return
     */
    public static String decodeHexStringToDec(String str) {
        str = HighLowHex(spaceHex(str));
        String value = new BigInteger(str, 16).toString();
        return value;
    }

    /**
     * 十六进制数隔空位
     *
     * @param str
     * @return
     */
    private static String spaceHex(String str) {
        char[] array = str.toCharArray();
        if (str.length() <= 2) return str;
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            int start = i + 1;
            if (start % 2 == 0) {
                buffer.append(array[i]).append(" ");
            } else {
                buffer.append(array[i]);
            }
        }
        return buffer.toString();
    }

    /**
     * 高位16进制转低位
     *
     * @param str
     * @return
     */
    private static String HighLowHex(String str) {
        if (str.trim().length() <= 2) return str;
        List<String> list = Arrays.asList(str.split(" "));
        Collections.reverse(list);
        StringBuffer stringBuffer = new StringBuffer();
        for (String string : list) {
            stringBuffer.append(string);
        }
        return stringBuffer.toString();
    }


    public static void main(String[] args) {

        byte[] bytes = new byte[]{
                0x02, 0x00, 0x6c, (byte) 0xF5, (byte) 0x71, 0x01,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
        };
//        System.out.print(byte2hex(reData.getData()));
//stringToBytes("02 00 05 E7 77 01 01 64");
        System.out.print(byte2hex(blockFCC(bytes)));


    }
}
