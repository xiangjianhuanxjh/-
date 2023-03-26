package com.javaee.accountbook.gui;

import com.javaee.accountbook.gui.util.PathUtils;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class MyTreeNodeRenderer extends DefaultTreeCellRenderer {
    //自定义树节点渲染器用于修改树节点的图标
    private ImageIcon rootIcon = null;
    private ImageIcon accountManagementIcon = null;
    private ImageIcon editAccountIcon = null;
    private ImageIcon checkAccountIcon = null;
    private ImageIcon statisticIcon = null;
    private ImageIcon tallyUpAccountIcon = null;
    private ImageIcon systemManagementIcon = null;
    private ImageIcon typeManagementIcon = null;
    private ImageIcon editParameterIcon = null;

    public MyTreeNodeRenderer() {
        rootIcon = new ImageIcon("src/main/resources/images/accountBook.png");
        accountManagementIcon = new ImageIcon("src/main/resources/images/accountManagement.png");
        editAccountIcon = new ImageIcon("src/main/resources/images/editAccount.png");
        checkAccountIcon = new ImageIcon("src/main/resources/images/checkAccount.png");
        statisticIcon = new ImageIcon("src/main/resources/images/statisticPicture.png");
        systemManagementIcon = new ImageIcon("src/main/resources/images/systemManagement.png");
        typeManagementIcon = new ImageIcon("src/main/resources/images/typeManagement.png");
        editParameterIcon = new ImageIcon("src/main/resources/images/editParameter.png");
    }

    //每画一个节点都会执行一次该函数
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        ImageIcon icon = null;
        //row即是表示渲染第几个节点
        switch(row) {
            case 0:
                icon = rootIcon;
                break;
            case 1:
                icon = accountManagementIcon;
                break;
            case 2:
                icon = editAccountIcon;
                break;
            case 3:
                icon = checkAccountIcon;
                break;
            case 4:
                icon = statisticIcon;
                break;
            case 5:
                icon = systemManagementIcon;
                break;
            case 6:
                icon = typeManagementIcon;
                break;
            case 7:
                icon = editParameterIcon;
                break;
        }
        this.setIcon(icon);
        return this;
    }
}
