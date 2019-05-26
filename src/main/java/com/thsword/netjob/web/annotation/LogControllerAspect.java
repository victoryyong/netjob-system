package com.thsword.netjob.web.annotation;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.ILogDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.Log;
import com.thsword.netjob.service.LogService;
import com.thsword.utils.ip.IPUtil;
import com.thsword.utils.object.UUIDUtil;


@Aspect
@Component 
public class  LogControllerAspect {
	// 注入service 用于把热值保存数据库  
    @Resource  
    private LogService logService; 
    // 本地异常日志记录对象  
    private static final Logger logger = Logger.getLogger(LogControllerAspect.class);  
  
  
    // Contorller层切点  
    @Pointcut("@annotation(com.thsword.netjob.web.annotation.LogControllerAnnotation)")  
    public void controllerAspect() {  
    }  
  
    // 前置通知 用于拦截contorller层记录 日志的操作  
    @Before("controllerAspect()")  
    public void doBefore(JoinPoint joinPoint) {  
    }
    
    
    @AfterReturning(pointcut="controllerAspect()")  
    public  void doAfter(JoinPoint joinPoint) {  
    	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder  
                .getRequestAttributes()).getRequest();  
    	try {
			addLog(joinPoint, null, 1);
			logger.info("=====请求结束====="+request.getRequestURI());
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    } 
  
    /** 
     * 异常通知 用于拦截service层记录异常日志 
     *  
     * @param joinPoint 
     * @param e 
     */  
    @AfterThrowing(pointcut = "controllerAspect()", throwing = "e")  
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder  
                .getRequestAttributes()).getRequest();  
        try {
        	addLog(joinPoint, e, 0);
            logger.info("=====请求结束====="+request.getRequestURI());  
        } catch (Exception ex) {  
        	System.out.println(ex.getMessage());
            // 记录本地异常日志  
            logger.error("异常信息:{}");  
            logger.error(ex.getMessage());  
        }  
        /* ==========记录本地异常日志========== */  
    }  
  
    /** 
     * 获取注解中对方法的描述信息 用于Controller层注解 
     *  
     * @param joinPoint 
     *            切点 
     * @return 方法描述 
     * @throws Exception 
     */  
    @SuppressWarnings("rawtypes")
	private String getControllerMethodDescription(JoinPoint joinPoint)  
            throws Exception {  
        // 获取目标类名  
        String targetName = joinPoint.getTarget().getClass().getName();  
        // 获取方法名  
        String methodName = joinPoint.getSignature().getName();  
        // 获取相关参数  
        Object[] arguments = joinPoint.getArgs();  
        // 生成类对象  
        Class targetClass = Class.forName(targetName);  
        // 获取该类的方法  
        Method[] methods = targetClass.getMethods();  
        String description = "";  
        for (Method method : methods) {  
            if (method.getName().equals(methodName)) {  
                Class[] clazzs = method.getParameterTypes();  
                if (clazzs.length == arguments.length) {  
                    description = method.getAnnotation(  
                    		LogControllerAnnotation.class).description();  
                    break;  
                }  
            }  
        }  
        return description;  
    }  
    
    private void addLog(JoinPoint joinPoint,Throwable e,int status)  
            throws Exception {  
    	try {  
    		HttpServletRequest request = null;
    		Object[] args = joinPoint.getArgs();
    		for (int i = 0; i < args.length; i++) {  
                if (args[i] instanceof HttpServletRequest) {  
                    request = (HttpServletRequest) args[i];  
                }
            }
            // *========控制台输出=========*//  
    		String userId = (String) request.getAttribute("userId");
    		String username = (String) request.getAttribute("username");
    		String subject = (String)request.getAttribute("subject");
    		if(null!=request.getAttribute("code")){
    			if((Integer)request.getAttribute("code")!=200){
    				return;
    			}
    		}
            logger.info("请求方法:"  
                    + (joinPoint.getTarget().getClass().getName() + "."  
                            + joinPoint.getSignature().getName() + "()"));  
            Log log = new Log();  
        	log.setUsername(username);
        	log.setUserId(userId);
            log.setId(UUIDUtil.get32UUID());
            
            Map<String,Object> maps = new HashMap<String, Object>();
			Enumeration em = request.getParameterNames();
			 while (em.hasMoreElements()) {
			    String name = (String) em.nextElement();
			    String value = request.getParameter(name);
			    if(name.equals("token")){
			    	continue;
			    }
		    	if(StringUtils.isEmpty(value)){
		    		maps.put(name, null);
		    	}else{
		    		maps.put(name, value);
		    	}
			}
			 
            String content ="";
            String description=getControllerMethodDescription(joinPoint);
            if(status == 1){
            	content = "管理员: "+username+"->"+description+" 成功!";
            	if(!StringUtils.isEmpty(subject)&&subject.equals(Global.JWT_SUBJECT_ADMIN))
            	content = "管理员: "+username+"->"+description+" 成功!";
            	if(!StringUtils.isEmpty(subject)&&subject.equals(Global.JWT_SUBJECT_APP))
        		content = "会员: "+username+"->"+description+" 成功!";
            	content+="param is "+JSONObject.toJSONString(maps);
            	log.setContent(content);  
            }else{
            	if(e!=null){
        		   StringWriter sw = new StringWriter();   
                   PrintWriter pw = new PrintWriter(sw, true);   
                   e.printStackTrace(pw);   
                   pw.flush();   
                   sw.flush();   
                   //return ; 
                   log.setErrorMsg(sw.toString());
            	}
            	content = "管理员: "+username +" "+getControllerMethodDescription(joinPoint)+" 失败!";
            	if(!StringUtils.isEmpty(subject)&&subject.equals(Global.JWT_SUBJECT_ADMIN))
        		content = "管理员: "+username +" "+getControllerMethodDescription(joinPoint)+" 失败!";
            	if(!StringUtils.isEmpty(subject)&&subject.equals(Global.JWT_SUBJECT_APP))
            	content = "会员: "+username +" "+getControllerMethodDescription(joinPoint)+" 失败!";
            	content+="param is "+JSONObject.toJSONString(maps);
            	log.setContent(content);  
            }
            log.setTitle(description);  
            log.setIp(IPUtil.getRemoteHost(request));  
            log.setCreateDate(new Date());  
            log.setStatus(status);  
            // 保存数据库  
            logService.addEntity(ILogDao.class, log);
        } catch (Exception ex) {  
        	ex.printStackTrace();
            // 记录本地异常日志  
            logger.error("==前置通知异常==异常信息:{}");  
            logger.error(e.getMessage());  
        } 
    }
}  
