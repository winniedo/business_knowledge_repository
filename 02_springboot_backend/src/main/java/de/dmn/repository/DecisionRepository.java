package de.dmn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import de.dmn.model.Decision;

@Repository
public interface DecisionRepository extends JpaRepository<Decision, Long>{
    @Query(value = "SELECT * FROM decision u WHERE u.name = ?1", nativeQuery = true)
    List<Decision> findByName(String name);
}