package com.example.kuaidi.mapper;

import com.example.kuaidi.entity.PickupRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface PickupRecordMapper {
    int insert(PickupRecord record);
    List<PickupRecord> findByPackageId(@Param("packageId") Integer packageId);
}
