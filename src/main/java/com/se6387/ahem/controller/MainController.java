package com.se6387.ahem.controller;

import com.se6387.ahem.model.CapturedPollutant;
import com.se6387.ahem.repository.CapturedPollutantRepository;
import com.se6387.ahem.sensor.AqiMeasurement;
import com.se6387.ahem.sensor.AqiPoint;
import com.se6387.ahem.sensor.AqiPolygon;
import com.se6387.ahem.sensor.AqiPolygons;
import com.se6387.ahem.service.DbTestTools;
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

    @Autowired
    DbTestTools dbTestTools;

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

    // Jamie Test TODO remove!
    // http://localhost:8082/v1/JamieTest?nLat=32.99&sLat=32.95&wLon=-96.8&eLon=-96.7
    /**/
    @GetMapping("/v1/JamieTest")
    public ResponseEntity<List<Integer>> getSensorIds(@RequestParam double nLat, @RequestParam double sLat,
                                                      @RequestParam double wLon, @RequestParam double eLon) {
        ResponseEntity<List<Integer>> responseEntity = new ResponseEntity<>();
        List<Integer> result = (List<Integer>) pollutantService.getSensorIds(nLat, sLat, wLon, eLon);
        return responseEntity.success(result);
    }

    // http://localhost:8082/v1/db_tools/wip
    @GetMapping("v1/db_tools/wip")
    public ResponseEntity<String> wip() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>();
        dbTestTools.wip();
        return responseEntity.success("wip done");
    }

    @GetMapping("v1/db_tools/reset_data") // jtsmith
    public ResponseEntity<String> resetData() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>();
        String result = "START: " + new Date().toString() + "\n";
        dbTestTools.resetData();
        result += "END: " + new Date().toString() +"\n";
        return responseEntity.success(result);
    }

    @GetMapping("v1/pollutant/get_aqi_polygons") // jtsmith
    // http://localhost:8082/v1/pollutant/get_aqi_polygons?nLat=32.99&sLat=32.95&wLon=-96.8&eLon=-96.7&decimalPlaces=3
    public ResponseEntity<AqiPolygons> getAqiPolygons(@RequestParam double nLat, @RequestParam double sLat, @RequestParam double wLon, @RequestParam double eLon, @RequestParam int decimalPlaces) {
        ResponseEntity<AqiPolygons> responseEntity = new ResponseEntity<AqiPolygons>();
        AqiPolygons result = pollutantService.getAqiPolygons(nLat, sLat, wLon, eLon, decimalPlaces);
        return responseEntity.success(result);
    }

    @GetMapping("v1/pollutant/get_aqi_point") // jtsmith
    // http://localhost:8082/v1/pollutant/get_aqi_point?latitude=32.971&longitude=-96.738
    public ResponseEntity<AqiPoint> getAqiPoint(@RequestParam double latitude, @RequestParam double longitude) {
        ResponseEntity<AqiPoint> responseEntity = new ResponseEntity<AqiPoint>();
        AqiPoint result = pollutantService.getAqiPoint(latitude, longitude);
        return responseEntity.success(result);
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

    }

}
