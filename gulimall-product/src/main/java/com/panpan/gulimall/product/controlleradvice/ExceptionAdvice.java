package com.panpan.gulimall.product.controlleradvice;

import com.panpan.common.exceptionenum.ExceptionNumber;
import com.panpan.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

/**
 * @author panpan
 * @create 2021-06-02 下午7:40
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.panpan.gulimall")
public class ExceptionAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R resolveValidException(MethodArgumentNotValidException validException){
        log.error("数据校验异常："+validException.getMessage()+validException.getClass());
        HashMap<String, String> map = new HashMap<>();
        validException.getBindingResult().getFieldErrors().forEach((item)->map.put(item.getField(),item.getDefaultMessage()));
         return R.error(ExceptionNumber.VALID_EXCEPTHION.getCode(),ExceptionNumber.VALID_EXCEPTHION.getMessage()).put("data",map);
     }



}
