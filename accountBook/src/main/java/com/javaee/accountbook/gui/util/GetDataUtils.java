package com.javaee.accountbook.gui.util;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.javaee.accountbook.mapper.TypeMapper;
import com.javaee.accountbook.pojo.Type;
import com.javaee.accountbook.service.TypeService;
import com.javaee.accountbook.service.impl.RecordServiceImpl;
import com.javaee.accountbook.service.impl.TypeServiceImpl;
import com.javaee.accountbook.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class GetDataUtils {

    TypeServiceImpl typeService;

    private String[] types;

    public static String[] init(){
        String[] types = new String[]{"餐饮", "购物", "交通","转账","学习费用","任意"};   //测试用例
        return types;
    }

    public GetDataUtils(TypeServiceImpl typeService) {
        this.typeService = typeService;
    }

    public String[] getTypes(){
        //这里编写从数据库获取 消费类型 数组的代码
        String[] type = typeService.getType();
        return type;
    }
}
