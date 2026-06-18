package com.example.kuaidi.mapper;

import com.example.kuaidi.entity.UserPhone;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface UserPhoneMapper {
    List<UserPhone> findByStaffId(@Param("staffId") Integer staffId);
    UserPhone findByPhone(@Param("phone") String phone);
    UserPhone findById(@Param("id") Integer id);
    int insert(UserPhone userPhone);
    int deleteById(@Param("id") Integer id);
    int countByStaffId(@Param("staffId") Integer staffId);
}
