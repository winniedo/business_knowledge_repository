package de.dmn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import de.dmn.model.InputData;

@Repository
public interface InputDataRepository extends JpaRepository<InputData, Long>{
    @Query(value = "SELECT * FROM input_data u WHERE u.name = ?1", nativeQuery = true)
    List<InputData> findByName(String name);
}