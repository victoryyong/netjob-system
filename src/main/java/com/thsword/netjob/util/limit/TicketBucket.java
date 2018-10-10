package com.thsword.netjob.util.limit;

public class TicketBucket {
    private int duration;//时间段   毫秒
    private volatile int times;//次数      如10 000毫秒内50次调用
    private volatile long lastAccess; //时间戳   毫秒    最后一次使用
    private double capacity;   //容量 保存初始设置 
    
    public TicketBucket(int duration, int times) {
        super();
        this.duration = duration;
        this.times = times;
        this.lastAccess = System.currentTimeMillis();
        this.capacity = times;
    }
    
    public long getLastAccess(){
        return this.lastAccess;
    }

    public synchronized boolean getTicket(){
        this.supplyTimes();
        if(this.times > 0){
            this.times = this.times - 1;
            return true;
        }
        return false;
    }
    
    private void supplyTimes(){
        long lastTime = this.lastAccess;
        long now = System.currentTimeMillis();
        double addTimes = (double) ((now - lastTime) * (double)(capacity/duration)) + this.times;
        if(addTimes < capacity){
            this.times = (int) Math.round(addTimes);
        }else{
            this.times = (int) Math.round(capacity);
        }
        this.lastAccess = now;
    }
    
    

    

}
