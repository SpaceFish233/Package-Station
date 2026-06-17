package com.example.kuaidi.mapper;

import com.example.kuaidi.entity.Shelf;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ShelfMapper {
    List<Shelf> findAll();
    Shelf findById(@Param("id") Integer id);
    Shelf findByCode(@Param("shelfCode") String shelfCode);
    int insert(Shelf shelf);
    int update(Shelf shelf);
    int deleteById(@Param("id") Integer id);
    int incrementUsedCount(@Param("id") Integer id);
    int decrementUsedCount(@Param("id") Integer id);
}
