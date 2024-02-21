package com.project.exception.base;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Date;
@Getter
@Setter
public class CustomException extends RuntimeException{
    private int status;
    private Date time;

    public CustomException(String message, int status, Date time){
        super(message);
        this.status = status;
        this.time = time;
    }

    public static class NotFoundException extends CustomException {
        public NotFoundException(String message, int status, Date time) {
            super(message, status, time);
        }
    }

    public static class BadRequestException extends CustomException {
        public BadRequestException(String message, int status, Date time) {
            super(message, status, time);
        }
    }

    public static class NotImplementedException extends CustomException {
        public NotImplementedException(String message, int status, Date time) {
            super(message, status, time);
        }
    }

}
