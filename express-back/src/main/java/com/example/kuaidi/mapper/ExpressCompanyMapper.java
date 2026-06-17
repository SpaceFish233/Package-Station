package com.example.kuaidi.mapper;

import com.example.kuaidi.entity.ExpressCompany;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ExpressCompanyMapper {
    List<ExpressCompany> findAll();
    ExpressCompany findById(@Param("id") Integer id);
    int insert(ExpressCompany company);
    int update(ExpressCompany company);
    int deleteById(@Param("id") Integer id);
}
