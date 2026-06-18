package com.example.kuaidi.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserPhone {
    private Integer id;
    private Integer staffId;
    private String phone;
    private LocalDateTime createTime;
}
