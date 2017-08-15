package com.lzh.framework.base.bean;

/**
 * 返回数据对象
 * Created by lizhuohang on 17/8/15.
 */
public class Data {
    /**
     * 模型数据
     */
    private Object model;

    public Data(Object model) {
        this.model = model;
    }

    public Object getModel() {
        return model;
    }
}
