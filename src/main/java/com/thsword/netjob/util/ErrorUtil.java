package com.thsword.netjob.util;

public class ErrorUtil{
	private int code;
	private String msg;

	/**
	 * 请求已完成。
	 */
	public static final int HTTP_SUCCESS = 200;
	
	/**
	 * 请求失败
	 */
	public static final int HTTP_FAIL = -200;

	/**
	 * 服务器内部错误
	 */
	public static final int HTTP_ERROR = 500;

	/** --------------------网络begin---------------------------- */

	/**
	 * 网络异常
	 */
	public static final int NETWORK_ERROR = -101;

	/**
	 * 网络链接超时
	 */
	public static final int NETWORK_ERROR_TIMEOUT = -102;

	/** --------------------网络end---------------------------- */

	/** --------------------文件begin---------------------------- */

	/**
	 * 非法路径
	 */
	public static final int FILE_ILLEGAL_PATH = -201;

	/**
	 * 非法文件
	 */
	public static final int FILE_ILLEGAL_EXT = -202;

	/**
	 * 文件不存在
	 */
	public static final int FILE_NOT_EXISTS = -203;

	/**
	 * 文件上传出错
	 */
	public static final int FILE_UPLOAD_ERROR = -204;
	
	/**
	 * 未绑定手机
	 */
	public static final int NOT_HAS_AUTH_PHONE = -205;
	/** --------------------文件end---------------------------- */

	/** --------------------登陆begin---------------------------- */
	/**
	 * 用户校验出错
	 */
	public static final int LOGIN_ERROR = -301;

	/**
	 * 密码错误
	 */
	public static final int LOGIN_ERROR_PWD = -302;

	/**
	 * 账号不存在
	 */
	public static final int LOGIN_ERROR_USER = -303;

	/**
	 * 账号被冻结
	 */
	public static final int LOGIN_ERROR_USER_DISABLED = -304;

	/**
	 * 验证码错误
	 */
	public static final int LOGIN_ERROR_VERIFYCODE = -305;

	/**
	 * 需要验证码
	 */
	public static final int LOGIN_ERROR_NEED_VERIFYCODE = -306;

	/**
	 * 用户未登陆
	 */
	public static final int LOGIN_WITHOUT = -307;

	/**
	 * 用户退出出错
	 */
	public static final int LOGIN_OUT_ERROR = -308;
	
	/**
	 * 账号或密码错误
	 */
	public static final int LOGIN_NAME_PASSWORD_ERROR = -309;
	
	/**
	 * 账号已存在
	 */
	public static final int REGISTER_NAME_EXSIST = -310;
	
	/**
	 * 角色名已存在
	 */
	public static final int ROLE_NAME_EXSIST = -311;
	
	/**
	 * 角色码已存在
	 */
	public static final int ROLE_CODE_EXSIST = -312;
	
	/**
	 * 权限名已存在
	 */
	public static final int PERMISSION_NAME_EXSIST = -313;
	
	
	/**
	 * 权限码已存在
	 */
	public static final int PERMISSION_CODE_EXSIST = -314;
	
	/**
	 * 存在子元素，禁止删除
	 */
	public static final int PERMISSION_HAS_CHILD = -315;

	
	/** --------------------登陆end---------------------------- */

	/** --------------------request请求begin---------------------------- */
	/**
	 * 没有权限
	 */
	public static final int REQUEST_NO_PERMISSION = -401;

	/**
	 * token异常
	 */
	public static final int REQUEST_TOKEN_ERROR = -402;

	/**
	 * token已过期
	 */
	public static final int REQUEST_TOKEN_TIME_OUT = -403;

	/**
	 * 需要token认证信息
	 */
	public static final int REQUEST_TOKEN_NEEDED = -404;

	/**
	 * 参数异常
	 */
	public static final int REQUEST_INVALID_PARAM = -405;

	/**
	 * 无效请求
	 */
	public static final int REQUEST_INVALID_URL = -406;

	/**
	 * 没有权限
	 */
	public static final int REQUEST_NO_SUPERMANAGER_PERMISSION = -407;

	/** --------------------request请求end---------------------------- */
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public static String getMessage(int code) {
		String message = "";
		switch (code) {
		case 500:
			message = "系统内部错误 ";
			break;
		case 200:
			message = "操作成功";
			break;
		case -101:
			message = "网络异常 ";
			break;
		case -102:
			message = "网络链接超时";
			break;
		case -201:
			message = "非法路径";
			break;
		case -202:
			message = "非法文件";
			break;
		case -203:
			message = "文件不存在";
			break;
		case -204:
			message = "文件上传出错";
			break;
		case -205:
			message = "请绑定手机";
			break;
		case -301:
			message = "用户校验出错";
			break;
		case -302:
			message = "密码错误";
			break;
		case -303:
			message = "账号不存在";
			break;
		case -304:
			message = "账号被冻结";
			break;
		case -305:
			message = "验证码错误";
			break;
		case -306:
			message = "需要验证码";
			break;
		case -307:
			message = "用户未登陆";
			break;
		case -308:
			message = "用户退出出错";
			break;
		case -309:
			message = "账号或密码错误";
			break;
		case -310:
			message = "账号已存在";
			break;
		case -311:
			message = "角色名已存在";
			break;
		case -312:
			message = "角色码已存在";
			break;
		case -313:
			message = "权限名已存在";
			break;
		case -314:
			message = "权限码已存在";
			break;
		case -315:
			message = "存在子元素，禁止删除";
			break;
		case -401:
			message = "没有权限";
			break;
		case -402:
			message = "token异常";
			break;
		case -403:
			message = "token已过期";
			break;
		case -404:
			message = "需要token认证信息";
			break;
		case -405:
			message = "参数异常";
			break;
		case -406:
			message = "请求无效";
			break;
		case -407:
			message = "您不是超管，无此权限";
			break;
		}
		return message;
	}
}
