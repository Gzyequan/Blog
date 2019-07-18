package com.yequan.common.shiro.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;

/**
 * @Auther: yq
 * @Date: 2019/7/15 10:50
 * @Description:
 */
public class TestShiro {

    public static void main(String[] args) {
        //1.通过工厂加载配置
        IniSecurityManagerFactory factory =
                new IniSecurityManagerFactory("E:\\IntellijIdeaWorkPlace\\webWorkplace\\CommonWeb\\web\\src\\main\\resources\\shiro.ini");
        //通过工厂获取安全管理器
        SecurityManager securityManager = factory.getInstance();
        //注册subject对象
        SecurityUtils.setSecurityManager(securityManager);
        //获取subject对象
        Subject subject = SecurityUtils.getSubject();
        //获取验证对象
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "hello");
        //验证
        try {
            subject.login(token);
            System.out.println("验证通过..." + token.getPrincipal());
        } catch (UnknownAccountException e) {
            System.out.println("验证未通过,用户不存在");
            e.printStackTrace();
        } catch (IncorrectCredentialsException e) {
            System.out.println("验证未通过,密码错误");
            e.printStackTrace();
        } catch (LockedAccountException e) {
            System.out.println("验证未通过,用户被锁定");
            e.printStackTrace();
        }

    }

}
