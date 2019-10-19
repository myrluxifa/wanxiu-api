package com.wanxiu.dto;

import lombok.Data;

@Data
public class ClientCoinsLogDTO {
    private String coins;

    private String type;

    private String createTime;

    public ClientCoinsLogDTO() {
    }

    public ClientCoinsLogDTO(String coins, String type, String createTime) {
        this.coins = coins;
        this.type = type;
        this.createTime = createTime;
    }
}
