package com.wanxiu.dto;

import lombok.Data;

@Data
public class HongBaoDTO {
    private String hongbaoKey;

    public HongBaoDTO() {
    }

    public HongBaoDTO(String hongbaoKey) {
        this.hongbaoKey = hongbaoKey;
    }
}
