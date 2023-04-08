package com.se6387.ahem.service;

import com.se6387.ahem.view.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author Jun
 * @create 2023-04-07 11:37 AM
 * @email miles.j.hoo@gmail.com
 **/

@Service
public class MapService {

    @Autowired
    private MyOsmReader myOsmReader;

    public List<Coordinate> route(Coordinate start, Coordinate end, List<Integer> sensitivePollutants) {
        return myOsmReader.route(start, end, sensitivePollutants);
    }

}
