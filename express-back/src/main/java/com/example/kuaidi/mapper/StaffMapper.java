package com.example.kuaidi.mapper;

import com.example.kuaidi.entity.Staff;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StaffMapper {
    Staff findByUsername(@Param("username") String username);
    Staff findById(@Param("id") Integer id);
    int insert(Staff staff);
    int updateRealName(@Param("id") Integer id, @Param("realName") String realName);
    int updatePassword(@Param("id") Integer id, @Param("password") String password);
}
