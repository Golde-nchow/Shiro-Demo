package complete.logout;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "complete.logout.dao")
public class CompleteLogoutApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompleteLogoutApplication.class, args);
	}

}
