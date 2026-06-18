package com.example.kuaidi.service;

import com.example.kuaidi.entity.UserPhone;
import java.util.List;

public interface UserPhoneService {
    List<UserPhone> getByStaffId(Integer staffId);
    void add(Integer staffId, String phone);
    void delete(Integer id, Integer staffId);
}
