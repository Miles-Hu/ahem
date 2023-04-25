package com.se6387.ahem.service;

import com.se6387.ahem.model.CapturedPollutant;
import com.se6387.ahem.model.Pollutant;
import com.se6387.ahem.model.Sensor;
import com.se6387.ahem.repository.CapturedPollutantRepository;
import com.se6387.ahem.repository.PollutantRepository;
import com.se6387.ahem.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DbTestTools {

    @Autowired
    private CapturedPollutantRepository capturedPollutantRepository;
    @Autowired
    private PollutantRepository pollutantRepository;
    @Autowired
    private SensorRepository sensorRepository;

    /*
    @author jtsmith

    Clears all sensors and captured pollutant data from the db and repopulates them
     */
    public void resetData() {
        emptyDatabaseMeasurements();
        emptySensorTable();
        populateSensorTable();
        Instant timestamp = Instant.now();
        Instant startTime = timestamp.minusSeconds(60L * 60L); // 1 hour before
        Instant endTime = timestamp.plusSeconds(60L * 60L *12L) ; // 12 hours after
        populateTestDataMeasurements(startTime, endTime, 300); // 5 min interval

    }

    public void emptyDatabase() {
        capturedPollutantRepository.deleteAll();
        pollutantRepository.deleteAll();
        sensorRepository.deleteAll();
    }

    public void emptyDatabaseMeasurements() {
        capturedPollutantRepository.deleteAll();
    }

    public void emptySensorTable() {
        sensorRepository.deleteAll();
    }

    /*
    populates the immediate UTD area
     */
    public void populateSensorTable() {
        this.populateSensorTable(33.0000, 32.9770, -96.7600, -96.7430, 3 );
    }

    /*
    adds an evenly spaced grid of interpolated sensors to the specified number of decinal places
     */
    public void populateSensorTable(double nLat, double sLat, double wLon, double eLon, int decimalPlaces) {
        double increment = Math.pow(10, -decimalPlaces);
        double curLat = this.roundDouble(sLat, decimalPlaces);
        double curLon = this.roundDouble(wLon, decimalPlaces);
        double maxLat = nLat;
        double maxLon = eLon;

        while (curLat <= maxLat) {
            while (curLon <= maxLon) {
                Sensor record = new Sensor();
                record.setLatitude(new BigDecimal(curLat));
                record.setLongitude(new BigDecimal(curLon));
                record.setRadiusMeters(new BigDecimal(-decimalPlaces));
                sensorRepository.save(record);

                curLon = this.roundDouble(curLon + increment, decimalPlaces);
            }
            curLon = this.roundDouble(wLon, decimalPlaces);
            curLat = this.roundDouble(curLat + increment, decimalPlaces);
        }

    }

    double roundDouble(double num, int places) {
        return Math.round(num * Math.pow(10, places)) / Math.pow(10, places);

    }

    double get_random_pollutant_value(double[] breakpoints) {
        double AQI_500_BREAKPOINT_HI = breakpoints[0];
        int decimalPlaces = (int) breakpoints[7];
        return roundDouble(AQI_500_BREAKPOINT_HI * Math.pow(Math.random(), 6), decimalPlaces);
    }


/*
Attempts to generate realistic data for every sensor
 */
    public void populateEverySensorWithRandomData(Instant timestamp) {

        List<Sensor> sensors = sensorRepository.findAll();
        List<Pollutant> pollutants = pollutantRepository.findAll();

        List<CapturedPollutant> records = new ArrayList<>();

        for(Sensor s: sensors) {
            for (Pollutant p: pollutants) {

                CapturedPollutant record = new CapturedPollutant();
                record.setSensorId(s.getSensorId());
                record.setPollutantId(p.getPollutantId());
                record.setDatetime(Date.from(timestamp));
                record.setValue(new BigDecimal(this.get_random_pollutant_value(p.getAqiBreakpoints())));
                record.setAqi(-1);
                record.setUnit("");
                records.add(record);
            }
        }

        capturedPollutantRepository.saveAll(records);
    }

    /**
     * @param interval seconds
     */
    public void populateTestDataMeasurements(Instant startTime, Instant endTime, long interval) {
        Instant timestamp =  Instant.from(startTime);

        while (timestamp.compareTo(endTime) <= 0) {
            populateEverySensorWithRandomData(timestamp);
            timestamp = timestamp.plusSeconds(interval);
        }
    }

    public void wip() {
        // for testing purposes
        emptyDatabaseMeasurements();
        emptySensorTable();
        populateSensorTable();
        Instant timestamp = Instant.now();
        Instant startTime = timestamp.minusSeconds(60L * 60L); // 1 hour before
        Instant endTime = timestamp.plusSeconds(60L * 60L) ; // 1 hour after
        populateTestDataMeasurements(startTime, endTime, 300); // 5 min interval
    }

}
