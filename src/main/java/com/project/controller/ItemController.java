package com.project.controller;

import com.project.dto.request.CategoryDTO;
import com.project.dto.request.ItemDTO;
import com.project.model.Result;
import com.project.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/item")
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "/all")
    public ResponseEntity<Result> getAllItem() {
        List<ItemDTO> listItem = itemService.getAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query list item successfully", listItem));
    }

    @GetMapping(value = "")
    public ResponseEntity<Result> getAllItem(@Param("keyword") String keyword, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Page<ItemDTO> listItem = itemService.getAll(keyword, pageNo, 10);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query list item successfully", listItem));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Result> getById(@PathVariable Integer id) {
        ItemDTO itemDto = itemService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query item by id successfully", itemDto));
    }

    @PostMapping(value = "")
    public ResponseEntity<Result> insertItem(@Valid @RequestBody ItemDTO newItem) {
        ItemDTO itemDto = itemService.save(newItem);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Insert item successfully", itemDto));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Result> updateItem(@Valid @RequestBody ItemDTO newItem, @PathVariable Integer id) {
        ItemDTO itemDto = itemService.update(newItem, id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Update item successfully", itemDto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Result> deleteItem(@PathVariable Integer id, @RequestParam(name="check", defaultValue = "false") boolean check) {
        this.itemService.delete(id, check);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Delete item successfully", null));
    }
}

