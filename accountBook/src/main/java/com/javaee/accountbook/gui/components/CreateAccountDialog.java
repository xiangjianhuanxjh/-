package com.javaee.accountbook.gui.components;

import com.javaee.accountbook.gui.listener.ActionDoneListener;
import com.javaee.accountbook.pojo.Record;
import com.javaee.accountbook.service.impl.RecordServiceImpl;
import com.javaee.accountbook.utils.SpringContextUtil;
import org.jdesktop.swingx.JXDatePicker;
import com.javaee.accountbook.gui.util.GetDataUtils;
import com.javaee.accountbook.gui.util.ScreenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;



public class CreateAccountDialog extends JDialog {
    //编辑账单-》点击”添加“按钮 弹出的对话框

    final int WIDTH = 400;
    final int HEIGHT = 300;



    public CreateAccountDialog(JFrame jf, String title, boolean isModel, ActionDoneListener listener) {
        super(jf, title, isModel);
        this.setBounds((ScreenUtils.getScreenWidth() - WIDTH) / 2, (ScreenUtils.getScreenHeight() - HEIGHT) / 2, WIDTH, HEIGHT);

        Box hBox = Box.createHorizontalBox();
        Box vBox = Box.createVerticalBox();

        JPanel input = new JPanel();
        input.setLayout(new GridLayout(4, 2, 0, 30));
        JLabel moneyLabel = new JLabel("消费金额");
        JTextField moneyTextField = new JTextField(15);
        JLabel dateLabel = new JLabel("消费日期");
        JXDatePicker datePicker = new JXDatePicker(new Date());
        JLabel typeLabel = new JLabel("消费类型");
        JComboBox<String> typeComboBox = new JComboBox<String>();

        GetDataUtils getDataUtils = SpringContextUtil.getBean(GetDataUtils.class);
        String[] types =getDataUtils.getTypes();
        for(int i =1;i<types.length;i++){
            typeComboBox.addItem(types[i]);
        }

        JLabel commentLabel = new JLabel("备注");
        JTextField commentTextField = new JTextField(15);
        input.add(moneyLabel);
        input.add(moneyTextField);
        input.add(dateLabel);
        input.add(datePicker);
        input.add(typeLabel);
        input.add(typeComboBox);
        input.add(commentLabel);
        input.add(commentTextField);

        Box btnBox = Box.createHorizontalBox();
        JButton createBtn = new JButton("添加");
        createBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //获取用户输入
                    double money = Double.parseDouble(moneyTextField.getText().trim());
                    Date date = datePicker.getDate();
                    String type = typeComboBox.getItemAt(typeComboBox.getSelectedIndex());
                    String comment = commentTextField.getText().trim();

                    //这里编写数据库插入代码
                    RecordServiceImpl recordService = SpringContextUtil.getBean(RecordServiceImpl.class);
                    recordService.insertRecord(money,date,type,comment);

                    JOptionPane.showMessageDialog(jf, "添加成功！");
                    listener.done(null);    //使用回调函数更新表格
                    dispose();
                } catch (Exception ex) {
                    System.out.println(ex);
                    JOptionPane.showMessageDialog(jf, "输入了非法字符！", "警告", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnBox.add(createBtn);

        vBox.add(Box.createVerticalStrut(20));
        vBox.add(input);
        vBox.add(Box.createVerticalStrut(5));
        vBox.add(btnBox);
        vBox.add(Box.createVerticalStrut(20));

        hBox.add(Box.createHorizontalStrut(20));
        hBox.add(vBox);
        hBox.add(Box.createHorizontalStrut(20));

        this.add(hBox);
    }
}
