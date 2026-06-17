package com.example.kuaidi.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PickupRecord {
    private Integer id;
    private Integer packageId;
    private Integer pickupType; // 1-用户自助 2-工作人员
    private LocalDateTime pickupTime;
    private Integer operatorId;
}
