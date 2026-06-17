package com.example.kuaidi.vo;

import lombok.Data;
import java.util.List;

@Data
public class PageResultVO<T> {
    private Long total;
    private List<T> list;

    public PageResultVO(Long total, List<T> list) {
        this.total = total;
        this.list = list;
    }
}
