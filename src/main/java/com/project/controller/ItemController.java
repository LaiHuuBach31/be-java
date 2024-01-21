package com.project.controller;

import com.project.dto.ItemDTO;
import com.project.model.Item;
import com.project.model.Result;
import com.project.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/item")
public class ItemController {

    private final ItemService itemService;
    private final ModelMapper modelMapper;

    @GetMapping(value = "")
    public ResponseEntity<Result> getAllItem(@Param("keyword") String keyword, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Page<Item> listItem = itemService.pagination(pageNo, 5);
        if(keyword != null){
            listItem = this.itemService.search(keyword, pageNo, 5);
        }
        if (!listItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Result(200, "Query list item successfully", listItem));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Result(404, "No items found", null));
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Result> getById(@PathVariable Integer id) {
        Item item = itemService.findById(id);
        if (item != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Result(200, "Query item successfully", item));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Result(404, "Cannot find item", null));
        }
    }

    @PostMapping(value = "")
    public ResponseEntity<Result> insertItem(@Valid @RequestBody ItemDTO newItem,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> errorMessages = errors.stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Result(400, "Validation failed", errorMessages));
        }
        List<Item> foundItem = itemService.findByName(newItem.getName().trim());
        if (!foundItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                    .body(new Result(501, "Item name already taken", ""));
        }
        Item itemRequest = modelMapper.map(newItem, Item.class);
        Item item = itemService.saveOrUpdate(itemRequest);
        ItemDTO itemResponse = modelMapper.map(item, ItemDTO.class);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Insert item successfully", itemResponse));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Result> updateItem(@Valid @RequestBody ItemDTO newItem,
                                                 BindingResult bindingResult, @PathVariable Integer id) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> errorMessages = errors.stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Result(400, "Validation failed", errorMessages));
        }
        List<Item> foundItem = itemService.findByName(newItem.getName().trim());
        if (!foundItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                    .body(new Result(501, "Item name already taken", ""));
        }
        Item updateItem = itemService.findById(id);
        if (updateItem != null) {
            updateItem.setName(newItem.getName());
            updateItem.setStatus(newItem.getStatus());
            updateItem = itemService.saveOrUpdate(updateItem);
        } else {
            Item itemRequest = modelMapper.map(newItem, Item.class);
            itemRequest.setId(id);
            updateItem = itemService.saveOrUpdate(itemRequest);
        }
        ItemDTO itemResponse = modelMapper.map(updateItem, ItemDTO.class);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Update item successfully", itemResponse));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Result> deleteItem(@PathVariable Integer id) {
        boolean exists = itemService.existsById(id);
        if (exists) {
            itemService.delete(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Result(200, "Delete item successfully", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Result(404, "Cannot find item to delete", ""));
    }
}

