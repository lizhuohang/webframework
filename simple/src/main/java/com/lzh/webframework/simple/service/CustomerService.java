package com.lzh.webframework.simple.service;

import com.lzh.webframework.simple.helper.DataBaseHelper;
import com.lzh.webframework.simple.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by lizhuohang on 17/8/11.
 */
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);


    public List<Customer> getCustomerList() {
        String sql = "select * from tb_customer";
        return DataBaseHelper.queryEntityList(Customer.class, sql);
    }

    public Customer getCustomer(long id) {
        String sql = "select * from tb_customer where id = ? ";
        return DataBaseHelper.queryEntity(Customer.class, sql, id);
    }

    public boolean createCustomer(Map<String, Object> fieldMap) {
        return DataBaseHelper.insertEntity(Customer.class, fieldMap);
    }

    public boolean updateCustomer(long id, Map<String, Object> fieldMap) {
        return DataBaseHelper.updateEntity(Customer.class, id, fieldMap);
    }

    public boolean deleteCustomer(long id) {
        return DataBaseHelper.deleteEntity(Customer.class, id);
    }
}
