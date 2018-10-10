package com.thsword.netjob.util.limit;

import java.util.List;

/**
 * spring配置  springmvc-servlet.xml
 */
public class RateLimiterChain {
    /*private static RateLimiterChain instance = new RateLimiterChain();
    private RateLimiterChain(){
        this.rateLimiters = new ArrayList<>();
        String[] urls = {"/share/getkey"};
        int[] timeSelection = {10000};
        int[] times = {5};
        SimpleUrlRateLimiter rl = new SimpleUrlRateLimiter(urls, timeSelection, times);
        rateLimiters.add(rl);
    }
    
    public static RateLimiterChain getInstance(){
        return instance;
    }*/
    
    private List<RateLimiter> rateLimiters;
    
    /**
     * 
     * @param url
     * @param userID
     * @param ip
     * @return 
     * true  pass
     * false unpass
     */
    public boolean access(String url, String userID, String ip){
        if(this.rateLimiters != null && this.rateLimiters.size() > 0){
            for(RateLimiter limiter : this.rateLimiters){
                if(limiter.support(url, userID, ip)){
                    if(!limiter.access(url, userID, ip)){
                        return false;
                    }else{
                        continue;
                    }
                }
            }
        }
        return true;
    }


    public List<RateLimiter> getRateLimiters() {
        return rateLimiters;
    }


    public void setRateLimiters(List<RateLimiter> rateLimiters) {
        this.rateLimiters = rateLimiters;
    }
    

}
