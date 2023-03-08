package com.panpan.common.volidator;

import com.panpan.common.validnote.Number;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author panpan
 * @create 2021-06-02 下午9:07
 */
public class NumberVolidator implements ConstraintValidator<Number,Integer> {
    private Set set=new HashSet();
    @Override
    public void initialize(Number constraintAnnotation) {
        Arrays.stream(constraintAnnotation.vals()).forEach((item)->set.add(item));
    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return set.contains(integer);
    }
}
