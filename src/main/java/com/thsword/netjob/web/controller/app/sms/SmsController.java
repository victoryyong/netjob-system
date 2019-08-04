package com.thsword.netjob.web.controller.app.sms;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.log4j.Log4j2;

import org.joda.time.DateTime;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.netjob.util.PhoneCheckUtils;
import com.thsword.netjob.util.RedisUtils;
import com.thsword.netjob.util.httpclient.HttpClientUtils;
import com.thsword.netjob.web.exception.ServiceException;
import com.thsword.utils.object.RandomUtil;

/**
 * 短信验证码
 * 
 * @author Lenovo
 *
 */
@RestController
@Api(tags = "NETJOB-SMS", description = "短信接口")
@Log4j2
public class SmsController {
	@ApiOperation(value = "获取验证码", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "phone", value = "电话", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "type", value = "验证码", dataType = "int", paramType = "query", required = true), })
	@RequestMapping("app/visitor/getIdentifyCode")
	public void list(HttpServletRequest request, HttpServletResponse response,
			@RequestParam String phone, @RequestParam int type)
			throws Exception {
		if (!PhoneCheckUtils.isChinaPhoneLegal(phone)) {
			throw new ServiceException("手机格式不正确");
		}
		String redisKey = "";
		if (type == Global.JUHE_SMSMESSAGE_IDENTIFY_TYPE_1) {
			redisKey = "member:forgetLoginPwd:" + phone;
		} else if (type == Global.JUHE_SMSMESSAGE_IDENTIFY_TYPE_2) {
			redisKey = "member:forgetPayPwd:" + phone;
		} else {
			throw new ServiceException("类型异常");
		}
		String redisValue = RedisUtils.get(redisKey);
		if (!StringUtils.isEmpty(redisValue)) {
			JSONObject redisObj = JSONObject.parseObject(redisValue);
			Long dateLong = redisObj.getLong("limitTime");
			if (DateTime.now().isBefore(dateLong)) {
				throw new ServiceException("请勿频繁获取验证码");
			}
		}
		String appKey = Global.getSetting(Global.JUHE_ACCESS_SMS_KEY);
		String identifyCode = RandomUtil.getRandomNum(6);
		Map<String, String> params = new HashMap<String, String>();
		params.put("mobile", phone);// 接收短信的手机号码
		params.put("tpl_id", Global.JUHE_SMSMESSAGE_IDENTIFY_TEMPLATE);// 短信模板ID，请参考个人中心短信模板设置
		params.put("tpl_value", "#code#=" + identifyCode);// 变量名和变量值对。如果你的变量名或者变量值中带有#&=中的任意一个特殊符号，请先分别进行urlencode编码后再传递，<a
															// href="http://www.juhe.cn/news/index/id/50"
															// target="_blank">详细说明></a>
		params.put("key", appKey);// 应用APPKEY(应用详细页查询)
		params.put("dtype", "json");// 返回数据的格式,xml或json，默认json
		String jsonString = "";
		try {
			jsonString = HttpClientUtils.post(Global.JUHE_SMSMESSAGE_URL,
					params, null);
		} catch (Exception e) {
			throw new ServiceException("请求验证码出错");
		}
		JSONObject smsInfo = JSONObject.parseObject(jsonString);
		if (null == smsInfo) {
			throw new ServiceException("请求验证码出错");
		}
		if (smsInfo.getString("error_code").equals("0")) {
			JSONObject obj = new JSONObject();
			obj.put("phone", phone);
			obj.put("identifyCode", identifyCode);
			obj.put("limitTime", DateTime.now().plusMinutes(1).getMillis());
			RedisUtils.set(redisKey, obj.toJSONString(),
					Global.JUHE_SMSMESSAGE_TIMEOUT);
			JsonResponseUtil.successCodeResponse(response, request);
		} else {
			log.info(smsInfo.getString("reason"));
			throw new ServiceException("请求验证码出错");
		}
	}
}
