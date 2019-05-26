package com.thsword.netjob.web.annotation;

import java.lang.reflect.Method;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.thsword.netjob.dao.IAccountDao;
import com.thsword.netjob.dao.IAccountDepositDao;
import com.thsword.netjob.pojo.app.Account;
import com.thsword.netjob.pojo.app.AccountDeposit;
import com.thsword.netjob.service.AccountDepositService;
import com.thsword.netjob.service.AccountService;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.netjob.web.annotation.AccountCheckAnnotation.CHECKTYPE;

@Aspect
@Component 
public class AccountCheckAspect {
    @Resource  
    private AccountDepositService accountDepositService;
    @Resource  
    private AccountService accountService;
  
  
    // Contorller层切点  
    @Pointcut("@annotation(com.thsword.netjob.web.annotation.AccountCheckAnnotation)")  
    public void controllerAspect() {  
    }  
  
    // 前置通知 用于拦截contorller层记录 日志的操作  
    @Before("controllerAspect()")  
    public void doBefore(JoinPoint joinPoint) {  
    	HttpServletRequest request = null;
    	HttpServletResponse response = null;
		Object[] args = joinPoint.getArgs();
		for (int i = 0; i < args.length; i++) {  
            if (args[i] instanceof HttpServletRequest) {  
                request = (HttpServletRequest) args[i];  
            }
            if (args[i] instanceof HttpServletResponse) {  
            	response = (HttpServletResponse) args[i];  
            }
        }
		String memberId = request.getAttribute("memberId")+"";
		try {
			CHECKTYPE type = getType(joinPoint);
			boolean flag = false;
			switch (type) {
			case ACTIVE:
				flag = checkAccount(memberId);
				JsonResponseUtil.successBodyResponse(flag, response, request);
				break;
			case DEPOSIT:
				flag = checkDepositAccount(memberId);
				JsonResponseUtil.successBodyResponse(flag, response, request);
				break;
			case BOTH:
				flag = checkAccount(memberId)&&checkDepositAccount(memberId);
				JsonResponseUtil.successBodyResponse(flag, response, request);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    }
    
    
    @AfterReturning(pointcut="controllerAspect()")  
    public  void doAfter(JoinPoint joinPoint) {  
    } 
  
    /** 
     * @param joinPoint 
     * @param e 
     */  
    @AfterThrowing(pointcut = "controllerAspect()", throwing = "e")  
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
    }  
  
     
    /** 
     * 获取注解中对方法的描述信息 用于Controller层注解 
     *  
     * @param joinPoint 
     *            切点 
     * @return 方法描述 
     * @throws Exception 
     */    
    private CHECKTYPE getType(JoinPoint joinPoint)throws Exception{
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
        CHECKTYPE type = CHECKTYPE.ACTIVE;  
        for (Method method : methods) {  
            if (method.getName().equals(methodName)) {  
                Class[] clazzs = method.getParameterTypes();  
                if (clazzs.length == arguments.length) {  
                	type = method.getAnnotation(  
                    		AccountCheckAnnotation.class).type();  
                    break;  
                }  
            }  
        }  
    	return type;
    }
    
    /**
     * 检查保证金
     */
    private boolean checkDepositAccount(String memberId) throws Exception{
    	AccountDeposit ac = new AccountDeposit();
    	ac.setMemberId(memberId);
    	ac = (AccountDeposit) accountDepositService.queryEntity(IAccountDepositDao.class, ac);
    	if(null!=ac){
    		return true;
    	}
    	return false;
    }
    
    /**
     * 检查账户
     */
    private boolean checkAccount(String memberId) throws Exception{
    	Account ac=new Account();
		ac.setMemberId(memberId);
		ac = (Account) accountService.queryEntity(IAccountDao.class,ac);
    	if(null!=ac){
    		return true;
    	}
    	return false;
    }
} 
