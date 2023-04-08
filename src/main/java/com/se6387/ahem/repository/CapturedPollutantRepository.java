package com.se6387.ahem.repository;

import com.se6387.ahem.model.CapturedPollutant;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface CapturedPollutantRepository extends JpaRepository<CapturedPollutant, Integer> {

    @Query("select d from CapturedPollutant d where d.datetime >= :from and d.datetime <= :to ")
    List<CapturedPollutant> findByDatetimeBetween(@Param("from") Date from, @Param("to") Date to);

}
