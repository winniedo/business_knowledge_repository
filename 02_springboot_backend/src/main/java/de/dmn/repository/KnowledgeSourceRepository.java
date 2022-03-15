package de.dmn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import de.dmn.model.KnowledgeSource;

@Repository
public interface KnowledgeSourceRepository extends JpaRepository<KnowledgeSource, Long>{
    @Query(value = "SELECT * FROM knowledge_source u WHERE u.name = ?1", nativeQuery = true)
    List<KnowledgeSource> findByName(String name);
}