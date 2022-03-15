package de.dmn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.dmn.model.PerformanceMeasure;

@Repository
public interface PerformanceMeasureRepository extends JpaRepository<PerformanceMeasure, Long>{

}