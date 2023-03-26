package com.javaee.accountbook.gui.components;

import com.javaee.accountbook.mapper.TypeMapper;
import com.javaee.accountbook.service.impl.RecordServiceImpl;
import com.javaee.accountbook.service.impl.TypeServiceImpl;
import com.javaee.accountbook.utils.SpringContextUtil;
import org.jdesktop.swingx.JXDatePicker;
import com.javaee.accountbook.gui.util.GetDataUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Vector;


public class CheckAccountComponent extends Box {
    //查询账单界面
    final int WIDTH = 800;
    final int HEIGHT = 600;

    private JTable accountTable;    //表
    private Vector<String> titles;  //表字段
    private Vector<Vector> tableData;   //存储二维表格数据
    private DefaultTableModel accountTableModel;

    GetDataUtils getDataUtils;
    //查询结果统计图
    JTabbedPane statisticPanel = new JTabbedPane();
    JPanel rank = new JPanel();

//    @PostConstruct
//    public void init(){
//        getDataUtils=SpringContextUtil.getBean(GetDataUtils.class);
//        System.out.println("----------------------------");
//        System.out.println(getDataUtils.getTypes());
//        System.out.println("----------------------------");
//    }

    public CheckAccountComponent(JFrame jf,GetDataUtils getDataUtils) throws IOException {
        //垂直布局盒子
        super(BoxLayout.Y_AXIS);


        //查询框
        Box checkHBox = Box.createHorizontalBox();
        JPanel checkPanel = new JPanel();
        checkPanel.setLayout(new GridLayout(3, 4, 0, 20));

        JLabel moneyLabel = new JLabel("消费金额");
        moneyLabel.setIcon(new ImageIcon("src/main/resources/images/money.png"));
//        moneyLabel.setIconTextGap();
        JComboBox<String> moneyOperators = new JComboBox<String>();
        String[] moneyOperatorItems = new String[]{"大于", "等于", "小于"};
        for (String s : moneyOperatorItems) {
            moneyOperators.addItem(s);
        }
        JTextField moneyTextField = new JTextField(15);
        checkPanel.add(moneyLabel);
        checkPanel.add(moneyOperators);
        checkPanel.add(moneyTextField);
        checkPanel.add(new JPanel());

        JLabel dateLabel = new JLabel("消费日期");
        dateLabel.setIcon(new ImageIcon("src/main/resources/images/time.png"));
        JLabel toLabel = new JLabel("        至");
        JXDatePicker startDatePicker = new JXDatePicker(new Date());
        JXDatePicker endDatePicker = new JXDatePicker(new Date());
        checkPanel.add(dateLabel);
        checkPanel.add(startDatePicker);
        checkPanel.add(toLabel);
        checkPanel.add(endDatePicker);

        JLabel typeLabel = new JLabel("消费类型");
        typeLabel.setIcon(new ImageIcon("src/main/resources/images/type.png"));
        JComboBox<String> typeComboBox = new JComboBox<String>();

        String[] types = getDataUtils.getTypes();
        for(int i =0;i<types.length;i++){
            typeComboBox.addItem(types[i]);
        }

        checkPanel.add(typeLabel);
        checkPanel.add(typeComboBox);
        checkPanel.add(new JPanel());
        checkPanel.add(new JPanel());

        JButton checkBtn = new JButton("查询");
        checkBtn.setIcon(new ImageIcon("src/main/resources/images/search.png"));
        JButton exportBtn = new JButton("消费排行");
        exportBtn.setIcon(new ImageIcon("src/main/resources/images/rank.png"));

        checkHBox.add(Box.createHorizontalStrut(20));
        checkHBox.add(checkPanel);
        checkHBox.add(Box.createHorizontalStrut(100));
        checkHBox.add(checkBtn);
        checkHBox.add(Box.createHorizontalStrut(20));
        checkHBox.add(exportBtn);
        checkHBox.add(Box.createHorizontalStrut(100));
        checkHBox.setMaximumSize(new Dimension(800, 100));

        //查询及统计结果
        Box resultHBox = Box.createHorizontalBox();
            //查询结果
                //查询结果表格
        JPanel checkResultPanel = new JPanel();
        checkResultPanel.setLayout(new BorderLayout());
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
        JScrollPane sp = new JScrollPane(accountTable);
        checkResultPanel.add(sp, BorderLayout.CENTER);
                //查询结果总体统计
        JPanel checkResultSumPanel = new JPanel();
        checkResultSumPanel.setLayout(new GridLayout(1, 4, 0, 0));
        checkResultSumPanel.setPreferredSize(new Dimension(400, 50));
        JLabel sumLabel = new JLabel("总花费");
        sumLabel.setIcon(new ImageIcon("src/main/resources/images/资金.png"));
        JLabel sumNumber = new JLabel();
        JLabel dailyMeanLabel = new JLabel("日均花费");
        dailyMeanLabel.setIcon(new ImageIcon("src/main/resources/images/资金.png"));
        JLabel dailyMeanNumber = new JLabel();
        checkResultSumPanel.add(sumLabel);
        checkResultSumPanel.add(sumNumber);
        checkResultSumPanel.add(dailyMeanLabel);
        checkResultSumPanel.add(dailyMeanNumber);

        checkResultPanel.add(checkResultSumPanel, BorderLayout.SOUTH);
        checkResultPanel.setMaximumSize(new Dimension(400, 460));

        statisticPanel.add("消费排行榜", rank);

        //给查询和导出按钮设置事件监听器
        checkBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double money = Double.parseDouble(moneyTextField.getText().trim());
                    String moneyOperator = moneyOperators.getItemAt(moneyOperators.getSelectedIndex());
                    Date startDate = startDatePicker.getDate();
                    Date endDate = endDatePicker.getDate();
                    String type = typeComboBox.getItemAt(typeComboBox.getSelectedIndex());


                    //测试用例（这里只需编写代码从数据库获取data即可）
                    Vector<Vector> data = getSelectedData(moneyOperator, money, startDate, endDate, type);

                    tableData.clear();  //先清空数据
//                    for (int i = 0; i < data.size(); i++) {
                        for (Vector datum : data) {
                            tableData.add(datum);
                        }
//                    }
                    accountTableModel.fireTableDataChanged();   //刷新表格

                    //这里编写计算消费总额及日均消费的代码给sum和dailyMean
                    RecordServiceImpl recordService = SpringContextUtil.getBean(RecordServiceImpl.class);
                    double[] sumAndAvg = recordService.getRecordSumAndAvgByCondition(moneyOperator,money,startDate,endDate,type);
                    double sum = sumAndAvg[0];
                    double dailyMean = sumAndAvg[1];
                    sumNumber.setText("￥" + sum);
                    dailyMeanNumber.setText("￥" + dailyMean);

                    JOptionPane.showMessageDialog(jf, "查询成功！");
                } catch (Exception ex) {
                    System.out.println(ex);
                    JOptionPane.showMessageDialog(jf, "输入了非法字符！", "警告", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        exportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double money = Double.parseDouble(moneyTextField.getText().trim());
                String moneyOperator = moneyOperators.getItemAt(moneyOperators.getSelectedIndex());
                Date startDate = startDatePicker.getDate();
                Date endDate = endDatePicker.getDate();
                getRank(moneyOperator,money,startDate,endDate);
            }
        });

        resultHBox.add(checkResultPanel);
        resultHBox.add(Box.createHorizontalStrut(5));
        resultHBox.add(statisticPanel);


        //组装
        this.add(Box.createVerticalStrut(20));
        this.add(checkHBox);
        this.add(Box.createVerticalStrut(20));
        this.add(resultHBox);


    }

    public Vector<Vector> getSelectedData(String moneyOperator,Double money, Date startDate, Date endDate,String type){
        RecordServiceImpl recordService = SpringContextUtil.getBean(RecordServiceImpl.class);
        Vector<Vector> data = new Vector<Vector>();
        data = recordService.getRecordByCondition(moneyOperator,money,startDate,endDate,type);
        return data;
    }


    public void getRank(String moneyOperator,Double money, Date startDate, Date endDate) {
        RecordServiceImpl recordService = SpringContextUtil.getBean(RecordServiceImpl.class);
        GetDataUtils getDataUtils = SpringContextUtil.getBean(GetDataUtils.class);
        int row = getDataUtils.getTypes().length-1;
        int column = 2;
        rank.removeAll();
        rank.setLayout(new GridLayout(row, column));
        //获取数据

        Vector<Vector> data = recordService.getRecordRank(moneyOperator, money, startDate, endDate);

        for (Vector v : data) {
            String type = String.valueOf(v.get(0));
            String number = String.valueOf(v.get(1));
            rank.add(new JLabel(type));
            rank.add(new JLabel(number));
        }

        statisticPanel.add("消费排行榜", this.rank);
    }
}
