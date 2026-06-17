package com.example.kuaidi.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ExpressCompany {
    private Integer id;
    private String companyName;
    private String code;
    private LocalDateTime createTime;
}
