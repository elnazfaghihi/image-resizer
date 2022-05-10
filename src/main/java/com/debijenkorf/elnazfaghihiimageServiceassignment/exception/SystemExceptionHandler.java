package com.debijenkorf.elnazfaghihiimageServiceassignment.exception;

import com.debijenkorf.elnazfaghihiimageServiceassignment.exception.runtime.SystemException;
import com.debijenkorf.elnazfaghihiimageServiceassignment.exception.runtime.SystemRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.Locale;

@ControllerAdvice
public class SystemExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(SystemExceptionHandler.class);


    @ExceptionHandler(SystemRuntimeException.class)
    public ResponseEntity<ErrorMessage> handleSystemException(SystemRuntimeException systemRuntimeException, Locale locale) {
        String errorMessage = systemRuntimeException.getLocalizedMessage(locale);
        logger.info(errorMessage, systemRuntimeException);
        return new ResponseEntity<>(new ErrorMessage(new Date(), errorMessage), HttpStatus.valueOf(systemRuntimeException.getHttpErrorCode()));
    }

    @ExceptionHandler(SystemException.class)
    public ResponseEntity<ErrorMessage> handleSystemException(SystemException systemException, Locale locale) {
        String errorMessage = systemException.getLocalizedMessage(locale);
        logger.error(errorMessage, systemException);
        return new ResponseEntity<>(new ErrorMessage(new Date(), errorMessage), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handle(Exception ex) {
        logger.error(ex.getLocalizedMessage(), ex);
        return new ResponseEntity<>(new ErrorMessage(new Date(), "Internal error occurred"), HttpStatus.NOT_FOUND);
    }
}
