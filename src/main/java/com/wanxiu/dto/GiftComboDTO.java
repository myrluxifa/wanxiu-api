package com.wanxiu.dto;

import lombok.Data;

@Data
public class GiftComboDTO {
    private String num;

    private String svgaUrl;

    public GiftComboDTO() {
    }

    public GiftComboDTO(String num, String svgaUrl) {
        this.num = num;
        this.svgaUrl = svgaUrl;
    }
}
