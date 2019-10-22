package com.tik.android.component.libcommon;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonUtil {

    private static final Gson GSON = new GsonBuilder().create();

    public static <T> T json2object(String jsonString, Class<T> cls) {
        try {
            return (T) GSON.fromJson(jsonString, cls);
        } catch (Exception e) {
        }
        return null;
    }

    public static <T> T map2object(Map map, Class<T> cls) {
        try {
            JsonElement jsonElement = GSON.toJsonTree(map);
            return (T) GSON.fromJson(jsonElement, cls);
        } catch (Exception e) {
        }
        return null;
    }


    public static <T> List<T> json2List(String jsonString, Class<T> cls) {
        List<T> list = new ArrayList<>();
        try {
            list = GSON.fromJson(jsonString, new TypeToken<List<T>>() {
            }.getType());
        } catch (Exception e) {
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public static <T> T json2object(String json, TypeToken<T> typeToken) {
        try {
            return (T) GSON.fromJson(json, typeToken.getType());
        } catch (Exception e) {
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T json2object(String json, Type type) {
        try {
            return (T) GSON.fromJson(json, type);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * java对象转为json对象
     */
    public static String object2json(Object obj) {
        return GSON.toJson(obj);
    }
}
