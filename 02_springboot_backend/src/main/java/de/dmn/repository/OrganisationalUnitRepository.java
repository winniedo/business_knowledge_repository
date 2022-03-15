package de.dmn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import de.dmn.model.OrganisationalUnit;

@Repository
public interface OrganisationalUnitRepository extends JpaRepository<OrganisationalUnit, Long>{
    @Query(value = "SELECT * FROM organisational_unit u WHERE u.name = ?1", nativeQuery = true)
    List<OrganisationalUnit> findByName(String name);
}