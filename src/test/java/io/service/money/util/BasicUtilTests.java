package io.service.money.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 15.11.2018
 */
public class BasicUtilTests extends Assert {

    @Test
    public void isStrEmpty() {
        assertTrue(BasicUtils.isEmpty(""));
    }

    @Test
    public void isStrEmptyForNull() {
        String value = null;
        assertTrue(BasicUtils.isEmpty(value));
    }

    @Test
    public void isCollectionEmpty() {
        ArrayList<Object> list = new ArrayList<>();
        assertTrue(BasicUtils.isEmpty(list));
    }

    @Test
    public void isCollectionEmptyForNull() {
        ArrayList<Object> list = null;
        assertTrue(BasicUtils.isEmpty(list));
    }

    @Test
    public void isMapEmpty() {
        Map<String, Object> map = new HashMap<>();
        assertTrue(BasicUtils.isEmpty(map));
    }

    @Test
    public void isMapEmptyForNull() {
        Map<String, Object> map = null;
        assertTrue(BasicUtils.isEmpty(map));
    }
}
