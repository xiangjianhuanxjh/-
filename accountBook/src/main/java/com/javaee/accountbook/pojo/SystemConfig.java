package com.javaee.accountbook.pojo;

import lombok.Data;

@Data
public class SystemConfig {
    private Long id;
    private Double budget;

    public SystemConfig(Long id, Double budget) {
        this.id = id;
        this.budget = budget;
    }
}
