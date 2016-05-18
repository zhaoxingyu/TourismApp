package com.buu.tourism.util;

import android.content.Context;

public class ResourceUtil {

    public static int getLayoutId(Context paramContext, String paramString) {
        if ((paramContext == null) || (paramString == null) || paramString.trim().equals("")) {
            return -1;
        }
        try {
            return paramContext.getResources().getIdentifier(paramString, "layout", paramContext.getPackageName());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }
    }

    public static int getStringId(Context paramContext, String paramString) {
        if ((paramContext == null) || (paramString == null) || paramString.trim().equals("")) {
            return -1;
        }
        try {
            return paramContext.getResources().getIdentifier(paramString, "string", paramContext.getPackageName());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }
    }

    public static int getDrawableId(Context paramContext, String paramString) {
        if ((paramContext == null) || (paramString == null) || paramString.trim().equals("")) {
            return -1;
        }
        try {
            return paramContext.getResources().getIdentifier(paramString, "drawable", paramContext.getPackageName());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }
    }

    public static int getStyleId(Context paramContext, String paramString) {
        if ((paramContext == null) || (paramString == null) || paramString.trim().equals("")) {
            return -1;
        }
        try {
            return paramContext.getResources().getIdentifier(paramString, "style", paramContext.getPackageName());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }
    }

    public static int getId(Context paramContext, String paramString) {
        if ((paramContext == null) || (paramString == null) || paramString.trim().equals("")) {
            return -1;
        }
        try {
            return paramContext.getResources().getIdentifier(paramString, "id", paramContext.getPackageName());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }
    }

    public static int getColorId(Context paramContext, String paramString) {
        if ((paramContext == null) || (paramString == null) || paramString.trim().equals("")) {
            return -1;
        }
        try {
            return paramContext.getResources().getIdentifier(paramString, "color", paramContext.getPackageName());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }
    }
    
    public static int getDimenId(Context paramContext, String paramString) {
        if ((paramContext == null) || (paramString == null) || paramString.trim().equals("")) {
            return -1;
        }
        try {
            return paramContext.getResources().getIdentifier(paramString, "dimen", paramContext.getPackageName());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }
    }

}
