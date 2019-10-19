package com.wanxiu.service.impl;

import com.wanxiu.dto.BannerDTO;
import com.wanxiu.dto.RoomTypeDTO;
import com.wanxiu.entity.LiveBanner;
import com.wanxiu.entity.LiveRoomType;
import com.wanxiu.entity.LiveSysAppVersion;
import com.wanxiu.mapper.HomeMapper;
import com.wanxiu.repository.LiveBannerRepository;
import com.wanxiu.repository.LiveRoomTypeRepository;
import com.wanxiu.repository.LiveSysAppVersionRepository;
import com.wanxiu.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HomeServiceImpl implements HomeService {

    @Autowired
    private LiveBannerRepository liveBannerRepository;

    @Autowired
    private HomeMapper homeMapper;

    @Autowired
    private LiveSysAppVersionRepository liveSysAppVersionRepository;

    @Autowired
    private LiveRoomTypeRepository liveRoomTypeRepository;

    @Override
    public List<BannerDTO> getBanner(String roomType) {
        List<LiveBanner> list=liveBannerRepository.findByStatusAndRoomTypeOrderBySortNum(0,roomType);
        return homeMapper.getBanner(list);
    }

    public List<RoomTypeDTO> getRoomTypeList(){
        List<LiveRoomType>  liveRoomTypes=liveRoomTypeRepository.findByStatusOrderByOrder(0);
        List<RoomTypeDTO> roomTypeDTOS=new ArrayList<>();
        liveRoomTypes.forEach(x->roomTypeDTOS.add(homeMapper.getRoomType(x)));
        return roomTypeDTOS;
    }

    public List<LiveSysAppVersion> getLiveSysAppVersions(String type,String versionNum){
        List<LiveSysAppVersion> liveSysAppVersions=liveSysAppVersionRepository.findByTypeAndVersion(Integer.valueOf(type),versionNum);
        return liveSysAppVersions;
    }
}
