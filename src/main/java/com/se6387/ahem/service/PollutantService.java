package com.se6387.ahem.service;

import com.se6387.ahem.model.CapturedPollutant;
import com.se6387.ahem.model.Pollutant;
import com.se6387.ahem.model.PollutantEnum;
import com.se6387.ahem.model.Sensor;
import com.se6387.ahem.repository.PollutantRepository;
import com.se6387.ahem.repository.SensorRepository;
import com.se6387.ahem.repository.CapturedPollutantRepository;
import com.se6387.ahem.sensor.AqiMeasurement;
import com.se6387.ahem.sensor.AqiPoint;
import com.se6387.ahem.sensor.AqiPolygon;
import com.se6387.ahem.sensor.AqiPolygons;
import com.se6387.ahem.utils.DatabaseCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class PollutantService {


    @Autowired
    private PollutantRepository pollutantRepository;

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private CapturedPollutantRepository capturedPollutantRepository;

    public List<AqiPoint> getAqiPoints(double longitude, double latitude) {
        List<Pollutant> all = pollutantRepository.findAll();
        return null;
    }

    /*
    public UserEntity getUserByIdWithTypedQuery(Long id) {
    TypedQuery<UserEntity> typedQuery
      = getEntityManager().createQuery("SELECT u FROM UserEntity u WHERE u.id=:id", UserEntity.class);
    typedQuery.setParameter("id", id);
    return typedQuery.getSingleResult();
}
     */

    public List<Integer> getSensorIds(double nLat, double sLat, double wLon, double eLon)
    {
        ArrayList<Integer> ids = new ArrayList<Integer>();
        List<Sensor> sensors = sensorRepository.boxOfSensors(new BigDecimal(nLat), new BigDecimal(sLat),
                new BigDecimal(wLon), new BigDecimal(eLon));
        for (Sensor s: sensors) ids.add(s.getSensorId());
        return ids;
    }

    private PollutantEnum getPollutantEnum(String abbreviation) {
        PollutantEnum result = null;

        if ("PM25".equals(abbreviation)) result = PollutantEnum.PM25;
        else if ("PM10".equals(abbreviation)) result = PollutantEnum.PM10;
        else if ("O3".equals(abbreviation)) result = PollutantEnum.O3;
        else if ("SO2".equals(abbreviation)) result = PollutantEnum.SO2;
        else if ("CO".equals(abbreviation)) result = PollutantEnum.CO;
        else if ("NO2".equals(abbreviation)) result = PollutantEnum.NO2;

        return result;
    }

    /*
    As currently implemented, decimalPlaces is ignored and a value of 3 is assumed
     */
    public AqiPolygons getAqiPolygons(double nLat, double sLat, double wLon, double eLon, int decimalPlaces) {
        decimalPlaces = 3; //TODO this is bad; ignoring decimalPlace parameter
        double increment = Math.pow(10, -decimalPlaces) / 2.0;
        List<AqiPolygon> polygons = new ArrayList<>();
        List<Sensor> sensors = sensorRepository.boxOfSensors(new BigDecimal(nLat), new BigDecimal(sLat),
                new BigDecimal(wLon), new BigDecimal(eLon));
        List<Pollutant> pollutants = pollutantRepository.findAll();

        for (Sensor s : sensors) {
            Date now = new Date();
            List<AqiMeasurement> measurements = new ArrayList<AqiMeasurement>();
            for (Pollutant p : pollutants) {
                CapturedPollutant capturedPollutant = getMostRecent(s.getSensorId(), p.getPollutantId(), now);
                measurements.add(new AqiMeasurement(
                        getPollutantEnum(p.getAbbreviation()),
                        p.aqiFromRawMeasurement(capturedPollutant.getValue().doubleValue()),
                        capturedPollutant.getDatetime().toInstant())
                );
            }
            polygons.add(new AqiPolygon(
                    roundDouble(s.getLatitude().doubleValue() + increment, decimalPlaces + 1),
                    roundDouble(s.getLatitude().doubleValue() - increment, decimalPlaces + 1 ),
                    roundDouble(s.getLongitude().doubleValue() - increment, decimalPlaces + 1),
                    roundDouble(s.getLongitude().doubleValue() + increment, decimalPlaces + 1),
                    measurements
            ));
        }
        return new AqiPolygons(polygons);
    }

    private double roundDouble(double num, int places) {
        return Math.round(num * Math.pow(10, places)) / Math.pow(10, places);
    }

    private double getDistance(double lat, double lon, Sensor sensor) {
        double a = sensor.getLatitude().doubleValue() - lat;
        double b = sensor.getLongitude().doubleValue() - lon;
        double c = Math.sqrt(a*a + b*b);
        return c;

    }

    private Sensor getNearestSensor(List<Sensor> sensors, double latitude, double longitude) {
        // this assumes Euclidean geometry and is only intended for a small set of sensors.
        Sensor result = sensors.get(0);
        double distance = 100;
        for (Sensor sensor : sensors) {
            // double tmpDis = Math.abs(sensor.getSensorId().doubleValue() - latitude) + Math.abs(sensor.getLongitude().doubleValue() - longitude);
            double tmpDis = getDistance(latitude, longitude, sensor);
            if (tmpDis < distance) {
                result = sensor;
                distance = tmpDis;
            }
        }
        return result;
    }

    public AqiPoint getAqiPoint(double latitude, double longitude){
        // TODO wip
        //double increment = 1; // this is dangerously assuming that we have sensors every third decimal place . . .
        List<Pollutant> pollutantsTable = DatabaseCacheUtil.POLLUTANTS_TABLE;
        List<Sensor> sensors = DatabaseCacheUtil.SENSORS_TABLE;

        Sensor nearest = getNearestSensor(sensors, latitude, longitude);

        Date now = new Date();
        List<AqiMeasurement> measurements = new ArrayList<AqiMeasurement>();
        for (Pollutant p : pollutantsTable) {
            CapturedPollutant capturedPollutant = getMostRecent(nearest.getSensorId(), p.getPollutantId(), now);
            measurements.add(new AqiMeasurement(
                    getPollutantEnum(p.getAbbreviation()),
                    p.aqiFromRawMeasurement(capturedPollutant.getValue().doubleValue()),
                    capturedPollutant.getDatetime().toInstant())
            );
        }
        //System.out.println(measurements);
        return new AqiPoint(latitude, longitude, measurements);
    }


    private CapturedPollutant getMostRecent(Integer sensorId, Integer pollutantId, Date now) {
        Map<Integer, Map<Integer, List<CapturedPollutant>>> map = DatabaseCacheUtil.CAPTURED_POLLUTANT_TABLE_MAP;
        List<CapturedPollutant> capturedPollutants = map.get(pollutantId).get(sensorId);
        CapturedPollutant result = capturedPollutants.get(0);
        long difference = Long.MAX_VALUE;
        for (int i = 1; i < capturedPollutants.size(); i++) {
            CapturedPollutant tmp = capturedPollutants.get(i);
            if (tmp.getDatetime().compareTo(now) < 0 && now.getTime() - tmp.getDatetime().getTime() < difference) {
                result = tmp;
                difference = now.getTime() - tmp.getDatetime().getTime();
            }
        }
        return result;
    }


}
