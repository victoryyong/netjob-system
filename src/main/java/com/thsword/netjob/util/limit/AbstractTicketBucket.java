package com.thsword.netjob.util.limit;

public abstract class AbstractTicketBucket {
    private int duration;//时间段   毫秒
    private double capacity;   //容量 保存初始设置 
    
    public AbstractTicketBucket(int duration, int times) {
        super();
        this.duration = duration;
        this.setTimes(times);
        this.setLastAccess(System.currentTimeMillis());
        this.capacity = times;
    }

    public synchronized boolean getTicket(){
        this.supplyTimes();
        int times = this.getTimes();
        if(times > 0){
            this.setTimes(times - 1);
            return true;
        }
        return false;
    }
    
    /**
     * 设置获取次数
     */
    protected abstract int getTimes();
    protected abstract void setTimes(int times);
    
    /**
     * 设置获取最后时间戳
     */
    protected abstract long getLastAccess();
    protected abstract void setLastAccess(long lastAccess);
    
    private void supplyTimes(){
        long lastTime = this.getLastAccess();
        int times = this.getTimes();
        long now = System.currentTimeMillis();
        double addTimes = (double) ((now - lastTime) * (double)(capacity/duration)) + times;
        if(addTimes < capacity){
            this.setTimes( (int) Math.round(addTimes) );
        }else{
            this.setTimes( (int) Math.round(capacity) );
        }
        this.setLastAccess(now);
    }
    
    

    

}
