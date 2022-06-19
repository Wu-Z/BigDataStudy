package com.flink.jerry.common.utils;

import com.google.gson.Gson;

public class GsonUtil {

    private static Gson gson = new Gson();

    public static String toJson(Object o) {
        return gson.toJson(o);
    }

}
