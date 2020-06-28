package person.control.filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.apache.shiro.web.util.WebUtils;
import person.control.model.User;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

/**
 * @author: Kam-Chou
 * @date: 2020/6/27 10:28
 * @description: session控制过滤器
 * @version: 1.0
 */
public class SessionControlFilter extends AccessControlFilter {

    /**
     * 踢出地址
     */
    private String kickOutUrl;

    /**
     * 默认踢出之前的登录的用户，true表示踢出之后登录的
     */
    private boolean kickOutAfter = false;

    /**
     * 同一个账号的最大会话数
     */
    private int maxSession = 1;

    private SessionManager sessionManager;

    private Cache<String, Deque<Serializable>> cache;

    /**
     * 是否允许访问，这里是false，也就是说会执行下面访问拒绝的操作
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    /**
     * 访问拒绝时，是否自己处理，false表示自己处理，不执行后面拦截器; true表示继续执行拦截链，自己不处理
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);

        // 如果还未登录，或者不是 “记住我”，那么就执行登录
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            return true;
        }

        Session session = subject.getSession();
        // 因为 Principal 传的是 User，所以可以强转为 User
        String username = ((User)subject.getPrincipal()).getUsername();
        Serializable sessionId = session.getId();

        Deque<Serializable> deque = cache.get(username);
        if (deque == null) {
            deque = new LinkedList<>();
            cache.put(username, deque);
        }

        // 如果没有该数据则放入缓存
        if (!deque.contains(sessionId) && session.getAttribute("kickOut") == null) {
            deque.push(sessionId);
        }

        // 如果队列中的数量大于最大会话数，则开始踢人
        while (deque.size() > maxSession) {

            // 被踢出的人
            Serializable kickedOutSessionId;

            // 是否踢出后登录的人, 这里的逻辑是有些不一样
            // 理解为【是获取后登陆的人的sessionId，还是前登录人的sessionId】
            if (kickOutAfter) {
                kickedOutSessionId = deque.removeFirst();
            } else {
                // 踢出之前登录的人
                kickedOutSessionId = deque.removeLast();
            }

            Session kickedOutSession = sessionManager.getSession(new WebSessionKey(kickedOutSessionId, request, response));
            if (kickedOutSession != null) {
                // 指定的 session 设置 kickOut 属性，并不是当前的session
                // 设置踢出标识
                kickedOutSession.setAttribute("kickOut", true);
            }

        }

        // 如果某个会话意识到自己的 session 有 kickOut属性，则直接注销当前会话.
        if (session.getAttribute("kickOut") != null) {
            subject.logout();
            // 重定向到踢出的url
            WebUtils.issueRedirect(request, response, kickOutUrl);
            return false;
        }

        return true;
    }

    /////////////////////////////////////// setter 和 getter /////////////////////////////////

    public void setKickOutUrl(String kickOutUrl) {
        this.kickOutUrl = kickOutUrl;
    }

    public void setKickOutAfter(boolean kickOutAfter) {
        this.kickOutAfter = kickOutAfter;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void setCache(CacheManager manager) {
        this.cache = manager.getCache("shiro-activeSessionCache");
    }
}
