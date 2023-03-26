package com.javaee.accountbook.gui.components;

import com.javaee.accountbook.gui.listener.ActionDoneListener;
import com.javaee.accountbook.pojo.Record;
import com.javaee.accountbook.service.SystemConfigService;
import com.javaee.accountbook.service.impl.RecordServiceImpl;
import com.javaee.accountbook.service.impl.SystemConfigServiceImpl;
import com.javaee.accountbook.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Vector;
import java.util.jar.JarEntry;


public class EditAccountComponent extends Box {
    //编辑账单界面
    final int WIDTH = 800;
    final int HEIGHT = 600;

    private JTable accountTable;    //表
    private Vector<String> titles;  //表字段
    private Vector<Vector> tableData;   //存储二维表格数据
    private DefaultTableModel accountTableModel;

    //当前表格显示账单日期
    private LocalDate presentDate = LocalDate.now();
    JLabel presentDateLabel = new JLabel();


    //本月支出
    JLabel costThisMonthNumberLabel;
    //剩余预算
    JLabel residualBudgetNumberLabel;

    public EditAccountComponent(JFrame jf) {
        //垂直布局盒子
        super(BoxLayout.Y_AXIS);

        //“添加”、“修改”、“删除”按钮界面
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(236, 242, 179));    //设置背景颜色
        buttonPanel.setMaximumSize(new Dimension(1000, 80));   //设置最大宽高
        buttonPanel.setLayout(new GridLayout(1, 10));    //设置网格布局
        JButton getPreviousDateBillBtn = new JButton("前一天");
        getPreviousDateBillBtn.setIcon(new ImageIcon("src/main/resources/images/arrow_left_32x32.png"));
        JButton getNextDateBillBtn = new JButton("后一天");
        getNextDateBillBtn.setIcon(new ImageIcon("src/main/resources/images/arrow_right_32x32.png"));
        JLabel costThisMonthLabel = new JLabel("本月支出");
        costThisMonthLabel.setIcon(new ImageIcon("src/main/resources/images/资金.png"));
        costThisMonthNumberLabel = new JLabel();
        JLabel residualBudgetLabel = new JLabel("剩余预算");
        residualBudgetLabel.setIcon(new ImageIcon("src/main/resources/images/资金.png"));
        residualBudgetNumberLabel = new JLabel();
        JButton createBtn = new JButton("添加");
        createBtn.setIcon(new ImageIcon("src/main/resources/images/添加.png"));
        JButton updateBtn = new JButton("修改");
        updateBtn.setIcon(new ImageIcon("src/main/resources/images/修改.png"));
        JButton deleteBtn = new JButton("删除");
        deleteBtn.setIcon(new ImageIcon("src/main/resources/images/删除.png"));
        getPreviousDateBillBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presentDate = presentDate.minusDays(1);
                presentDateLabel.setText(String.valueOf(presentDate));
                getTableData();
            }
        });
        getNextDateBillBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (LocalDate.now().isEqual(presentDate)) {
                    JOptionPane.showMessageDialog(jf, "已经是最新时间！", "警告", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                presentDate = presentDate.plusDays(1);
                presentDateLabel.setText(String.valueOf(presentDate));
                getTableData();
            }
        });
        createBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreateAccountDialog(jf, "添加账单信息", true, new ActionDoneListener() {
                    @Override
                    public void done(Object result) {
                        getTableData();
                    }
                }).setVisible(true);
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
                new UpdateAccountDialog(jf, "修改账单信息", true, new ActionDoneListener() {
                    @Override
                    public void done(Object result) {
                        getTableData();
                    }
                }, id).setVisible(true);
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

                //这里编写代码根据id删除数据库对应账单数据
                RecordServiceImpl recordService = SpringContextUtil.getBean(RecordServiceImpl.class);
                recordService.deleteOneRecordById(id);


                JOptionPane.showMessageDialog(jf, "删除成功！");
                getTableData(); //更新表格
            }
        });
        buttonPanel.add(getPreviousDateBillBtn);
        buttonPanel.add(getNextDateBillBtn);
        buttonPanel.add(presentDateLabel);
        buttonPanel.add(costThisMonthLabel);
        buttonPanel.add(costThisMonthNumberLabel);
        buttonPanel.add(residualBudgetLabel);
        buttonPanel.add(residualBudgetNumberLabel);
        buttonPanel.add(createBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        presentDateLabel.setText(String.valueOf(presentDate));
        this.add(buttonPanel);

        //消费账单表
        String[] titlesArray = {"编号", "消费金额", "时间", "消费类型", "备注"};
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
     * 获取用户选择的当前日期账单数据
     */
    public void getTableData() {
//        测试用例（这里只需编写代码从数据库获取data即可）
        RecordServiceImpl recordService = SpringContextUtil.getBean(RecordServiceImpl.class);

        Vector<Vector> data = recordService.getRecordListByLocalDate(presentDate);

        tableData.clear();  //先清空数据
        for (Vector datum : data) {
            tableData.add(datum);
        }
        accountTableModel.fireTableDataChanged();   //刷新表格

        //获取本月支出和剩余预算
        getCostThisMonthAndResidualBudgetNumber();
    }

    /**
     * 获取本月支出和剩余预算
     */
    public void getCostThisMonthAndResidualBudgetNumber() {
        RecordServiceImpl recordService = SpringContextUtil.getBean(RecordServiceImpl.class);
        SystemConfigServiceImpl systemConfigService = SpringContextUtil.getBean(SystemConfigServiceImpl.class);
        costThisMonthNumberLabel.setText(String.valueOf(recordService.getCostDuringDates(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()), LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()))));
        residualBudgetNumberLabel.setText(String.valueOf(systemConfigService.getBudget() - recordService.getCostDuringDates(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()), LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()))));
    }
}
