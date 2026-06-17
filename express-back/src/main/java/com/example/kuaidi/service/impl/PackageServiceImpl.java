package com.example.kuaidi.service.impl;

import com.example.kuaidi.dto.InboundDTO;
import com.example.kuaidi.dto.OutboundDTO;
import com.example.kuaidi.entity.Package;
import com.example.kuaidi.entity.PickupRecord;
import com.example.kuaidi.entity.Shelf;
import com.example.kuaidi.exception.BusinessException;
import com.example.kuaidi.mapper.PackageMapper;
import com.example.kuaidi.mapper.PickupRecordMapper;
import com.example.kuaidi.mapper.ShelfMapper;
import com.example.kuaidi.service.PackageService;
import com.example.kuaidi.utils.PickupCodeUtil;
import com.example.kuaidi.vo.PageResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PackageServiceImpl implements PackageService {

    @Autowired
    private PackageMapper packageMapper;

    @Autowired
    private ShelfMapper shelfMapper;

    @Autowired
    private PickupRecordMapper pickupRecordMapper;

    @Override
    @Transactional
    public Package inbound(InboundDTO dto) {
        // 校验运单号唯一
        if (packageMapper.findByTrackingNumber(dto.getTrackingNumber()) != null) {
            throw new BusinessException("运单号已存在");
        }

        // 校验货架容量
        if (dto.getShelfId() != null) {
            Shelf shelf = shelfMapper.findById(dto.getShelfId());
            if (shelf == null) {
                throw new BusinessException("货架不存在");
            }
            if (shelf.getUsedCount() >= shelf.getCapacity()) {
                throw new BusinessException("货架已满，请选择其他货架");
            }
        }

        // 生成唯一取件码
        String pickupCode = generateUniquePickupCode();

        Package pkg = new Package();
        pkg.setTrackingNumber(dto.getTrackingNumber());
        pkg.setReceiverName(dto.getReceiverName());
        pkg.setReceiverPhone(dto.getReceiverPhone());
        pkg.setExpressCompanyId(dto.getExpressCompanyId());
        pkg.setWeight(dto.getWeight());
        pkg.setPickupCode(pickupCode);
        pkg.setShelfId(dto.getShelfId());
        pkg.setStatus(0); // 在库
        pkg.setRemark(dto.getRemark());

        packageMapper.insert(pkg);

        // 更新货架已用数量
        if (dto.getShelfId() != null) {
            shelfMapper.incrementUsedCount(dto.getShelfId());
        }

        return packageMapper.findById(pkg.getId());
    }

    @Override
    @Transactional
    public Package selfOutbound(OutboundDTO dto) {
        Package pkg = findPackageForOutbound(dto.getPickupCode(), dto.getReceiverPhone());
        return processOutbound(pkg, 1, null); // pickupType=1 用户自助
    }

    @Override
    @Transactional
    public Package staffOutbound(OutboundDTO dto, Integer operatorId) {
        Package pkg = packageMapper.findById(dto.getPackageId());
        if (pkg == null) {
            throw new BusinessException("包裹不存在");
        }
        if (pkg.getStatus() != 0) {
            throw new BusinessException("该包裹不在库中，无法出库");
        }
        return processOutbound(pkg, 2, operatorId); // pickupType=2 工作人员
    }

    @Override
    public Package queryByPickupCodeOrPhone(String pickupCode, String phone) {
        if (pickupCode != null && !pickupCode.isEmpty()) {
            Package pkg = packageMapper.findByPickupCode(pickupCode);
            if (pkg != null && pkg.getStatus() == 0) {
                return pkg;
            }
        }
        if (phone != null && !phone.isEmpty()) {
            List<Package> list = packageMapper.findByReceiverPhone(phone);
            if (!list.isEmpty()) {
                return list.get(0);
            }
        }
        return null;
    }

    @Override
    public PageResultVO<Package> queryPackages(String trackingNumber, String pickupCode,
                                                String phone, Integer status, Integer page, Integer size) {
        int offset = (page - 1) * size;
        List<Package> list = packageMapper.findWithConditions(trackingNumber, pickupCode, phone, status, offset, size);
        long total = packageMapper.countWithConditions(trackingNumber, pickupCode, phone, status);
        return new PageResultVO<>(total, list);
    }

    private Package findPackageForOutbound(String pickupCode, String phone) {
        Package pkg = null;
        if (pickupCode != null && !pickupCode.isEmpty()) {
            pkg = packageMapper.findByPickupCode(pickupCode);
        } else if (phone != null && !phone.isEmpty()) {
            List<Package> list = packageMapper.findByReceiverPhone(phone);
            if (!list.isEmpty()) {
                pkg = list.get(0);
            }
        }
        if (pkg == null) {
            throw new BusinessException("未找到符合条件的包裹");
        }
        if (pkg.getStatus() != 0) {
            throw new BusinessException("该包裹不在库中，无法出库");
        }
        return pkg;
    }

    private Package processOutbound(Package pkg, Integer pickupType, Integer operatorId) {
        // 更新包裹状态
        packageMapper.updateStatus(pkg.getId(), 1); // 已取件
        packageMapper.updateOutTime(pkg.getId());

        // 记录取件记录
        PickupRecord record = new PickupRecord();
        record.setPackageId(pkg.getId());
        record.setPickupType(pickupType);
        record.setOperatorId(operatorId);
        pickupRecordMapper.insert(record);

        // 更新货架已用数量
        if (pkg.getShelfId() != null) {
            shelfMapper.decrementUsedCount(pkg.getShelfId());
        }

        return packageMapper.findById(pkg.getId());
    }

    private String generateUniquePickupCode() {
        String code;
        int attempts = 0;
        do {
            code = PickupCodeUtil.generate();
            attempts++;
            if (attempts > 100) {
                throw new BusinessException("取件码生成失败，请重试");
            }
        } while (packageMapper.findByPickupCode(code) != null);
        return code;
    }
}
