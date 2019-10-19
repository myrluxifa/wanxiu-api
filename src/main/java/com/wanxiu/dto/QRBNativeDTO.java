package com.wanxiu.dto;

import lombok.Data;

@Data
public class QRBNativeDTO {
    private String url;
    private String orderId;

    public QRBNativeDTO(){

    }

    public QRBNativeDTO(String url, String orderId) {
        this.url = url;
        this.orderId = orderId;
    }
}
