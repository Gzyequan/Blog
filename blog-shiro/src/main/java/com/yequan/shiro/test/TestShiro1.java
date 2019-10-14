package com.yequan.shiro.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

/**
 * @Auther: yq
 * @Date: 2019/10/12 15:09
 * @Description:
 */
public class TestShiro1 {

    public static void main(String[] args) {
        //构建SecurityManager工厂,IniSecurityManagerFactory可以从ini文件中初始化SecurityManager环境
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-cryptography.ini");
        //通过factory创建securityManager
        SecurityManager securityManager = factory.getInstance();
        //将securityManager设置到运行环境中
        SecurityUtils.setSecurityManager(securityManager);
        //创建Subject实例,该实例认证需要上边创建的securityManager进行
        Subject subject = SecurityUtils.getSubject();
        //创建token令牌,记录用户认证的身份和凭证,即用户名和密码
        UsernamePasswordToken token = new UsernamePasswordToken("yequan", "1234");
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        //用户认证状态
        boolean authenticated = subject.isAuthenticated();
        System.out.println("用户认证状态: " + authenticated);

        //用户退出
        subject.logout();
        authenticated = subject.isAuthenticated();
        System.out.println("用户认证状态: " + authenticated);

    }

}
