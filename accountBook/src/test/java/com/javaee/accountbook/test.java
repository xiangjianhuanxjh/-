package com.javaee.accountbook;


import com.javaee.accountbook.gui.util.GetDataUtils;
import com.javaee.accountbook.mapper.RecordMapper;
import com.javaee.accountbook.pojo.Record;
import com.javaee.accountbook.pojo.Type;
import com.javaee.accountbook.service.RecordService;
import com.javaee.accountbook.service.TypeService;
import com.javaee.accountbook.service.impl.RecordServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

@SpringBootTest
public class test {
    @Autowired
    private RecordService recordService;


    @Autowired
    RecordServiceImpl recordServiceImpl;


    @Autowired
    private RecordMapper recordMapper;


    @Test
    public  void testUtil(){

    }


    @Test
    public void testmapper(){
        LocalDate start = LocalDate.parse("2023-02-21");
        LocalDate end = LocalDate.parse("2023-02-21");

        List<Map<String, Object>> maps = recordMapper.selectSumByType(start, end);
        for (Map<String, Object> map : maps) {
            System.out.println(map.get("type"));
        }
        System.out.println(maps);

    }

    @Test
    public void testService(){
//        LocalDate start = LocalDate.parse("2022-02-15");
//        LocalDate end = LocalDate.parse("2023-02-22");
//
//        Map<LocalDate, Vector<Vector>> monthlyData = recordServiceImpl.getMonthlyData(start, end);
//        System.out.println(monthlyData);
//
////        Map<String, Vector<Vector>> weeklyData = recordServiceImpl.getWeeklyData(start, end);
////        System.out.println(weeklyData);
//////
////        Map<LocalDate, Vector<Vector>> dailyData = recordServiceImpl.getDailyData(start, end);
////        System.out.println(dailyData);
//


    }


    @Test
    public void testRank() throws ParseException {
        Date start,end;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        start = dateFormat.parse("2023-02-01");
        end = dateFormat.parse("2023-02-20");
        Vector<Vector> rank = recordServiceImpl.getRecordRank("大于", 100.0, start, end);
        System.out.println(rank);
    }


}
