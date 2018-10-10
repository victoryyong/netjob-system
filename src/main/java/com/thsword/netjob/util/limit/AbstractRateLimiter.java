package com.thsword.netjob.util.limit;

import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractRateLimiter implements RateLimiter{
    private static final long HALF_DAY = 12 * 3600 * 1000;
    private final static HashMap<String, TicketBucket> ticketBucketMap = new HashMap<>();

    private final static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final static Lock readLock = rwl.readLock();
    private final static Lock writeLock = rwl.writeLock();
    
    @Override
    public boolean access(String url, String userID, String ip) {
        String bulidKey = bulidKey(url, userID, ip);
        readLock.lock();
        TicketBucket ticketBucket = ticketBucketMap.get(bulidKey);
        if (ticketBucket == null) {
            readLock.unlock();
            writeLock.lock();
            try{
                if(ticketBucketMap.get(bulidKey) == null){
                   
                    ticketBucket = new TicketBucket(getTimeSelection(url, userID, ip), getTimes(url, userID, ip));

                    ticketBucketMap.put(bulidKey, ticketBucket);
                }
                ticketBucket = ticketBucketMap.get(bulidKey);
                this.cleanBucketMap(ticketBucketMap);
                readLock.lock();
            }finally{
                writeLock.unlock();
            }
        }
        try{
            return ticketBucket.getTicket();
        }finally{
            readLock.unlock();
        }
    }
    
    /**
     * 可以做些清理工作
     * @param ticketBucketMap
     */
    protected void cleanBucketMap(HashMap<String, TicketBucket> ticketBucketMap){
        long now = System.currentTimeMillis();
        Set<Entry<String, TicketBucket>> entrySet = ticketBucketMap.entrySet();
        for (Entry<String, TicketBucket> entry : entrySet) {
            TicketBucket ticketBucket = entry.getValue();
            long duration = now - ticketBucket.getLastAccess();
            if(duration > HALF_DAY){
                ticketBucketMap.remove(entry.getKey());
            }
        }
    }
    
    /**
     * 唯一键，针对什么做限制
     * 例如：
     * userID + url  单个用户的单个url做限制
     * url  单个url做限制
     * ip   单个ip做限制
     * userID 单个用户做限制
     */
    protected abstract String bulidKey(String url, String userID, String ip);
    
    protected abstract int getTimeSelection(String url, String userID, String ip);
    
    protected abstract int getTimes(String url, String userID, String ip);
    
}
