package com.yequan.commonweb.controller;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class BaseController {

    public BaseController() {
        super();
    }


    public void referText(HttpServletResponse response, String jsonStr) {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter write = null;
        try {
            write = response.getWriter();
            response.getWriter().print(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (write != null) {
                write.close();
            }
        }
    }

}
