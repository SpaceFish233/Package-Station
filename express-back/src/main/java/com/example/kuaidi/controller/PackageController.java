package com.example.kuaidi.controller;

import com.example.kuaidi.dto.InboundDTO;
import com.example.kuaidi.dto.OutboundDTO;
import com.example.kuaidi.entity.Package;
import com.example.kuaidi.service.PackageService;
import com.example.kuaidi.vo.PageResultVO;
import com.example.kuaidi.vo.ResultVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
public class PackageController {

    @Autowired
    private PackageService packageService;

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
