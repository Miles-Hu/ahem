package com.se6387.ahem.controller;

import com.se6387.ahem.model.CapturedPollutant;
import com.se6387.ahem.repository.CapturedPollutantRepository;
import com.se6387.ahem.sensor.AqiMeasurement;
import com.se6387.ahem.sensor.AqiPolygon;
import com.se6387.ahem.sensor.AqiPolygons;
import com.se6387.ahem.service.MapService;
import com.se6387.ahem.service.Pollutant;
import com.se6387.ahem.service.PollutantService;
import com.se6387.ahem.view.Coordinate;
import com.se6387.ahem.view.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Jun
 * @create 2023-04-07 11:36 AM
 * @email miles.j.hoo@gmail.com
 **/

@RequestMapping()
@RestController
public class MainController {

    @Autowired
    private MapService mapService;

    @Autowired
    private CapturedPollutantRepository capturedPollutantRepository;

    @Autowired
    private PollutantService pollutantService;

    @GetMapping("/v1/route")
    public ResponseEntity<List<Coordinate>> route(@RequestParam String coordinates,
                                                  @RequestParam(required = false) List<Integer> sensitivePollutants) {
        ResponseEntity<List<Coordinate>> responseEntity = new ResponseEntity<>();
        String[] split = coordinates.split(",");
        if (split.length!= 4) {
            return responseEntity.fail("Parameter error");
        }
        Coordinate start = new Coordinate(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
        Coordinate end = new Coordinate(Double.parseDouble(split[2]), Double.parseDouble(split[3]));
        return responseEntity.success(mapService.route(start, end, sensitivePollutants));
    }

    @GetMapping("/v1/captured_pollutants")
    public ResponseEntity<List<CapturedPollutant>> getCapturedPollutant(@RequestParam long startTimestamp, @RequestParam long endTimestamp) {
        ResponseEntity<List<CapturedPollutant>> responseEntity = new ResponseEntity<>();
        List<CapturedPollutant> byDatetimeBetween = capturedPollutantRepository.findByDatetimeBetween(new Date(startTimestamp), new Date(endTimestamp));
        return responseEntity.success(byDatetimeBetween);
    }

    @GetMapping("/v1/visualization/polygons")
    public ResponseEntity<AqiPolygons> polygons(@RequestParam String boundary, @RequestParam Integer decimalPlaces) {


        pollutantService.getAqiPoints(0, 0);

        ResponseEntity responseEntity = new ResponseEntity();
        if (boundary == null || boundary.trim().length() == 0) {
            return responseEntity.fail("Parameter error");
        }

        String[] split = boundary.split(",");
        double northernLatitude = Double.parseDouble(split[0]);
        double southernLatitude = Double.parseDouble(split[1]);
        double westernLongitude = Double.parseDouble(split[2]);
        double easternLongitude = Double.parseDouble(split[3]);

        return responseEntity.success(pollutantService
                .getAqiPolygons(northernLatitude, southernLatitude, westernLongitude, easternLongitude, decimalPlaces));

        /*AqiMeasurement PM25 = new AqiMeasurement(Pollutant.PM25, 10, Instant.now());
        AqiMeasurement PM10 = new AqiMeasurement(Pollutant.PM10, 100, Instant.now());
        AqiMeasurement O3 = new AqiMeasurement(Pollutant.O3, 200, Instant.now());
        AqiMeasurement SO2 = new AqiMeasurement(Pollutant.SO2, 300, Instant.now());
        AqiMeasurement CO = new AqiMeasurement(Pollutant.CO, 400, Instant.now());
        AqiMeasurement NO2 = new AqiMeasurement(Pollutant.NO2, 500, Instant.now());
        List<AqiMeasurement> aqiMeasurements = new ArrayList<>(Arrays.asList(PM25, PM10, O3, SO2, CO, NO2));
        AqiPolygon ap = new AqiPolygon(northernLatitude, southernLatitude, westernLongitude, easternLongitude, aqiMeasurements);
        List<AqiPolygon> list = new ArrayList<>();
        list.add(ap);
        AqiPolygons aqiPolygons = new AqiPolygons(list);
        return responseEntity.success(aqiPolygons);*/

    }

}
