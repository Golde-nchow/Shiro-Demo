package frequent.read.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author: Kam-Chou
 * @date: 2020/7/4 23:59
 * @description: Redis配置
 * @version: 1.0
 */
@Configuration
public class RedisConfig {

    @Value("spring.redis.host")
    private String host;

    @Value("spring.redis.port")
    private String port;

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大空闲数
        jedisPoolConfig.setMaxIdle(300);
        // 连接池的最大数据库连接数
        jedisPoolConfig.setMaxTotal(1000);
        // 最大建立连接等待时间
        jedisPoolConfig.setMaxWaitMillis(1000);
        // 逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
        jedisPoolConfig.setMinEvictableIdleTimeMillis(300000);
        // 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
        jedisPoolConfig.setNumTestsPerEvictionRun(10);
        // 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(30000);
        // 是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个
        jedisPoolConfig.setTestOnBorrow(true);
        // 在空闲时检查有效性, 默认false
        jedisPoolConfig.setTestWhileIdle(true);

        return jedisPoolConfig;
    }


}
