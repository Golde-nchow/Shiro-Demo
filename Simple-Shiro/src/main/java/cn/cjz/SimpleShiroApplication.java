package cn.cjz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "cn.cjz.dao")
public class SimpleShiroApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleShiroApplication.class, args);
	}

}
