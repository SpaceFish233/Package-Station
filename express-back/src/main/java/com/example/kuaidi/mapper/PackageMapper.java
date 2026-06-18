package com.example.kuaidi.mapper;

import com.example.kuaidi.entity.Package;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface PackageMapper {
    int insert(Package pkg);
    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);
    int updateOutTime(@Param("id") Integer id);
    int updatePickupCode(@Param("id") Integer id, @Param("pickupCode") String pickupCode);
    Package findById(@Param("id") Integer id);
    Package findByTrackingNumber(@Param("trackingNumber") String trackingNumber);
    Package findByPickupCode(@Param("pickupCode") String pickupCode);
    List<Package> findByReceiverPhone(@Param("phone") String phone);
    List<Package> findByPhones(@Param("phones") List<String> phones);
    List<Package> findByTrackingNumberOrPhone(@Param("trackingNumber") String trackingNumber, @Param("phone") String phone);
    List<Package> findWithConditions(@Param("trackingNumber") String trackingNumber,
                                     @Param("pickupCode") String pickupCode,
                                     @Param("phone") String phone,
                                     @Param("status") Integer status,
                                     @Param("offset") Integer offset,
                                     @Param("size") Integer size);
    long countWithConditions(@Param("trackingNumber") String trackingNumber,
                             @Param("pickupCode") String pickupCode,
                             @Param("phone") String phone,
                             @Param("status") Integer status);
    long countTodayIn();
    long countTodayOut();
    long countStock();
    long countByShelfId(@Param("shelfId") Integer shelfId);
}
