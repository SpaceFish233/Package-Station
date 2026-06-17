package com.example.kuaidi.dto;

import lombok.Data;

@Data
public class OutboundDTO {
    private Integer packageId;
    private String pickupCode;
    private String receiverPhone;
    private Integer pickupType; // 1-用户自助 2-工作人员
}
