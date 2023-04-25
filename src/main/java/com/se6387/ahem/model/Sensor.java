package com.se6387.ahem.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Table
@Entity
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC") // strategy 设置使用数据库主键自增策略；generator 设置插入完成后，查询最后生成的 ID 填充到该属性中。
    private Integer sensorId;

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal radiusMeters;

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public void setRadiusMeters(BigDecimal radiusMeters) {
        this.radiusMeters = radiusMeters;
    }

    public Integer getSensorId() {
        return sensorId;
    }

}