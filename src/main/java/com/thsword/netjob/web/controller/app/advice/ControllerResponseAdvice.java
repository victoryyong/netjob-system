package com.thsword.netjob.web.controller.app.advice;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ControllerResponseAdvice implements ResponseBodyAdvice {

	@Override
	public Object beforeBodyWrite(Object arg0, MethodParameter arg1,
			MediaType arg2, Class arg3, ServerHttpRequest arg4,
			ServerHttpResponse arg5) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean supports(MethodParameter arg0, Class arg1) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
