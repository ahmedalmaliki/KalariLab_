package com.example.kalarilab;

import java.util.HashMap;
import java.util.Map;

public class VolleyCallbackMap {
    private Map<String, String> map = new HashMap<>();

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map.putAll(map);
    }



}
