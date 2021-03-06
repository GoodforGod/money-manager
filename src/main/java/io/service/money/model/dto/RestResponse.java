package io.service.money.model.dto;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 13.11.2018
 */
public class RestResponse<T> {

    private boolean isError;
    private String errorDetails;
    private T result;

    private RestResponse(boolean isError, String errorDetails, T result) {
        this.isError = isError;
        this.errorDetails = errorDetails;
        this.result = result;
    }

    public static <T> RestResponse<T> valid(T t) {
        return new RestResponse<>(false, "", t);
    }

    public static <T> RestResponse<T> error(String errorDetails) {
        return new RestResponse<>(true, errorDetails, null);
    }

    public boolean isError() {
        return isError;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public T getResult() {
        return result;
    }
}
