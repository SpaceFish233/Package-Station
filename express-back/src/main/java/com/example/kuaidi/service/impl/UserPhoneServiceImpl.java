package com.example.kuaidi.service.impl;

import com.example.kuaidi.entity.UserPhone;
import com.example.kuaidi.exception.BusinessException;
import com.example.kuaidi.mapper.UserPhoneMapper;
import com.example.kuaidi.service.UserPhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserPhoneServiceImpl implements UserPhoneService {

    @Autowired
    private UserPhoneMapper userPhoneMapper;

    @Override
    public List<UserPhone> getByStaffId(Integer staffId) {
        return userPhoneMapper.findByStaffId(staffId);
    }

    @Override
    public void add(Integer staffId, String phone) {
        // 校验手机号格式
        if (phone == null || !phone.matches("^1\\d{10}$")) {
            throw new BusinessException("请输入正确的手机号");
        }
        // 检查手机号是否已被其他用户绑定
        UserPhone existing = userPhoneMapper.findByPhone(phone);
        if (existing != null) {
            throw new BusinessException("该手机号已被其他用户绑定");
        }
        // 限制每个用户最多绑定5个额外手机号
        int count = userPhoneMapper.countByStaffId(staffId);
        if (count >= 5) {
            throw new BusinessException("最多只能绑定5个额外手机号");
        }
        UserPhone userPhone = new UserPhone();
        userPhone.setStaffId(staffId);
        userPhone.setPhone(phone);
        userPhoneMapper.insert(userPhone);
    }

    @Override
    public void delete(Integer id, Integer staffId) {
        // 校验记录归属
        UserPhone userPhone = userPhoneMapper.findById(id);
        if (userPhone == null) {
            throw new BusinessException("记录不存在");
        }
        if (!userPhone.getStaffId().equals(staffId)) {
            throw new BusinessException("无权删除该记录");
        }
        userPhoneMapper.deleteById(id);
    }
}
