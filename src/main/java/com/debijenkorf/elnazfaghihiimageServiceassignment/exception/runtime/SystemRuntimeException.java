package com.debijenkorf.elnazfaghihiimageServiceassignment.exception.runtime;

import com.debijenkorf.elnazfaghihiimageServiceassignment.exception.ErrorCode;
import com.debijenkorf.elnazfaghihiimageServiceassignment.message.ExceptionMessageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.NoSuchMessageException;

import java.beans.ConstructorProperties;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SystemRuntimeException extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(SystemRuntimeException.class);

    private static final ExceptionMessageSource exceptionMessageSource = new ExceptionMessageSource();

    private Object[] args;
    private Locale locale;
    private ErrorCode errorCode;

    @ConstructorProperties({"errorCode, message", "args"})
    public  SystemRuntimeException(ErrorCode errorCode, String message, Object... args) {
        super(message);
        this.args = args;
        this.errorCode = errorCode;
    }

    @ConstructorProperties({"errorCode, message", "args"})
    public SystemRuntimeException(ErrorCode errorCode, String message, List<Object> args) {
        super(message);
        this.args = args.toArray();
        this.errorCode = errorCode;
    }

    @ConstructorProperties({"message", "args"})
    public SystemRuntimeException(String message, Object... args) {
        super(message);
        this.args = args;
        this.errorCode = ErrorCode.InvalidRequest;
    }

    public SystemRuntimeException(ErrorCode errorCode, String message, Throwable t) {
        super(message, t);
        this.errorCode = errorCode;
    }

    public SystemRuntimeException(Throwable t) {
        super(t.getMessage(), t);
        this.errorCode = ErrorCode.ServerError;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public int getHttpErrorCode() {
        return errorCode.getHttpErrorCode();
    }


    @Override
    public String getLocalizedMessage() {
        try {
            Object[] args = this.args != null ? Arrays.stream(this.args).map(Object::toString).toArray() : null;
            return exceptionMessageSource.getMessage(this.getMessage(), args, this.locale);
        } catch (NoSuchMessageException e) {
            logger.error("could not find exception message, error is = " + e.getMessage());
            return exceptionMessageSource.getMessage("Exception.unexpected", null, this.locale);
        }
    }

    public String getLocalizedMessage(Locale locale) {
        try {
            Object[] args = this.args != null ? Arrays.stream(this.args).map(Object::toString).toArray() : null;
            return exceptionMessageSource.getMessage(this.getMessage(), args, locale);
        } catch (NoSuchMessageException e) {
            logger.error("could not find exception message, error is = " + e.getMessage());
            return exceptionMessageSource.getMessage("Exception.unexpected", null, locale);

        }
    }
}
