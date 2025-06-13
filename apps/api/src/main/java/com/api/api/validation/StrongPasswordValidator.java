package com.api.api.validation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {
    @Override
    public void initialize(StrongPassword constraintAnnotation) {
    }
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isBlank()) {
            return true;
        }
        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        String specials = "!@#$%^&*()_+-={}[]:;\"'|<>,.?/~`";
        for (char passwordChar : password.toCharArray()) {
            if (Character.isLowerCase(passwordChar))
                hasLower = true;
            else if (Character.isUpperCase(passwordChar))
                hasUpper = true;
            else if (Character.isDigit(passwordChar))
                hasDigit = true;
            else if (specials.indexOf(passwordChar) >= 0)
                hasSpecial = true;
            if (hasLower && hasUpper && hasDigit && hasSpecial) {
                return true;
            }
        }
        return false;
    }
}
