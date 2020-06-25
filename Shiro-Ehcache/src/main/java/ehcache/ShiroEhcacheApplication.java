package ehcache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "ehcache.dao")
public class ShiroEhcacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShiroEhcacheApplication.class, args);
	}

}
