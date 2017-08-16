package com.lzh.framework.web.controller;

import com.lzh.framework.base.annotation.Action;
import com.lzh.framework.base.annotation.Controller;
import com.lzh.framework.base.annotation.Inject;
import com.lzh.framework.base.bean.Data;
import com.lzh.framework.base.bean.Param;
import com.lzh.framework.base.bean.View;
import com.lzh.framework.web.service.TestService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lizhuohang on 17/8/15.
 */
@Controller
public class TestController {

    @Inject
    private TestService testService;

    @Action("GET:/index")
    public Data index(Param param) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 123);
        map.put("name", "lzh");
//        map.put("name", testService.getName());
        Data target = new Data(map);

        testService.testTransaction();
        return target;
    }

    @Action("get:/indexPage")
    public View indexPage(Param param) {
        View view = new View("hello.jsp");
        view.addModel("current", "2107-01-01");
        return view;
    }
}
