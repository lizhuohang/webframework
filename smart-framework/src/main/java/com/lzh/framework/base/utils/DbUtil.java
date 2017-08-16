package com.lzh.framework.base.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lizhuohang on 17/8/14.
 */
public class DbUtil {
    public static String getTableName(Class<?> entityClass) {
        String className = "tb" + entityClass.getSimpleName();

        Matcher matcher = Pattern.compile("[A-Z]").matcher(className);
        StringBuilder builder = new StringBuilder(className);
        for (int i = 0; matcher.find(); i++) {
            builder.replace(matcher.start() + i, matcher.end() + i, "_" + matcher.group().toLowerCase());
        }
        if (builder.charAt(0) == '_') {
            builder.deleteCharAt(0);
        }
        return builder.toString();

    }
}
