package com.yequan.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.yequan.common.util.Logger;

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

    /**
     * 渲染信息,接收信息字符串
     *
     * @param response
     * @param msg
     * @throws IOException
     */
    void renderMsg(HttpServletResponse response, String msg) {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            if (null != outputStream)
                outputStream.write(msg.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            Logger.error(e.getMessage(), e);
        } finally {
            if (null != outputStream) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    Logger.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 渲染信息,接收信息对象
     *
     * @param response
     * @param msgObj
     * @throws IOException
     */
    void renderMsg(HttpServletResponse response, Object msgObj) {
        String msg = JSON.toJSONString(msgObj);
        renderMsg(response, msg);
    }

    /**
     * 渲染页面
     *
     * @param request
     * @param response
     * @param page
     */
    void renderPage(HttpServletRequest request, HttpServletResponse response, String page) {
        try {
            request.getRequestDispatcher(page).forward(request, response);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
    }

}
