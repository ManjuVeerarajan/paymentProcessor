package com.mobpay.Payment.dao;

public enum PlatformEnum {
    AIRAPAY(1),
    MOBYPAY(2);
 
    public final int Value;
 
    private PlatformEnum(int value)
    {
        Value = value;
    }
}
