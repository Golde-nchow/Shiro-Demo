package frequent.read;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "frequent.read.dao")
public class ShiroRedisFrequentlyReadApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShiroRedisFrequentlyReadApplication.class, args);
	}

}
