package com.example.kuaidi.service.impl;

import com.example.kuaidi.entity.ExpressCompany;
import com.example.kuaidi.exception.BusinessException;
import com.example.kuaidi.mapper.ExpressCompanyMapper;
import com.example.kuaidi.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private ExpressCompanyMapper companyMapper;

    @Override
    public List<ExpressCompany> findAll() {
        return companyMapper.findAll();
    }

    @Override
    public ExpressCompany findById(Integer id) {
        return companyMapper.findById(id);
    }

    @Override
    public void add(ExpressCompany company) {
        companyMapper.insert(company);
    }

    @Override
    public void update(ExpressCompany company) {
        if (companyMapper.findById(company.getId()) == null) {
            throw new BusinessException("快递公司不存在");
        }
        companyMapper.update(company);
    }

    @Override
    public void delete(Integer id) {
        if (companyMapper.findById(id) == null) {
            throw new BusinessException("快递公司不存在");
        }
        companyMapper.deleteById(id);
    }
}
