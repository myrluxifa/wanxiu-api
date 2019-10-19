package com.wanxiu.dto;

import lombok.Data;

import java.util.List;

@Data
public class AnchorBalanceDTO {
    private String balance;

    private List<CashWithrawalDTO> cashWithrawalDTOList;

    public AnchorBalanceDTO(){

    }

    public AnchorBalanceDTO(String balance,List<CashWithrawalDTO> cashWithrawalDTOList) {
        this.balance = balance;
        this.cashWithrawalDTOList=cashWithrawalDTOList;
    }
}
