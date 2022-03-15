package de.dmn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import de.dmn.model.BusinessKnowledgeModel;

@Repository
public interface BusinessKnowledgeModelRepository extends JpaRepository<BusinessKnowledgeModel, Long>{
    @Query(value = "SELECT * FROM business_knowledge_model u WHERE u.name = ?1", nativeQuery = true)
    List<BusinessKnowledgeModel> findByName(String name);
}