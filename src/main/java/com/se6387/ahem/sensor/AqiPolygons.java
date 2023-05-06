package com.se6387.ahem.sensor;

import com.se6387.ahem.model.CapturedPollutant;
import com.se6387.ahem.model.Pollutant;
import com.se6387.ahem.model.Sensor;
import com.se6387.ahem.repository.CapturedPollutantRepository;
import com.se6387.ahem.repository.SensorRepository;
import org.hibernate.Session;
import org.springframework.data.jpa.provider.HibernateUtils;

import java.util.List;

/*
 * @author jtsmith@utdallas.edu
 */

public class AqiPolygons {

    private final List<AqiPolygon> polygons;

    public AqiPolygons(List<AqiPolygon> polygons) {
        this.polygons = polygons;
    }

    public List<AqiPolygon> getPolygons() {
        return this.polygons;
    }

}
