package me.omico.util.device;

import java.util.Objects;

/**
 * @author Omico 2017/7/22
 */

public class CheckOSVariant extends Property {

    public static final String COMMON = "common";
    public static final String ZUI = "zui";

    public static final String PROP_ZUI_VERSION = "ro.com.zui.version";

    private static String variant;

    public CheckOSVariant init() {
        variant = COMMON;
        if (isZui()) variant = ZUI;
        return this;
    }

    public String getOSVariant() {
        return variant;
    }

    public static boolean isZui() {
        return checkProp(PROP_ZUI_VERSION);
    }

    private static boolean checkProp(String prop) {
        String value = getProperty(prop);
        return value != null && !Objects.equals(value, UNKNOWN) && !value.isEmpty();
    }
}
