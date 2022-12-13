package kg.peaksoft.giftlistb6.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordValid, String> {
    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        if (password.length()>3){
            return true;
        }else{
            return false;
        }
    }
}