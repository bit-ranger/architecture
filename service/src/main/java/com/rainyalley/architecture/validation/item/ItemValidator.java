package com.rainyalley.architecture.validation.item;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

/**
 * @author bin.zhang
 */
public class ItemValidator implements ConstraintValidator<Item, Object> {

    private Item annotation;

    @Override
    public void initialize(Item constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if(value == null){
            return true;
        }
        if(annotation.items().length > 0){
            return Arrays.stream(annotation.items()).anyMatch(item -> item.equals(String.valueOf(value)));
        } else {

        }
        return false;
    }
}
