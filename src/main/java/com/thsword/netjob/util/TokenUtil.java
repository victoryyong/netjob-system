package com.thsword.netjob.util;

import java.util.Date;

import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.Token;
import com.thsword.utils.object.JWTUtil;
import com.thsword.utils.object.UUIDUtil;

public class TokenUtil {
	 /**
     * 创建token
    * @Title: createJWT 
    * @Description: 
    * @param @param dto
    * @param @return
    * @param @throws Exception
    * @return 
    * @throws
     */
    public static String createJWT(Token dto) throws Exception {
        return JWTUtil.createJWT(dto.getId(), dto.getIssuer(),dto.getSubject(), dto.getExpires(),dto.getSecretKey());
    }
    /**
     * 添加新token
    
     * @Description:TODO
    
     * @param userId
     * @param userName
     * @param subject
     * @return
     * @throws Exception
    
     * Token
    
     * @exception:
    
     * @author: yong
    
     * @time:2018年5月10日 下午9:51:47
     */
    public static Token getToken(String userId,String userName,String subject,String secretKey) throws Exception{
		Token token = new Token();
		token.setUserId(userId);
		token.setUsername(userName);
		token.setId(UUIDUtil.get32UUID());
		token.setSecretKey(secretKey);
		token.setIssuer(Global.JWT_ISSUERT);
		token.setSubject(subject);
		if(subject.equals(Global.JWT_SUBJECT_ADMIN)){
			token.setExpires(new Date(new Date().getTime()+Global.JWT_ADMIN_EXPIRESS_TIME));
			token.setSessionDate(new Date(new Date().getTime()+Global.JWT_ADMIN_SESSION_TIME_OUT));
		}else if(subject.equals(Global.JWT_SUBJECT_APP)){
			token.setExpires(new Date(new Date().getTime()+Global.JWT_APP_EXPIRESS_TIME));
			token.setSessionDate(new Date(new Date().getTime()+Global.JWT_APP_SESSION_TIME_OUT));
		}
		token.setExtendDate(new Date(new Date().getTime()+Global.JWT_EXTEND_TIME));
		token.setAccess_token(createJWT(token));
		token.setCreateDate(new Date());
		token.setCreateBy(userId);
		return token;
	}
}
