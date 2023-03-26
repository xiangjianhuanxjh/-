package com.javaee.accountbook.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Type {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String typeName;

}
