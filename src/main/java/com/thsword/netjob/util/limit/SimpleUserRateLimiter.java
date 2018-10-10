package com.thsword.netjob.util.limit;
/**
 * 所有用户所有接口使用同一频率
 */
public class SimpleUserRateLimiter extends AbstractRateLimiter{
    private int timeSelection;
    private int times;

    public SimpleUserRateLimiter(int timeSelection, int times) {
        super();
        this.timeSelection = timeSelection;
        this.times = times;
        this.check();
    }

    @Override
    public boolean support(String url, String userID, String ip) {
        return true;
    }

    @Override
    protected String bulidKey(String url, String userID, String ip) {
        return userID == null? "userID" : userID;
    }

    @Override
    protected int getTimeSelection(String url, String userID, String ip) {
        return timeSelection;
    }

    @Override
    protected int getTimes(String url, String userID, String ip) {
        return times;
    }
    
    private void check(){
        if(this.timeSelection <= 0 || this.times <= 0){
            throw new IllegalArgumentException("timeSelection,times 必须为正");
        }
    }

}
