package com.se6387.ahem.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Table
@Entity
public class CapturedPollutant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC") // strategy 设置使用数据库主键自增策略；generator 设置插入完成后，查询最后生成的 ID 填充到该属性中。
    private Integer capturedPollutantId;
    private Integer sensorId;
    private Integer pollutantId;
    private Date datetime;
    private BigDecimal value;
    private Integer aqi;
    private String unit;
}