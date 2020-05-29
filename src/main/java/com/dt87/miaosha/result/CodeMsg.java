package com.dt87.miaosha.result;

public class CodeMsg {


    private int code;
    private String msg;

    //通用异常
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
    //登录模块 5002XX
    public static CodeMsg MOBILE_NULL = new CodeMsg(500210, "账号为空");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500220, "账号格式错误");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500230, "密码错误");
    public static CodeMsg PASSWORD_NULL = new CodeMsg(500240, "密码为空");
    //商品模块 5003XX

    //订单模块 5004XX
    public static CodeMsg NOT_LOGIN = new CodeMsg(500300, "未登陆");
    public static CodeMsg NO_COUNT = new CodeMsg(500310, "库存不足");
    public static CodeMsg AGAIN = new CodeMsg(500320, "秒杀过！");
    public static CodeMsg ERROR_PATh = new CodeMsg(500330, "地址错误！");
    public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(500340, "验证码错误！");
    public static CodeMsg REQUEST_QUICK = new CodeMsg(500350, "请求过于频繁！");
    //秒杀模块 5005XX


    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
