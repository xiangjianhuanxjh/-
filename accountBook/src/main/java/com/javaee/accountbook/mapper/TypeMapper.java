

package com.javaee.accountbook.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.javaee.accountbook.pojo.Type;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeMapper extends BaseMapper<Type> {
    List<String> selectTypeName();
}
