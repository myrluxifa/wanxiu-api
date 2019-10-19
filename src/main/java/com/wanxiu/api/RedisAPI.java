package com.wanxiu.api;

import com.wanxiu.redis.RedisServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value="redisAPI",tags={"缓存"})
public class RedisAPI {

    @Autowired
    private RedisServer redisServer;

    @RequestMapping(value = "/setCache",method = RequestMethod.POST)
    @ApiOperation(value = "插入缓存")
    public Object setCache(String key,String value){

        return redisServer.setCacheValue(key,value);
    }

    @RequestMapping(value = "/setCacheForTime",method = RequestMethod.POST)
    @ApiOperation(value = "插入缓存并设置有效时长")
    public Object setCacheForTime(String key,String value,long time){

        return redisServer.setCacheValueFotTime(key,value,time);
    }

    @RequestMapping(value = "/removeCache",method = RequestMethod.POST)
    @ApiOperation(value = "清除缓存")
    public Object removeCache(String key){

        return redisServer.delCacheByKey(key);
    }

    @RequestMapping(value = "/getCache",method = RequestMethod.POST)
    @ApiOperation(value = "获取缓存的值")
    public Object getEntity(String key){
        return redisServer.getCacheValue(key);
    }
}
