package io.service.money.controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.javalin.Context;
import io.service.money.model.ParseBox;
import io.service.money.model.dto.RestResponse;
import io.service.money.util.BasicUtils;

import java.util.logging.Logger;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 14.11.2018
 */
public abstract class BasicController {

    final Logger logger = java.util.logging.Logger.getLogger(BasicController.class.getName());

    private Gson gson = new Gson();

    <T> T convert(String json, Class<T> tClass) {
        try {
            return gson.fromJson(json, tClass);
        } catch (JsonSyntaxException e) {
            logger.warning(e.getMessage());
            return null;
        }
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

    ParseBox getQueryParam(String paramName, Context context) {
        final String param = context.queryParam(paramName);
        return (BasicUtils.isEmpty(param))
                ? ParseBox.error(RestResponse.error(paramName + " param was not presented"))
                : ParseBox.valid(param);
    }

}
