package com.example.ahmed.myapplication02.superX;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JObject extends JValue {

    JSONObject object = null;

    public JObject(Object value) {
        if (value == null) {
            object = new JSONObject();
            return;
        }

        if (value instanceof JObject) {
            object = ((JObject) value).object;
            return;
        }

        if (value instanceof JSONObject) {
            object = (JSONObject) value;
            return;
        }

        try {
            object = new JSONObject(String.valueOf(value));
        } catch (JSONException e) {
        }
    }

    public JObject() {
        this(null);
    }

    @Override
    public String toString() {
        try {
            return object.toString(4);
        } catch (JSONException e) {
            return object.toString();
        }
    }

    @Override
    public boolean isValid() {
        return object != null && object instanceof JSONObject;
    }

    @Override
    public Object opt(String key, Object fallback, boolean create) {
        Object value = wrap(object.opt(key));
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
            object.put(key, JValue.unwrap(value));
            return true;
        } catch (JSONException e) {
        }
        return false;
    }

    @Override
    public boolean rm(String key) {
        object.remove(key);
        return true;
    }

    @Override
    public Object get(String path, Object fallback, boolean create) {
        JPath jPath = new JPath(path);
        JValue object = preparePath(jPath, create);
        if (object == null) {
            return fallback;
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
        return object.put(jPath.pick(), value);
    }

    @Override
    public boolean remove(String path) {
        JPath jPath = new JPath(path);
        JValue object = preparePath(jPath, false);
        if (object == null) {
            return true;
        }
        return object.rm(jPath.pick());
    }

    @Override
    public List<String> keys() {
        Iterator<String> keys = object.keys();
        List<String> list = new ArrayList<>();
        String key;
        while (keys.hasNext()) {
            key = keys.next();
            list.add(key);
        }
        return list;
    }

    @Override
    public Map<String, Object> getMap() {
        Iterator<String> keys = object.keys();
        Map<String, Object> map = new HashMap<>();
        String key;
        while (keys.hasNext()) {
            key = keys.next();
            Object value = opt(key);
            map.put(key, value);
        }
        return map;
    }

    public JSONObject getObject() {
        return object;
    }
}
