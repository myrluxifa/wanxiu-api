package com.wanxiu.service.impl;

import com.wanxiu.common.Common;
import com.wanxiu.entity.LiveSysLevel;
import com.wanxiu.entity.LiveUser;
import com.wanxiu.repository.LiveSysLevelRepository;
import com.wanxiu.repository.LiveUserRepository;
import com.wanxiu.service.LevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.OpenOption;
import java.util.Optional;

@Component
public class LevelServiceImpl implements LevelService {

    @Autowired
    private LiveUserRepository liveUserRepository;

    @Autowired
    private LiveSysLevelRepository levelRepository;

    @Override
    public void addExp(int coin, String userId,int showId) {
        Optional<LiveUser> optionalLiveUser =liveUserRepository.findById(userId);
        if(optionalLiveUser.isPresent()){
            LiveUser liveUser=optionalLiveUser.get();
            Integer exp=liveUser.getExperience();
            exp=exp+(coin* Common.EXP_COIN_RATIO);
            Integer maxEx=levelRepository.getMaxEx();
            if(exp>maxEx) exp=maxEx;

            LiveSysLevel liveSysLevel=levelRepository.getLevelByEx(exp);
            liveUser.setExperience(exp);
            liveUser.setLevel(liveSysLevel.getId());
            liveUserRepository.save(liveUser);

            LiveUser anchor=liveUserRepository.findByShowId(showId);
            int anchor_exp=anchor.getExperience();
            anchor_exp=anchor_exp+(coin*Common.EXP_COIN_RATIO);
            if(anchor_exp>maxEx) anchor_exp=maxEx;
            LiveSysLevel anchorLevel=levelRepository.getLevelByEx(anchor_exp);
            anchor.setLevel(anchorLevel.getId());
            anchor.setExperience(anchor_exp);
        }

    }
}
