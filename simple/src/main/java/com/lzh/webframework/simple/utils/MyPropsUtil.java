package com.lzh.webframework.simple.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by lizhuohang on 17/8/14.
 */
public class MyPropsUtil {
    private static final Logger logger = LoggerFactory.getLogger(MyPropsUtil.class);

    private static Properties properties = null;

    static {
        loadProps("config.properties");
    }

    /**
     * 加载配置文件
     *
     * @param fileName
     * @return
     */
    public static boolean loadProps(String fileName) {
        InputStream is = null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (is == null) {
                throw new FileNotFoundException(fileName + " file is not found");
            }
            properties = new Properties();
            properties.load(is);
        } catch (IOException e) {
            logger.error("load properties file failure", e);
            return false;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error("close input stream failure", e);
                }
            }
        }
        return true;
    }

    public static String getString(String key) {
        return getString(key, StringUtils.EMPTY);
    }

    public static String getString(String key, String defaultValue) {
        String value = defaultValue;
        if (properties != null && properties.containsKey(key)) {
            value = properties.getProperty(key);
        }
        return value;
    }
}
