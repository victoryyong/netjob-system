package com.thsword.netjob.dao;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class JedisFactory implements FactoryBean<JedisCluster>, InitializingBean {

    private JedisCluster jedisCluster;
    private Integer timeout;
    private Integer soTimeout;
    private Integer maxRedirections;
    private String redisClusterConnection;
    private GenericObjectPoolConfig genericObjectPoolConfig;
    private Pattern p = Pattern.compile("^.+[:]\\d{1,5}\\s*$");
    private String password;

    public JedisFactory() {
    }

    public JedisCluster getObject() {
        return this.jedisCluster;
    }

    public Class<? extends JedisCluster> getObjectType() {
        return this.jedisCluster != null?this.jedisCluster.getClass():JedisCluster.class;
    }

    public boolean isSingleton() {
        return true;
    }

    private Set<HostAndPort> parseHostAndPort() throws Exception {
        try {
            Set<HostAndPort> haps = new HashSet<HostAndPort>();
            for(String hostAndPort : redisClusterConnection.split(",") ) {
                boolean isIpPort = this.p.matcher(hostAndPort).matches();
                if(!isIpPort) {
                    throw new IllegalArgumentException("ip 或 port 不合法");
                }
                String[] ipAndPort = hostAndPort.split(":");
                HostAndPort hap = new HostAndPort(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
                haps.add(hap);
            }
            return haps;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("解析 jedis 配置文件失败", e);
        }
    }

    public void afterPropertiesSet() throws Exception {
        Set<HostAndPort> haps = this.parseHostAndPort();
        this.jedisCluster = new JedisCluster(haps, this.timeout, this.soTimeout.intValue(), this.maxRedirections, this.password, this.genericObjectPoolConfig);
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public void setMaxRedirections(int maxRedirections) {
        this.maxRedirections = maxRedirections;
    }

    public void setGenericObjectPoolConfig(GenericObjectPoolConfig genericObjectPoolConfig) {
        this.genericObjectPoolConfig = genericObjectPoolConfig;
    }

    public void setRedisClusterConnection(String redisClusterConnection) {
        this.redisClusterConnection = redisClusterConnection;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
