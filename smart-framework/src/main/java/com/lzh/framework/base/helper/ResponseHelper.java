package com.lzh.framework.base.helper;

import com.lzh.framework.base.bean.Data;
import com.lzh.framework.base.bean.View;
import com.lzh.framework.base.utils.JsonUtil;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by lizhuohang on 17/8/17.
 */
public final class ResponseHelper {
    public static void handleViewResult(View view, HttpServletRequest request, HttpServletResponse response) throws
            IOException, ServletException {
        String path = view.getPath();
        if (StringUtils.isNotEmpty(path)) {
            if (path.startsWith("/")) {
                response.sendRedirect(request.getContextPath() + path);
            } else {
                Map<String, Object> model = view.getModel();
                for (String key : model.keySet()) {
                    request.setAttribute(key, model.get(key));
                }
                path = ConfigHelper.getAppJspPath() + path;
                request.getRequestDispatcher(path).forward(request, response);
            }
        }
    }

    public static void handleDataResult(Data data, HttpServletResponse response) throws IOException {
        Object model = data.getModel();
        if (model != null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            String json = JsonUtil.toJson(model);
            writer.write(json);
            writer.flush();
            writer.close();
        }
    }
}
