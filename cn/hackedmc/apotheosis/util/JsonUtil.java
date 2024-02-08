package cn.hackedmc.apotheosis.util;

import com.google.gson.JsonObject;

public class JsonUtil {
    private final JsonObject object;

    public JsonUtil(JsonObject object) {
        this.object = object;
    }

    public String getString(String name, String defaultValue) {
        return object.has(name) ? object.get(name).getAsString() : defaultValue;
    }
}
