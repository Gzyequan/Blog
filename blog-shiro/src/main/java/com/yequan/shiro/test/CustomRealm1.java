package com.yequan.shiro.test;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @Auther: yq
 * @Date: 2019/10/12 15:47
 * @Description:
 */
public class CustomRealm1 extends AuthorizingRealm {

    @Override
    public String getName() {
        return "CustomRealm1";
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //从token中获取用户身份信息
        String username = (String) authenticationToken.getPrincipal();
        if (!username.equals("yequan")) {
            return null;
        }

        //获取用户密码
        String password = "123";
        //返回认证信息
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username, password, getName());
        return info;
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }
}
