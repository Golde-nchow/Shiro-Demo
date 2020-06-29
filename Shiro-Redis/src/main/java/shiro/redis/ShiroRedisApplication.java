package shiro.redis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "shiro.redis.dao")
public class ShiroRedisApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShiroRedisApplication.class, args);
	}

}
