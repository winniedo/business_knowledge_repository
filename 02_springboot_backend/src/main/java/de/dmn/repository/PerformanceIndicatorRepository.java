package de.dmn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.dmn.model.PerformanceIndicator;

@Repository
public interface PerformanceIndicatorRepository extends JpaRepository<PerformanceIndicator, Long>{

}