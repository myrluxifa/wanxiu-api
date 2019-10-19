package com.wanxiu.dto;

import lombok.Data;

@Data
public class LootHongBaoDTO {

    private String amount;

    private String status;

    public LootHongBaoDTO(){}

    public LootHongBaoDTO(String amount,String status){
        this.amount=amount;
        this.status=status;
    }
}
