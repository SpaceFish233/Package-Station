package com.example.kuaidi.service;

import com.example.kuaidi.dto.LoginDTO;
import com.example.kuaidi.entity.Staff;
import java.util.Map;

public interface StaffService {
    Map<String, Object> login(LoginDTO dto);
    void register(Staff staff);
    Staff getById(Integer id);
    void updateRealName(Integer id, String realName);
    void updatePassword(Integer id, String oldPassword, String newPassword);
}
