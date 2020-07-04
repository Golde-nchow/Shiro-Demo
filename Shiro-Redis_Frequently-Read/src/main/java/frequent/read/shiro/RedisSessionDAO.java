package frequent.read.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.crazycake.shiro.RedisManager;
import org.springframework.util.SerializationUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: Kam-Chou
 * @date: 2020/7/1 23:42
 * @description: Redis针对Session持久化操作类
 * @version: 1.0
 */
public class RedisSessionDAO extends AbstractSessionDAO {

    private static final String SESSION_PREFIX = "shiro:session";

    private String keyPrefix = SESSION_PREFIX;

    private static final int DEFAULT_EXPIRE = 1800;
    private int expire = DEFAULT_EXPIRE;

    private RedisManager redisManager;

    @Override
    protected Serializable doCreate(Session session) {
        if (session == null) {
            throw new UnknownSessionException("session是null");
        }
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        saveSession(session);

        return sessionId;
    }

    /**
     * 保存session
     */
    private void saveSession(Session session) throws UnknownSessionException {
        if (session == null || session.getId() == null) {
            throw new UnknownSessionException("session 是 null");
        }

        String redisSessionKey = getRedisSessionKey(session.getId());
        byte[] sessionBytes = SerializationUtils.serialize(session);

        redisManager.set(redisSessionKey.getBytes(), sessionBytes, DEFAULT_EXPIRE);
    }

    /**
     * 获取Redis的key
     */
    private String getRedisSessionKey(Serializable sessionId) {
        return keyPrefix + sessionId;
    }

    /**
     * 读取session
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            return null;
        }

        System.out.println("=========================");
        System.out.println("读取Redis");

        String preKey = getRedisSessionKey(sessionId);
        return (Session) SerializationUtils.deserialize(redisManager.get(preKey.getBytes()));
    }

    /**
     * 更新session
     */
    @Override
    public void update(Session session) throws UnknownSessionException {
        // 如果session需要验证，代表已经过期
        if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) {
            return;
        }
        saveSession(session);
    }

    @Override
    public void delete(Session session) {
        if (session == null || session.getId() == null) {
            throw new UnknownSessionException("session 是 null");
        }
        redisManager.del(getRedisSessionKey(session.getId()).getBytes());
    }

    /**
     * 获取活跃的session数量
     */
    @Override
    public Collection<Session> getActiveSessions() {
        Set<Session> sessionSet = new HashSet<>();
        Set<byte[]> keys = redisManager.keys((keyPrefix + "*").getBytes());
        if (keys != null && keys.size() > 0) {
            for (byte[] key : keys) {
                Session session = (Session) SerializationUtils.deserialize(redisManager.get(key));
                sessionSet.add(session);
            }
        }

        return sessionSet;
    }

    public void setRedisManager(RedisManager redisManager) {
        this.redisManager = redisManager;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public int getExpire() {
        return expire;
    }

}
