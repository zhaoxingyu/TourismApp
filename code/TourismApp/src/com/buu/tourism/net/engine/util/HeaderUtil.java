package com.buu.tourism.net.engine.util;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.buu.tourism.net.engine.CaseInsensitiveMapVS;

public class HeaderUtil {
    public static final String HEADER_STATUS_KEY = "statusline";

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
     * 获取header中的状态行信息
     * @param key
     * @param header
     * @return
     */
    public static String getStatusLine(Map<String, List<String>> header) {
        String value = "";
        if (null != header) {
            List<String> values = header.get(HEADER_STATUS_KEY);
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
        Map<String, String> result = new CaseInsensitiveMapVS();
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
