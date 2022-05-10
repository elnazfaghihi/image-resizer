package com.debijenkorf.elnazfaghihiimageServiceassignment.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    InvalidRequest(HttpStatus.BAD_REQUEST),
    Forbidden(HttpStatus.FORBIDDEN),
    MethodNotAllowed(HttpStatus.METHOD_NOT_ALLOWED),
    ServerError(HttpStatus.INTERNAL_SERVER_ERROR),
    NotFound(HttpStatus.NOT_FOUND, "not_found");

    private HttpStatus httpErrorCode;
    private String oauthErrorCode;

    ErrorCode(HttpStatus httpErrorCode, String oauthErrorCode) {
        this.httpErrorCode = httpErrorCode;
        this.oauthErrorCode = oauthErrorCode;
    }

    ErrorCode(HttpStatus httpErrorCode) {
        this.httpErrorCode = httpErrorCode;
        this.oauthErrorCode = httpErrorCode.getReasonPhrase();
    }

    public Integer getHttpErrorCode() {
        return httpErrorCode.value();
    }

    public HttpStatus getHttpStatus() {
        return httpErrorCode;
    }

    public String getOauthErrorCode() {
        return oauthErrorCode;
    }
}
