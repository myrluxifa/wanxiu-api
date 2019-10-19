package com.wanxiu.common;

public class AccountParam {
    public static final String TYPE_0 = "0";
    public static final String TYPE_1 = "1";
    public static final String TYPE_2 = "2";
    public static final String TYPE_3 = "3";
    public static final String TYPE_4 = "4";

    public static final int CHANGE_TYPE_FROM_GIFT = 0; //礼物收入
    public static final int CHANGE_TYPE_NOT_FROM_GIFT = 1; //非礼物收入
    public static final int CHANGE_TYPE_WITHDRAW = 2; //提现
    public static final int CHANGE_TYPE_EXPEND = 3; //消费
    public static final int CHANGE_TYPE_RECHARGE = 4; //充值
    public static final int CHANGE_TYPE_INCOME_MANAGER = 5; //购买守护收入
    public static final int CHANGE_TYPE_EXPEND_MANAGER = 6; //购买守护消费
    public static final int CHANGE_TYPE_EXPEND_GIFT = 7; //礼物消费

    public static final int CHANGE_TYPE_EXPEND_HONGBAO=8;//红包消费
    public static final int CHANGE_TYPE_FROM_HONGBAO=9;//红包收入
    public static final int CHANGE_TYPE_FROM_HONGBAO_RETURN=10;//红包退还



    public static final int ERROR_FEE_NOT_ENOUGH = -101; //提现余额不足
    public static final int ERROR_ACCOUNT_NON_EXISTS = -102; //账户不存在
}
