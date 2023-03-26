package com.javaee.accountbook.gui.components;

import com.javaee.accountbook.gui.listener.ActionDoneListener;
import com.javaee.accountbook.gui.util.GetDataUtils;
import com.javaee.accountbook.service.impl.RecordServiceImpl;
import com.javaee.accountbook.service.impl.SystemConfigServiceImpl;
import com.javaee.accountbook.utils.SpringContextUtil;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import org.jfree.ui.TextAnchor;


public class StatisticComponent extends Box {
    //统计账单界面
    final int WIDTH = 800;
    final int HEIGHT = 600;

    //起始日期
    JLabel firstDateLabel;
    //终止日期
    JLabel lastDateLabel;

    public StatisticComponent(JFrame jf) {
        //垂直布局盒子
        super(BoxLayout.Y_AXIS);


        JTabbedPane jtp = new JTabbedPane();
        jtp.add("饼图", new JPanel());
        jtp.add("柱状图", new JPanel());
        jtp.add("折线图",new JPanel());

        //按钮界面
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(236, 242, 179));    //设置背景颜色
        buttonPanel.setMaximumSize(new Dimension(1000, 80));   //设置最大宽高
        buttonPanel.setLayout(new GridLayout(1, 7));    //设置网格布局
        JLabel dateLabel1 = new JLabel("日期范围：");
        firstDateLabel = new JLabel();
        JLabel dateLabel2 = new JLabel("至");
        lastDateLabel = new JLabel();
        JButton recentYearBtn = new JButton("最近一年");
        recentYearBtn.setIcon(new ImageIcon("src/main/resources/images/本年.png"));
        JButton recentMonthBtn = new JButton("最近一月");
        recentMonthBtn.setIcon(new ImageIcon("src/main/resources/images/本月.png"));
        JButton recentWeekBtn = new JButton("最近一周");
        recentWeekBtn.setIcon(new ImageIcon("src/main/resources/images/本周.png"));
        recentYearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDate firstDate = LocalDate.now().minusDays(365);
                LocalDate lastDate = LocalDate.now();
                firstDateLabel.setText(String.valueOf(firstDate));
                lastDateLabel.setText(String.valueOf(lastDate));
                getStatisticPictureForYear(jtp,firstDate, lastDate);
            }
        });
        recentMonthBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDate firstDate = LocalDate.now().minusDays(31);
                LocalDate lastDate = LocalDate.now();
                firstDateLabel.setText(String.valueOf(firstDate));
                lastDateLabel.setText(String.valueOf(lastDate));
                getStatisticPictureForMonth(jtp,firstDate, lastDate);
            }
        });
        recentWeekBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDate firstDate = LocalDate.now().minusDays(7);
                LocalDate lastDate = LocalDate.now();
                firstDateLabel.setText(String.valueOf(firstDate));
                lastDateLabel.setText(String.valueOf(lastDate));
                getStatisticPictureForWeek(jtp,firstDate, lastDate);
            }
        });

        buttonPanel.add(recentYearBtn);
        buttonPanel.add(recentMonthBtn);
        buttonPanel.add(recentWeekBtn);
        buttonPanel.add(dateLabel1);
        buttonPanel.add(firstDateLabel);
        buttonPanel.add(dateLabel2);
        buttonPanel.add(lastDateLabel);
        this.add(buttonPanel);
        this.add(jtp);
    }

    /**
     * 获取各个图
     * @param firstDate
     * @param lastDate
     */
    public void getStatisticPictureForYear(JTabbedPane jtp,LocalDate firstDate, LocalDate lastDate) {
        RecordServiceImpl recordService = SpringContextUtil.getBean(RecordServiceImpl.class);
        Map<LocalDate, Vector<Vector>> data = null;
        //判断要获得的是最近一年/月/周，获得对应每月/周/日的数据
        data = recordService.getMonthlyData(firstDate, lastDate);

        firstDateLabel.setText(String.valueOf(firstDate));
        lastDateLabel.setText(String.valueOf(lastDate));

        //这里编写生成统计图的代码
        DefaultPieDataset dataSet_pie = new DefaultPieDataset();//创建数据集
        DefaultCategoryDataset dataSet_bar = new DefaultCategoryDataset();
        DefaultCategoryDataset dataSet_line = new DefaultCategoryDataset();

        GetDataUtils getDataUtils = SpringContextUtil.getBean(GetDataUtils.class);
        String[] types= getDataUtils.getTypes();

        int type_num = types.length;
        double[] sum = new double[type_num];
        for (double v : sum) {
            v = 0;
        }

        //直方图和折线图
        data.forEach((time,value)->{
            String time_set = String.valueOf(time.getYear()).substring(2)+"/"+String.valueOf(time.getMonth().getValue()) ;
            System.out.println(time_set);
            value.forEach(vector -> {
                Double cost = (Double) vector.get(1);
                String type = (String) vector.get(0);
                System.out.println(cost + type);
                dataSet_bar.addValue(cost,type,time_set);
                dataSet_line.addValue(cost,type,time_set);
                for(int i = 1 ;i<types.length;i++){
                    if(vector.get(0).equals(types[i]))
                        sum[i] += cost;
                }
            });
        });
        //饼图
        for(int i=1;i<type_num;i++){
            dataSet_pie.setValue(types[i],sum[i]);
        }

        //创建主题样式
        StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
        //设置标题字体
        standardChartTheme.setExtraLargeFont(new Font("宋书", Font.BOLD, 20));
        //设置图例的字体
        standardChartTheme.setRegularFont(new Font("宋书", Font.PLAIN, 15));
        //设置轴向的字体
        standardChartTheme.setLargeFont(new Font("宋书", Font.PLAIN, 15));
        //应用主题样式
        ChartFactory.setChartTheme(standardChartTheme);

        JFreeChart pie_chart = ChartFactory.createPieChart(
                "花费饼状图",
                dataSet_pie,
                false, true, true);
        PiePlot pie = (PiePlot) (pie_chart.getPlot());
        pie.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}({2})", NumberFormat.getNumberInstance(), new DecimalFormat("0.0%")));

        JFreeChart bar_chart = ChartFactory.createBarChart3D(
                "花费柱状图",
                "总计",
                "费用",
                dataSet_bar, PlotOrientation.VERTICAL,true,true,true);
        CategoryPlot bar_plot = bar_chart.getCategoryPlot();
        BarRenderer3D customBarRenderer = (BarRenderer3D) bar_plot.getRenderer();
        customBarRenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());// 显示每个柱的数值
        customBarRenderer.setBaseItemLabelsVisible(true);
        customBarRenderer.setBasePositiveItemLabelPosition(
                new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));

        JFreeChart line_chart= ChartFactory.createLineChart("花费折线图", "日期",
                "费用",dataSet_line);
        CategoryPlot line_plot=line_chart.getCategoryPlot();
        LineAndShapeRenderer renderer = (LineAndShapeRenderer)line_plot.getRenderer();
        DecimalFormat decimalformat1 = new DecimalFormat("##.##");
        renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", decimalformat1));
        renderer.setItemLabelsVisible(true);//设置项标签显示
        renderer.setBaseItemLabelsVisible(true);//基本项标签显示
        renderer.setShapesFilled(Boolean.TRUE);//在数据点显示实心的小图标
        renderer.setShapesVisible(true);//设置显示小图标

        jtp.setComponentAt(0,new ChartPanel(pie_chart));
        jtp.setComponentAt(1,new ChartPanel(bar_chart));
        jtp.setComponentAt(2,new ChartPanel(line_chart));
    }

    public void getStatisticPictureForMonth(JTabbedPane jtp,LocalDate firstDate, LocalDate lastDate) {
        RecordServiceImpl recordService = SpringContextUtil.getBean(RecordServiceImpl.class);
        Map<LocalDate, Vector<Vector>> data = null;
        //判断要获得的是最近一年/月/周，获得对应每月/周/日的数据
        data = recordService.getWeeklyData(firstDate, lastDate);


        firstDateLabel.setText(String.valueOf(firstDate));
        lastDateLabel.setText(String.valueOf(lastDate));

        //这里编写生成统计图的代码
        DefaultPieDataset dataSet_pie = new DefaultPieDataset();//创建数据集
        DefaultCategoryDataset dataSet_bar = new DefaultCategoryDataset();
        DefaultCategoryDataset dataSet_line = new DefaultCategoryDataset();

        GetDataUtils getDataUtils = SpringContextUtil.getBean(GetDataUtils.class);
        String[] types= getDataUtils.getTypes();

        int type_num = types.length;
        double[] sum = new double[type_num];
        for (double v : sum) {
            v = 0;
        }

        //直方图和折线图
        data.forEach((time,value)->{
            String time_set = String.valueOf(time.getMonthValue()) + "/" + String.valueOf(time.getDayOfMonth())+ "--"+String.valueOf(time.plusDays(7).getDayOfMonth());
            value.forEach(vector -> {
                Double cost = (Double) vector.get(1);
                String type = (String) vector.get(0);
                System.out.println(cost + type);
                dataSet_bar.addValue(cost,type,time_set);
                dataSet_line.addValue(cost,type,time_set);
                for(int i = 1 ;i<types.length;i++){
                    if(vector.get(0).equals(types[i]))
                        sum[i] += cost;
                }
            });
        });
        //饼图
        for(int i=1;i<type_num;i++){
            dataSet_pie.setValue(types[i],sum[i]);
        }


        //创建主题样式
        StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
        //设置标题字体
        standardChartTheme.setExtraLargeFont(new Font("宋书", Font.BOLD, 20));
        //设置图例的字体
        standardChartTheme.setRegularFont(new Font("宋书", Font.PLAIN, 15));
        //设置轴向的字体
        standardChartTheme.setLargeFont(new Font("宋书", Font.PLAIN, 15));
        //应用主题样式
        ChartFactory.setChartTheme(standardChartTheme);

        JFreeChart pie_chart = ChartFactory.createPieChart(
                "花费饼状图",
                dataSet_pie,
                false, true, true);
        PiePlot pie = (PiePlot) (pie_chart.getPlot());
        pie.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}({2})", NumberFormat.getNumberInstance(), new DecimalFormat("0.0%")));

        JFreeChart bar_chart = ChartFactory.createBarChart3D(
                "花费柱状图",
                "总计",
                "费用",
                dataSet_bar, PlotOrientation.VERTICAL,true,true,true);
        CategoryPlot plot = bar_chart.getCategoryPlot();
        BarRenderer3D customBarRenderer = (BarRenderer3D) plot.getRenderer();
        customBarRenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());// 显示每个柱的数值
        customBarRenderer.setBaseItemLabelsVisible(true);
        customBarRenderer.setBasePositiveItemLabelPosition(
                new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));

        JFreeChart line_chart= ChartFactory.createLineChart("花费折线图", "日期",
                "费用",dataSet_line);
        CategoryPlot line_plot=line_chart.getCategoryPlot();
        LineAndShapeRenderer renderer = (LineAndShapeRenderer)line_plot.getRenderer();
        DecimalFormat decimalformat1 = new DecimalFormat("##.##");
        renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", decimalformat1));
        renderer.setItemLabelsVisible(true);//设置项标签显示
        renderer.setBaseItemLabelsVisible(true);//基本项标签显示
        renderer.setShapesFilled(Boolean.TRUE);//在数据点显示实心的小图标
        renderer.setShapesVisible(true);//设置显示小图标

        jtp.setComponentAt(0,new ChartPanel(pie_chart));
        jtp.setComponentAt(1,new ChartPanel(bar_chart));
        jtp.setComponentAt(2,new ChartPanel(line_chart));
    }

    public void getStatisticPictureForWeek(JTabbedPane jtp,LocalDate firstDate, LocalDate lastDate) {
        RecordServiceImpl recordService = SpringContextUtil.getBean(RecordServiceImpl.class);
        Map<LocalDate, Vector<Vector>> data = null;
        //判断要获得的是最近一年/月/周，获得对应每月/周/日的数据
        data = recordService.getDailyData(firstDate, lastDate);


        firstDateLabel.setText(String.valueOf(firstDate));
        lastDateLabel.setText(String.valueOf(lastDate));

        //这里编写生成统计图的代码
        DefaultPieDataset dataSet_pie = new DefaultPieDataset();//创建数据集
        DefaultCategoryDataset dataSet_bar = new DefaultCategoryDataset();
        DefaultCategoryDataset dataSet_line = new DefaultCategoryDataset();

        GetDataUtils getDataUtils = SpringContextUtil.getBean(GetDataUtils.class);
        String[] types= getDataUtils.getTypes();

        int type_num = types.length;
        double[] sum = new double[type_num];
        for (double v : sum) {
            v = 0;
        }

        //直方图和折线图
        data.forEach((time,value)->{
            String time_set = String.valueOf( time.getMonthValue())+ "/" + String.valueOf(time.getDayOfMonth());
            value.forEach(vector -> {
                Double cost = (Double) vector.get(1);
                String type = (String) vector.get(0);
                System.out.println(cost + type);
                dataSet_bar.addValue(cost,type,time_set);
                dataSet_line.addValue(cost,type,time_set);
                for(int i = 1 ;i<types.length;i++){
                    if(vector.get(0).equals(types[i]))
                        sum[i] += cost;
                }
            });
        });
        //饼图
        for(int i=1;i<type_num;i++){
            dataSet_pie.setValue(types[i],sum[i]);
        }

        //创建主题样式
        StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
        //设置标题字体
        standardChartTheme.setExtraLargeFont(new Font("宋书", Font.BOLD, 20));
        //设置图例的字体
        standardChartTheme.setRegularFont(new Font("宋书", Font.PLAIN, 15));
        //设置轴向的字体
        standardChartTheme.setLargeFont(new Font("宋书", Font.PLAIN, 15));
        //应用主题样式
        ChartFactory.setChartTheme(standardChartTheme);

        JFreeChart pie_chart = ChartFactory.createPieChart(
                "花费饼状图",
                dataSet_pie,
                false, true, true);
        PiePlot pie = (PiePlot) (pie_chart.getPlot());
        pie.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}({2})", NumberFormat.getNumberInstance(), new DecimalFormat("0.0%")));

        JFreeChart bar_chart = ChartFactory.createBarChart3D(
                "花费柱状图",
                "总计",
                "费用",
                dataSet_bar, PlotOrientation.VERTICAL,true,true,true);
        CategoryPlot plot = bar_chart.getCategoryPlot();
        BarRenderer3D customBarRenderer = (BarRenderer3D) plot.getRenderer();
        customBarRenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());// 显示每个柱的数值
        customBarRenderer.setBaseItemLabelsVisible(true);
        customBarRenderer.setBasePositiveItemLabelPosition(
                new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));

        JFreeChart line_chart= ChartFactory.createLineChart("花费折线图", "日期",
                "费用",dataSet_line);
        CategoryPlot line_plot=line_chart.getCategoryPlot();
        LineAndShapeRenderer renderer = (LineAndShapeRenderer)line_plot.getRenderer();
        DecimalFormat decimalformat1 = new DecimalFormat("##.##");
        renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", decimalformat1));
        renderer.setItemLabelsVisible(true);//设置项标签显示
        renderer.setBaseItemLabelsVisible(true);//基本项标签显示
        renderer.setShapesFilled(Boolean.TRUE);//在数据点显示实心的小图标
        renderer.setShapesVisible(true);//设置显示小图标

        jtp.setComponentAt(0,new ChartPanel(pie_chart));
        jtp.setComponentAt(1,new ChartPanel(bar_chart));
        jtp.setComponentAt(2,new ChartPanel(line_chart));
    }
}
