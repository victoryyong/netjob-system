package com.thsword.netjob.web.advice;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thsword.netjob.web.controller.base.BaseResponse;
import com.thsword.netjob.web.exception.ServiceException;

@ResponseBody
@ControllerAdvice
@Log4j2
public class BaseControllerExceptionAdvice{
	
	  /**
     * 全局异常捕捉处理
     * @param ex
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public BaseResponse  errorHandler(Exception ex,HttpServletRequest request, HttpServletResponse response) {
    	 List<String> paramList = new ArrayList<>(8);
         for (Object key : request.getParameterMap().keySet()) {
             paramList.add(key + "=" + request.getParameter(key.toString()));
         }
        log.error("request url->{},error!param is  error msg is {}",request.getRequestURI(),paramList,ex);
        return BaseResponse.fail();
    }

    /**
     * 拦截捕捉自定义异常 ServiceException.class
     * @param ex
     * @return
     */
    
    @ExceptionHandler(value = ServiceException.class)
    public BaseResponse myErrorHandler(ServiceException ex) {
    	return BaseResponse.fail(ex.getMessage());
    }
}
