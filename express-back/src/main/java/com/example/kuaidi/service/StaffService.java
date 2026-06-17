package com.example.kuaidi.service;

import com.example.kuaidi.dto.LoginDTO;
import java.util.Map;

public interface StaffService {
    Map<String, Object> login(LoginDTO dto);
}
