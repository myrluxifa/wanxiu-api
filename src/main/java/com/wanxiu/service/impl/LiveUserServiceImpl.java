package com.wanxiu.service.impl;

import com.wanxiu.api.RankAPI;
import com.wanxiu.common.AccountParam;
import com.wanxiu.common.Common;
import com.wanxiu.dto.IfAttentionAndGuard;
import com.wanxiu.dto.UserCardDTO;
import com.wanxiu.dto.UserInfoDTO;
import com.wanxiu.dto.WechatResUserInfoDTO;
import com.wanxiu.dto.res.ResEntity;
import com.wanxiu.entity.*;
import com.wanxiu.mapper.LiveUserMapper;
import com.wanxiu.repository.*;
import com.wanxiu.service.LiveUserService;
import com.wanxiu.util.NumUtil;
import com.wanxiu.util.PagePlugin;
import com.wanxiu.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class LiveUserServiceImpl implements LiveUserService {

    @Autowired
    private LiveUserMapper liveUserMapper;

    @Autowired
    private LiveUserRepository liveUserRepository;

    @Autowired
    private LiveSysLevelRepository liveSysLevelRepository;

    @Autowired
    private LiveAccountRepository liveAccountRepository;

    @Autowired
    private LiveFansRepository liveFansRepository;

    @Autowired
    private LiveCashAccountRepository liveCashAccountRepository;

    @Autowired
    private LiveCoinsLogRepository liveCoinsLogRepository;

    @Autowired
    private LiveGiftRankRepository liveGiftRankRepository;

    @Autowired
    private LiveUserGuardianRepository liveUserGuardianRepository;

    public UserInfoDTO login(String phone) {

        Optional<LiveUser> optionalLiveUser =liveUserRepository.findByUserName(phone);
        if(optionalLiveUser.isPresent()){
            LiveUser user=optionalLiveUser.get();
            return getUserInfoByUser(user);
        }else{
            return new UserInfoDTO();
        }
    }

    public UserInfoDTO getUserInfo(String userId){
        Optional<LiveUser> optionalLiveUser=liveUserRepository.findById(userId);
        if(optionalLiveUser.isPresent()){
            LiveUser user=optionalLiveUser.get();
            return getUserInfoByUser(user);
        }else{
            return new UserInfoDTO();
        }
    }

    public UserInfoDTO  getUserInfoByUser(LiveUser user){
            String age="-";
            if(!"-".equals(user.getBirthday())){
                age=TimeUtil.comouteAge(user.getBirthday());
            }

            LiveSysLevel liveSysLevel=liveSysLevelRepository.getLevelByEx(user.getExperience());
            String levelName=String.valueOf(liveSysLevel.getSort());
            int attentionCnt=liveFansRepository.countByFans(user.getId());
            Optional<LiveAccount> optionalLiveAccount=liveAccountRepository.findByUserId(user.getId());
            int coins=0;
            if(optionalLiveAccount.isPresent()){
                coins=optionalLiveAccount.get().getCoins();
            }
            return liveUserMapper.getUserInfo(user,age,levelName,attentionCnt,coins);
    }


    public UserInfoDTO loginByWeChat(String openId){
        Optional<LiveUser> optionalLiveUser=liveUserRepository.findByOpenId(openId);
        if(optionalLiveUser.isPresent()){
            LiveUser user=optionalLiveUser.get();
            return getUserInfoByUser(user);
        }else{
            return new UserInfoDTO();
        }
    }

    public ResEntity bindWechat(String openId, String userId){
        Optional<LiveUser> optionalLiveUser=liveUserRepository.findById(userId);
        if(optionalLiveUser.isPresent()){
            LiveUser liveUser=optionalLiveUser.get();
            if(StringUtils.isNotBlank(liveUser.getOpenId())){
                return new ResEntity(Common.RESULT.WECHAT_BOUND_409);
            }

            if(liveUserRepository.countByOpenId(openId)>0){
                return new ResEntity(Common.RESULT.WECHAT_BOUND_409);
            }

            liveUser.setOpenId(openId);
            liveUserRepository.save(liveUser);
            UserInfoDTO userInfoDTO=getUserInfoByUser(liveUser);
            return new ResEntity(Common.RESULT.SUCCESS,userInfoDTO);
        }else{
            return new ResEntity(Common.RESULT.USER_404);
        }
    }


    public ResEntity bindPhone(String phone,String userId){
        Optional<LiveUser> optionalLiveUser=liveUserRepository.findById(userId);
        if(optionalLiveUser.isPresent()){
            LiveUser liveUser=optionalLiveUser.get();
            if(StringUtils.isNotBlank(liveUser.getUserName())){
                return new ResEntity(Common.RESULT.PHONE_BOUND_409);
            }
            if(liveUserRepository.countByUserName(phone)>0){
                return new ResEntity(Common.RESULT.PHONE_BOUND_409);
            }

            liveUser.setUserName(phone);
            liveUserRepository.save(liveUser);
            UserInfoDTO userInfoDTO=getUserInfoByUser(liveUser);
            return new ResEntity(Common.RESULT.SUCCESS,userInfoDTO);
        }else{
            return new ResEntity(Common.RESULT.USER_404);
        }
    }

    @Transactional
    public UserInfoDTO register(String phone){
        LiveUser liveUser= new LiveUser();
        liveUser.setUserName(phone);
        liveUser.setPart("01");
        liveUser.setShowId(NumUtil.randomNum(6));
        liveUser.setStatus(0);
        liveUser.setApprovalStatus(0);
        liveUser.setExperience(1);
        liveUser.setLevel("1");
        liveUser.setBirthday("-");
        liveUser.setFans(0);
        liveUser.setPraise(0);
        liveUser.setStreaming(0);
        LiveUser user=liveUserRepository.save(liveUser);
        if(user!=null){
            liveAccountRepository.save(new LiveAccount(user.getId(),0,0,0,0,0,0,new Date(),new Date(),""));
            liveCashAccountRepository.save(new LiveCashAccount(user.getId(),new BigDecimal(0.00),new Date()));
            return getUserInfoByUser(user);
        }else{
            return new UserInfoDTO();
        }
    }

    @Transactional
    public UserInfoDTO registerByWechat(WechatResUserInfoDTO wechatResUserInfoDTO){
        LiveUser liveUser= new LiveUser();
        liveUser.setUserName("");
        liveUser.setPart("01");
        liveUser.setShowId(NumUtil.randomNum(6));
        liveUser.setStatus(0);
        liveUser.setApprovalStatus(0);
        liveUser.setExperience(1);
        liveUser.setBirthday("-");
        liveUser.setLevel("1");
        liveUser.setPraise(0);
        liveUser.setSex(Integer.valueOf(wechatResUserInfoDTO.getSex()));
        liveUser.setHeadPortrait(wechatResUserInfoDTO.getHeadimgurl());
        liveUser.setFans(0);
        liveUser.setPraise(0);
        liveUser.setStreaming(0);
        try {
            liveUser.setNickname(new String(wechatResUserInfoDTO.getNickname().getBytes("ISO-8859-1"), "UTF-8"));
        }catch (Exception e){
            liveUser.setNickname("已重置");
        }
        liveUser.setOpenId(wechatResUserInfoDTO.getOpenid());
        LiveUser user=liveUserRepository.save(liveUser);
        if(user!=null){
            liveAccountRepository.save(new LiveAccount(user.getId(),0,0,0,0,0,0,new Date(),new Date(),""));
            liveCashAccountRepository.save(new LiveCashAccount(user.getId(),new BigDecimal(0.00),new Date()));
            return getUserInfoByUser(user);
        }else{
            return new UserInfoDTO();
        }
    }

    public boolean check(String phone){
        Optional<LiveUser> optional=liveUserRepository.findByUserName(phone);
        return optional.isPresent();
    }

    @Override
    public boolean checkByWechat(String openid) {
        Optional<LiveUser> optional=liveUserRepository.findByOpenId(openid);
        return optional.isPresent();
    }

    public ResEntity ifAttentionAndGuard(String showId,String userId){
        LiveUser liveUser=liveUserRepository.findByShowId(Integer.valueOf(showId));
        Optional<LiveFans> liveFansOptional=liveFansRepository.findByFansAndIdol(userId,liveUser.getId());

        String ifAttention="false";
        if(liveFansOptional.isPresent()){
            ifAttention="true";
        }
        Optional<LiveUserGuardian> optionalLiveUserGuardian=liveUserGuardianRepository.findByUserIdAndTargetIdAndEndTimeGreaterThan(userId,Integer.valueOf(showId),new Date());
        String ifGuard="false";
        String guardType="";
        if(optionalLiveUserGuardian.isPresent()){
            ifGuard="true";
            if(optionalLiveUserGuardian.get().getProjectId().equals("1")){
                guardType="M";
            }else{
                guardType="Y";
            }
        }
        return new ResEntity(Common.RESULT.SUCCESS,new IfAttentionAndGuard(ifAttention,ifGuard,guardType));
    }


    public ResEntity userCard(String showId,String userId){
        LiveUser liveUser=liveUserRepository.findByShowId(Integer.valueOf(showId));
        //关注数
        int attentionCnt=liveFansRepository.countByFans(liveUser.getId());
        //粉丝数
        int fansCnt=liveFansRepository.countByIdol(liveUser.getId());

        //送出礼物
        int giveCoins=liveCoinsLogRepository.sumByTypeAndUserId(AccountParam.TYPE_3,liveUser.getId());
        //收到礼物
        int reCoins=liveCoinsLogRepository.sumByTypeAndUserId(AccountParam.TYPE_4,liveUser.getId());

        LiveSysLevel liveSysLevel=liveSysLevelRepository.getLevelByEx(liveUser.getExperience());
        String levelName=String.valueOf(liveSysLevel.getSort());

        Optional<LiveFans> optionalLiveFans=liveFansRepository.findByFansAndIdol(userId,liveUser.getId());
        String ifAttention="false";
        if(optionalLiveFans.isPresent()){
            ifAttention="true";

        }

        List<LiveGiftRank> rightGiftRankList=liveGiftRankRepository.findByShowIdAndUserTypeAndTypeAndCycleIdOrderByCoinsDesc(PagePlugin.pagePlugin(1,1),Integer.valueOf(showId),"G","M",RankAPI.getCycleId("M"));


        String rightHP="";
        if(rightGiftRankList.size()>0){
            Optional<LiveUser> rightUser=liveUserRepository.findById(rightGiftRankList.get(0).getUserId());
            if(rightUser.isPresent()){
                rightHP=rightUser.get().getHeadPortrait();
            }
        }


        List<LiveGiftRank> liveGiftRankList=liveGiftRankRepository.findByShowIdAndUserTypeAndTypeAndCycleIdOrderByCoinsDesc(PagePlugin.pagePlugin(1,1),Integer.valueOf(showId),"N","M",RankAPI.getCycleId("M"));

        String leftHP="";
        if(liveGiftRankList.size()>0){
            Optional<LiveUser> leftUser=liveUserRepository.findById(liveGiftRankList.get(0).getUserId());
            if(leftUser.isPresent()){
                leftHP=leftUser.get().getHeadPortrait();
            }
        }

        UserCardDTO userCardDTO=new UserCardDTO(liveUser,
                levelName,
                leftHP,
                rightHP,
                ifAttention,
                String.valueOf(attentionCnt),
                String.valueOf(fansCnt),
                String.valueOf(reCoins),
                String.valueOf(giveCoins)
                );
        return new ResEntity(Common.RESULT.SUCCESS,userCardDTO);
    }
}
