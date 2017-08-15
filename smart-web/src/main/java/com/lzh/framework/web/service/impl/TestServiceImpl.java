package com.lzh.framework.web.service.impl;

import com.lzh.framework.base.annotation.Service;
import com.lzh.framework.web.service.TestService;

/**
 * Created by lizhuohang on 17/8/15.
 */
@Service
public class TestServiceImpl implements TestService {
    @Override
    public String getName() {
        return "Smart web";
    }
}
