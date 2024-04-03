package com.project.controller;

import com.project.dto.request.CategoryDTO;
import com.project.dto.request.OrderDTO;
import com.project.model.Result;
import com.project.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/order")
public class OrderController {

    private final OrderService orderService;
    @GetMapping(value = "/all")
    public ResponseEntity<Result> getAllOrder() {
        List<OrderDTO> listOrder = orderService.getAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query list order successfully", listOrder));
    }

    @GetMapping(value = "")
    public ResponseEntity<Result> getAllOrder(@Param("keyword") String keyword, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Page<OrderDTO> listOrder = orderService.getAll(keyword, pageNo, 10);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query list order successfully", listOrder));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Result> getById(@PathVariable Integer id) {
        OrderDTO orderDto = orderService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query category by id successfully", orderDto));
    }

    @PostMapping()
    public ResponseEntity<Result> insertOrder( @Valid @RequestBody OrderDTO newOrder) {
        OrderDTO orderDto = orderService.save(newOrder);
        System.out.println(orderDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Insert order successfully", orderDto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Result> deleteOrder(@PathVariable Integer id, @RequestParam(name="check", defaultValue = "false") boolean check) {
        this.orderService.delete(id, check);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Delete category successfully", null));
    }
}
