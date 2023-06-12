package ru.practicum.core.validation.DateBefore;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateBeforeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateBefore {
    String message() default "Date must be late";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int hours() default 0;

    int days() default 0;
}