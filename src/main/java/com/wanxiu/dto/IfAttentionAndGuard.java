package com.wanxiu.dto;

import lombok.Data;

@Data
public class IfAttentionAndGuard {

    private String ifAttention;

    private String ifGuard;

    private String guardType;

    public IfAttentionAndGuard(){

    }

    public IfAttentionAndGuard(String ifAttention,String ifGuard,String guardType) {
        this.ifAttention = ifAttention;
        this.ifGuard=ifGuard;
        this.guardType=guardType;
    }
}
