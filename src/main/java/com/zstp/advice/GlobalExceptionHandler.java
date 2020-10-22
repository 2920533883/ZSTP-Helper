package com.zstp.advice;

import com.zstp.entity.R;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServletRequestBindingException.class)
    public R myServletRequestBindingException(Exception e){
        System.out.println("请求参数不可为空！");
        e.printStackTrace();
        return new R(400, "请求参数不可为空！", null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public R myDataIntegrityViolationException(Exception e){
        System.out.println("请求参数不正确！");
        e.printStackTrace();
        return new R(400, "请求参数不正确！", null);
    }



    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R myHttpRequestMethodNotSupportedException(Exception e){
        System.out.println("请求不正确！");
        e.printStackTrace();
        return new R(405, "请求不正确" ,null);
    }

    @ExceptionHandler(OutOfMemoryError.class)
    public R myOutOfMemoryError(Exception e){
        System.out.println("请求过多！请稍后重试！");
        e.printStackTrace();
        return new R(500, "请求过多,请稍后重试！", null);
    }

    @ExceptionHandler(Exception.class)
    public R myException(Exception e) {
        e.printStackTrace();
        return new R(500, "服务器出现异常！请稍后重试", null);
    }

}
