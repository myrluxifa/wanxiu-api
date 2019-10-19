package com.wanxiu.api;

import com.google.gson.Gson;
import com.wanxiu.common.Common;
import com.wanxiu.config.TencentSMSConfig;
import com.wanxiu.config.WechatConfig;
import com.wanxiu.dto.AccessTokenDTO;
import com.wanxiu.dto.UserInfoDTO;
import com.wanxiu.dto.WechatResUserInfoDTO;
import com.wanxiu.dto.res.ResEntity;
import com.wanxiu.entity.LiveFans;
import com.wanxiu.entity.LiveUser;
import com.wanxiu.redis.RedisServer;
import com.wanxiu.repository.LiveFansRepository;
import com.wanxiu.repository.LiveUserGuardianRepository;
import com.wanxiu.repository.LiveUserRepository;
import com.wanxiu.service.LiveUserService;
import com.wanxiu.util.NumUtil;
import com.wanxiu.util.SMSUtil;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@Api(value="liveUserAPI",tags={"用户"})
public class LiveUserAPI {

    @Autowired
    private LiveUserService liveUserService;

    @Autowired
    private RedisServer redisServer;

    @Autowired
    private TencentSMSConfig tencentSMSConfig;

    @Autowired
    private WechatConfig wechatConfig;

    @Autowired
    private Gson gson;

    @Autowired
    private LiveUserRepository userRepository;

    @Autowired
    private LiveUserGuardianRepository liveUserGuardianRepository;

    @Autowired
    private LiveFansRepository liveFansRepository;

    @Autowired
    private LiveFansRepository fansRepository;

    @PostMapping("api/user/update")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "id",value="id",required = true),
            @ApiImplicitParam(paramType = "query",name = "sex",value="0女1男"),
            @ApiImplicitParam(paramType = "query",name = "birthday",value="1990-01-01"),
            @ApiImplicitParam(paramType = "query",name = "signature",value="个性签名"),
            @ApiImplicitParam(paramType = "query",name = "nickname",value="昵称"),
            @ApiImplicitParam(paramType = "query",name = "headPortrait",value="头像")
    })
    public ResEntity<Object> update(String id, String sex, String birthday, String signature, String nickname, String headPortrait) {

        Optional<LiveUser> userOpt = userRepository.findById(id);

        LiveUser user = userOpt.get();
        if(StringUtils.isNotBlank(sex)) {
            user.setSex(Integer.valueOf(sex));
        }

        if(StringUtils.isNotBlank(birthday)) {
            user.setBirthday(birthday);
        }

        if (StringUtils.isNotBlank(signature)) {
            user.setSignature(signature);
        }

        if (StringUtils.isNotBlank(nickname)) {
            user.setNickname(nickname);
        }

        if (StringUtils.isNotBlank(headPortrait)) {
            user.setHeadPortrait(headPortrait);
        }
        UserInfoDTO userInfoDTO = liveUserService.login(userRepository.save(user).getUserName());
        return new ResEntity<Object>(Common.RESULT.SUCCESS, userInfoDTO);
    }

    @RequestMapping(value = "/sendSMS",method = RequestMethod.POST)
    @ApiOperation(value = "发送短信")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "phone",value="电话",required = true)
    })
    public ResEntity<UserInfoDTO> sendSMS(String phone){
        int appid=tencentSMSConfig.getAppid();
        String appkey=tencentSMSConfig.getAppkey();
        String code=String.valueOf(NumUtil.randomNum(5));
        SMSUtil.sendSMS(appid,appkey,phone,code);
        redisServer.setCacheValueFotTime(phone,code ,1000*60*5);
        return new ResEntity<>(Common.RESULT.SUCCESS);
    }

    @RequestMapping(value = "/verifySMS",method = RequestMethod.POST)
    @ApiOperation(value = "校验短信")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="phone",value="电话号",required = true),
            @ApiImplicitParam(paramType = "query",name="code",value = "验证码",required = true)
    })
    public ResEntity verifySMS(String phone,String code){
        String _code=(String) redisServer.getCacheValue(phone);
        if(_code.equals(code)) {
            redisServer.delCacheByKey(phone);
            return new ResEntity(Common.RESULT.SUCCESS,"OK");
        }else {
            return new ResEntity(Common.RESULT.FAIL,"NO");
        }
    }


    @RequestMapping(value="/login",method = RequestMethod.POST)
    @ApiOperation(value = "登录or注册")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="phone",value="电话号",required = true)
    })
    public ResEntity<UserInfoDTO> login(String phone){
        boolean ifRegister=liveUserService.check(phone);
        if(ifRegister==true){
            UserInfoDTO userInfoDTO = liveUserService.login(phone);
            return new ResEntity<UserInfoDTO>(Common.RESULT.SUCCESS,userInfoDTO);
        }else{
            UserInfoDTO userInfoDTO=liveUserService.register(phone);
            return new ResEntity<UserInfoDTO>(Common.RESULT.SUCCESS,userInfoDTO);
        }
    }

    @RequestMapping(value = "/getAccessToken",method = RequestMethod.POST)
    @ApiOperation(value = "获得access_token")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "code",value = "code",required = true),
            @ApiImplicitParam(paramType = "query",name = "from",value = "streamer")
    })
    public ResEntity<String> getAccessToken(String code, String from){
        String url;
        if ("streamer".equals(from)) {
            url="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+wechatConfig.getAppid2()
                    +"&secret="+wechatConfig.getSecret2()
                    +"&code="+code
                    +"&grant_type=authorization_code";
        } else {
            url="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+wechatConfig.getAppid()
                    +"&secret="+wechatConfig.getSecret()
                    +"&code="+code
                    +"&grant_type=authorization_code";
        }


        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        RestTemplate client=new RestTemplate();
        HttpEntity<String> formEntity = new HttpEntity<String>(headers);
        AccessTokenDTO accessTokenDTO=gson.fromJson(client.getForObject(url,String.class), AccessTokenDTO.class);
        return new ResEntity(Common.RESULT.SUCCESS,accessTokenDTO)  ;
    }

    @RequestMapping(value = "/refreshToken",method = RequestMethod.POST)
    @ApiOperation("刷新access_token")
    public ResEntity<String> refreshToken(String code,String refreshToken){
        String url="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+wechatConfig.getAppid()
                +"&secret="+wechatConfig.getSecret()
                +"&grant_type=grant_type"+
                "refresh_token="+refreshToken;

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        RestTemplate client=new RestTemplate();
        HttpEntity<String> formEntity = new HttpEntity<String>(headers);
        AccessTokenDTO accessTokenDTO=gson.fromJson(client.getForObject(url,String.class),AccessTokenDTO.class);
        return new ResEntity<>(Common.RESULT.SUCCESS,accessTokenDTO);
    }

    public <T> T getForObject(String url,Class<T> t){
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        RestTemplate client=new RestTemplate();
        HttpEntity<String> formEntity = new HttpEntity<String>(headers);

        ResponseEntity<String> response=client.exchange(url, HttpMethod.GET,formEntity,String.class);
        T dto=gson.fromJson(response.getBody(),t);
        //T dto=gson.fromJson(client.getForObject(url,String.class),t);
        return dto;
    }

    @RequestMapping(value = "/loginByWechat",method = RequestMethod.POST)
    @ApiOperation(value = "微信登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="openid",value="openid",required = true)
    })
    public ResEntity<UserInfoDTO> loginByWechat(String openid,String accessToken){
        boolean ifRegister=liveUserService.checkByWechat(openid);
        if(ifRegister){
            UserInfoDTO userInfoDTO=liveUserService.loginByWeChat(openid);
            return new ResEntity<UserInfoDTO>(Common.RESULT.SUCCESS,userInfoDTO);
        }else{
            String url="https://api.weixin.qq.com/sns/userinfo?access_token="+accessToken+"&openid="+openid;
            WechatResUserInfoDTO wechatResUserInfoDTO=getForObject(url, WechatResUserInfoDTO.class);
            if(wechatResUserInfoDTO.getOpenid().isEmpty()&&wechatResUserInfoDTO.getErrmsg().length()>0){
                return new ResEntity(wechatResUserInfoDTO.getErrcode(),wechatResUserInfoDTO.getErrmsg());
            }

            return new ResEntity<>(Common.RESULT.SUCCESS,liveUserService.registerByWechat(wechatResUserInfoDTO));
        }
    }

    @RequestMapping(value = "/loginByBaofang",method = RequestMethod.POST)
    @ApiOperation(value = "点歌器登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="userName",value="包房号",required = true),
            @ApiImplicitParam(paramType = "query",name="password",value = "密码",required = true)
    })
    public ResEntity<UserInfoDTO> loginByBaofang(String userName,String password){
        boolean ifRegister=liveUserService.check(userName);
        if(ifRegister){
            UserInfoDTO userInfoDTO=liveUserService.login(userName);
            return new ResEntity<UserInfoDTO>(Common.RESULT.SUCCESS,userInfoDTO);
        }else{
            return new ResEntity<>(Common.RESULT.USER_404);
        }
    }


    @RequestMapping(value = "/getUserInfo",method = RequestMethod.POST)
    @ApiOperation(value = "获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="userId",value="用户编号",required = true)
    })
    public ResEntity<UserInfoDTO> getUserInfo(String userId){
        return new ResEntity<UserInfoDTO>(Common.RESULT.SUCCESS,liveUserService.getUserInfo(userId));
    }

    @RequestMapping(value = "/api/user/streamer/info",method = RequestMethod.POST)
    @ApiOperation(value = "获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="showId",value="主播房间号",required = true)
    })
    public ResEntity<UserInfoDTO> getUserInfoByShowId(Integer showId){
        return new ResEntity<UserInfoDTO>(Common.RESULT.SUCCESS, userRepository.findByShowId(showId));
    }

    @ApiOperation(value = "绑定微信")
    @RequestMapping(value = "/bindWechat",method = RequestMethod.POST)
    public ResEntity<UserInfoDTO> bindWechat(String openId,String userId){
        return liveUserService.bindWechat(openId,userId);
    }

    @ApiOperation(value = "手机号绑定")
    @RequestMapping(value = "/bindPhone",method = RequestMethod.POST)
    public ResEntity<UserInfoDTO> bindPhone(String phone,String userId){
        return liveUserService.bindPhone(phone,userId);
    }


    @ApiOperation(value = "名片")
    @RequestMapping(value = "/userCard",method = RequestMethod.POST)
    public ResEntity userCard(String showId,String userId){
        return liveUserService.userCard(showId,userId);
    }

    @ApiOperation(value = "主播端名片")
    @RequestMapping(value = "/api/user/card",method = RequestMethod.POST)
    public ResEntity<Object> card(String userId, String targetId){
        Optional<LiveFans> fansOpt = fansRepository.findByFansAndIdol(userId, targetId);
        Optional<LiveUser> userOpt = userRepository.findById(targetId);
        ;
        Map<String, String> result = new HashMap<>();
        result.put("signature", userOpt.get().getSignature());
        result.put("follow", fansOpt.isPresent() + "");
        return new ResEntity(Common.RESULT.SUCCESS, result);
    }


    @ApiOperation(value = "是否是守护是否关注")
    @RequestMapping(value = "/ifGuardAndAttention",method = RequestMethod.POST)
    public ResEntity ifGuardAndAttention(String showId,String userId){
        return liveUserService.ifAttentionAndGuard(showId,userId);
    }

    @ApiOperation(value = "上播")
    @PostMapping("/api/user/stream/start")
    public ResEntity<Object> startStream(String userId) {

        Optional<LiveUser> userOpt = userRepository.findById(userId);
        userOpt.get().setStreaming(1);
        userOpt.get().setLastShowTime(new Date());
        userRepository.save(userOpt.get());

        return new ResEntity<>(Common.RESULT.SUCCESS);
    }

    @ApiOperation(value = "下播")
    @PostMapping("/api/user/stream/stop")
    public ResEntity<Object> stopStream(String userId) {

        Optional<LiveUser> userOpt = userRepository.findById(userId);
        userOpt.get().setStreaming(0);
        userOpt.get().setLastShowTime(new Date());
        userRepository.save(userOpt.get());

        return new ResEntity<>(Common.RESULT.SUCCESS);
    }
}
