package com.se6387.ahem.service;

import com.se6387.ahem.model.Pollutant;
import com.se6387.ahem.repository.PollutantRepository;
import com.se6387.ahem.sensor.AqiPoint;
import com.se6387.ahem.sensor.AqiPolygons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PollutantService {


    @Autowired
    private PollutantRepository pollutantRepository;

    public List<AqiPoint> getAqiPoints(double longitude, double latitude) {
        List<Pollutant> all = pollutantRepository.findAll();
        return null;
    }

    public AqiPolygons getAqiPolygons(double nLat, double sLat, double wLon, double eLon, int decimalPlaces) {
        //TODO
        return null;
    }

    public AqiPoint getAqiPoint(double latitude, double longitude){
        // TODO
        return null;
    }




}
