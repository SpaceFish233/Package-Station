package com.example.kuaidi.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class InboundDTO {
    private String trackingNumber;
    private String receiverName;
    private String receiverPhone;
    private Integer expressCompanyId;
    private BigDecimal weight;
    private Integer shelfId;
    private String remark;
}
