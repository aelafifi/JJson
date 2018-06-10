package com.example.ahmed.myapplication02.superX;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JArray extends JValue {

    JSONArray array = null;

    public JArray(Object value) {
        if (value == null) {
            array = new JSONArray();
            return;
        }

        if (value instanceof JArray) {
            array = ((JArray) value).array;
            return;
        }

        if (value instanceof JSONObject) {
            array = (JSONArray) value;
            return;
        }

        try {
            array = new JSONArray(String.valueOf(value));
        } catch (JSONException e) {
        }
    }

    public JArray() {
        this(null);
    }

    @Override
    public String toString() {
        try {
            return array.toString(4);
        } catch (JSONException e) {
            return array.toString();
        }
    }

    @Override
    public boolean isValid() {
        return array != null && array instanceof JSONArray;
    }

    @Override
    public Object opt(String key, Object fallback, boolean create) {
        Object value = wrap(array.opt(Integer.parseInt(key)));
        if (value == null) {
            if (create) {
                put(key, fallback);
            }
            return fallback;
        }
        return value;
    }

    @Override
    public boolean put(String key, Object value) {
        try {
            array.put(Integer.parseInt(key), JValue.unwrap(value));
            return true;
        } catch (JSONException e) {
        }
        return false;
    }

    @Override
    public boolean rm(String key) {
        int index = Integer.parseInt(key);
        if (index > array.length()) {
            return true;
        }
        return put(key, null);
    }

    @Override
    public Object get(String path, Object fallback, boolean create) {
        JPath jPath = new JPath(path);
        JValue object = preparePath(jPath, create);
        if (object == null) {
            return fallback;
        }
        if (!jPath.isDigits(0)) {
            return null;
        }
        return object.opt(jPath.pick(), fallback, create);
    }

    @Override
    public boolean set(String path, Object value) {
        JPath jPath = new JPath(path);
        JValue object = preparePath(jPath, true);
        if (object == null) {
            return false;
        }
        if (!jPath.isDigits(0)) {
            return false;
        }
        return object.put(jPath.pick(), value);
    }

    @Override
    public boolean remove(String path) {
        JPath jPath = new JPath(path);
        JValue object = preparePath(jPath, false);
        if (object == null) {
            return true;
        }
        if (!jPath.isDigits(0)) {
            return true;
        }
        return object.rm(jPath.pick());
    }

    @Override
    public List<String> keys() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add("" + i);
        }
        return list;
    }

    @Override
    public Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<>();
        String key;
        for (int i = 0; i < array.length(); i++) {
            key = "" + i;
            map.put(key, opt(key));
        }
        return map;
    }

    public JSONArray getArray() {
        return array;
    }
}
