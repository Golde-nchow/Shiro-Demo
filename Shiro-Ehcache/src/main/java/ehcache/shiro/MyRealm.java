package ehcache.shiro;

import ehcache.dao.PermissionMapper;
import ehcache.dao.RoleMapper;
import ehcache.dao.UserMapper;
import ehcache.model.Permission;
import ehcache.model.Role;
import ehcache.model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

import static org.apache.shiro.web.filter.mgt.DefaultFilter.user;

/**
 * @author: Kam-Chou
 * @date: 2020/6/20 18:16
 * @description: 自定义访问逻辑
 * @version: 1.0
 */
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * 获取授权信息（角色和权限的添加）
     * @param principals 存储的是
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        // 获取用户
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        // 从数据库获取用户所在的角色
        Set<Role> roles =this.roleMapper.findRolesByUserId(user.getUid());
        // 添加角色
        SimpleAuthorizationInfo authorizationInfo =  new SimpleAuthorizationInfo();
        // 进行角色的添加
        for (Role role : roles) {
            authorizationInfo.addRole(role.getRole());
        }

        // 从数据库查询用户拥有的权限
        Set<Permission> permissions = permissionMapper.findPermissionsByRoleId(roles);
        // 设置权限
        for (Permission permission:permissions) {
            authorizationInfo.addStringPermission(permission.getPermission());
        }

        return authorizationInfo;
    }

    /**
     * 获取身份验证信息（第一次登陆的时候使用）
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        // 获取用户名和密码（第一种方式）
        //String username = (String) authenticationToken.getPrincipal();
        //String password = new String((char[]) authenticationToken.getCredentials());

        // 获取用户名和密码（第二种方式）
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = usernamePasswordToken.getUsername();
        String password = new String(usernamePasswordToken.getPassword());

        // 从数据库查询用户信息
        User user = this.userMapper.findByUserName(username);

        // 进行校验操作，或者使用CredentialsMatcher
        if (user == null) {
            throw new UnknownAccountException("用户名或密码错误！");
        }
        if (!password.equals(user.getPassword())) {
            throw new IncorrectCredentialsException("用户名或密码错误！");
        }
        if ("1".equals(user.getState())) {
            throw new LockedAccountException("账号已被锁定,请联系管理员！");
        }

        // principal, credentials, realmName
        return new SimpleAuthenticationInfo(user, user.getPassword(), user.getName());
    }

    ////////////////////////////// 缓存 /////////////////////////////

    /**
     * 清除所有的授权信息缓存
     */
    public void clearCurrentAuthenticationInfo() {
        // 删除当前的用户授权信息缓存
        getAuthorizationCache().remove(SecurityUtils.getSubject().getPrincipals());
    }
}
