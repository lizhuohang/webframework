package com.lzh.framework.web.service.impl;

import com.lzh.framework.base.annotation.Service;
import com.lzh.framework.base.annotation.Transaction;
import com.lzh.framework.base.helper.DataBaseHelper;
import com.lzh.framework.web.model.Customer;
import com.lzh.framework.web.service.TestService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lizhuohang on 17/8/15.
 */
@Service
public class TestServiceImpl implements TestService {
    @Override
    public String getName() {
        return "Smart web";
    }

    @Override
    @Transaction
    public void testTransaction() {
        Map<String, Object> fieldMap = new HashMap<>();
        fieldMap.put("name", "test");
        fieldMap.put("contact", "test1");
        fieldMap.put("telephone", "18611111111");
        DataBaseHelper.insertEntity(Customer.class, fieldMap);
    }
}
