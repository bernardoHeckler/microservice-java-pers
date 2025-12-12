package br.edu.atitus.von_core_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableFeignClients
@EnableCaching
public class ProductServiceApplication {

	public static void main(String[] args) {
		Dotenv.configure()
			.directory("./") 
			.ignoreIfMalformed()
			.ignoreIfMissing()
			.systemProperties()
			.load();

		SpringApplication.run(ProductServiceApplication.class, args);
	}

}