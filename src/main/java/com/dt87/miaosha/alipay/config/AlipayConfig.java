package com.dt87.miaosha.alipay.config;

import java.io.FileWriter;
import java.io.IOException;

public class AlipayConfig {
    //↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016102400750593";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCTLTGN1+Nhz9uuSKltsSI75DNyRgrkf2Rk+BQvZBf6aOYsl4bqzGVoVe3HcWCl3M9TVZMMqvWym78cwiZg16dduL5VQfLbxMGf9S8tvDoaXGrxMGt1Ua18EufVyfxFrbZTQul7f65xZIt6XYWBeNt6u5pcZ1XHXqMGnosIcYsrdeSmekM6Rkwoet1RPjLrpwcgHLmUqSUkqcap6ECXHOLI1h5cBtDCikAA+S9lbTcpTLYz/ckr+cQKRYQtF3oiq7ud92/ezFCVt7uiXGXgGk9WgYqbivVwLzOtfLqrOCowB2lrp7XJ5IrvtEdnkcQt1REqra3SBhIm6DSNRCgIPdaXAgMBAAECggEACf0nP9lHNXxGDK4h0bAUAryaQUc/tgJvberoc873UaeM97nRDl6RA2PRn5eyV4+FAsLsGBACXPz1G56RvEDmuxWu1VqEgSX5fkHF0fTUfLWJDJWctXm1rJYb5gaveIsxtLT148vmqwuH6+YtCXyKaOoSr/LTf5SMvAZGqWwLZgefnvtGy79nGb7ae+N9b+pQcreXBEq8cqpyVPWmj93jnHpGwzPclqlaDTMdfNWN1NTuomMCLSWOw3TnE4Y93Kw/q1vdTQiC8dAKMLRAUszz2DGLuJJAvrsI2TraSK1XPAghfuUZC5INxbi885lzgJ8DdMwOFhLMXe8WJA5AOdftAQKBgQDmutZ81/9iTlGz4UMMdqKAK6CJ2WXrVfpL/K4pGYx9MuHDay7+GwZT1W+87Vyv2+L6RKjPXVb6EjyxXb8ASi9GwiVoNEBPUWChUuXJCq6mAkNYgT5384Bu4kr5qeUJMjy4EBAwYMXMhP+25M8wc2QyK+dxMlKyxR4bzxE+KkbwHQKBgQCjS7QEmSwLZcoRzC8aSjY7nXYZ3ieOrSM4aJ+k/GMTCENc7XgQqttdDyc4dSB9fvsH8f/+Zw3YLxNEgxF9XIIxMqN9Bkx3dtDjBArP/wQkbbeGPWiAMWowSg960QBBmmAD1MQvu1vnaH0oqf0ag/cVG1los+ikZ5OH/JCYXX7LQwKBgQCXfsAMcuqNiq0cteCHU3H5RCwBtwuzjN1ufTeBZvQW1Izl+f99+xKZgv/+BcAg+/i5qNFKbpgIVGcjclFSCGOoSxuyovRC4ETZSAv2iBwKhlvfxeTVb725kL331fS7qA+0L1/h0/isA/u/A9eCkZGNW8R4T7l7BfByA/DnZ2zyhQKBgQCUo8lGUkL0UQBhuPG7RYgeNdOV8lEhn1oq85k17gMlBOWEhig/S7cH+04hJwSfF2Hbtp3l7dp9SUSJnaDi8r0TmVuV55DdQYscycYp1KDpo/FlQSqJXfGh6Rg0HGDWVnZFpnu6UxpyBvu+cxWP+EWFsfJtyy3d0OZK50ybVKegzwKBgGFhimWhAYxyxPQNSFc8MRODNmgpJdnaNieyI+VjQOAfuUBoxoPXmy5S3Bq7H5+HT6gsOdI+g3ZQr9f0+noVYfFX07EQ1Ha7O7hrvwSya/c0GuZkqwGcUPlfyb7IXw15OZccokNP6/UbXXGJTzrp357SvFyq1/F8xqApSXnz6bD6";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAky0xjdfjYc/brkipbbEiO+QzckYK5H9kZPgUL2QX+mjmLJeG6sxlaFXtx3FgpdzPU1WTDKr1spu/HMImYNenXbi+VUHy28TBn/UvLbw6Glxq8TBrdVGtfBLn1cn8Ra22U0Lpe3+ucWSLel2FgXjberuaXGdVx16jBp6LCHGLK3XkpnpDOkZMKHrdUT4y66cHIBy5lKklJKnGqehAlxziyNYeXAbQwopAAPkvZW03KUy2M/3JK/nECkWELRd6Iqu7nfdv3sxQlbe7olxl4BpPVoGKm4r1cC8zrXy6qzgqMAdpa6e1yeSK77RHZ5HELdURKq2t0gYSJug0jUQoCD3WlwIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://localhost:8080/alipay/updateOrderState";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://localhost:8080/alipay/updateOrderState";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     *
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis() + ".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
