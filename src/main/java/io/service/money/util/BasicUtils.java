package io.service.money.util;

import java.util.Collection;
import java.util.Map;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 13.11.2018
 */
public class BasicUtils {

    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <K, T> boolean isEmpty(Map<K, T> map) {
        return map == null || map.isEmpty();
    }
}
