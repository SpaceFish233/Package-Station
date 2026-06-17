package com.example.kuaidi.controller;

import com.example.kuaidi.service.StatisticsService;
import com.example.kuaidi.vo.ResultVO;
import com.example.kuaidi.vo.StatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/today")
    public ResultVO<StatisticsVO> today() {
        return ResultVO.success(statisticsService.getTodayStatistics());
    }
}
