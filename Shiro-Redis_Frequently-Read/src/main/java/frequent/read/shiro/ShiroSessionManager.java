package frequent.read.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.ServletRequest;
import java.io.Serializable;

/**
 * @author: Kam-Chou
 * @date: 2020/7/4 17:35
 * @description: ShiroSession管理器
 * @version: 1.0
 */
public class ShiroSessionManager extends DefaultWebSessionManager {

    /**
     * Shiro获取session逻辑
     * 使用 DefaultWebSessionManager 获取 ServletRequest，然后缓存到 Request
     *
     * 在单次的请求周期内，都可以从 Request 中获取该 session.
     */
    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        Serializable sessionId = getSessionId(sessionKey);

        ServletRequest request = null;
        if (sessionKey instanceof WebSessionKey) {
            request = ((WebSessionKey) sessionKey).getServletRequest();
        }

        if (null != request && null != sessionId) {
            Object session = request.getAttribute(sessionId.toString());
            if (session != null) {
                return (Session) session;
            }
        }

        // 如果 request 获取不到，那么才执行shiro的逻辑去获取session
        // 然后放到 request 缓存，等待下次获取
        Session session = super.retrieveSession(sessionKey);
        if (null != request && null != sessionId) {
            request.setAttribute(sessionId.toString(), session);
        }

        return session;
    }
}
