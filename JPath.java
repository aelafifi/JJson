package com.example.ahmed.myapplication02.superX;

import java.util.Arrays;

public class JPath {
    private String[] path;

    public JPath(String path) {
        this.path = path.trim().split("\\s*\\.\\s*");
    }

    String pick() {
        String part = this.path[0];
        path = Arrays.copyOfRange(path, 1, path.length);
        return part;
    }

    boolean isDigits(int index) {
        return path[index].matches("^[0-9]+$");
    }

    String get(int index) {
        return path[index];
    }

    int size() {
        return path.length;
    }

    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < path.length; i++) {
            str += "." + path[i];
        }
        return str.substring(1);
    }
}
