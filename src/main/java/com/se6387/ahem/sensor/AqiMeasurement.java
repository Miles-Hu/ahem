package com.se6387.ahem.sensor;

import com.se6387.ahem.service.PollutantEnum;
import java.time.Instant;

/*
 * @author jtsmith@utdallas.edu
 */

public class AqiMeasurement {

    private final PollutantEnum pollutantId;
    private final int value;
    private final Instant timestamp;

    public AqiMeasurement(PollutantEnum pollutantId, int value, Instant timestamp) {
        this.pollutantId = pollutantId;
        this.value = value;
        this.timestamp = timestamp;
    }

    public PollutantEnum getPollutantId() {
        return this.pollutantId;
    }

    public int getValue() {
        return this.value;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    @Override
    public String toString() {
        return "AqiMeasurement{" +
                "pollutantId=" + pollutantId +
                ", value=" + value +
                ", timestamp=" + timestamp +
                '}';
    }
}
