package com.wanxiu.dto.res;

import com.wanxiu.common.Common;
import com.wanxiu.common.ServiceRes;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResEntity<T> {
    private String result;

    private String errmsg;

    private Object detail;

    public ResEntity(Common.RESULT result){
        this.result=result.getCode();
        this.errmsg=result.getErrmsg();
    }

    public ResEntity(Common.RESULT result, Object detail) {
        this.result = result.getCode();
        this.errmsg = result.getErrmsg();
        this.detail = detail;
    }

    public ResEntity(Common.RESULT result,String errmsg, Object detail) {
        this.result = result.getCode();
        this.errmsg = errmsg;
        this.detail = detail;
    }

    public ResEntity(String result,String errmsg){
        this.result=result;
        this.errmsg=errmsg;
    }

    public ResEntity(String result,String errmsg, T detail){
        this.result=result;
        this.errmsg=errmsg;
        this.detail=detail;
    }

    public ResEntity(ServiceRes serviceRes){
        this.result=String.valueOf(serviceRes.getCode());
        this.errmsg=serviceRes.getMessage();
        this.detail=serviceRes.getData();
    }
}
