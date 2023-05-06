package com.se6387.ahem.model;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@Table
@Entity
public class Pollutant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
    // strategy 设置使用数据库主键自增策略；generator 设置插入完成后，查询最后生成的 ID 填充到该属性中。
    private Integer pollutantId;

    public String getAbbreviation() {
        return abbreviation;
    }

    private String abbreviation;
    private String fullName;

    /*
      Breakpoints for pollutant AQIs listed in Table 5 of EPA 454/B-18-007 September 2018
    (Technical Assistance Document for the Reporting of Daily Air Quality – the Air Quality Index (AQI))
    https://www.airnow.gov/sites/default/files/2020-05/aqi-technical-assistance-document-sept2018.pdf

    The last digit is the number of decimal places for the measurement
     */
    public double[] getAqiBreakpoints() {

        double[] result;

        if ("PM25".equals(abbreviation)) {
            result = new double[]{500.4, 350.4, 250.4, 150.4, 55.4, 35.4, 12.0, 1.0};
        } else if ("PM10".equals(abbreviation)) {
            result = new double[]{604.0, 504.0, 424.0, 354.0, 254.0, 154.0, 54.0, 0.0};
        } else if ("O3".equals(abbreviation)) { // 8-hour
            result = new double[]{0.604, 0.504, 0.200, 0.105, 0.085, 0.070, 0.054, 3.0};
        } else if ("SO2".equals(abbreviation)) {
            result = new double[]{1004.0, 804.0, 604.0, 304.0, 185.0, 75.0, 35.0, 0.0};
        } else if ("CO".equals(abbreviation)) {
            result = new double[]{50.4, 40.4, 30.4, 15.4, 12.4, 9.4, 4.4, 1.0};
        } else if ("NO2".equals(abbreviation)) {
            result = new double[]{2049.0, 1649.0, 1249.0, 649.0, 360.0, 100.0, 53.0, 0.0};
        } else {
            result = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        }

        return result;
    }

    double truncateDouble(double num, int places) {
        return (new BigDecimal(num)).setScale(places, RoundingMode.DOWN).doubleValue();
    }

    /*
    These two functions are adapted from the PurpleAir algorithm posted at
    https://community.purpleair.com/t/how-to-calculate-the-us-epa-pm2-5-aqi/877/11
    */

    // Convert US AQI from raw measurement data
    public int aqiFromRawMeasurement(double value) {
        if (value < 0 || value != value) return -1;

        double[] breakpoint= this.getAqiBreakpoints();
        if (value > 2*breakpoint[0]) return -1;

        int AQI_MEASUREMENT_DECIMAL_PLACES = (int) breakpoint[7];
        double AQI_500_BREAKPOINT_HI = breakpoint[0];
        double AQI_400_BREAKPOINT_HI = breakpoint[1];
        double AQI_300_BREAKPOINT_HI = breakpoint[2];
        double AQI_200_BREAKPOINT_HI = breakpoint[3];
        double AQI_150_BREAKPOINT_HI = breakpoint[4];
        double AQI_100_BREAKPOINT_HI = breakpoint[5];
        double AQI_050_BREAKPOINT_HI = breakpoint[6];
        double AQI_500_BREAKPOINT_LO = AQI_400_BREAKPOINT_HI + Math.pow(10, -AQI_MEASUREMENT_DECIMAL_PLACES);
        double AQI_400_BREAKPOINT_LO = AQI_300_BREAKPOINT_HI + Math.pow(10, -AQI_MEASUREMENT_DECIMAL_PLACES);
        double AQI_300_BREAKPOINT_LO = AQI_200_BREAKPOINT_HI + Math.pow(10, -AQI_MEASUREMENT_DECIMAL_PLACES);
        double AQI_200_BREAKPOINT_LO = AQI_150_BREAKPOINT_HI + Math.pow(10, -AQI_MEASUREMENT_DECIMAL_PLACES);
        double AQI_150_BREAKPOINT_LO = AQI_100_BREAKPOINT_HI + Math.pow(10, -AQI_MEASUREMENT_DECIMAL_PLACES);
        double AQI_100_BREAKPOINT_LO = AQI_050_BREAKPOINT_HI + Math.pow(10, -AQI_MEASUREMENT_DECIMAL_PLACES);
        double AQI_050_BREAKPOINT_LO = 0;

        double measurement = truncateDouble(value, AQI_MEASUREMENT_DECIMAL_PLACES);

        if (measurement > AQI_500_BREAKPOINT_LO)   // Hazardous
            return this.calc_aqi(measurement, 500, 401, AQI_500_BREAKPOINT_HI, AQI_500_BREAKPOINT_LO);
        else if (measurement > AQI_400_BREAKPOINT_LO)  // Hazardous
            return this.calc_aqi(measurement, 400, 301, AQI_400_BREAKPOINT_HI, AQI_400_BREAKPOINT_LO);
        else if (measurement > AQI_300_BREAKPOINT_LO)  // Very Unhealthy
            return this.calc_aqi(measurement, 300, 201, AQI_300_BREAKPOINT_HI, AQI_300_BREAKPOINT_LO);
        else if (measurement > AQI_200_BREAKPOINT_LO)  // Unhealthy
            return this.calc_aqi(measurement, 200, 151, AQI_200_BREAKPOINT_HI, AQI_200_BREAKPOINT_LO);
        else if (measurement > AQI_150_BREAKPOINT_LO)  // Unhealthy for Sensitive Groups
            return this.calc_aqi(measurement, 150, 101, AQI_150_BREAKPOINT_HI, AQI_150_BREAKPOINT_LO);
        else if (measurement > AQI_100_BREAKPOINT_LO)  // Moderate
            return this.calc_aqi(measurement, 100, 51, AQI_100_BREAKPOINT_HI, AQI_100_BREAKPOINT_LO);
        else if (measurement >= AQI_050_BREAKPOINT_LO)  // Good
            return this.calc_aqi(measurement, 50, 0, AQI_050_BREAKPOINT_HI, AQI_050_BREAKPOINT_LO);
        else return -1;
    }

    // Calculate AQI from standard ranges
    /*
    Returns the Air Quality Index (AQI) for a pollutant

    Adaptation of Equation 1 of EPA 454/B-18-007 September 2018
    (Technical Assistance Document for the Reporting of Daily Air Quality – the Air Quality Index (AQI))

    Cp: the truncated concentration of pollutant
    BPh: the concentration breakpoint that is greater than or equal to Cp
    BPl: the concentration breakpoint that is less than or equal to Cp
    Ih: the AQI value corresponding to BPh
    Il: the AQI value corresponding to BPl
    */
    private int calc_aqi(double Cp, int Ih, int Il, double BPh, double BPl) {
        int a =(Ih - Il);
        double b =(BPh - BPl);
        double c =(Cp - BPl);
        return (int) Math.round((a / b) * c + Il);
    }

    public Integer getPollutantId() {
        return pollutantId;
    }
}