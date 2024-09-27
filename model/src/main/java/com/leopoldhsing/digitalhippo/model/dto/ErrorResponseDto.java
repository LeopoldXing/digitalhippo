package com.leopoldhsing.digitalhippo.model.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Objects;

public class ErrorResponseDto {

    private String apiPath;

    private HttpStatus errorCode;

    private String errorMessage;

    private LocalDateTime errorTime;

    public ErrorResponseDto() {
    }

    public ErrorResponseDto(String apiPath, HttpStatus errorCode, String errorMessage, LocalDateTime errorTime) {
        this.apiPath = apiPath;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorTime = errorTime;
    }

    @Override
    public String toString() {
        return "ErrorResponseDto{" +
                "apiPath='" + apiPath + '\'' +
                ", errorCode=" + errorCode +
                ", errorMessage='" + errorMessage + '\'' +
                ", errorTime=" + errorTime +
                '}';
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }

    public HttpStatus getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(HttpStatus errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getErrorTime() {
        return errorTime;
    }

    public void setErrorTime(LocalDateTime errorTime) {
        this.errorTime = errorTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ErrorResponseDto that = (ErrorResponseDto) o;
        return Objects.equals(apiPath, that.apiPath) && errorCode == that.errorCode && Objects.equals(errorMessage, that.errorMessage) && Objects.equals(errorTime, that.errorTime);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(apiPath);
        result = 31 * result + Objects.hashCode(errorCode);
        result = 31 * result + Objects.hashCode(errorMessage);
        result = 31 * result + Objects.hashCode(errorTime);
        return result;
    }
}
