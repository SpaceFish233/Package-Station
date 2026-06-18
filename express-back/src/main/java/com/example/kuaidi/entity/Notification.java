package com.example.kuaidi.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Notification {
    private Integer id;
    private String title;
    private String content;
    private Integer publisherId;
    private LocalDateTime createTime;

    // 关联查询字段
    private String publisherName;
}
