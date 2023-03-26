package com.javaee.accountbook.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaee.accountbook.mapper.SystemConfigMapper;
import com.javaee.accountbook.pojo.SystemConfig;
import com.javaee.accountbook.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig> implements SystemConfigService {
    @Autowired
    SystemConfigMapper systemConfigMapper;

    public Double getBudget() {
        return systemConfigMapper.selectById(1).getBudget();
    }
}
