package com.example.kuaidi.entity;

import lombok.Data;

@Data
public class Shelf {
    private Integer id;
    private String shelfCode;
    private Integer capacity;
    private Integer usedCount;
    private Integer status;
}
