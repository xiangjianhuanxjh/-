package com.javaee.accountbook.gui.components;

import com.javaee.accountbook.gui.listener.ActionDoneListener;
import com.javaee.accountbook.pojo.Record;
import com.javaee.accountbook.service.impl.RecordServiceImpl;
import com.javaee.accountbook.utils.SpringContextUtil;
import org.jdesktop.swingx.JXDatePicker;
import com.javaee.accountbook.gui.util.GetDataUtils;
import com.javaee.accountbook.gui.util.ScreenUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UpdateAccountDialog extends JDialog {
    //编辑账单-》点击”修改“按钮 弹出的对话框

    final int WIDTH = 400;
    final int HEIGHT = 300;
    String id;  //单条账单费用id，用于获取该id账单信息并修改，由EditAccountComponent界面选中单条账单后传递过来
    JTextField moneyTextField;
    JXDatePicker datePicker;
    JComboBox<String> typeComboBox;
    JTextField commentTextField;

    public UpdateAccountDialog(JFrame jf, String title, boolean isModel, ActionDoneListener listener, String id) {
        super(jf, title, isModel);
        this.id = id;
        this.setBounds((ScreenUtils.getScreenWidth() - WIDTH) / 2, (ScreenUtils.getScreenHeight() - HEIGHT) / 2, WIDTH, HEIGHT);

        Box hBox = Box.createHorizontalBox();
        Box vBox = Box.createVerticalBox();


        GetDataUtils getDataUtils = SpringContextUtil.getBean(GetDataUtils.class);;


        JPanel input = new JPanel();
        input.setLayout(new GridLayout(4, 2, 0, 30));
        JLabel moneyLabel = new JLabel("消费金额");
        moneyTextField = new JTextField(15);
        JLabel dateLabel = new JLabel("消费日期");
        datePicker = new JXDatePicker(new Date());
        JLabel typeLabel = new JLabel("消费类型");
        typeComboBox = new JComboBox<String>();

        String[] types =getDataUtils.getTypes();
        for(int i =1;i<types.length;i++){
            typeComboBox.addItem(types[i]);
        }

        JLabel commentLabel = new JLabel("备注");
        commentTextField = new JTextField(15);
        input.add(moneyLabel);
        input.add(moneyTextField);
        input.add(dateLabel);
        input.add(datePicker);
        input.add(typeLabel);
        input.add(typeComboBox);
        input.add(commentLabel);
        input.add(commentTextField);

        Box btnBox = Box.createHorizontalBox();
        JButton updateBtn = new JButton("修改");
        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //获取用户输入
                    double money = Double.parseDouble(moneyTextField.getText().trim());
                    Date date = datePicker.getDate();
                    String type = typeComboBox.getItemAt(typeComboBox.getSelectedIndex());
                    String comment = commentTextField.getText().trim();


                    //这里编写数据库修改代码
                    RecordServiceImpl recordService = SpringContextUtil.getBean(RecordServiceImpl.class);
                    recordService.updateRecord(id,money,date,type,comment);

                    JOptionPane.showMessageDialog(jf, "修改成功！");
                    listener.done(null);    //使用回调函数更新表格
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(jf, "输入了非法字符！", "警告", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnBox.add(updateBtn);

        vBox.add(Box.createVerticalStrut(20));
        vBox.add(input);
        vBox.add(Box.createVerticalStrut(5));
        vBox.add(btnBox);
        vBox.add(Box.createVerticalStrut(20));

        hBox.add(Box.createHorizontalStrut(20));
        hBox.add(vBox);
        hBox.add(Box.createHorizontalStrut(20));

        this.add(hBox);

        getAccount();   //获取单条数据并显示
    }

    /**
     * 根据成员变量id（由EditAccountComponent选中并传递参数过来）获取单条账单数据并显示
     */
    public void getAccount() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

        //测试用例（这里编写代码根据id从数据库获取单条账单信息）
        RecordServiceImpl recordService = SpringContextUtil.getBean(RecordServiceImpl.class);

        Record record = recordService.getById(id);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("cost", record.getCost());
        result.put("date", record.getTime());
        result.put("type", record.getType());
        result.put("comment", record.getComment());

        moneyTextField.setText(result.get("cost").toString());
        try {
            datePicker.setDate(sdf.parse(result.get("date").toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        typeComboBox.setSelectedItem(result.get("type").toString());
        commentTextField.setText(result.get("comment").toString());
    }
}
