package com.wanxiu.mapper;

import com.wanxiu.dto.BannerDTO;
import com.wanxiu.dto.RoomTypeDTO;
import com.wanxiu.entity.LiveBanner;
import com.wanxiu.entity.LiveRoomType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HomeMapper {
    
    @Mappings({})
    List<BannerDTO> getBanner(List<LiveBanner> liveBannerArray);

    @Mappings({})
    RoomTypeDTO getRoomType(LiveRoomType liveRoomType);
}
