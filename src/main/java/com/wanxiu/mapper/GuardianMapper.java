package com.wanxiu.mapper;

import com.wanxiu.dto.GuardianDTO;
import com.wanxiu.entity.LiveSysGuardian;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

@Mapper(componentModel="spring")
public interface GuardianMapper {


    @Mappings({})
    public GuardianDTO getGuardian(LiveSysGuardian liveSysGuardian);
}
