package retry.limit.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import retry.limit.dao.UserMapper;
import retry.limit.model.User;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: Kam-Chou
 * @date: 2020/6/29 13:06
 * @description: Shiro密码比对类，由于不需要加密，所以使用SimpleCredentialsMatcher
 * @version: 1.0
 */
public class RetryLimitCredentialsMatcher extends SimpleCredentialsMatcher {

    @Autowired
    private UserMapper userMapper;

    private Cache<String, AtomicInteger> retryCache;

    public RetryLimitCredentialsMatcher(CacheManager cache) {
        retryCache = cache.getCache("passwordRetryCache");
    }

    /**
     * 密码匹配
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        // 获取登录用户信息
        User user = (User) token.getPrincipal();
        // 从缓冲获取重试次数
        AtomicInteger retryTime = retryCache.get(user.getName());
        // 如果没有重试过，则放入缓存
        if (retryTime == null) {
            retryTime = new AtomicInteger(0);
        }
        // 如果重试次数大于5次，则锁定用户
        if (retryTime.incrementAndGet() > 5) {
            // 如果用户的状态是正常，则修改为不正常.
            if ("0".equals(user.getState())) {
                User temp = new User();
                temp.setState("1");
                temp.setUid(user.getUid());

                userMapper.update(temp);
            }
            System.out.println("锁定用户");
            throw new LockedAccountException();
        }
        boolean isMatch = super.doCredentialsMatch(token, info);
        // 如果成功登陆，则从缓存中移除此用户的重试次数.
        if (isMatch) {
            retryCache.remove(user.getName());
        }
        return isMatch;
    }

    /**
     * 用户的解锁操作
     */
    public void unlockAccount(String username) {
        User user = userMapper.findByUserName(username);
        if (user != null) {
            User temp = new User();
            temp.setUid(user.getUid());
            temp.setState("0");
            // 数据持久化
            userMapper.update(user);
            // 从缓存中移除此
            retryCache.remove(username);
        }
    }
}
