package com.se6387.ahem.sensor;

import com.se6387.ahem.model.Pollutant;
import com.se6387.ahem.repository.PollutantRepository;

import javax.persistence.EntityManagerFactory;
import java.util.List;

public class AqiRequest {

    EntityManagerFactory emf;

    public static String getPollutants(PollutantRepository pollutantRepository) {
        String result = "";
        result += "pollutantExample: " + pollutantRepository +"\n";
        List<Pollutant> pollutants = pollutantRepository.findAll();
        result += "pollutants: ";
        for (Pollutant p : pollutants) result += " [ " + p + " ]";
        result += "\n";
        return result;
    }

    public AqiMeasurement measurement() {
        AqiMeasurement result = null;



        return result;

    }
}
