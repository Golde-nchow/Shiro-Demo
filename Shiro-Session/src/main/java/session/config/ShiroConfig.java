package session.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import session.shiro.MyRealm;

import java.util.LinkedHashMap;

/**
 * @author: Kam-Chou
 * @date: 2020/6/20 17:35
 * @description: Shiro配置类
 * @version: 1.0
 */
@Configuration
public class ShiroConfig {

    /**
     * Shiro资源过滤器
     * Shiro 可控制 Web请求必须经过 Shiro 主过滤器的拦截
     * @param securityManager 安全事务管理器
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 设置登录地址，非页面名
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 设置登录后跳转的页面
        shiroFilterFactoryBean.setSuccessUrl("/index");
        // 设置无权限页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");

        // 配置访问权限, 使用 LinkedHashMap 保证顺序
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/", "anon");
        //其他资源都需要认证  authc 表示需要认证才能进行访问
        filterChainDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 安全管理器
     * @param myRealm 连接Shiro和数据的桥梁，当Shiro执行操作的时候，都会去Realm获取数据，进行鉴权
     */
    @Bean
    public SecurityManager securityManager(MyRealm myRealm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();

        // 设置自定义realm
        securityManager.setRealm(myRealm);
        // 设置自定义Session管理
        securityManager.setSessionManager(sessionManager);

        return securityManager;
    }

    /**
     * 自定义授权和鉴权
     */
    @Bean
    public MyRealm shiroRealm() {
        return new MyRealm();
    }

    /**
     * 当我们想要在thymeleaf中使用注解，则必须有该配置.
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

    /**
     * 开启注解模式，例如: @RequirePermissions，@RequireRoles
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /**
     * 开启自动创建代理类
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * 配置sessionId生成策略
     */
    @Bean
    public SessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    /**
     * 配置 SessionDAO，用于对session的操作进行持久化.
     * MemorySessionDAO：基于内存会话维护.
     * EnterpriseCacheSessionDAO：提供了缓存功能的会话，MapCache来实现.
     */
    @Bean
    public SessionDAO sessionDAO(SessionIdGenerator sessionIdGenerator) {
        MemorySessionDAO memorySessionDAO = new MemorySessionDAO();
        memorySessionDAO.setSessionIdGenerator(sessionIdGenerator);
        return memorySessionDAO;
    }

    /**
     * 配置Cookie，与其他Cookie要分开
     */
    @Bean
    public SimpleCookie simpleCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("my-session-cookie");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        return simpleCookie;
    }

    /**
     * 配置会话管理器
     */
    @Bean
    public SessionManager sessionManager(SimpleCookie simpleCookie, SessionDAO sessionDAO) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionIdCookie(simpleCookie);
        sessionManager.setSessionDAO(sessionDAO);

        // 过期配置，默认30分钟
        sessionManager.setGlobalSessionTimeout(600000);
        // 定时检测过期session
        sessionManager.setSessionIdUrlRewritingEnabled(true);
        // 每次检测的时间
        sessionManager.setSessionValidationInterval(60000);
        // 取消URL重写
        sessionManager.setSessionIdUrlRewritingEnabled(false);

        return sessionManager;
    }
}
