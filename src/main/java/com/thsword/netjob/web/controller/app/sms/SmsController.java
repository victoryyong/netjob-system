package com.thsword.netjob.web.controller.app.sms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.log4j.Log4j2;

import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.netjob.util.PhoneCheckUtils;
import com.thsword.netjob.util.RedisUtils;
import com.thsword.netjob.util.httpclient.HttpClientUtils;
import com.thsword.utils.object.RandomUtil;

/**
 * 短信验证码
 * @author Lenovo
 *
 */
@Controller
@Log4j2
public class SmsController {
	@RequestMapping("app/visitor/getIdentifyCode")
	public void list(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="phone",required=true)String phone,
			@RequestParam(value="type",required=true)int type
			) throws Exception {
		if(!PhoneCheckUtils.isChinaPhoneLegal(phone)){
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "手机格式不正确", response, request);
			return;
		}
		String redisKey = "";
		if(type==Global.JUHE_SMSMESSAGE_IDENTIFY_TYPE_1){
			redisKey = "member:forgetLoginPwd:"+phone;
		}else if(type==Global.JUHE_SMSMESSAGE_IDENTIFY_TYPE_2){
			redisKey = "member:forgetPayPwd:"+phone;
		}else{
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "类型异常", response, request);
			return;
		}
		String redisValue = RedisUtils.get(redisKey);
		if(!StringUtils.isEmpty(redisValue)){
			JSONObject redisObj = JSONObject.parseObject(redisValue);
			Long dateLong = redisObj.getLong("limitTime");
			if(DateTime.now().isBefore(dateLong)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "请勿频繁获取验证码", response, request);
				return;
			}
		}
		String appKey = Global.getSetting(Global.JUHE_ACCESS_SMS_KEY);
		String identifyCode = RandomUtil.getRandomNum(6);
		Map<String, String> params = new HashMap<String, String>();
		 params.put("mobile",phone);//接收短信的手机号码
         params.put("tpl_id",Global.JUHE_SMSMESSAGE_IDENTIFY_TEMPLATE);//短信模板ID，请参考个人中心短信模板设置
         params.put("tpl_value","#code#="+identifyCode);//变量名和变量值对。如果你的变量名或者变量值中带有#&=中的任意一个特殊符号，请先分别进行urlencode编码后再传递，<a href="http://www.juhe.cn/news/index/id/50" target="_blank">详细说明></a>
         params.put("key",appKey);//应用APPKEY(应用详细页查询)
         params.put("dtype","json");//返回数据的格式,xml或json，默认json
		String jsonString = "";
		try {
			jsonString = HttpClientUtils.post(Global.JUHE_SMSMESSAGE_URL, params,null);
		} catch (Exception e) {
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "请求验证码出错", response, request);
			return;
		}
		JSONObject smsInfo = JSONObject.parseObject(jsonString);
		if(null==smsInfo){
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "请求验证码出错", response, request);
			return;
		}
		if(smsInfo.getString("error_code").equals("0")){
			JSONObject obj = new JSONObject();
			obj.put("phone", phone);
			obj.put("identifyCode", identifyCode);
			obj.put("limitTime", DateTime.now().plusMinutes(1).getMillis());
			RedisUtils.set(redisKey, obj.toJSONString(), Global.JUHE_SMSMESSAGE_TIMEOUT);
			JsonResponseUtil.successCodeResponse(response, request);
			return;
		}else{
			log.info(smsInfo.getString("reason"));
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "请求验证码出错", response, request);
			return;
		}
	}
}
