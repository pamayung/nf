package com.wildhan.nf.core.util;

public class TextUtils {
    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    public static boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
