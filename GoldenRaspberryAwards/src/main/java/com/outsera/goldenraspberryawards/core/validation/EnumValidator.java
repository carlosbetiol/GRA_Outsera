package com.outsera.goldenraspberryawards.core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class EnumValidator implements ConstraintValidator<EnumValidation, Object> {
    private Class<? extends Enum<?>> enumClass;
    private String valuesValid;

    @Override
    public void initialize(EnumValidation constraintAnnotation) {
        enumClass = constraintAnnotation.enumClass();
        valuesValid = constraintAnnotation.valuesValid();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (value instanceof List<?> valueList) {
            return valueList.isEmpty() || valueList.stream().allMatch(this::isEnumValid);
        } else {
            return isEnumValid(value);
        }
    }

    private boolean isEnumValid(Object enumValue) {
        if (enumValue == null) {
            return false;
        }

        List<String> validValuesList = Arrays.asList(valuesValid.split(","));
        if (!validValuesList.isEmpty()) {
            return validValuesList.contains(enumValue.toString());
        }
        return false;
    }
}
