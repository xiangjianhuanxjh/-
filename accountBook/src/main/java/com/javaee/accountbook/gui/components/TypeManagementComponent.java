package com.javaee.accountbook.gui.components;

import com.javaee.accountbook.pojo.Type;
import com.javaee.accountbook.service.impl.TypeServiceImpl;
import com.javaee.accountbook.utils.SpringContextUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class TypeManagementComponent extends Box {
    //消费类型管理界面
    final int WIDTH = 800;
    final int HEIGHT = 600;

    private JTable accountTable;    //表
    private Vector<String> titles;  //表字段
    private Vector<Vector> tableData;   //存储二维表格数据
    private DefaultTableModel accountTableModel;

    public TypeManagementComponent(JFrame jf) {
        //垂直布局盒子
        super(BoxLayout.Y_AXIS);

        //“添加”、“修改”、“删除”按钮界面
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(236, 242, 179));    //设置背景颜色
        buttonPanel.setMaximumSize(new Dimension(1000, 80));   //设置最大宽高
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));    //设置从右向左的流式布局
        JButton createBtn = new JButton("添加");
        JButton updateBtn = new JButton("修改");
        JButton deleteBtn = new JButton("删除");
        createBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String type = JOptionPane.showInputDialog("请输入新的消费类型");
                if (type == null) {
                    return;
                }
                if (type.length() == 0) {
                    JOptionPane.showMessageDialog(jf, "名称不能为空！");
                    return;
                }

                //这里编写 将用户输入的消费类型加入数据库 的代码
                TypeServiceImpl typeService = SpringContextUtil.getBean(TypeServiceImpl.class);
                typeService.saveType(type);

                JOptionPane.showMessageDialog(jf, "添加成功！");
                getTableData(); //更新表格
            }
        });
        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取选中条目的行号，没有则返回-1
                int selectedRow = accountTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(jf, "请选择需要修改的条目！");
                    return;
                }
                String id = accountTableModel.getValueAt(selectedRow, 0).toString();
                String oldType = accountTableModel.getValueAt(selectedRow, 1).toString();
                String newType = JOptionPane.showInputDialog("请修改消费类型", oldType);

                //这里编写 修改对应id的消费类型 的代码
                TypeServiceImpl typeService = SpringContextUtil.getBean(TypeServiceImpl.class);
                typeService.updateType(oldType,newType);

                JOptionPane.showMessageDialog(jf, "修改成功！");
                getTableData(); //更新表格
            }
        });
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取选中条目的行号，没有则返回-1
                int selectedRow = accountTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(jf, "请选择需要删除的条目！");
                    return;
                }
                //删除操作比较敏感，需要再次跳出对话框确认
                int result = JOptionPane.showConfirmDialog(jf, "确认删除该条目？", "确认删除", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.NO_OPTION) {
                    return;
                }
                String id = accountTableModel.getValueAt(selectedRow, 0).toString();

                //这里编写代码根据id删除数据库对应消费类型数据
                TypeServiceImpl typeService = SpringContextUtil.getBean(TypeServiceImpl.class);
                typeService.deleteType(id);

                JOptionPane.showMessageDialog(jf, "删除成功！");
                getTableData(); //更新表格
            }
        });
        buttonPanel.add(createBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        this.add(buttonPanel);

        //消费账单表
        String[] titlesArray = {"编号", "消费类型"};
        titles = new Vector<String>();
        for (String title : titlesArray) {
            titles.add(title);
        }
        tableData = new Vector<Vector>();
        accountTableModel = new DefaultTableModel(tableData, titles);
        accountTable = new JTable(accountTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;   //设置表格不可直接编辑
            }
        };
        accountTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //设置表格只能选中单行
        JScrollPane sp = new JScrollPane(accountTable);
        this.add(sp);
        getTableData();
    }

    /**
     * 获取所有消费类型数据
     */
    public void getTableData() {
        TypeServiceImpl typeService = SpringContextUtil.getBean(TypeServiceImpl.class);
        Vector<Vector> data = typeService.getAllType();


        tableData.clear();  //先清空数据
        for (Vector datum : data) {
            tableData.add(datum);
        }
        accountTableModel.fireTableDataChanged();   //刷新表格
    }
}
