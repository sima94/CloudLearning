package com.cloudlearning.cloud.configuration.validation.annotations;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.reflect.Field;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;


@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = { FieldsEqualityValidator.class })
public @interface FieldsEquality {

    String message() default "{FieldsEquality.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Name of the first field that will be compared.
     *
     * @return name
     */
    String firstFieldName();

    /**
     * Name of the second field that will be compared.
     *
     * @return name
     */
    String secondFieldName();

    @Target({ TYPE, ANNOTATION_TYPE })
    @Retention(RUNTIME)
    public @interface List {
        FieldsEquality[] value();
    }
}

class FieldsEqualityValidator implements ConstraintValidator<FieldsEquality, Object> {

    private static final Logger log = LoggerFactory.getLogger(FieldsEqualityValidator.class);

    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(FieldsEquality constraintAnnotation) {
        firstFieldName = constraintAnnotation.firstFieldName();
        secondFieldName = constraintAnnotation.secondFieldName();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null)
            return true;

        try {
            Class<?> clazz = value.getClass();

            Field firstField = ReflectionUtils.findField(clazz, firstFieldName);
            firstField.setAccessible(true);
            Object first = firstField.get(value);

            Field secondField = ReflectionUtils.findField(clazz, secondFieldName);
            secondField.setAccessible(true);
            Object second = secondField.get(value);

            if (first != null && second != null && !first.equals(second)) {
                ConstraintValidatorContext.ConstraintViolationBuilder cvb = context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate());
                //cvb.addPropertyNode(firstFieldName).addConstraintViolation();

                cvb.addPropertyNode(secondFieldName).addConstraintViolation();

                return false;
            }
        } catch (Exception e) {
            log.error("Cannot validate fields equality in '" + value + "'!", e);
            return false;
        }

        return true;
    }
}