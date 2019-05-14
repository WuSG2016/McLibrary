package com.wsg.mclibrary.common.config;


import android.text.TextUtils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.Iterator;

/**
 * xml 格式
 * <?xml version="1.0" encoding="UTF-8"?>
 * <serials>
 * <serial id="001">
 * <baudrate>9600</baudrate>
 * <port>dev/ttyO3</port>
 * </serial>
 * </serials>
 * @author WuSG
 */
public class ParseSerialFile {
    public static String id;
    public static String port;
    public static String baudrate;

    /**
     * 解析XML
     *
     * @param path     文件路径
     * @param fileName 文件名
     * @return 是否加载成功
     */
    public static boolean parseXml(String path, String fileName) {
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(fileName)) {
            return false;
        }
        File file = new File(path, fileName);
        if (file.exists()) {
            SAXReader reader = new SAXReader();
            Document document;
            try {
                document = reader.read(file);
                Element root = document.getRootElement();
                Iterator it = root.elementIterator();
                while (it.hasNext()) {
                    Element element = (Element) it.next();
                    id = element.attributeValue("id");
                    port = element.elementText("port");
                    baudrate = element.elementText("baudrate");
                }
                return true;
            } catch (DocumentException e) {
                e.printStackTrace();
            }

        }
        return false;
    }

}
