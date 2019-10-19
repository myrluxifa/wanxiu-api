package com.wanxiu.service;

import com.wanxiu.dto.BannerDTO;
import com.wanxiu.dto.RoomTypeDTO;
import com.wanxiu.entity.LiveSysAppVersion;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HomeService {
    List<BannerDTO> getBanner(String roomType);

    List<RoomTypeDTO> getRoomTypeList();

    List<LiveSysAppVersion> getLiveSysAppVersions(String type,String versionNum);
}
