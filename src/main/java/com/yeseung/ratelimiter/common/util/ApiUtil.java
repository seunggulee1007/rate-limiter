package com.yeseung.ratelimiter.common.util;

public class ApiUtil {

    public static <T> ApiResponse<T> success(T response) {
        return new ApiResponse<>("success", response);
    }

    public static ApiResponse<Void> fail(String message) {
        return new ApiResponse<>(message, null);
    }

    public record ApiResponse<T>(String message, T response) {

    }

}
