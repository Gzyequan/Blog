package com.yequan.shiro.realm;

import com.yequan.common.application.response.AppResult;
import com.yequan.pojo.entity.SysPermissionDO;
import com.yequan.pojo.entity.SysRoleDO;
import com.yequan.pojo.entity.SysUserDO;
import com.yequan.user.service.IAdminPermissionService;
import com.yequan.user.service.IAdminRoleService;
import com.yequan.user.service.IOrdinaryUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Auther: yq
 * @Date: 2019/10/12 14:18
 * @Description:
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private IOrdinaryUserService ordinaryUserService;

    @Autowired
    private IAdminRoleService adminRoleService;

    @Autowired
    private IAdminPermissionService adminPermissionService;

    @Override
    public String getName() {
        return "userRealm";
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    /**
     * 授权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String phone = (String) principalCollection.getPrimaryPrincipal();
        SysUserDO sysUser = ordinaryUserService.selectByMobilephone(phone);
        Integer userId = sysUser.getId();
        List<SysRoleDO> sysRoleList = adminRoleService.getRoleByUserId(userId);

//        List<SysPermissionDO> sysPermissionList = adminPermissionService.getSysPermissionByRoleId(sysRole.getId());

        return null;
    }

    /**
     * 认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        return null;
    }
}
