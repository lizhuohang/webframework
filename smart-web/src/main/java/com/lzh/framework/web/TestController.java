package com.lzh.framework.web;

import com.lzh.framework.base.annotation.Action;
import com.lzh.framework.base.annotation.Controller;
import com.lzh.framework.base.bean.Data;
import com.lzh.framework.base.bean.Param;
import com.lzh.framework.base.bean.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lizhuohang on 17/8/15.
 */
@Controller
public class TestController {
    @Action("GET:/index")
    public Data index(Param param) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 123);
        map.put("name", "lzh");
        Data target = new Data(map);
        return target;
    }

    @Action("get:/indexPage")
    public View indexPage(Param param) {
        View view = new View("hello.jsp");
        view.addModel("current", "2107-01-01");
        return view;
    }
}