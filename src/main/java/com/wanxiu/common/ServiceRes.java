package com.wanxiu.common;

import lombok.Data;

@Data
public class ServiceRes<T> {
    private int code;
    private String message;
    private T data;

    public boolean isFail() {
        if (this.code < 0) {
            return true;
        } else {
            return false;
        }
    }

    public ServiceRes fail(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        return this;
    }

    public ServiceRes fail(int code, String message) {
        this.code = code;
        this.message = message;
        return this;
    }

    public ServiceRes fail(String message) {
        this.code = -100;
        this.message = message;
        return this;
    }

    public ServiceRes success(T data) {
        this.code = 100;
        this.message = "success";
        this.data = data;
        return this;
    }

    public ServiceRes success() {
        this.code = 100;
        this.message = "success";
        return this;
    }

    public ServiceRes() {
    }

    public ServiceRes(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ServiceRes(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
