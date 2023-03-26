package com.javaee.accountbook.gui.util;

public class PathUtils {
    private static final String PATH = "images/";

    public static String getRealPath(String relativePath) {
        return PATH + relativePath;
    }

}
