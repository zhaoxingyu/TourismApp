package com.buu.tourism.bean;

/**
 * 景区信息bean，通过server返回的数据来生成对象
 * @author xingyu10
 *
 */
public class ScenicInfoBean extends TourismBean {

    private static final long serialVersionUID = 1L;

    public ScenicInfos data;

    public class ScenicInfos extends Bean {

        private static final long serialVersionUID = 1L;

        public ScenicInfo[] result;
    }

    

}