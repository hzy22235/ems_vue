package com.baizhi.cache;

import com.baizhi.utils.ApplicationContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.locks.ReadWriteLock;

@Slf4j
public class RedisCache implements Cache {
    private String id;//拿到的对象

    public RedisCache(String id){
        log.info("当前缓存id:[{}]---------------------",id);
        this.id=id;
    }
    @Override
    public ReadWriteLock getReadWriteLock() {
        return null;
    }

    @Override
    public String getId() {

       return this.id;
    }

    @Override//放入缓存
    public void putObject(Object key, Object value) {
        log.info("放入缓存的key:[{}]。",key.toString());
        log.info("放入缓存的value:[{}]。",value.toString());
        getRedisTemplate().opsForHash().put(id,key.toString(),value);

    }

    @Override//获取缓存
    public Object getObject(Object key) {
        log.info("获取的缓存key:[{}]",key);
        return getRedisTemplate().opsForHash().get(id,key.toString());

    }

    @Override//删除缓存
    public Object removeObject(Object o) {
        return null;
    }

    @Override
    public void clear() {
        log.info("清除缓存");
        getRedisTemplate().delete(id);
    }

    @Override
    public int getSize() {
        return  getRedisTemplate().opsForHash().size(id).intValue();
    }


    public RedisTemplate getRedisTemplate(){//统一通过建立的工具类获取redistemplate的句柄
       RedisTemplate redisTemplate= (RedisTemplate) ApplicationContextUtils.getBean("redisTemplate");
       redisTemplate.setKeySerializer(new StringRedisSerializer());//调整redis的序列化策略为string的序列化方式
       redisTemplate.setHashKeySerializer(new StringRedisSerializer());
       return redisTemplate;
    }
}
