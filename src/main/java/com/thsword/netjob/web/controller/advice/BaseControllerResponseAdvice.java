package com.thsword.netjob.web.controller.advice;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.thsword.netjob.web.controller.base.BaseResponse;

@ControllerAdvice
public class BaseControllerResponseAdvice implements ResponseBodyAdvice{
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter arg1,
			MediaType arg2, Class arg3, ServerHttpRequest request,
			ServerHttpResponse response) {
		String uri = request.getURI().getPath();
		if(body instanceof BaseResponse 
				|| uri.indexOf("/swagger-resources")>=0
				||uri.indexOf("/swagger-ui")>=0
				||uri.indexOf("/v2/api-docs")>=0
				||uri.indexOf("/webjars")>=0
				){
			return body;
		}{
			return BaseResponse.success(body);
		}
	}

	@Override
	public boolean supports(MethodParameter arg0, Class arg1) {
		return true;
	}

}
