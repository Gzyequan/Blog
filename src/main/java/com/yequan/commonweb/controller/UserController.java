package com.yequan.commonweb.controller;

import com.alibaba.fastjson.JSON;
import com.yequan.commonweb.pojo.User;
import com.yequan.commonweb.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: yq
 * @Date: 2019/6/28 14:07
 * @Description:
 */
@Controller
@RequestMapping("/user/")
public class UserController extends BaseController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping("{id}")
    @ResponseBody
    public void findUserById(HttpServletResponse response,@PathVariable("id") Integer id) {
        User user = iUserService.selectUserById(id);
        this.referText(response, JSON.toJSONString(user));
    }

}
