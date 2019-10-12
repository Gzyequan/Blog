package com.yequan.common.filter;

import com.yequan.common.util.Logger;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Auther: yq
 * @Date: 2019/7/9 08:35
 * @Description:
 */
public class BaseFilter {

    /**
     * 将内容输出
     *
     * @param servletResponse
     * @param content
     */
    public void write(ServletResponse servletResponse, String content) {
        write(servletResponse, content.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 将内容输出
     *
     * @param servletResponse
     * @param content
     */
    public void write(ServletResponse servletResponse, byte[] content) {
        ServletOutputStream out = null;
        try {
            out = servletResponse.getOutputStream();
            out.write(content);
            out.flush();
        } catch (IOException e) {
            Logger.error(e.getMessage(), e);
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    Logger.error(e.getMessage(), e);
                }
            }
        }
    }

}
