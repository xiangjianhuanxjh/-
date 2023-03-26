package com.javaee.accountbook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.javaee.accountbook.gui.util.GetDataUtils;
import com.javaee.accountbook.mapper.RecordMapper;
import com.javaee.accountbook.pojo.Record;
import com.javaee.accountbook.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {

    @Autowired
    RecordMapper recordMapper;

    GetDataUtils getDataUtils;

    public RecordServiceImpl(GetDataUtils getDataUtils) {
        this.getDataUtils = getDataUtils;
    }

    public Vector<Vector> getRecordList(){
        Vector<Vector> data = new Vector<>();
        //测试用例（这里只需编写代码从数据库获取data即可）
        List<Record> list = this.list();
        for(Record record:list){
            Vector v = new Vector();
            v.add(record.getId());
            v.add(record.getCost());
            v.add(record.getTime());
            v.add(record.getType());
            v.add(record.getComment());
            data.add(v);
        }
        return data;
    }

    public Vector<Vector> getRecordListByLocalDate(LocalDate ld){
        Vector<Vector> data = new Vector<>();
        //测试用例（这里只需编写代码从数据库获取data即可）
        LambdaQueryWrapper<Record> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Record::getTime, String.valueOf(ld));
        List<Record> list = recordMapper.selectList(lqw);
        for(Record record:list){
            Vector v = new Vector();
            v.add(record.getId());
            v.add(record.getCost());
            v.add(record.getTime());
            v.add(record.getType());
            v.add(record.getComment());
            data.add(v);
        }
        return data;
    }

    public void insertRecord(double cost,Date date,String type,String comment){
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();

        Record record = new Record();
        record.setComment(comment);
        record.setCost(cost);
        record.setType(type);
        record.setTime(localDate);
        this.save(record);
    }

    public void updateRecord(String id,double cost,Date date,String type,String comment){
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();

        Record record = new Record();
        record.setId(Long.parseLong(id));
        record.setComment(comment);
        record.setCost(cost);
        record.setType(type);
        record.setTime(localDate);
        this.updateById(record);
    }

    public Record selectOneRecordById(String id){
        QueryWrapper<Record> recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.eq("id",Long.parseLong(id));
        Record res = this.getOne(recordQueryWrapper);
        return res;
    }

    public void deleteOneRecordById(String id){
        QueryWrapper<Record> recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.eq("id",Long.parseLong(id));
        this.remove(recordQueryWrapper);
    }

    public Vector<Vector> getRecordByCondition(String moneyOperator, double money, Date startDate, Date endDate, String type){
        //先将Date类型转换
//        System.out.println("传进来的参数："+ type);
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        java.sql.Date newStartDate = new java.sql.Date(startTime);
        java.sql.Date newEndDate = new java.sql.Date(endTime);

        Vector<Vector> data = new Vector<>();
        QueryWrapper<Record> queryWrapper;
        List<Record> list = null;
        switch (moneyOperator){
            case "大于":
                queryWrapper = new QueryWrapper<>();
                queryWrapper.gt("cost",money)
                        .between("time",newStartDate,newEndDate)
                        .eq(!type.equals("任意"),"type",type)
                        .orderByAsc("time");
                list = recordMapper.selectList(queryWrapper);
//                list = this.list(queryWrapper);

                break;
            case "小于":
                queryWrapper = new QueryWrapper<>();
                queryWrapper.lt("cost",money)
                        .between("time",newStartDate,newEndDate)
                        .eq(!type.equals("任意"),"type",type)
                        .orderByAsc("time");

                list = recordMapper.selectList(queryWrapper);
                break;
            case "等于":
                queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("cost",money)
                        .between("time",newStartDate,newEndDate)
                        .eq(!type.equals("任意"),"type",type)
                        .orderByAsc("time");
                list = recordMapper.selectList(queryWrapper);
                break;

        }
        for(Record record:list){
            Vector v = new Vector();
            v.add(record.getId());
            v.add(record.getCost());
            v.add(record.getTime());
            v.add(record.getType());
            v.add(record.getComment());
            data.add(v);
        }
        return data;
    }


    public Vector<Vector> getRecordRank(String moneyOperator, double money, Date startDate, Date endDate){
        //先将Date类型转换
//        System.out.println("传进来的参数："+ type);
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        java.sql.Date newStartDate = new java.sql.Date(startTime);
        java.sql.Date newEndDate = new java.sql.Date(endTime);

        Vector<Vector> rank = new Vector<>();
        QueryWrapper<Record> queryWrapper;
        List<Map<String, Object>> records = null;
        switch (moneyOperator){
            case "大于":
                records = recordMapper.selectTypeCostRankGt(money, startDate, endDate);
                break;
            case "小于":
                records = recordMapper.selectTypeCostRankLt(money, startDate, endDate);
                break;
            case "等于":
                records = recordMapper.selectTypeCostRankEq(money, startDate, endDate);
                break;
        }
        for (Map<String, Object> record : records) {
            Vector t = new Vector();
            t.add(record.get("type"));
            t.add(record.get("cost"));
           rank.add(t);
        }
        return rank;
    }



    public double[] getRecordSumAndAvgByCondition(String moneyOperator, double money, Date startDate, Date endDate, String type){
        //先将Date类型转换
//        System.out.println("传进来的参数："+ type);
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        java.sql.Date newStartDate = new java.sql.Date(startTime);
        java.sql.Date newEndDate = new java.sql.Date(endTime);

        double[] res = new double[2];

        QueryWrapper<Record> queryWrapper;
        List<Record> list = null;
        switch (moneyOperator){
            case "大于":
                queryWrapper = new QueryWrapper<>();
                queryWrapper.gt("cost",money)
                        .between("time",newStartDate,newEndDate)
                        .eq(type != "任意","type",type);
                list = recordMapper.selectList(queryWrapper);
//                list = this.list(queryWrapper);


                break;
            case "小于":
                queryWrapper = new QueryWrapper<>();
                queryWrapper.lt("cost",money)
                        .between("time",newStartDate,newEndDate)
                        .eq(type != "任意","type",type);

                list = recordMapper.selectList(queryWrapper);
                break;
            case "等于":
                queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("cost",money)
                        .between("time",newStartDate,newEndDate)
                        .eq(type != "任意","type",type);

                list = recordMapper.selectList(queryWrapper);
                break;

        }
        double sum = 0,avg =0;
        for(Record record:list){
           sum+=record.getCost();
        }
        avg = sum/list.size();
        res[0] = sum;
        res[1] = avg;
        return res;
    }

    public int getCostDuringDates(LocalDate firstDate, LocalDate endDate) {
        LambdaQueryWrapper<Record> lqw = new LambdaQueryWrapper<>();
        lqw.le(Record::getTime, endDate);
        lqw.ge(Record::getTime, firstDate);
        List<Record> recordList = recordMapper.selectList(lqw);
        int cost = 0;
        for (Record r : recordList) {
            cost += r.getCost();
        }
        return cost;
    }

    public Vector<Vector> getRecordDuringDates(LocalDate firstDate, LocalDate endDate) {
        Vector<Vector> data = new Vector<>();
        LambdaQueryWrapper<Record> lqw = new LambdaQueryWrapper<>();
        lqw.le(Record::getTime, endDate);
        lqw.ge(Record::getTime, firstDate);
        List<Record> recordList = recordMapper.selectList(lqw);
        for(Record record : recordList){
            Vector v = new Vector();
            v.add(record.getId());
            v.add(record.getCost());
            v.add(record.getTime());
            v.add(record.getType());
            v.add(record.getComment());
            data.add(v);
        }
        return data;
    }

    /**
     * 根据日期，统计该日期内每种消费类型的花费总额
     * @param firstDate
     * @param endDate
     * @return
     */
    public Vector<Vector> getSumDuringDatesByType(LocalDate firstDate, LocalDate endDate) {
        List<Map<String, Object>> records = null;
        records = recordMapper.selectSumByType(firstDate, endDate);
        String[] types = getDataUtils.getTypes();
        //标记每种消费类型是否存在records中
        Boolean[] check = new Boolean[types.length];
        for(int i=0;i< check.length;i++){
            check[i] = false;
        }
        Vector<Vector> data = new Vector<>();
        for (Map<String, Object> datum : records) {
            for (int i =1;i<types.length;i++) {
                if(types[i].equals(datum.get("type")))
                    check[i]=true;
            }
            Vector t = new Vector<>();
            t.add(datum.get("type"));
            t.add(datum.get("cost"));
            data.add(t);
        }
        //如果有未记录的消费类型，加上去
        for(int i=1;i< check.length;i++){
            if(!check[i]){
                Vector t = new Vector<>();
                t.add(types[i]);
                t.add((Double)0.0);
                data.add(t);
            }
        }

        return data;
    }



    public Vector<Vector> getRank() {
        Vector<Vector> rank = new Vector<>();
        LambdaQueryWrapper<Record> lqw;
        String[] typeList = getDataUtils.getTypes();
        for (int i = 1; i <= typeList.length-1; i++) {
            lqw = new LambdaQueryWrapper<>();
            lqw.eq(Record::getType, i);

            List<Record> recordList = recordMapper.selectList(lqw);
            Vector temp = new Vector();
            temp.add(typeList[i - 1]);
            int sum = 0;
            for (Record r : recordList) {
                sum += r.getCost();
            }
            temp.add(sum);
            rank.add(temp);
        }
        return rank;
    }


    /**
     * 为最近一年的图表提供每月的数据
     */
    public Map<LocalDate,Vector<Vector>> getMonthlyData(LocalDate firstDate, LocalDate endDate){
        Map<LocalDate,Vector<Vector>> returnData = new TreeMap<>(new Comparator<LocalDate>() {
            @Override
            public int compare(LocalDate o1,LocalDate o2) {
                return o1.compareTo(o2);
            }
        });

        //先得到每天的数据，再按照天数划分
        LocalDate nextMonth = firstDate;
        LocalDate firstDayOfMonth = null,
                lastDayOfMonth = firstDate.with(TemporalAdjusters.lastDayOfMonth());    //得到起始日期当月最后一天


        returnData.put(firstDate,getSumDuringDatesByType(firstDate,lastDayOfMonth));

        Vector<Vector> data = new Vector<>();
        //得到之后各个月的数据，加到data中
        do{
            nextMonth = nextMonth.plusMonths(1);
            firstDayOfMonth = nextMonth.with(TemporalAdjusters.firstDayOfMonth());
            lastDayOfMonth = nextMonth.with(TemporalAdjusters.lastDayOfMonth());
            if(lastDayOfMonth.isBefore(endDate)) {
                data = getSumDuringDatesByType(firstDayOfMonth,lastDayOfMonth);
            }else{
                data = getSumDuringDatesByType(firstDayOfMonth, endDate);
            }
            //将月份对应的数据放入返回结果中
            returnData.put(nextMonth,data);

        }while(firstDayOfMonth.plusMonths(1).isBefore(endDate));

        return returnData;
    }


    public Map<LocalDate,Vector<Vector>> getWeeklyData(LocalDate firstDate, LocalDate endDate){
        Map<LocalDate,Vector<Vector>> returnData = new TreeMap<>(new Comparator<LocalDate>() {
            @Override
            public int compare(LocalDate o1, LocalDate o2) {
                return o1.compareTo(o2);
            }
        });
        LocalDate thisMonday = firstDate.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)).plusDays(1);
        LocalDate thisSunday = firstDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).minusDays(1);


        Vector<Vector> dataOfWeek = null;


        while(thisMonday.isBefore(endDate)){
            if(thisSunday.isAfter(endDate)){
                dataOfWeek = getSumDuringDatesByType(thisMonday, endDate);
                returnData.put(thisMonday,dataOfWeek);
            }else{
                dataOfWeek = getSumDuringDatesByType(thisMonday, thisSunday);
                returnData.put(thisMonday,dataOfWeek);
            }
            thisMonday = thisMonday.plusWeeks(1);
            thisSunday = thisSunday.plusWeeks(1);
        }

        return returnData;
    }


    public Map<LocalDate,Vector<Vector>> getDailyData(LocalDate firstDate, LocalDate endDate){
        Map<LocalDate,Vector<Vector>> returnData = new TreeMap<>(new Comparator<LocalDate>() {
            @Override
            public int compare(LocalDate o1, LocalDate o2) {
                return o1.compareTo(o2);
            }
        });
        LocalDate now = firstDate;

        Vector<Vector> dataOfDay = null;


        while(now.isBefore(endDate)){
            dataOfDay = getSumDuringDatesByType(now, now);
            returnData.put(now,dataOfDay);
            now = now.plusDays(1L);
        }
        dataOfDay = getSumDuringDatesByType(endDate, endDate);
        returnData.put(now,dataOfDay);

        return returnData;
    }



    /**
     * 为最近一月的图表提供每天的数据
     */
//    public Vector<Vector> getDailyDataForMonth(LocalDate firstDate, LocalDate endDate){
//
//    }




}
