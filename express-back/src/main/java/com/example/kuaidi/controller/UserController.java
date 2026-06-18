package com.example.kuaidi.controller;

import com.example.kuaidi.entity.Staff;
import com.example.kuaidi.entity.UserPhone;
import com.example.kuaidi.service.StaffService;
import com.example.kuaidi.service.UserPhoneService;
import com.example.kuaidi.vo.ResultVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private StaffService staffService;

    @Autowired
    private UserPhoneService userPhoneService;

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public ResultVO<Map<String, Object>> getInfo(HttpServletRequest request) {
        Integer staffId = (Integer) request.getAttribute("staffId");
        Staff staff = staffService.getById(staffId);
        Map<String, Object> info = new HashMap<>();
        info.put("id", staff.getId());
        info.put("username", staff.getUsername());
        info.put("realName", staff.getRealName());
        info.put("phone", staff.getPhone());
        info.put("role", staff.getRole());
        return ResultVO.success(info);
    }

    /**
     * 修改昵称
     */
    @PutMapping("/profile")
    public ResultVO<Void> updateProfile(@RequestBody Map<String, String> body, HttpServletRequest request) {
        Integer staffId = (Integer) request.getAttribute("staffId");
        staffService.updateRealName(staffId, body.get("realName"));
        return ResultVO.success(null);
    }

    /**
     * 修改密码
     */
    @PutMapping("/password")
    public ResultVO<Void> updatePassword(@RequestBody Map<String, String> body, HttpServletRequest request) {
        Integer staffId = (Integer) request.getAttribute("staffId");
        staffService.updatePassword(staffId, body.get("oldPassword"), body.get("newPassword"));
        return ResultVO.success(null);
    }

    /**
     * 获取额外手机号列表
     */
    @GetMapping("/phones")
    public ResultVO<List<UserPhone>> getPhones(HttpServletRequest request) {
        Integer staffId = (Integer) request.getAttribute("staffId");
        return ResultVO.success(userPhoneService.getByStaffId(staffId));
    }

    /**
     * 添加额外手机号
     */
    @PostMapping("/phones")
    public ResultVO<Void> addPhone(@RequestBody Map<String, String> body, HttpServletRequest request) {
        Integer staffId = (Integer) request.getAttribute("staffId");
        userPhoneService.add(staffId, body.get("phone"));
        return ResultVO.success(null);
    }

    /**
     * 删除额外手机号
     */
    @DeleteMapping("/phones/{id}")
    public ResultVO<Void> deletePhone(@PathVariable Integer id, HttpServletRequest request) {
        Integer staffId = (Integer) request.getAttribute("staffId");
        userPhoneService.delete(id, staffId);
        return ResultVO.success(null);
    }
}
