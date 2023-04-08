package com.se6387.ahem.repository;


import com.se6387.ahem.model.Pollutant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PollutantRepository extends JpaRepository<Pollutant, Integer> {
}
