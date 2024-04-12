package com.avinash.danumalk;

import com.avinash.danumalk.auth.AuthenticationService;
import com.avinash.danumalk.auth.RegisterRequest;
import com.avinash.danumalk.reactions.ReactionType;
import com.avinash.danumalk.reactions.ReactionTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

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
			AuthenticationService service,
			ReactionTypeRepository reactionTypeRepository
	) {
		return args -> {
			var admin = RegisterRequest.builder()
					.usersName("DanumaLK")
					.email("DanumaLK")
					.password("password")
					.role(ADMIN)
					.build();
			System.out.println("Admin token: " + service.register(admin).getAccessToken());

			// Initialize ReactionType data
			initializeReactionTypes(reactionTypeRepository);

		};
	}

	private void initializeReactionTypes(ReactionTypeRepository reactionTypeRepository) {
		// Check if reaction types exist, and if not, initialize them
		if (reactionTypeRepository.count() == 0) {
			// Initialize reaction types (add more as needed)
			ReactionType likeType = new ReactionType("like", "LIKE");
			ReactionType loveType = new ReactionType("love","LOVE");


			// Save the reaction types to the database
			reactionTypeRepository.saveAll(List.of(likeType, loveType));

			System.out.println("Initialized ReactionTypes: " + reactionTypeRepository.count());
		} else {
			System.out.println("ReactionTypes already initialized.");
		}

}
}

