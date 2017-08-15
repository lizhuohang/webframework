package com.lzh.framework.base.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回视图对象
 * Created by lizhuohang on 17/8/15.
 */
public class View {
    /**
     * 视图路径
     */
    private String path;

    /**
     * 模型数据
     */
    private Map<String, Object> model;

    public String getPath() {
        return path;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public View(String path) {
        this.path = path;
        this.model = new HashMap<>();
    }

    public View addModel(String key, Object value) {
        model.put(key, value);
        return this;
    }
}
