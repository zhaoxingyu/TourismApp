package com.buu.tourism.net.engine.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class HeaderUtil {
    /**
     * 获取header中key对应的value
     * @param key
     * @param header
     * @return
     */
    public static String getValue(String key, Map<String, List<String>> header) {
        String value = null;
        if (null != header) {
            List<String> values = header.get(key);
            if (null != values && values.size() > 0) {
                value = values.get(0);
            }
        }
        return value;
    }
    
    /**
     * 转换header为Map对象
     * 
     * @param header
     * @return
     */
    public static Map<String, String> convertHeader2Map(Map<String, List<String>> header) {
        Map<String, String> result = new HashMap<String, String>();
        if (null != header) {
            Set<Entry<String, List<String>>> entrySet = header.entrySet();
            if (entrySet != null) {
                try {
                    for (Entry<String, List<String>> entry : entrySet) {
                        String key = entry.getKey();
                        List<String> value = entry.getValue();
                        if (null != value && value.size() > 0) {
                            result.put(key, value.get(0));
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        return result;
    }
}
