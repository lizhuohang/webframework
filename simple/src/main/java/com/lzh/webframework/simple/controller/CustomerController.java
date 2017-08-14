package com.lzh.webframework.simple.controller;

import com.lzh.webframework.simple.model.Customer;
import com.lzh.webframework.simple.service.CustomerService;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by lizhuohang on 17/8/11.
 */
@WebServlet("/customer/*")
public class CustomerController extends HttpServlet {

    private CustomerService customerService;

    @Override
    public void init() throws ServletException {
        customerService = new CustomerService();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // TODO
        String path = req.getPathInfo();
        if (StringUtils.isBlank(path)) {
            // 对应path : /customer   客户列表
            gotoList(req, resp);
        } else if (path.equals("search") && req.getMethod().equals("POST")) {
            // 对应path : /customer/search    查询客户

        } else if (path.equals("show")) {
            // 对应path : /customer/show   进入 查看客户 界面
        } else if (path.equals("create")) {
            // 对应path : /customer/create
            if (req.getMethod().equals("GET")) {
                // 进入 创建客户 页面
            } else if (req.getMethod().equals("POST")) {
                // 创建客户
            }
        } else if (path.equals("edit")) {
            // 对应path : /customer/edit
            if (req.getMethod().equals("GET")) {
                // 进入 编辑客户 界面
            } else if (req.getMethod().equals("POST")) {
                // 编辑客户
            }
        } else if (path.equals("delete") && req.getMethod().equals("DELETE")) {
            // 对应path : /customer/delete    删除客户
        }
    }

    private void gotoList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Customer> list = customerService.getCustomerList();
        req.setAttribute("customerList", list);
        req.getRequestDispatcher("/WEB-INF/views/customer.jsp").forward(req, resp);
    }

}
