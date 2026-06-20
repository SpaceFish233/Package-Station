package com.example.kuaidi.utils;

/**
 * 取件码生成工具
 * 格式：{货架编号}-{序号}，如 A-01-1
 */
public class PickupCodeUtil {

    /**
     * 生成取件码
     * @param shelfCode 货架编号，如 A-01
     * @param sequence  该货架上的序号（从1开始）
     * @return 取件码，如 A-01-1
     */
    public static String generate(String shelfCode, int sequence) {
        return shelfCode + "-" + sequence;
    }

    /**
     * 生成已失效的取件码
     * @param pickupCode 原取件码，如 A-01-1
     * @param sequence   失效序号（从1开始），如 1 → A-01-1-已失效1
     * @return 已失效标记，如 A-01-1-已失效1
     */
    public static String markInvalid(String pickupCode, int sequence) {
        return pickupCode + "-已失效" + sequence;
    }
}
