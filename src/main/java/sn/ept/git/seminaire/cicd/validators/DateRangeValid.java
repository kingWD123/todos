package sn.ept.git.seminaire.cicd.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateRangeValidator.class)
@Documented
public @interface DateRangeValid {
    String message() default "La date de debut doit etre anterieure a la date de fin";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
