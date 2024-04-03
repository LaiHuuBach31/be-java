package com.project.controller;

import com.project.dto.response.OrderDetailViewDTO;
import com.project.model.Result;
import com.project.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/orderdetail")
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    @GetMapping(value = "")
    public ResponseEntity<Result> getAllOrder(@Param("keyword") String keyword, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Page<OrderDetailViewDTO> listOrder = orderDetailService.getAll(keyword, pageNo, 10);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query list order detail successfully", listOrder));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Result> getById(@PathVariable Integer id) {
        OrderDetailViewDTO orderDetailViewDTO = orderDetailService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query category by id successfully", orderDetailViewDTO));
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Result> deleteOrder(@PathVariable Integer id, @RequestParam(name="check", defaultValue = "false") boolean check) {
        this.orderDetailService.delete(id, check);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Delete order detail successfully", null));
    }
}
