package com.example.kuaidi.service.impl;

import com.example.kuaidi.dto.LoginDTO;
import com.example.kuaidi.entity.Staff;
import com.example.kuaidi.exception.BusinessException;
import com.example.kuaidi.mapper.StaffMapper;
import com.example.kuaidi.service.StaffService;
import com.example.kuaidi.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 校验密码：优先 BCrypt，兼容 $2b$ 前缀，回退明文比对
     */
    private boolean checkPassword(String rawPassword, String storedPassword) {
        if (storedPassword == null) return false;
        // BCrypt 格式：$2a$ 或 $2b$ 或 $2y$ 开头
        if (storedPassword.startsWith("$2")) {
            // Java BCryptPasswordEncoder 只认 $2a$，统一替换前缀
            String normalized = storedPassword.replace("$2b$", "$2a$").replace("$2y$", "$2a$");
            return passwordEncoder.matches(rawPassword, normalized);
        }
        // 明文回退（过渡期兼容旧数据）
        return storedPassword.equals(rawPassword);
    }

    @Override
    public void register(Staff staff) {
        // 校验用户名（手机号）唯一
        if (staffMapper.findByUsername(staff.getUsername()) != null) {
            throw new BusinessException("该手机号已注册");
        }
        // 加密密码
        staff.setPassword(passwordEncoder.encode(staff.getPassword()));
        // 强制为普通用户
        staff.setRole(1);
        staffMapper.insert(staff);
    }

    @Override
    public Map<String, Object> login(LoginDTO dto) {
        Staff staff = staffMapper.findByUsername(dto.getUsername());
        if (staff == null) {
            throw new BusinessException("用户名不存在");
        }
        if (!checkPassword(dto.getPassword(), staff.getPassword())) {
            throw new BusinessException("密码错误");
        }

        String token = jwtUtil.generateToken(staff.getId(), staff.getUsername(), staff.getRole());

        Map<String, Object> staffInfo = new HashMap<>();
        staffInfo.put("id", staff.getId());
        staffInfo.put("username", staff.getUsername());
        staffInfo.put("realName", staff.getRealName());
        staffInfo.put("phone", staff.getPhone());
        staffInfo.put("role", staff.getRole());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("staffInfo", staffInfo);
        return result;
    }

    @Override
    public Staff getById(Integer id) {
        return staffMapper.findById(id);
    }

    @Override
    public void updateRealName(Integer id, String realName) {
        if (realName == null || realName.trim().isEmpty()) {
            throw new BusinessException("昵称不能为空");
        }
        staffMapper.updateRealName(id, realName.trim());
    }

    @Override
    public void updatePassword(Integer id, String oldPassword, String newPassword) {
        Staff staff = staffMapper.findById(id);
        if (staff == null) {
            throw new BusinessException("用户不存在");
        }
        if (!checkPassword(oldPassword, staff.getPassword())) {
            throw new BusinessException("原密码错误");
        }
        if (newPassword == null || newPassword.length() < 6) {
            throw new BusinessException("新密码长度不能少于6位");
        }
        staffMapper.updatePassword(id, passwordEncoder.encode(newPassword));
    }
}
