package com.example.kuaidi.service.impl;

import com.example.kuaidi.mapper.PackageMapper;
import com.example.kuaidi.service.StatisticsService;
import com.example.kuaidi.vo.StatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private PackageMapper packageMapper;

    @Override
    public StatisticsVO getTodayStatistics() {
        StatisticsVO vo = new StatisticsVO();
        vo.setInCount(packageMapper.countTodayIn());
        vo.setOutCount(packageMapper.countTodayOut());
        vo.setStockCount(packageMapper.countStock());
        return vo;
    }
}
