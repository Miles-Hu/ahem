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
/*
    public static AqiPolygons get(int nLat, int sLat, int wLon, int eLon, int decimalPlaces,
                                  CapturedPollutantRepository capturedPollutantRepository,
                                  SensorRepository sensorRepository) {
        AqiPolygons result = null;

        List<CapturedPollutant> caputuredPollutants = capturedPollutantRepository.findAll
        result += "pollutantExample: " + pollutantRepository +"\n";
        List<Pollutant> pollutants = pollutantRepository.findAll();
        result += "pollutants: ";
        for (Pollutant p : pollutants) result += " [ " + p + " ]";
        result += "\n";

        return result;
    }

    private List<Integer> getSensorIds(int nLat, int sLat, int wLon, int eLon, int decimalPlaces,
                                       SensorRepository sensorRepository) {
        Session session = HibernateUtil();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Item> cr = cb.createQuery(Item.class);
        Root<Item> root = cr.from(Item.class);
        cr.select(root);

        Query<Item> query = session.createQuery(cr);
        List<Item> results = query.getResultList();
    }
 */
}
