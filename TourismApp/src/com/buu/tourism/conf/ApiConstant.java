package com.buu.tourism.conf;

public class ApiConstant {
    // 主机
    public static String mockup = "0";
    
    public static final String HOST_ONLINE = "http://101.200.143.180/";

    /** server返回的状态 */
    public static final String KEY_STATUS = "status";
    /** server返回的数据 */
    public static final String KEY_DATA = "data";
    /** server出现异常时,返回的描述信息 */
    public static final String KEY_MSG = "msg";

    public static final String HOST_TEST = "http://123.57.90.40/";

    public static class ResponseCode {
        /** 成功 */
        public static final int SUCCESS = 0; // 成功
        /** 用户未登录 */
        public static final int USER_OFFLINE = 1; // 用户未登录
        /** 会话已经过期，请重新登录 */
        public static final int SESSION_EXPIRED = 2; // 会话已经过期，请重新登录
        /** 发生异常 */
        public static final int EXCEPTION_OCCUR = 3; // 发生异常
        /** api不支持,用于错误的api id */
        public static final int API_NOT_SUPPORT = 4; // api不支持,用于错误的api id
        /** app版本过老，已不再维护 */
        public static final int APP_VERSION_EXPIRED = 5; // app版本过老，已不再维护
        /** 外部依赖错误 */
        public static final int APP_OUTER_DEPENDENCY_ERROR = 6; // 外部依赖错误
        /** 参数错误 */
        public static final int PARAMETER_ERROR = 7; // 参数错误
    }
}
