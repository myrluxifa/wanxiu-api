package com.wanxiu.mapper;

import com.wanxiu.dto.AttentionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel="spring")
public interface AttentionMapper {

    @Mappings({
            @Mapping(source = "level",target = "level"),
            @Mapping(source = "liveStatus",target = "liveStatus")
    })
    AttentionDTO getAttention (String idol, String level, String liveStatus,String nickname,String headPortrait,String signature,String sex,String showId,String lastShowTime);
}
