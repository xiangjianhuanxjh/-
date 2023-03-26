package com.javaee.accountbook.gui.util;

import java.awt.*;

public class ScreenUtils {
    public static int getScreenWidth() {
        //获取电脑屏幕宽度
        return Toolkit.getDefaultToolkit().getScreenSize().width;
    }

    public static int getScreenHeight() {
        //获取电脑屏幕宽度
        return Toolkit.getDefaultToolkit().getScreenSize().height;
    }
}
