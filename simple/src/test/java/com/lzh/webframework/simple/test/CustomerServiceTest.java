package com.lzh.webframework.simple.test;

import com.lzh.webframework.simple.helper.DataBaseHelper;
import com.lzh.webframework.simple.model.Customer;
import com.lzh.webframework.simple.service.CustomerService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhuohang on 17/8/14.
 */
public class CustomerServiceTest {
    private final CustomerService customerService;

    public CustomerServiceTest() {
        customerService = new CustomerService();
    }

    @Before
    public void init() throws IOException {
        // 初始化数据库
        String file = "sql/customer_init.sql";
        DataBaseHelper.executeSqlFile(file);
    }

    @Test
    public void getCustomerListTest() throws Exception {
        List<Customer> customerList = customerService.getCustomerList();
        Assert.assertEquals(2, customerList.size());
    }

    @Test
    public void getCustomerTest() throws Exception {
        long id = 1;
        Customer customer = customerService.getCustomer(id);
        Assert.assertNotNull(customer);
    }

    @Test
    public void createCustomerTest() throws Exception {
        Map<String, Object> fieldMap = new HashMap<>();
        fieldMap.put("name", "test");
        fieldMap.put("contact", "test1");
        fieldMap.put("telephone", "18611111111");
        Assert.assertTrue(customerService.createCustomer(fieldMap));
    }

    @Test
    public void updateCustomerTest() throws Exception {
        long id = 1;
        Map<String, Object> fileMap = new HashMap<>();
        fileMap.put("contact", "test2");
        Assert.assertTrue(customerService.updateCustomer(id, fileMap));
    }

    @Test
    public void deleteCustomerTest() throws Exception {
        long id = 1;
        Assert.assertTrue(customerService.deleteCustomer(id));
    }
}
