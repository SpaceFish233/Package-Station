package com.example.kuaidi.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Staff {
    private Integer id;
    private String username;
    private String password;
    private String realName;
    private String phone;
    private Integer role;        // 0-工作人员/管理员 1-普通用户
    private LocalDateTime createTime;
}
