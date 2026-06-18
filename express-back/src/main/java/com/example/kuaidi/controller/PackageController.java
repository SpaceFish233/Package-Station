package com.example.kuaidi.controller;

import com.example.kuaidi.dto.InboundDTO;
import com.example.kuaidi.dto.OutboundDTO;
import com.example.kuaidi.entity.Package;
import com.example.kuaidi.entity.UserPhone;
import com.example.kuaidi.service.PackageService;
import com.example.kuaidi.service.StaffService;
import com.example.kuaidi.service.UserPhoneService;
import com.example.kuaidi.vo.PageResultVO;
import com.example.kuaidi.vo.ResultVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/packages")
public class PackageController {

    @Autowired
    private PackageService packageService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private UserPhoneService userPhoneService;

    @PostMapping("/inbound")
    public ResultVO<Package> inbound(@RequestBody InboundDTO dto) {
        return ResultVO.success(packageService.inbound(dto));
    }

    @PostMapping("/outbound/self")
    public ResultVO<Package> selfOutbound(@RequestBody OutboundDTO dto) {
        return ResultVO.success(packageService.selfOutbound(dto));
    }

    @PostMapping("/outbound/staff")
    public ResultVO<Package> staffOutbound(@RequestBody OutboundDTO dto, HttpServletRequest request) {
        Integer operatorId = (Integer) request.getAttribute("staffId");
        return ResultVO.success(packageService.staffOutbound(dto, operatorId));
    }

    @GetMapping("/query")
    public ResultVO<List<Package>> query(@RequestParam(required = false) String trackingNumber,
                                          @RequestParam(required = false) String phone) {
        List<Package> list = packageService.queryByTrackingNumberOrPhone(trackingNumber, phone);
        return ResultVO.success(list);
    }

    /**
     * 查询当前用户关联的所有包裹（主手机号 + 额外手机号）
     */
    @GetMapping("/my")
    public ResultVO<List<Package>> myPackages(HttpServletRequest request) {
        Integer staffId = (Integer) request.getAttribute("staffId");
        // 收集所有手机号：主手机号 + 额外手机号
        List<String> phones = new ArrayList<>();
        com.example.kuaidi.entity.Staff staff = staffService.getById(staffId);
        if (staff != null && staff.getPhone() != null && !staff.getPhone().isEmpty()) {
            phones.add(staff.getPhone());
        }
        List<UserPhone> extraPhones = userPhoneService.getByStaffId(staffId);
        for (UserPhone up : extraPhones) {
            phones.add(up.getPhone());
        }
        return ResultVO.success(packageService.queryByPhones(phones));
    }

    @PostMapping("/confirm")
    public ResultVO<Package> confirmPickup(@RequestBody java.util.Map<String, Integer> body) {
        Integer packageId = body.get("packageId");
        return ResultVO.success(packageService.confirmPickup(packageId));
    }

    @GetMapping
    public ResultVO<PageResultVO<Package>> list(
            @RequestParam(required = false) String trackingNumber,
            @RequestParam(required = false) String pickupCode,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return ResultVO.success(packageService.queryPackages(trackingNumber, pickupCode, phone, status, page, size));
    }
}
