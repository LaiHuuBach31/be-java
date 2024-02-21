package com.project.exception.base;

import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseErrorException {
    private int code;
    private String message;
    private Date time;
}
