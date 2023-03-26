package com.javaee.accountbook.gui;

import com.javaee.accountbook.gui.components.*;
import com.javaee.accountbook.gui.util.GetDataUtils;
import com.javaee.accountbook.gui.util.ScreenUtils;
import com.javaee.accountbook.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.io.File;
import java.io.IOException;


@Component
public class MainGUI {
    JFrame mainWindow = new JFrame("记账本");
    final int WIDTH = 1000;
    final int HEIGHT = 600;

    GetDataUtils getDataUtils;

    //组装界面
    public void init() throws Exception{
        //设置窗口在屏幕正中
        mainWindow.setBounds((ScreenUtils.getScreenWidth() - WIDTH) / 2, (ScreenUtils.getScreenHeight() - HEIGHT) / 2, WIDTH, HEIGHT);
        //设置窗口不允许缩放
        mainWindow.setResizable(false);
        //设置窗口左上角图标
        mainWindow.setIconImage(ImageIO.read(new File("src/main/resources/images/accountBook.png")));
        //使用水平分割面板，分线移动时组件连续变化
        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);

        //使用树组件用于功能选择
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("记账本");
        DefaultMutableTreeNode accountManagement = new DefaultMutableTreeNode("账单管理");
        DefaultMutableTreeNode systemManagement = new DefaultMutableTreeNode("系统管理");
        DefaultMutableTreeNode editAccount = new DefaultMutableTreeNode("编辑账单");
        DefaultMutableTreeNode checkAccount = new DefaultMutableTreeNode("查询账单");
        DefaultMutableTreeNode statistic = new DefaultMutableTreeNode("数据统计");
        DefaultMutableTreeNode editParameter = new DefaultMutableTreeNode("参数设置");
        DefaultMutableTreeNode typeManagement = new DefaultMutableTreeNode("消费类型管理");
        accountManagement.add(editAccount);
        accountManagement.add(checkAccount);
        accountManagement.add(statistic);
        systemManagement.add(typeManagement);
        systemManagement.add(editParameter);
        root.add(accountManagement);
        root.add(systemManagement);
        JTree tree = new JTree(root);   //创建树
        Color bgColor = new Color(236, 242, 179);  //设置树的背景颜色
        MyTreeNodeRenderer myRenderer = new MyTreeNodeRenderer();
        myRenderer.setBackgroundNonSelectionColor(bgColor); //设置树节点未选中时背景颜色与树的背景颜色一致
        myRenderer.setBackgroundSelectionColor(new Color(140, 140, 140));    //设置树节点被选中时背景颜色

        tree.setBackground(bgColor);    //设置树的背景颜色
        tree.setCellRenderer(myRenderer);    //设置自定义树节点渲染器渲染图标
        tree.expandRow(1);  //设置默认展开“账单管理”
        tree.setSelectionRow(3);    //设置默认选中“查询及统计账单”
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                //获取选中节点对象并根据选中节点在水平分割面板右边显示
                Object node = e.getNewLeadSelectionPath().getLastPathComponent();
                if (editAccount.equals(node)) {
                    sp.setRightComponent(new EditAccountComponent(mainWindow));
                } else if (checkAccount.equals(node)) {
                    try {
                        sp.setRightComponent(new CheckAccountComponent(mainWindow,getDataUtils));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (statistic.equals(node)) {
                    sp.setRightComponent(new StatisticComponent(mainWindow));

                }else if (typeManagement.equals(node)) {
                    sp.setRightComponent(new TypeManagementComponent(mainWindow));
                }else if (editParameter.equals(node)) {
                    sp.setRightComponent(new EditParameterComponent(mainWindow,tree,myRenderer));
                }
                sp.setDividerLocation(200);
            }
        });

        //设置水平分割面板属性
        sp.setLeftComponent(tree);
        sp.setRightComponent(new CheckAccountComponent(mainWindow,getDataUtils));
        sp.setDividerLocation(200);
        sp.setDividerSize(10);

        //将水平分割面板加入mainWindow
        mainWindow.add(sp);
        //设置右上角叉叉关闭方式
//        mainWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置为
        //设置窗口可见
        mainWindow.setVisible(true);
    }

//    @PostConstruct
//    public void start(){
//        this.getDataUtils =  SpringContextUtil.getBean(GetDataUtils.class);
//        System.out.println("----------------------------");
//        System.out.println(getDataUtils.getTypes());
//        System.out.println("----------------------------");
//    }

    public MainGUI( GetDataUtils getDataUtils) throws Exception {
        this.getDataUtils = getDataUtils;
        System.out.println("----------------------------");
        System.out.println(getDataUtils.getTypes());
        System.out.println("----------------------------");
        init();
    }
}
