package com.javaee.accountbook.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.javaee.accountbook.pojo.Record;
import org.apache.ibatis.annotations.Param;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

@Repository
public interface RecordMapper extends BaseMapper<Record> {

    /**
     * 选择大于xx金额，时间范围在start--end的账单记录
     * @param money
     * @param startDate
     * @param endDate
     * @return
     */
    List<Map<String,Object>> selectTypeCostRankGt(@Param("cost") double money, @Param("startDate") Date startDate, @Param("endDate") Date endDate);


    /**
     *选择小于xx金额，时间范围在start--end的账单记录
     */
    List<Map<String,Object>> selectTypeCostRankLt(@Param("cost") double money, @Param("startDate") Date startDate, @Param("endDate") Date endDate);


    /**
     *选择等于xx金额，时间范围在start--end的账单记录
     */
    List<Map<String,Object>> selectTypeCostRankEq(@Param("cost") double money, @Param("startDate") Date startDate, @Param("endDate") Date endDate);


    /**
     * 根据类型统计花费总数
     * @param startDate
     * @param endDate
     * @return
     */
    List<Map<String,Object>> selectSumByType(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


}
