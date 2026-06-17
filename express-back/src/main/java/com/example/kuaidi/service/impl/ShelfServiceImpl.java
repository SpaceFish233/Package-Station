package com.example.kuaidi.service.impl;

import com.example.kuaidi.entity.Shelf;
import com.example.kuaidi.exception.BusinessException;
import com.example.kuaidi.mapper.ShelfMapper;
import com.example.kuaidi.service.ShelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShelfServiceImpl implements ShelfService {

    @Autowired
    private ShelfMapper shelfMapper;

    @Override
    public List<Shelf> findAll() {
        return shelfMapper.findAll();
    }

    @Override
    public Shelf findById(Integer id) {
        return shelfMapper.findById(id);
    }

    @Override
    public void add(Shelf shelf) {
        if (shelf.getUsedCount() == null) {
            shelf.setUsedCount(0);
        }
        if (shelf.getStatus() == null) {
            shelf.setStatus(1);
        }
        shelfMapper.insert(shelf);
    }

    @Override
    public void update(Shelf shelf) {
        if (shelfMapper.findById(shelf.getId()) == null) {
            throw new BusinessException("货架不存在");
        }
        shelfMapper.update(shelf);
    }

    @Override
    public void delete(Integer id) {
        if (shelfMapper.findById(id) == null) {
            throw new BusinessException("货架不存在");
        }
        shelfMapper.deleteById(id);
    }
}
