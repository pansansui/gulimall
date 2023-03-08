package com.panpan.common.validnote;

import com.panpan.common.volidator.NumberVolidator;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.*;

/**
 * @author panpan
 * @create 2021-06-02 下午8:48
 */
@Documented
@Constraint(
        validatedBy = {NumberVolidator.class}
)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Number {
    String message() default "{com.panpan.common.validnote.Number.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int[] vals() default {};

}
