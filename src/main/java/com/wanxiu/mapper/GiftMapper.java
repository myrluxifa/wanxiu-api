package com.wanxiu.mapper;

import com.wanxiu.dto.GIftWithComboDTO;
import com.wanxiu.dto.GiftDTO;
import com.wanxiu.entity.LiveGift;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

@Mapper(componentModel="spring")
public interface GiftMapper {

    @Mappings({})
    public GiftDTO getGift (LiveGift liveGift);

    @Mappings({})
    GIftWithComboDTO getGiftWithCombo (LiveGift liveGift);
}
