package me.omico.util.device;

import android.annotation.SuppressLint;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author Omico 2017/7/23
 */

public class Property {

    private static final String SYSTEM_PROPERTIES = "android.os.SystemProperties";
    private static final String GET = "get";
    private static final String SET = "set";
    private static final String UNKNOWN = "unknown";

    private static Class<?> c;

    @SuppressLint("PrivateApi")
    Property() {
        try {
            c = Class.forName(SYSTEM_PROPERTIES);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        String value;
        try {
            Method get = c.getMethod(GET, String.class, String.class);
            value = (String) (get.invoke(c, key, UNKNOWN));
        } catch (Exception e) {
            e.printStackTrace();
            value = null;
        }
        return value;
    }

    public static void setProperty(String key, String value) {
        try {
            Method set = c.getMethod(SET, String.class, String.class);
            set.invoke(c, key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkProp(String prop) {
        String value = getProperty(prop);
        return value != null && !Objects.equals(value, UNKNOWN);
    }

    public static boolean checkPropExist(String prop) {
        return checkProp(prop) && !getProperty(prop).isEmpty();
    }

    public static boolean checkPropContain(String prop, String containString) {
        return checkProp(prop) && getProperty(prop).contains(containString);
    }
}
