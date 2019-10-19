package com.wanxiu.dto;

import lombok.Data;

import java.util.List;

@Data
public class GIftWithComboDTO {
    private String id;

    private String iconUrl;

    private String name;

    private String showCoin;

    private String animationUrl;

    private List<GiftComboDTO> giftComboDTOS;
}
