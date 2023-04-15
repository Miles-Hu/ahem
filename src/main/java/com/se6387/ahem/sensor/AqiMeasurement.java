package com.se6387.ahem.sensor;

import com.se6387.ahem.service.Pollutant;
import java.time.Instant;

/*
 * @author jtsmith@utdallas.edu
 */

public class AqiMeasurement {

    private final Pollutant pollutantId;
    private final int value;
    private final Instant timestamp;

    public AqiMeasurement(Pollutant pollutantId, int value, Instant timestamp) {
        this.pollutantId = pollutantId;
        this.value = value;
        this.timestamp = timestamp;
    }

    public Pollutant getPollutantId() {
        return this.pollutantId;
    }

    public int getValue() {
        return this.value;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

}
