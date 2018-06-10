package com.example.ahmed.myapplication02.superX;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class JValue {
    public static final Object NULL = new Object() {
        @Override
        public boolean equals(Object o) {
            return o == this || o == null;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public String toString() {
            return "null";
        }
    };

    public abstract String toString();
    public abstract boolean isValid();

    public abstract Object opt(String key, Object fallback, boolean create);
    public abstract boolean put(String key, Object value);
    public abstract boolean rm(String key);

    public abstract Object get(String path, Object fallback, boolean create);
    public abstract boolean set(String path, Object value);
    public abstract boolean remove(String path);

    public abstract List<String> keys();
    public abstract Map<String, Object> getMap();

    private Map<String, String> objectMapToStringMap(Map<String, Object> map) {
        Map<String, String> ret = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            ret.put(entry.getKey(), String.valueOf(entry.getValue()));
        }
        return ret;
    }

    public Map<String, String> getStringMap() {
        return objectMapToStringMap(getMap());
    }

    public Map<String, Object> ease(JKeyMode mode) {
        return _ease("", this, mode);
    }

    public Map<String, Object> ease() {
        return ease(JKeyMode.Object);
    }

    public Map<String, String> easeString(JKeyMode mode) {
        return objectMapToStringMap(ease(mode));
    }

    public Map<String, String> easeString() {
        return easeString(JKeyMode.Object);
    }

    public String getQueryString() {
        Uri.Builder builder = Uri.parse("").buildUpon();
        for (Map.Entry<String, String> entry : easeString().entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue());
        }
        return builder.build().toString().substring(1);
    }

    public Object opt(String key, Object fallback) {
        return opt(key, fallback, false);
    }

    public Object opt(String key) {
        return opt(key, null);
    }

    public Object optNN(String key) {
        return opt(key, NULL);
    }

    public Object get(String path, Object fallback) {
        return get(path, fallback, false);
    }

    public Object get(String path) {
        return get(path, null);
    }

    public Object getNN(String path) {
        return get(path, NULL);
    }

    public static Object wrap(Object value) {
        if (value instanceof JSONObject) {
            return new JObject(value);
        }
        if (value instanceof JSONArray) {
            return new JArray(value);
        }
        return value;
    }
    public static Object unwrap(Object value) {
        if (value instanceof JObject) {
            return ((JObject) value).object;
        }
        if (value instanceof JArray) {
            return ((JArray) value).array;
        }
        return value;
    }

    protected JValue preparePath(JPath path, boolean create) {
        String key;
        JValue object = this;
        while (path.size() > 1) {
            if (object == null || !object.isValid()) {
                return null;
            }

            key = path.pick();

            Object value = object.opt(key);
            if (value == null) {
                if (create) {
                    return object.createPath(new JPath(key + "." + path.toString()));
                }
                return null;
            }
            if (value instanceof JObject) {
                object = new JObject(value);
                continue;
            }
            if (value instanceof JArray) {
                object = new JArray(value);
                continue;
            }
            return null;
        }
        return object;
    }

    private JValue createPath(JPath path) {
        String key;
        JValue object = this;
        while (path.size() > 1) {
            if (object == null || !object.isValid()) {
                return null;
            }
            key = path.pick();
            if (path.isDigits(0)) {
                // expect an array
                JArray obj = new JArray();
                if (!object.put(key, obj)) {
                    return null;
                }
                object = obj;
            } else {
                // expect an object
                JObject obj = new JObject();
                if (!object.put(key, obj)) {
                    return null;
                }
                object = obj;
            }
        }
        return object;
    }

    private String buildPath(String oldPath, String newPath, JKeyMode mode) {
        if (oldPath.equals("")) {
            return newPath;
        }
        if (mode == JKeyMode.Array) {
            return String.format("%s[%s]", oldPath, newPath);
        }
        return String.format("%s.%s", oldPath, newPath);
    }

    private Map<String, Object> _ease(String path, JValue object, JKeyMode mode) {
        Map<String, Object> map = new HashMap<>();
        String key;
        Object value;
        for (Map.Entry<String, Object> entry : object.getMap().entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            if (value instanceof JValue) {
                Map<String, Object> childMap = _ease(
                        buildPath(path, key, mode), (JValue) value, mode);
                for (Map.Entry<String, Object> entry2 : childMap.entrySet()) {
                    map.put(entry2.getKey(), entry2.getValue());
                }
                continue;
            }
            map.put(buildPath(path, key, mode), value);
        }
        return map;
    }
}
