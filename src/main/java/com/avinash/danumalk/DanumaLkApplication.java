package com.avinash.danumalk;

import com.avinash.danumalk.Reactions.ReactionType.ReactionType;
import com.avinash.danumalk.Reactions.ReactionType.ReactionTypeRepository;
import com.avinash.danumalk.auth.AuthenticationService;
import com.avinash.danumalk.auth.RegisterRequest;

import com.avinash.danumalk.posts.PostCategory;
import com.avinash.danumalk.posts.PostCategoryRepository;
import com.avinash.danumalk.posts.PostType;
import com.avinash.danumalk.posts.PostTypeRepository;
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
import java.util.UUID;


@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableScheduling
@EnableAsync
@SpringBootApplication(scanBasePackages = "com.avinash.danumalk")
public class DanumaLkApplication {


    public static void main(String[] args) {
		SpringApplication.run(DanumaLkApplication.class, args);
	}
//	@Bean
//	public CommandLineRunner commandLineRunner(
//			AuthenticationService service,
//			RoleRepository roleRepository,
//			PostTypeRepository postTypeRepository,
//			ReactionTypeRepository reactionTypeRepository
//
//
//	) {
//		return args -> {
//			initializeRoles(roleRepository);
//			initializePostTypes(postTypeRepository);
//			initializeReactionTypes(reactionTypeRepository);
//
//
//			var admin = RegisterRequest.builder()
//					.usersName("DanumaLK")
//					.email("DanumaLK")
//					.password("password")
//					.build();
//
//			System.out.println("Admin token: " + service.registerAdmin(admin).getAccessToken());
//
//
//
//		};
//	}
// 	@Bean
//	public CommandLineRunner commandLineRunner(
//
//			PostCategoryRepository  postCategoryRepository
//	) {
//		return args -> {
//			initializePostCategory(postCategoryRepository);
//
//
//
//		};
//	}

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

	private void initializePostTypes(PostTypeRepository postTypeRepository) {
		if (postTypeRepository.findAll().isEmpty()) {
			PostType imagePostType = new PostType(UUID.randomUUID(), "ImagePost", "IMAGE_POST");
			PostType textPostType = new PostType(UUID.randomUUID(), "TextPost", "TEXT_POST");
			postTypeRepository.save(imagePostType);
			System.out.println(imagePostType);
			postTypeRepository.save(textPostType);
			System.out.println(textPostType);
		}
	}

//	private void  initializePostCategory(PostCategoryRepository  postCategoryRepository) {
//		if (postCategoryRepository.findAll().isEmpty()) {
//			PostCategory postCategory1 = new PostCategory(UUID.randomUUID(), "General", "Gen");
//			postCategoryRepository.save(postCategory1);
//
//			PostCategory postCategory2 = new PostCategory(UUID.randomUUID(), "Education", "Edu");
//			postCategoryRepository.save(postCategory2);
//
//
//
//		}
//	}

	private void initializeReactionTypes(ReactionTypeRepository reactionTypeRepository) {
		createReactionTypeIfNotFound(reactionTypeRepository, "Like", "LIKE");
		createReactionTypeIfNotFound(reactionTypeRepository, "Dislike", "DISLIKE");
	}

	private void createReactionTypeIfNotFound(ReactionTypeRepository repository, String name, String key) {
		if (repository.findByKey(key).isEmpty()) {
			ReactionType reactionType = new ReactionType();
			reactionType.setName(name);
			reactionType.setKey(key);
			repository.save(reactionType);
		}
	}

}


