package com.avinash.danumalk.posts.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AtLeastOneImageUrlValidator.class) // Reference to the validator
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface AtLeastOneImageUrl {
    String message() default "Invalid image name or names";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}