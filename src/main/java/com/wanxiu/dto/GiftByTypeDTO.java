package com.wanxiu.dto;

import lombok.Data;

import java.util.List;

@Data
public class GiftByTypeDTO {
    private String type;

    private List<GIftWithComboDTO> giftDTOList;

    public GiftByTypeDTO() {
    }

    public GiftByTypeDTO(String type, List<GIftWithComboDTO> giftDTOList) {
        this.type = type;
        this.giftDTOList = giftDTOList;
    }
}
