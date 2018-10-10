package com.thsword.netjob.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonResponseUtil {

	public static final String CODE = "code";
	public static final String MESSAGE = "message";
	public static final String BODY = "body";
	public static final int HTTP_SUCCESS = 200;

	public static void successCodeResponse(HttpServletResponse response,HttpServletRequest request) {
		codeResponse(HTTP_SUCCESS, response, request);
	}

	public static void successMsgResponse(String message,
			HttpServletResponse response,HttpServletRequest request) {
		msgResponse(HTTP_SUCCESS, message, response, request);
	}

	public static void successBodyResponse(Object body, HttpServletResponse response,HttpServletRequest request) {
		bodyResponse(HTTP_SUCCESS, body, response, request);
	}

	public static void successBodyResponse(String msg, Object body,
			HttpServletResponse res,HttpServletRequest req) {
		bodyResponse(HTTP_SUCCESS, msg, body, res, req);
	}

	public static void codeResponse(int code, HttpServletResponse response,HttpServletRequest request) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=UTF-8");
			JSONObject json = new JSONObject();
			json.put(CODE, code);
			json.put(BODY, null);
			request.setAttribute(CODE, code);
			json.put(MESSAGE, ErrorUtil.getMessage(code));
			response.getWriter().write(
					JSONObject.toJSONString(json));
			/*response.getWriter().write(
					JSONObject.toJSONString(json,
							SerializerFeature.WriteMapNullValue));*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void msgResponse(int code, String message,
			HttpServletResponse response,HttpServletRequest request) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=UTF-8");
			JSONObject json = new JSONObject();
			json.put(CODE, code);
			json.put(BODY, null);
			request.setAttribute(CODE, code);
			json.put(MESSAGE, message);
			response.getWriter().write(
					JSONObject.toJSONString(json,
							SerializerFeature.WriteMapNullValue));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void bodyResponse(int code, String message, Object body,
			HttpServletResponse response,HttpServletRequest request) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=UTF-8");
			JSONObject json = new JSONObject();
			json.put(CODE, code);
			request.setAttribute(CODE, code);
			json.put(MESSAGE, message);
			json.put(BODY, body);
			response.getWriter().write(
					JSONObject.toJSONString(json));
			/*response.getWriter().write(
					JSONObject.toJSONString(json,
							SerializerFeature.WriteMapNullValue));*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void bodyResponse(int code, Object body,
			HttpServletResponse response,HttpServletRequest request) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=UTF-8");
			JSONObject json = new JSONObject();
			json.put(CODE, code);
			request.setAttribute(CODE, code);
			json.put(MESSAGE, ErrorUtil.getMessage(code));
			json.put(BODY, body);
			response.getWriter().write(
					JSONObject.toJSONString(json));
			/*response.getWriter().write(
					JSONObject.toJSONString(json,
							SerializerFeature.WriteMapNullValue));*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void responseMsg(String msg,
			HttpServletResponse response){
		try {
			response.getWriter().write(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
