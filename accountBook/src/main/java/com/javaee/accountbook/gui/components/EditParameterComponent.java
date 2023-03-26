package com.javaee.accountbook.gui.components;

import com.javaee.accountbook.gui.MyTreeNodeRenderer;
import com.javaee.accountbook.gui.util.ScreenUtils ;
import com.javaee.accountbook.pojo.SystemConfig;
import com.javaee.accountbook.service.impl.SystemConfigServiceImpl;
import com.javaee.accountbook.utils.SpringContextUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class EditParameterComponent extends Box {
    Border border1;
    Border border2;
    Border border3;
    public EditParameterComponent(JFrame jf, JTree tree, MyTreeNodeRenderer myRenderer){
        super(BoxLayout.Y_AXIS);
        //确定界面的组件
        JPanel title_panel = new JPanel();//标题组件
        Box box1 = Box.createVerticalBox();//大小组件
        Box adjust_box1 = Box.createHorizontalBox();//间隙组件
        Box adjust_box2 = Box.createHorizontalBox();//间隙组件
        Box adjust_box3 = Box.createHorizontalBox();//间隙组件
        Box box2 = Box.createVerticalBox();//颜色组件
        Box box3 = Box.createVerticalBox();//预算组件
        //title_panel设计
        title_panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        title_panel.setMaximumSize(new Dimension(1000,30));
//        title_panel.add(new JLabel("欢迎对界面参数进行调整"));
        adjust_box1.setMaximumSize(new Dimension(60,40));
        adjust_box2.setMaximumSize(new Dimension(60,25));
        adjust_box3.setMaximumSize(new Dimension(60,25));
        //box1组件设计
        box1.setMaximumSize(new Dimension(500,250));
        border1 = new EtchedBorder(EtchedBorder.RAISED);    //组件边界划线
        box1.setBorder(border1);
        JPanel width_panel = new JPanel();
        JPanel height_panel = new JPanel();
        JPanel button_panel = new JPanel();
        JButton size_button = new JButton("确定");
        JComboBox<Integer> width = new JComboBox<Integer>();
        width.addItem(1000);
        width.addItem(1050);
        width.addItem(1100);
        width.addItem(1150);
        width.addItem(1200);
        JComboBox<Integer> height = new JComboBox<Integer>();
        height.addItem(600);
        height.addItem(630);
        height.addItem(660);
        height.addItem(690);
        height.addItem(720);
        box1.add(Box.createVerticalStrut(10));
        box1.add(new JLabel("请设置系统的界面大小:"));
        box1.add(Box.createVerticalStrut(10));
        width_panel.add(new JLabel("设置系统宽度"));
        width_panel.add(width);
        box1.add(width_panel);
        box1.add(Box.createVerticalStrut(10));
        height_panel.add(new JLabel("设置系统高度"));
        height_panel.add(height);
        box1.add(width_panel);
        box1.add(Box.createVerticalStrut(10));
        box1.add(height_panel);
        box1.add(Box.createVerticalStrut(10));
        button_panel.add(size_button);
        box1.add(button_panel);
        //间隙组件设置
        adjust_box1.setMaximumSize(new Dimension(60,40));
        //box2组件设计
        border2 = new EtchedBorder(EtchedBorder.RAISED);
        box2.setBorder(border2);
        box2.add(Box.createVerticalStrut(10));
        box2.add(new JLabel("请设置界面的具体颜色"));
        JPanel color_panel = new JPanel();
        JComboBox<String> color_option = new JComboBox<String>();
        JPanel color_button_panel = new JPanel();
        color_option.addItem("黄色");
        color_option.addItem("绿色");
        color_option.addItem("蓝色");
        color_option.addItem("红色");
        color_panel.add(new JLabel("请选择界面颜色"));
        color_panel.add(color_option);
        JButton color_button = new JButton("确定");
        color_button_panel.add(color_button);
        box2.add(Box.createVerticalStrut(10));
        box2.add(color_panel);
        box2.add(Box.createVerticalStrut(10));
        box2.add(color_button_panel);
        box2.setMaximumSize(new Dimension(500,200));

        border3 = new EtchedBorder(EtchedBorder.RAISED);
        box3.setBorder(border3);
        box3.add(new JLabel("请设置你的预算"));
        box3.setMaximumSize(new Dimension(500,75));

        JButton budgetButton = new JButton("确定");
        JPanel budgetPanel = new JPanel();
        JTextField budgetTextField = new JTextField(); // 创建一个单行输入框
        budgetTextField.setEditable(true); // 设置输入框允许编辑
        budgetTextField.setColumns(11); // 设置输入框的长度为11个字符
        budgetPanel.add(budgetTextField); // 在面板上添加单行输入框
        box2.add(Box.createVerticalStrut(10));
        budgetPanel.add(budgetButton);
        box3.add(budgetPanel);

        this.add(title_panel);
        this.add(box1);
        this.add(adjust_box1);
        this.add(box2);
        this.add(adjust_box3);
        this.add(box3);
        size_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int new_width = width.getItemAt(width.getSelectedIndex());//获取用户选择的新宽度
                int new_height = height.getItemAt(height.getSelectedIndex());//获取用户选择的新高度
                title_panel.setMaximumSize(new Dimension(new_width,new_height/20));
                jf.setBounds((ScreenUtils.getScreenWidth() - new_width) / 2, (ScreenUtils.getScreenHeight() - new_height) / 2, new_width, new_height);

            }
        });
        color_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected_color = color_option.getItemAt(color_option.getSelectedIndex());
                Color set;
                if(Objects.equals(selected_color, "红色")){set = new Color(194,60,158);}
                else if(Objects.equals(selected_color, "蓝色")){set = new Color(38,113,216);}
                else if(Objects.equals(selected_color, "绿色")){set = new Color(56,198,150);}
                else {set = new Color(236, 242, 179);}
                tree.setBackground(set);
                myRenderer.setBackground(set);
                myRenderer.setBackgroundNonSelectionColor(set);
                tree.setCellRenderer(myRenderer);

            }
        });

        budgetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取用户输入
                double money = Double.parseDouble(budgetTextField.getText().trim());

                //这里编写数据库插入代码
                SystemConfigServiceImpl systemConfigService = SpringContextUtil.getBean(SystemConfigServiceImpl.class);
                SystemConfig budget = new SystemConfig(1L, money);
                systemConfigService.updateById(budget);

                JOptionPane.showMessageDialog(jf, "设置成功！");

            }
        });
    }
}
