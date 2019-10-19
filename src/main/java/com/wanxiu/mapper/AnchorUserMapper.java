package com.wanxiu.mapper;

import com.wanxiu.dto.AnchorInfoDTO;
import com.wanxiu.entity.LiveUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel="spring")
public interface AnchorUserMapper {
    @Mappings({
            @Mapping(source = "liveUser.id",target = "userId"),
            @Mapping(source = "age",target = "age"),
            @Mapping(source = "level",target = "level")
    })
    public AnchorInfoDTO getUserInfo (LiveUser liveUser, String age, String level);
}
