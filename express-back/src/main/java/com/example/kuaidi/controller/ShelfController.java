package com.example.kuaidi.controller;

import com.example.kuaidi.entity.Shelf;
import com.example.kuaidi.service.ShelfService;
import com.example.kuaidi.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shelves")
public class ShelfController {

    @Autowired
    private ShelfService shelfService;

    @GetMapping
    public ResultVO<List<Shelf>> list() {
        return ResultVO.success(shelfService.findAll());
    }

    @GetMapping("/{id}")
    public ResultVO<Shelf> detail(@PathVariable Integer id) {
        return ResultVO.success(shelfService.findById(id));
    }

    @PostMapping
    public ResultVO<?> add(@RequestBody Shelf shelf) {
        shelfService.add(shelf);
        return ResultVO.success();
    }

    @PutMapping("/{id}")
    public ResultVO<?> update(@PathVariable Integer id, @RequestBody Shelf shelf) {
        shelf.setId(id);
        shelfService.update(shelf);
        return ResultVO.success();
    }

    @DeleteMapping("/{id}")
    public ResultVO<?> delete(@PathVariable Integer id) {
        shelfService.delete(id);
        return ResultVO.success();
    }
}
