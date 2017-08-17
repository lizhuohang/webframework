package com.lzh.framework.base;

import com.lzh.framework.base.bean.Data;
import com.lzh.framework.base.bean.Handler;
import com.lzh.framework.base.bean.Param;
import com.lzh.framework.base.bean.View;
import com.lzh.framework.base.helper.BeanHelper;
import com.lzh.framework.base.helper.ConfigHelper;
import com.lzh.framework.base.helper.ControllerHelper;
import com.lzh.framework.base.helper.RequestHelper;
import com.lzh.framework.base.helper.ResponseHelper;
import com.lzh.framework.base.helper.UploadHelper;
import com.lzh.framework.base.utils.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 请求转发器
 * Created by lizhuohang on 17/8/15.
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        // 初始化相关 Helper 类
        HelperLoader.init();
        // 获取 ServletContext 对象 （用于注册Servlet）
        ServletContext servletContext = config.getServletContext();
        // 注册处理 JSP 的 servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");
        // 注册处理静态资源的默认 Servlet
        ServletRegistration staticServlet = servletContext.getServletRegistration("default");
        staticServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");
        // 上传文件插件初始化
        UploadHelper.init(servletContext);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取请求方法与路径
        String requestMethod = req.getMethod().toLowerCase();
        String requestPath = req.getPathInfo();

        if (requestPath.equals("/favicon.ico")) {
            return;
        }

        // 获取 Action 处理
        Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
        if (handler != null) {
            // 获取 Controller 类及其 Bean 实例
            Class<?> controllerClass = handler.getControllerClass();
            Object controllerBean = BeanHelper.getBean(controllerClass);

            // 创建请求参数对象
            Param param;
            if (UploadHelper.isMultipart(req)) {
                param = UploadHelper.createParam(req);
            } else {
                param = RequestHelper.createParam(req);
            }
            // 调用 Action 方法
            Method actionMethod = handler.getActionMethod();
            Object result;
            if (actionMethod.getParameterCount() != 0) {
                result = ReflectionUtil.invokeMethod(controllerBean, actionMethod, param);
            } else {
                result = ReflectionUtil.invokeMethod(controllerBean, actionMethod);
            }
            if (result instanceof View) {
                View view = (View) result;
                ResponseHelper.handleViewResult(view, req, resp);
            } else if (result instanceof Data) {
                Data data = (Data) result;
                ResponseHelper.handleDataResult(data, resp);
            }
        }
    }
}
