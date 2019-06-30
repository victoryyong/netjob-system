package com.thsword.netjob.web.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.log4j.Log4j2;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.thsword.netjob.dao.ITokenDao;
import com.thsword.netjob.dao.IUserDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.Token;
import com.thsword.netjob.pojo.User;
import com.thsword.netjob.service.RedisService;
import com.thsword.netjob.service.TokenService;
import com.thsword.netjob.service.UserService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.netjob.util.TokenUtil;
import com.thsword.utils.object.JWTUtil;

/***
 * token和权限认证拦截
 */
@Log4j2
public class AdminTokenFilter extends HttpServlet implements HandlerInterceptor {
	private static final long serialVersionUID = 5836290769748648967L;
	@Resource(name = "tokenService")
	private TokenService tokenService;
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "redisService")
	private RedisService redisService;

	public String[] allowUrls;
	
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		String url = request.getRequestURI();
		if (null != allowUrls && allowUrls.length >= 1) {
			for (String allowUrl : allowUrls) {
				if (url.indexOf(allowUrl) >= 0) {
					break;
				}
			}
		}
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object obj, ModelAndView modelView)
			throws Exception {
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		boolean flag = false;
		String url = request.getRequestURI();
		String userId = "";
		// 放行地址
		Token tokenDto = null;
		if (null != allowUrls && allowUrls.length >= 1) {
			for (String allowUrl : allowUrls) {
				if (url.indexOf(allowUrl) >= 0) {
					return true;
				}
			}
		}
		

		// 前后端token认证
		String token = request.getParameter("token");
		String subject = "";
		if (null != token && !"".equals(token)) {
			User user = null;
			try {
				String key = Global.getSetting(Global.JWT_SECRET_ADMIN_KEY);
				Claims claims = JWTUtil.parseJWT(token, key);
				String tokenId = claims.getId();
				subject = claims.getSubject();
				if (url.indexOf(subject) < 0) {
					JsonResponseUtil.codeResponse(
							ErrorUtil.REQUEST_TOKEN_ERROR, response,request);
					flag = false;
				} else {
					Date expire = claims.getExpiration();
					String issuer = claims.getIssuer();
					if (StringUtils.isEmpty(tokenId) || expire == null) {
						JsonResponseUtil.codeResponse(
								ErrorUtil.REQUEST_TOKEN_ERROR,
								response,request);
					} else {
							tokenDto = new Token();
							tokenDto.setId(tokenId);
							tokenDto.setAccess_token(token);
							tokenDto.setSubject(subject);
							tokenDto.setIssuer(issuer);

							List<Token> tokens = (List<Token>) tokenService
									.queryAllEntity(ITokenDao.class,tokenDto);
							// 正常token处理
							if (null != tokens && tokens.size() > 0) {
								//判断用户是否两个小时未交互
								tokenDto = tokens.get(0);
								if(!StringUtils.isEmpty(tokenDto.getSessionDate())){
									Date sessionDate = tokenDto.getSessionDate();
									//会话已经过期需要重新登录
									if(sessionDate.before(new Date())){
										log.info("params request "+ErrorUtil.getMessage(ErrorUtil.REQUEST_TOKEN_TIME_OUT));
										JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_TOKEN_TIME_OUT,response,request);
										flag = false;
									}else{
										//更新session时间
										tokenDto.setSessionDate(new Date(System.currentTimeMillis()+Global.JWT_ADMIN_SESSION_TIME_OUT));
										tokenService.updateEntity(ITokenDao.class, tokenDto);
										userId = tokenDto.getUserId();
										request.setAttribute("userId", userId);
										request.setAttribute("username", tokenDto.getUsername());
										user = (User) userService.queryEntityById(IUserDao.class, userId);
										if(user!=null){
											if(!StringUtils.isEmpty(user.getLevel())&&user.getLevel()==Global.SYS_USER_LEVEL_1){
												request.setAttribute("userLevel",Global.SYS_USER_LEVEL_1);
											}else{
												request.setAttribute("userLevel",Global.SYS_USER_LEVEL_2);
												request.setAttribute("citycode", user.getCitycode());
											}
											request.setAttribute("isAdmin", user.isAdmin());
											request.setAttribute("subject", subject);
										}
										flag = true;
									}
								}
							} else {
								log.info("request error"+ErrorUtil.getMessage(ErrorUtil.REQUEST_TOKEN_ERROR));
								JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_TOKEN_ERROR,response,request);
								flag = false;
							}
						}
					}
				// 过期异常
			} catch (ExpiredJwtException e) {
				tokenDto = new Token();
				tokenDto.setAccess_token(token);
				List<Token>  tokens = (List<Token>) tokenService.queryAllEntity(ITokenDao.class, tokenDto);
				if(null!=tokens&&tokens.size()>0){
					tokenDto=tokens.get(0);
					//token过期后再验证会话时间是否过期
					if(!StringUtils.isEmpty(tokenDto.getSessionDate())){
						Date sessionDate = tokenDto.getSessionDate();
						//会话也过期则需要重新登录
						if(sessionDate.before(new Date())){
							log.info("request error"+ErrorUtil.getMessage(ErrorUtil.REQUEST_TOKEN_TIME_OUT));
							JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_TOKEN_TIME_OUT,response,request);
							flag = false;
						//会话未过期生成新的token允许带上新的token重新请求
						}else{
							tokenDto = tokens.get(0);
							//subject = tokenDto.getSubject();
							long currentTime = System.currentTimeMillis();
							//设置宽限时间防止并发
							tokenDto.setExtendDate(new Date(currentTime+Global.JWT_EXTEND_TIME));
							tokenDto.setExpires(new Date(currentTime+Global.JWT_ADMIN_EXPIRESS_TIME));
							tokenDto.setSessionDate(new Date(currentTime+Global.JWT_ADMIN_SESSION_TIME_OUT));
							String newToken = TokenUtil.createJWT(tokenDto);
							tokenDto.setOld_token(tokenDto.getAccess_token());
							tokenDto.setAccess_token(newToken);
							tokenService.updateEntity(ITokenDao.class, tokenDto);
							if (!StringUtils.isEmpty(newToken)) {
								log.info("request error"+ErrorUtil.getMessage(ErrorUtil.REQUEST_TOKEN_TIME_OUT));
								JsonResponseUtil.bodyResponse(ErrorUtil.REQUEST_TOKEN_TIME_OUT,newToken, response,request);
							}
						}
					}
				}else{
					//根据旧的token去查询
					tokenDto.setOld_token(token);
					tokenDto.setAccess_token("");
					tokens = (List<Token>) tokenService.queryAllEntity(ITokenDao.class, tokenDto);
					//检查是否在宽限或者session允许范围时间内防止并发或者频繁请求或者多出登录时出现过期现象
					if(null!=tokens&&tokens.size()>0){
						tokenDto=tokens.get(0);
						//subject = tokenDto.getSubject();
						userId = tokenDto.getUserId();
						Date extendDate = tokenDto.getExtendDate();
						//旧的token请求允许情况  1.宽限时间内 (防止并发)
						if(extendDate.after(new Date())){
							log.info("request error"+ErrorUtil.getMessage(ErrorUtil.REQUEST_TOKEN_TIME_OUT));
							JsonResponseUtil.bodyResponse(ErrorUtil.REQUEST_TOKEN_TIME_OUT,tokenDto.getAccess_token(),response,request);
						}else{
							flag = false;
							log.info("request error"+ErrorUtil.getMessage(ErrorUtil.REQUEST_TOKEN_TIME_OUT));
							JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_TOKEN_TIME_OUT, response,request);
						}
					}else{
						log.info("request error"+ErrorUtil.getMessage(ErrorUtil.REQUEST_TOKEN_ERROR));
						JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_TOKEN_ERROR, response,request);
					}
				}
		    } catch (Exception e) {
		    	log.info("request error"+ErrorUtil.getMessage(ErrorUtil.REQUEST_TOKEN_ERROR),e);
		        JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_TOKEN_ERROR, response,request);
		    }
		} else {
			JsonResponseUtil.codeResponse(
					ErrorUtil.REQUEST_TOKEN_NEEDED, response,request);
		}
		
		return flag;
	}


	public String[] getAllowUrls() {
		return allowUrls;
	}

	public void setAllowUrls(String[] allowUrls) {
		this.allowUrls = allowUrls;
	}
	/*
	 * public static void main(String[] args) { String str =
	 * "appId=lzk20171017GIEKDA13DFED&accessKey=b5044b72d4dd4e95b43e2bd3fb67ff6e&timestamp=2017-10-18 14:53&courseId=84C51C5DE7A843F7A4B49E005B7FF028&reviewStatus=2&releaseStatus=1"
	 * ; System.out.println(SecurityUtils.getMd5(str)); }
	 */
	
}
