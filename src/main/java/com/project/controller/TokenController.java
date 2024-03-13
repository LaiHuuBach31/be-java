package com.project.controller;

import com.project.dto.request.TokenDTO;
import com.project.model.Result;
import com.project.service.TokenService;
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
@RequestMapping(value = "/api/v1/token")
public class TokenController {

    private final TokenService tokenService;

    @GetMapping(value = "/all")
    public ResponseEntity<Result> getAllToken() {
        List<TokenDTO> listCategory = tokenService.getAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query list token successfully", listCategory));
    }

    @GetMapping(value = "")
    public ResponseEntity<Result> getAllToken(@Param("keyword") String keyword, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Page<TokenDTO> listCategory = tokenService.getAll(keyword, pageNo, 50);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query list token successfully", listCategory));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Result> getById(@PathVariable Integer id) {
        TokenDTO tokenDTO = tokenService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query category by id successfully", tokenDTO));
    }

    @PostMapping()
    public ResponseEntity<Result> insertToken( @Valid @RequestBody TokenDTO newToken) {
        TokenDTO tokenDTO = tokenService.save(newToken);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Insert token successfully", tokenDTO));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Result> updateToken( @Valid @RequestBody TokenDTO newToken, @PathVariable Integer id) {
        TokenDTO tokenDTO = tokenService.update(newToken, id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Update token successfully", tokenDTO));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Result> deleteToken(@PathVariable Integer id, @RequestParam(name="check", defaultValue = "false") boolean check) {
        this.tokenService.delete(id, check);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Delete token successfully", null));
    }
}
