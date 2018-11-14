package io.service.money.model;

import io.service.money.model.dto.RestResponse;
import io.service.money.util.BasicUtils;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 14.11.2018
 */
public class ParseBox<T> {

    private String param;
    private RestResponse<T> restResponse;

    private ParseBox(String param, RestResponse<T> restResponse) {
        this.param = param;
        this.restResponse = restResponse;
    }

    public static <T> ParseBox<T> valid(String param) {
        return new ParseBox<>(param, null);
    }

    public static <T> ParseBox<T> error(RestResponse<T> restResponse) {
        return new ParseBox<>("", restResponse);
    }

    public boolean isEmpty() {
        return BasicUtils.isEmpty(param);
    }

    public String getParam() {
        return param;
    }

    public RestResponse<T> getRestResponse() {
        return restResponse;
    }
}
