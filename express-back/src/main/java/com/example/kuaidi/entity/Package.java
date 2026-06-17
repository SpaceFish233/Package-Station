package com.example.kuaidi.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Package {
    private Integer id;
    private String trackingNumber;
    private String receiverName;
    private String receiverPhone;
    private Integer expressCompanyId;
    private BigDecimal weight;
    private String pickupCode;
    private Integer shelfId;
    private Integer status; // 0-在库 1-已取件 2-异常
    private LocalDateTime inTime;
    private LocalDateTime outTime;
    private String remark;

    // 关联查询字段
    private String companyName;
    private String shelfCode;
}
