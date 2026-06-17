package com.example.kuaidi.service;

import com.example.kuaidi.dto.InboundDTO;
import com.example.kuaidi.dto.OutboundDTO;
import com.example.kuaidi.entity.Package;
import com.example.kuaidi.vo.PageResultVO;

public interface PackageService {
    Package inbound(InboundDTO dto);
    Package selfOutbound(OutboundDTO dto);
    Package staffOutbound(OutboundDTO dto, Integer operatorId);
    Package queryByPickupCodeOrPhone(String pickupCode, String phone);
    PageResultVO<Package> queryPackages(String trackingNumber, String pickupCode,
                                         String phone, Integer status, Integer page, Integer size);
}
