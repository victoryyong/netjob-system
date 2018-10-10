package com.thsword.netjob.util.limit;
/**
 * 频率限制器
 * @author RENYI856
 */
public interface RateLimiter {
    
    boolean support(String url, String userID, String ip);
    
    boolean access(String url, String userID, String ip);

}
