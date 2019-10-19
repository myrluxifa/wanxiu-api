package com.wanxiu.mapper;

import com.wanxiu.dto.ClientCoinsLogDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

@Mapper(componentModel="spring")
public interface CoinsLogMapper {

    @Mappings({
    })
     ClientCoinsLogDTO getClientCoinsDTO (String coins, String type, String createTime);
}
