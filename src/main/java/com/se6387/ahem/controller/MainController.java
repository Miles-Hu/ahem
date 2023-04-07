package com.se6387.ahem.controller;

import com.se6387.ahem.service.MapService;
import com.se6387.ahem.view.Coordinate;
import com.se6387.ahem.view.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @GetMapping("/v1/route")
    public ResponseEntity<List<Coordinate>> route(@RequestParam String coordinates) {
        ResponseEntity<List<Coordinate>> responseEntity = new ResponseEntity();
        String[] split = coordinates.split(",");
        if (split.length!= 4) {
            return responseEntity.fail("Parameter error");
        }
        Coordinate start = new Coordinate(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
        Coordinate end = new Coordinate(Double.parseDouble(split[2]), Double.parseDouble(split[3]));
        return responseEntity.success(mapService.route(start, end));
    }

}
