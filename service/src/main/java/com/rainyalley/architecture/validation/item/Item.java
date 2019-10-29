package com.rainyalley.architecture.validation.item;

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { ItemValidator.class })
public @interface Item {

    String message() default "{owl.validation.constraints.Item.message}";

    Class<ItemLoader> loader() default ItemLoader.class;

    String[] items() default {};
}
