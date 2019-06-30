package com.thsword.netjob.web.controller.base;

import java.util.Objects;

import lombok.Data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

@Data
public class BaseResponse{
	private  int code;
	private  String message;
	private  Object body = null;
	
	public static final int HTTP_SUCCESS = 200;
	public static final int HTTP_FAIL = -200;
	public static final String HTTP_SUCCESS_MESSAGE="success";
	public static final String HTTP_FAIL_MESSAGE="fail";
	
	public static BaseResponse success() {
		return successResponse(HTTP_SUCCESS,HTTP_SUCCESS_MESSAGE,null);
	}
	
	public static BaseResponse success(Object body) {
		return successResponse(HTTP_SUCCESS,HTTP_SUCCESS_MESSAGE,body);
	}
	
	public static BaseResponse success(String msg, Object body) {
		return successResponse(HTTP_SUCCESS, msg, body);
	}
	
	public static BaseResponse fail() {
		return successResponse(HTTP_FAIL,HTTP_FAIL_MESSAGE,null);
	}
	
	public static BaseResponse fail(String message) {
		return successResponse(HTTP_FAIL,message,null);
	}
	
	public static BaseResponse fail(int code,String message) {
		return successResponse(code,message,null);
	}
	

	private static BaseResponse successResponse(int code, String message, Object body) {
		return new BaseResponse(code,message,body);
	}
	
	private BaseResponse(int code, String message, Object body){
		this.code = code;
		this.message=message;
		this.body = body;
	}
	
	@Override
	public String toString() {
		if(Objects.isNull(this.body)){
            this.setBody(new Object());
        }
        return JSON.toJSONString(this,SerializerFeature.WriteMapNullValue);
	}
}
