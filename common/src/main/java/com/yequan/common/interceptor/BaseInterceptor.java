package com.yequan.common.interceptor;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Auther: yq
 * @Date: 2019/7/5 09:17
 * @Description:
 */
public class BaseInterceptor {

    protected void renderMsg(HttpServletResponse response, String msg) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            if (null != outputStream)
                outputStream.write(msg.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != outputStream) {
                outputStream.flush();
                outputStream.close();
            }
        }
    }

    protected void renderPage(HttpServletRequest request, HttpServletResponse response, String page) {
        try {
            request.getRequestDispatcher(page).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
