package com.lzh.framework.base.helper;

import com.lzh.framework.base.bean.FormParam;
import com.lzh.framework.base.bean.Param;
import com.lzh.framework.base.utils.CodecUtil;
import com.lzh.framework.base.utils.StreamUtil;
import com.lzh.framework.base.utils.StringUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by lizhuohang on 17/8/17.
 */
public final class RequestHelper {

    /**
     * 创建请求对象
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static Param createParam(HttpServletRequest request) throws IOException {
        List<FormParam> formParamList = new ArrayList<>();
        formParamList.addAll(parseParameterNames(request));
        formParamList.addAll(parserInputStream(request));
        return new Param(formParamList);
    }

    private static List<FormParam> parseParameterNames(HttpServletRequest request) {
        List<FormParam> formParamList = new ArrayList<>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String fieldName = paramNames.nextElement();
            String[] fieldValues = request.getParameterValues(fieldName);
            if (ArrayUtils.isNotEmpty(fieldValues)) {
                Object fieldValue;
                if (fieldValues.length == 1) {
                    fieldValue = fieldValues[0];
                } else {
                    StringBuilder sb = new StringBuilder("");
                    for (int i = 0; i < fieldValues.length; i++) {
                        sb.append(fieldValues[i]);
                        if (i != fieldValues.length - 1) {
                            sb.append(StringUtil.SEPARATOR);
                        }
                    }
                    fieldValue = sb.toString();
                }
                formParamList.add(new FormParam(fieldName, fieldValue));
            }
        }
        return formParamList;
    }

    private static List<FormParam> parserInputStream(HttpServletRequest request) throws IOException {
        List<FormParam> formParamList = new ArrayList<>();
        String body = CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
        if (StringUtils.isNotEmpty(body)) {
            String[] kvs = StringUtil.splitString(body, "&");
            if (ArrayUtils.isNotEmpty(kvs)) {
                for (String kv : kvs) {
                    String[] array = StringUtil.splitString(kv, "=");
                    if (ArrayUtils.isNotEmpty(array) && array.length == 2) {
                        String fieldName = array[0];
                        String fieldValue = array[1];
                        formParamList.add(new FormParam(fieldName, fieldValue));
                    }
                }
            }
        }
        return formParamList;
    }
}
