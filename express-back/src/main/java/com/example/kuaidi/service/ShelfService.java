package com.example.kuaidi.service;

import com.example.kuaidi.entity.Shelf;
import java.util.List;

public interface ShelfService {
    List<Shelf> findAll();
    Shelf findById(Integer id);
    void add(Shelf shelf);
    void update(Shelf shelf);
    void delete(Integer id);
}
