package com.avinash.danumalk;

import com.avinash.danumalk.auth.AuthenticationService;
import com.avinash.danumalk.auth.RegisterRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import static com.avinash.danumalk.user.Role.ADMIN;


@SpringBootApplication(scanBasePackages = "com.avinash.danumalk")
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableScheduling
public class DanumaLkApplication {

	public static void main(String[] args) {
		SpringApplication.run(DanumaLkApplication.class, args);
	}
	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service
	) {
		return args -> {
			var admin = RegisterRequest.builder()
					.usersName("DanumaLK")
					.email("DanumaLK")
					.password("password")
					.role(ADMIN)
					.build();
			System.out.println("Admin token: " + service.register(admin).getAccessToken());

		};
	}
}

