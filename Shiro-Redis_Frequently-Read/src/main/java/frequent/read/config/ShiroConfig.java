package frequent.read.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import frequent.read.shiro.MyRealm;
import frequent.read.shiro.RedisSessionDAO;
import frequent.read.shiro.ShiroSessionManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
        shiroFilterFactoryBean.setLoginUrl("/");
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
    public SecurityManager securityManager(MyRealm myRealm, RedisCacheManager redisCacheManager) {
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();

        // 设置自定义realm
        securityManager.setRealm(myRealm);
        // 设置redis缓存
        securityManager.setCacheManager(redisCacheManager);
        // 使用redis管理session
        securityManager.setSessionManager(sessionManager());

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
     * Redis缓存管理器
     */
    @Bean
    public RedisCacheManager redisCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        // 设置Redis管理器
        redisCacheManager.setRedisManager(redisManager());
        // 对Principal（User）中的username属性进行缓存
        redisCacheManager.setPrincipalIdFieldName("username");
        // 用户权限过期时间
        redisCacheManager.setExpire(60*30*1000);
        return redisCacheManager;
    }

    /**
     * Redis管理器
     */
    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost("127.0.0.1");
        redisManager.setPort(6379);
        return redisManager;
    }

    /**
     * 提供Redis对session的CRUD操作以及持久化
     */
    @Bean
    public SessionDAO sessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        // 设置Redis实例
        redisSessionDAO.setRedisManager(redisManager());
        // 设置session保存的时间
        redisSessionDAO.setExpire(200000);
        return redisSessionDAO;
    }

    /**
     * 创建sessionId对应的Cookie,保存在浏览器
     */
    @Bean
    public SimpleCookie sessionIdCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("redis-cookie");
        simpleCookie.setPath("/");
        simpleCookie.setHttpOnly(true);
        // 浏览器关闭时失效
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }

    /**
     * 会话管理器，设置会话超时和保存的地方
     */
    @Bean
    public SessionManager sessionManager() {
        // 使用自定义的 sessionManager
        ShiroSessionManager sessionManager = new ShiroSessionManager();

        // 对所有配置整合到session管理器中
        sessionManager.setSessionIdCookie(sessionIdCookie());
        sessionManager.setSessionDAO(sessionDAO());
        sessionManager.setCacheManager(redisCacheManager());

        sessionManager.setGlobalSessionTimeout(1800000);
        // 删除无用的session对象
        sessionManager.setDeleteInvalidSessions(true);
        // 定时对session进行检查
        sessionManager.setSessionValidationSchedulerEnabled(true);
        // 设置扫描时间
        sessionManager.setSessionValidationInterval(10*60*1000);
        // 取消对url重写
        sessionManager.setSessionIdUrlRewritingEnabled(false);

        return sessionManager;
    }
}
