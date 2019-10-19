package com.wanxiu.redis;

import com.wanxiu.common.Common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisServer {
    @Autowired
    private RedisTemplate redisTemplate;

    public Object getCacheValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Common.RESULT setCacheValue(String key, Object value) {
        try{
            redisTemplate.opsForValue().set(key,value);
        }catch (Exception e){
            return Common.RESULT.FAIL;
        }
        return Common.RESULT.SUCCESS;
    }

    public Common.RESULT setCacheValueFotTime(String key, String value, long time) {
        try{
            redisTemplate.opsForValue().set(key,value,time, TimeUnit.SECONDS);
        }catch (Exception e){
            return Common.RESULT.FAIL;
        }
        return Common.RESULT.SUCCESS;
    }


    public Common.RESULT delCacheByKey(String key) {
        try{
            redisTemplate.opsForValue().getOperations().delete(key);
            redisTemplate.opsForHash().delete("");
        }catch (Exception e){
            return Common.RESULT.FAIL;
        }
        return Common.RESULT.SUCCESS;
    }
}
