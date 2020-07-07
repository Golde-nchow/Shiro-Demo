package shiro.redis.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author: Kam-Chou
 * @date: 2020/7/5 16:45
 * @description: 自定义登出逻辑Filter
 * @version: 1.0
 */
public class ShiroLogoutFilter extends LogoutFilter {

    /**
     * 在登出前执行的操作
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        // 获取当前用户
         Subject subject = getSubject(request, response);
        // 第一种登出方式，简单.
        // subject.logout();

        // 安全管理器
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        MyRealm myRealm = (MyRealm) securityManager.getRealms().iterator().next();
        myRealm.clearCurrentAuthorizationInfo();
        myRealm.clearCurrentAuthenticationInfo();

        subject.logout();

        String redirectUrl = getRedirectUrl(request,response,subject);
        issueRedirect(request, response, redirectUrl);

        // 表示不执行后面的拦截器
        return false;
    }
}
