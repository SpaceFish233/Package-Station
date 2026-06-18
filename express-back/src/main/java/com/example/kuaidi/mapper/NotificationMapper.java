package com.example.kuaidi.mapper;

import com.example.kuaidi.entity.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface NotificationMapper {
    List<Notification> findAll();
    Notification findById(@Param("id") Integer id);
    int insert(Notification notification);
    int update(Notification notification);
    int deleteById(@Param("id") Integer id);
}
