package com.example.kuaidi.controller;

import com.example.kuaidi.entity.ExpressCompany;
import com.example.kuaidi.service.CompanyService;
import com.example.kuaidi.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping
    public ResultVO<List<ExpressCompany>> list() {
        return ResultVO.success(companyService.findAll());
    }

    @GetMapping("/{id}")
    public ResultVO<ExpressCompany> detail(@PathVariable Integer id) {
        return ResultVO.success(companyService.findById(id));
    }

    @PostMapping
    public ResultVO<?> add(@RequestBody ExpressCompany company) {
        companyService.add(company);
        return ResultVO.success();
    }

    @PutMapping("/{id}")
    public ResultVO<?> update(@PathVariable Integer id, @RequestBody ExpressCompany company) {
        company.setId(id);
        companyService.update(company);
        return ResultVO.success();
    }

    @DeleteMapping("/{id}")
    public ResultVO<?> delete(@PathVariable Integer id) {
        companyService.delete(id);
        return ResultVO.success();
    }
}
