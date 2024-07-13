package com.avinash.danumalk;

import com.avinash.danumalk.auth.AuthenticationService;
import com.avinash.danumalk.auth.RegisterRequest;

import com.avinash.danumalk.role.Role;
import com.avinash.danumalk.role.Permission;
import com.avinash.danumalk.role.RoleName;
import com.avinash.danumalk.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Set;





@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableScheduling
@EnableAsync
@SpringBootApplication
public class DanumaLkApplication {


    public static void main(String[] args) {
		SpringApplication.run(DanumaLkApplication.class, args);
	}
	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service,
			RoleRepository roleRepository

	) {
		return args -> {
			initializeRoles(roleRepository);


			var admin = RegisterRequest.builder()
					.usersName("DanumaLK")
					.email("DanumaLK")
					.password("password")
					.build();

			System.out.println("Admin token: " + service.registerAdmin(admin).getAccessToken());



		};
	}

	private void initializeRoles(RoleRepository roleRepository) {
		// Check if roles exist, and if not, initialize them
		if (roleRepository.findByName(RoleName.USER.name()).isEmpty()) {
			Role userRole = Role.builder()
					.name(RoleName.USER.name())
					.permissions(Set.of(
							Permission.USER_READ, Permission.USER_UPDATE, Permission.USER_DELETE, Permission.USER_CREATE,
							Permission.POST_READ, Permission.POST_CREATE, Permission.POST_UPDATE, Permission.POST_DELETE,
							Permission.COMMENT_READ, Permission.COMMENT_CREATE, Permission.COMMENT_UPDATE, Permission.COMMENT_DELETE,
							Permission.REACTION_READ, Permission.REACTION_CREATE, Permission.REACTION_UPDATE, Permission.REACTION_DELETE
					))
					.build();
			roleRepository.save(userRole);
		}

		if (roleRepository.findByName(RoleName.ADMIN.name()).isEmpty()) {
			Role adminRole = Role.builder()
					.name(RoleName.ADMIN.name())
					.permissions(Set.of(
							Permission.ADMIN_READ, Permission.ADMIN_UPDATE, Permission.ADMIN_DELETE, Permission.ADMIN_CREATE,
							Permission.USER_READ, Permission.USER_UPDATE, Permission.USER_DELETE, Permission.USER_CREATE,
							Permission.POST_READ, Permission.POST_CREATE, Permission.POST_UPDATE, Permission.POST_DELETE,
							Permission.COMMENT_READ, Permission.COMMENT_CREATE, Permission.COMMENT_UPDATE, Permission.COMMENT_DELETE,
							Permission.REACTION_READ, Permission.REACTION_CREATE, Permission.REACTION_UPDATE, Permission.REACTION_DELETE
					))
					.build();
			roleRepository.save(adminRole);
		}
	}

}


