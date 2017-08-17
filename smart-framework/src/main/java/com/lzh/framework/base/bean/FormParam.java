package com.lzh.framework.base.bean;

/**
 * Created by lizhuohang on 17/8/17.
 */
public class FormParam {

    private String fieldName;
    private Object fieldValue;

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public FormParam(String fieldName, Object fieldValue) {

        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
