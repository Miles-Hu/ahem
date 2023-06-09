package com.se6387.ahem.controller;

import com.se6387.ahem.model.CapturedPollutant;
import com.se6387.ahem.repository.CapturedPollutantRepository;
import com.se6387.ahem.sensor.AqiMeasurement;
import com.se6387.ahem.sensor.AqiPoint;
import com.se6387.ahem.sensor.AqiPolygon;
import com.se6387.ahem.sensor.AqiPolygons;
import com.se6387.ahem.service.DbTestTools;
import com.se6387.ahem.service.RouteService;
import com.se6387.ahem.service.PollutantService;
import com.se6387.ahem.view.Coordinate;
import com.se6387.ahem.view.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
    private RouteService mapService;

    @Autowired
    private CapturedPollutantRepository capturedPollutantRepository;

    @Autowired
    private PollutantService pollutantService;

    @Autowired
    DbTestTools dbTestTools;

    @GetMapping("/v1/route")
    public ResponseEntity<List<Coordinate>> route(@RequestParam String coordinates,
                                                  @RequestParam(required = false) List<Integer> sensitivePollutants) {
        System.out.println("Request for route: " + coordinates);
        ResponseEntity<List<Coordinate>> responseEntity = new ResponseEntity<>();
        String[] split = coordinates.split(",");
        if (split.length!= 4) {
            return responseEntity.fail("Parameter error");
        }
        Coordinate start = new Coordinate(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
        Coordinate end = new Coordinate(Double.parseDouble(split[2]), Double.parseDouble(split[3]));
        Collections.sort(sensitivePollutants);
        return responseEntity.success(mapService.route(start, end, sensitivePollutants));
    }

    @GetMapping("/v1/captured_pollutants")
    public ResponseEntity<List<CapturedPollutant>> getCapturedPollutant(@RequestParam long startTimestamp, @RequestParam long endTimestamp) {
        ResponseEntity<List<CapturedPollutant>> responseEntity = new ResponseEntity<>();
        List<CapturedPollutant> byDatetimeBetween = capturedPollutantRepository.findByDatetimeBetween(new Date(startTimestamp), new Date(endTimestamp));
        return responseEntity.success(byDatetimeBetween);
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
    // http://localhost:8082/v1/pollutant/get_aqi_polygons?nLat=32.9855&sLat=32.983&wLon=-96.75&eLon=-96.745&decimalPlaces=3
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

        System.out.println("Requesting polygons: " + boundary);

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
        AqiPolygons aqiPolygons = pollutantService
                .getAqiPolygons(northernLatitude, southernLatitude, westernLongitude, easternLongitude, decimalPlaces);


        int redNum = 0, yellowNum = 0, greenNum = 0;
        for (AqiPolygon polygon : aqiPolygons.getPolygons()) {
            int maxAQI = 0;
            for (AqiMeasurement aqiMeasurement : polygon.getMeasurements()) {
                maxAQI = Math.max(maxAQI, aqiMeasurement.getValue());
            }
            if (maxAQI > 150) {
                redNum++;
            } else if (maxAQI > 50) {
                yellowNum++;
            } else {
                greenNum++;
            }
        }
        System.out.println("redGridNum: " + redNum + ", yellowGridNum" + yellowNum +  ", greenGridNun: " + greenNum);
        System.out.println(aqiPolygons.getPolygons().size());
        return responseEntity.success(aqiPolygons);

    }

}
