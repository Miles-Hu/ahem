package com.se6387.ahem.utils;

import com.se6387.ahem.model.CapturedPollutant;
import com.se6387.ahem.model.Pollutant;
import com.se6387.ahem.model.Sensor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jun
 * @create 2023-04-29 10:05 AM
 * @email miles.j.hoo@gmail.com
 **/

public class DatabaseCacheUtil {

    public static final Map<Integer, Map<Integer, List<CapturedPollutant>>> CAPTURED_POLLUTANT_TABLE = new HashMap<>();

    public static final List<Sensor> SENSORS_TABLE = new ArrayList<>();

    public static final List<Pollutant> POLLUTANTS_TABLE = new ArrayList<>();
}
