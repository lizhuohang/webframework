package com.lzh.framework.base.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 编码与解码操作工具
 * Created by lizhuohang on 17/8/15.
 */
public final class CodecUtil {
    private static final Logger logger = LoggerFactory.getLogger(CodecUtil.class);

    /**
     * 将 URL 编码
     *
     * @param source
     * @return
     */
    public static String endcodeURL(String source) {
        String target;
        try {
            target = URLEncoder.encode(source, "UTF-8");
        } catch (Exception e) {
            logger.error("encode url failure", e);
            throw new RuntimeException(e);
        }
        return target;
    }

    /**
     * 将 URL 解码
     *
     * @param source
     * @return
     */
    public static String decodeURL(String source) {
        String target;
        try {
            target = URLDecoder.decode(source, "UTF-8");
        } catch (Exception e) {
            logger.error("decode url failure", e);
            throw new RuntimeException(e);
        }
        return target;
    }

    /**
     * MD5 加密
     *
     * @param source
     * @return
     */
    public static String md5(String source) {
        return DigestUtils.md5Hex(source);
    }
}
