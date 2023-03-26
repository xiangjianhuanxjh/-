package com.javaee.accountbook.pojo;


import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;

@Data
public class Record {
    private Long id;
    private String type;
    private Double cost;
    private String comment;

    private LocalDate time;



}
