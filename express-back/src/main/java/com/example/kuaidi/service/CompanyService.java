package com.example.kuaidi.service;

import com.example.kuaidi.entity.ExpressCompany;
import java.util.List;

public interface CompanyService {
    List<ExpressCompany> findAll();
    ExpressCompany findById(Integer id);
    void add(ExpressCompany company);
    void update(ExpressCompany company);
    void delete(Integer id);
}
