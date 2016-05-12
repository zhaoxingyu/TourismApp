package com.buu.tourism.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONStringer;

public class JsonUtil {
    
    public static String convert2JsonStr(Map<String,String> data) {
        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object();
            Set<Entry<String, String>> set = data.entrySet();
            for (Entry<String, String> entry : set) {
                String key = entry.getKey();
                String value = entry.getValue();
                jsonStringer.key(key).value(value);
            }
            jsonStringer.endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonStringer.toString();
    }
}
