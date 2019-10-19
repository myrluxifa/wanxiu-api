package com.wanxiu.common;

public class Common {

    public static String PART_USER="01";
    public static String PART_ANCHOR_USER="03";

    public static int EXP_COIN_RATIO=10;


    public  enum RESULT{
        SUCCESS("0","OK"),
        FAIL("1000","未知错误"),
        SMS_CODE_ERROR("1001","验证码错误"),
        WECHAT_NOT_REGISTER("1002","未绑定微信号"),
        WECHAT_RECHARGE_ERROR("1003","预支付接口调用失败"),
        PROJECT_ID_404("1004","产品编号未找到"),
        ACCOUNT_404("1005","账户信息查询失败"),
        USER_404("1006","用户不存在"),
        WECHAT_NOT_BOUND("1007","微信未绑定"),
        WITHDRAW_409("1008","提现已申请为审批，不能重复申请"),
        BALANCE_INSUFFICIENT("1009","余额不足"),
        HONGBAO_409("1010","已抢过该红包"),
        HONGBAO_404("1011","红包404"),
        WECHAT_BOUND_409("1012","微信已绑定"),
        PHONE_BOUND_409("1013","手机号已绑定"),
        HONGBAO_TOTALAMOUNNT_ERROR("1014","红包内秀币不能小于100秀币"),
        HONGBAO_COUNT_ERROR("1015","红包人数不能少于一人"),
        NEW_VERSION("1016","发现新版本"),
        ORDER_404("1017","订单404"),
        ORDER_UNPAY("1018","订单未支付");



        private String code;
        private String errmsg;
        RESULT(String code, String errmsg) {
            this.code=code;
            this.errmsg=errmsg;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getErrmsg() {
            return errmsg;
        }

        public void setErrmsg(String errmsg) {
            this.errmsg = errmsg;
        }
    }
}
