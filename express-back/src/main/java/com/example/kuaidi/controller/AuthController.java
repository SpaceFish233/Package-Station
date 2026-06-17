package com.example.kuaidi.controller;

import com.example.kuaidi.dto.LoginDTO;
import com.example.kuaidi.service.StaffService;
import com.example.kuaidi.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private StaffService staffService;

    @PostMapping("/login")
    public ResultVO<Map<String, Object>> login(@RequestBody LoginDTO dto) {
        return ResultVO.success(staffService.login(dto));
    }
}
