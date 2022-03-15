package de.dmn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import de.dmn.model.OutputData;

@Repository
public interface OutputDataRepository extends JpaRepository<OutputData, Long>{
    @Query(value = "SELECT * FROM output_data u WHERE u.name = ?1", nativeQuery = true)
    List<OutputData> findByName(String name);
}