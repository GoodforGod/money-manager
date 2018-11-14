package io.service.money.controller;

import com.google.gson.Gson;
import io.javalin.Context;
import io.service.money.model.ParseBox;
import io.service.money.model.dto.RestResponse;
import io.service.money.util.BasicUtils;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 14.11.2018
 */
public abstract class BasicController {

    private Gson gson = new Gson();

    <T> T convert(String json, Class<T> tClass) {
        return gson.fromJson(json, tClass);
    }

    <T> String convert(T t) {
        return gson.toJson(t);
    }

    <T> String validResponse(T t) {
        return convert(RestResponse.valid(t));
    }

    String errorResponse(String errorDetails) {
        return convert(RestResponse.error(errorDetails));
    }

    ParseBox getPathParam(String paramName, Context context) {
        final String param = context.pathParam(paramName);
        return (BasicUtils.isEmpty(param))
                ? ParseBox.error(RestResponse.error(paramName + " param was not presented"))
                : ParseBox.valid(param);
    }

    abstract public void handle();
}
