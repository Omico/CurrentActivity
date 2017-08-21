package me.omico.util.device;

import java.util.Objects;

/**
 * @author Omico 2017/7/22
 */

public class CheckOSVariant extends Property {

    public static final String COMMON = "common";
    public static final String ZUI = "zui";
    public static final String FLYME = "flyme";

    public static final String PROP_COMMON_BUILD_DISPLAY_ID = "ro.build.display.id";
    public static final String PROP_ZUI_VERSION = "ro.com.zui.version";
    public static final String PROP_FLYME_MODEL = "ro.product.flyme.model";

    private static String variant;

    public CheckOSVariant init() {
        variant = COMMON;
        if (isZui()) {
            variant = ZUI;
        } else if (isFlyme()) {
            variant = FLYME;
        }
        return this;
    }

    public String getOSVariant() {
        return variant;
    }

    public static boolean isZui() {
        return checkPropExist(PROP_ZUI_VERSION);
    }

    public static boolean isFlyme() {
        return checkPropExist(PROP_FLYME_MODEL) || checkPropContain(PROP_COMMON_BUILD_DISPLAY_ID, "Flyme");
    }

    private static boolean checkProp(String prop) {
        String value = getProperty(prop);
        return value != null && !Objects.equals(value, UNKNOWN);
    }

    private static boolean checkPropExist(String prop) {
        return checkProp(prop) && !getProperty(prop).isEmpty();
    }

    private static boolean checkPropContain(String prop, String containString) {
        return checkProp(prop) && getProperty(prop).contains(containString);
    }
}
