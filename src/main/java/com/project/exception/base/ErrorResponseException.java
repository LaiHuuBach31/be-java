package com.project.exception.base;

import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseException {
    private int status;
    private String message;
    private Date time;
}
