package com.se6387.ahem.repository;

import com.se6387.ahem.model.CapturedPollutant;
import com.se6387.ahem.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer> {

    @Query("select s from Sensor s where s.radiusMeters<0 and s.latitude <= :nLat and s.latitude >= :sLat and s.longitude >= :wLon and s.longitude <= :eLon")
    List<Sensor> boxOfSensors(@Param("nLat") BigDecimal nLat, @Param("sLat") BigDecimal sLat,
                              @Param("wLon") BigDecimal wLon, @Param("eLon") BigDecimal eLon);


}
