package com.dt87.miaosha.redis;

public class AccessKey extends BasePrefix {

    public AccessKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);

    }

    //	public static AccessKey getAccessKey = new AccessKey(5,"accessKey");
//	public static AccessKey getAccessKey = new AccessKey(50,"accessKey");
    public static AccessKey getAccessKey = new AccessKey(50, "accessKey");

    public static AccessKey withExpire(int expireSeconds) {
        return new AccessKey(expireSeconds, "access");
    }

}
