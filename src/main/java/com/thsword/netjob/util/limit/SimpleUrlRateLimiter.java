package com.thsword.netjob.util.limit;
/**
 * 用户的指定url使用指定的频率
 */
public class SimpleUrlRateLimiter extends AbstractRateLimiter{
    private String[] urls;
    private int timeSelection[];
    private int times[];

    public SimpleUrlRateLimiter(String[] urls, int[] timeSelection, int[] times) {
        super();
        this.urls = urls;
        this.timeSelection = timeSelection;
        this.times = times;
        this.check();
    }

    @Override
    public boolean support(String url, String userID, String ip) {
        int index = this.indexOfUrl(url);
        if(index >= 0) return true;
        return false;
    }

    @Override
    protected String bulidKey(String url, String userID, String ip) {
        int index = this.indexOfUrl(url);
        String urlIndex = ":"+urls[index];
        return userID == null ? "userID"+urlIndex : userID+urlIndex;
    }

    @Override
    protected int getTimeSelection(String url, String userID, String ip) {
        int index = this.indexOfUrl(url);
        return timeSelection[index];
    }

    @Override
    protected int getTimes(String url, String userID, String ip) {
        int index = this.indexOfUrl(url);
        return times[index];
    }
    
    private void check(){
        if(this.urls.length != this.timeSelection.length 
                || this.timeSelection.length != this.times.length
                || this.urls.length  != this.times.length){
            throw new IllegalArgumentException("urls,timeSelection,times 长度不一致");
        }
    }
    
    private int indexOfUrl(String url){
        if(urls != null && urls.length > 0){
            for (int i=0; i <urls.length; i++) {
                if (url.indexOf(urls[i]) >= 0) {
                    return i;
                }
            }
        }
        return -1;
    }

}
