package com.wanxiu.mapper;

import com.wanxiu.dto.UserInfoDTO;
import com.wanxiu.entity.LiveUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel="spring")
public interface LiveUserMapper {

    @Mappings({
            @Mapping(source = "liveUser.id",target = "userId"),
            @Mapping(source = "age",target = "age"),
            @Mapping(source = "level",target = "level"),
            @Mapping(source = "attentionCnt",target = "attentionCnt"),
            @Mapping(source = "coins", target = "coins")
    })
    public UserInfoDTO getUserInfo (LiveUser liveUser,String age,String level,int attentionCnt,int coins);
}
