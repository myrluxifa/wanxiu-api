package com.wanxiu.service.impl;

import com.wanxiu.dto.AnchorInfoDTO;
import com.wanxiu.entity.LiveSysLevel;
import com.wanxiu.entity.LiveUser;
import com.wanxiu.mapper.AnchorUserMapper;
import com.wanxiu.repository.AnchorUserRepository;
import com.wanxiu.repository.LiveSysLevelRepository;
import com.wanxiu.repository.LiveUserRepository;
import com.wanxiu.service.AnchorUserService;
import com.wanxiu.util.NumUtil;
import com.wanxiu.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AnchorUserServiceImpl implements AnchorUserService {

    @Autowired
    private AnchorUserRepository anchorUserRepository;

    @Autowired
    private LiveUserRepository liveUserRepository;

    @Autowired
    private LiveSysLevelRepository liveSysLevelRepository;

    @Autowired
    private AnchorUserMapper anchorUserMapper;

    @Override
    public AnchorInfoDTO anchorLogin(String phone) {
        Optional<LiveUser> optionalLiveUser =anchorUserRepository.findByUserName(phone);
        if(optionalLiveUser.isPresent()){
            LiveUser user=optionalLiveUser.get();
            String age="-";
            if(!user.getBirthday().isEmpty()){
                age= TimeUtil.comouteAge(user.getBirthday());
            }


            LiveSysLevel liveSysLevel=liveSysLevelRepository.getLevelByEx(user.getExperience());
            String levelName=liveSysLevel.getName();
            return anchorUserMapper.getUserInfo(user,age,levelName);
        }else{
            return new AnchorInfoDTO();
        }
    }

    public AnchorInfoDTO register(String phone){
        LiveUser liveUser= new LiveUser();
        liveUser.setUserName(phone);
        liveUser.setPart("03");
        liveUser.setShowId(NumUtil.randomNum(6));
        liveUser.setStatus(0);
        liveUser.setApprovalStatus(0);
        liveUser.setExperience(1);
        liveUser.setBirthday("-");
        LiveUser user=liveUserRepository.save(liveUser);
        if(user!=null){
            LiveSysLevel liveSysLevel=liveSysLevelRepository.getLevelByEx(user.getExperience());
            String levelName=liveSysLevel.getName();
            return anchorUserMapper.getUserInfo(user,"-",levelName);
        }else{
            return new AnchorInfoDTO();
        }
    }
}
