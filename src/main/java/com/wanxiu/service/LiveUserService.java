package com.wanxiu.service;

import com.wanxiu.dto.UserInfoDTO;
import com.wanxiu.dto.WechatResUserInfoDTO;
import com.wanxiu.dto.res.ResEntity;
import com.wanxiu.entity.LiveUser;
import org.springframework.stereotype.Service;

@Service
public interface LiveUserService {

     UserInfoDTO register(String phone);

     UserInfoDTO login(String phone);

     boolean check(String phone);

     boolean checkByWechat(String openid);

    UserInfoDTO loginByWeChat(String openId);

    UserInfoDTO registerByWechat(WechatResUserInfoDTO wechatResUserInfoDTO);

    UserInfoDTO getUserInfo(String userId);

    ResEntity bindWechat(String openId, String userId);

    ResEntity bindPhone(String phone,String userId);

    ResEntity userCard(String showId,String userId);

    ResEntity ifAttentionAndGuard(String showId,String userId);
}
