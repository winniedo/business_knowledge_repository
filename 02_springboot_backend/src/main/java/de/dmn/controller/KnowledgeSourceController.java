package de.dmn.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.dmn.exception.ResourceNotFoundException;
import de.dmn.model.KnowledgeSource;
import de.dmn.repository.KnowledgeSourceRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/dmnapi/v1/knowledgeSource/")
public class KnowledgeSourceController {

    @Autowired
    private KnowledgeSourceRepository knowledgeSourceRepository;

    // get all knowledge source
    @GetMapping("/")
    public List<KnowledgeSource> getAllKnowledgeSource() {
        return knowledgeSourceRepository.findAll();
    }

    // create knowledge source rest api
    @PostMapping("/")
    public KnowledgeSource createKnowledgeSource(@RequestBody KnowledgeSource knowledgeSource) {
        List<KnowledgeSource> elt = knowledgeSourceRepository.findByName(knowledgeSource.getName());
        if (elt.size() != 0) {
            return knowledgeSourceRepository.save(elt.get(0));
        } else {
            return knowledgeSourceRepository.save(knowledgeSource);
        }
    }

    // get knowledge source by id rest api
    @GetMapping("/{id}")
    public ResponseEntity<KnowledgeSource> getKnowledgeSourceById(@PathVariable Long id) {
        KnowledgeSource knowledgeSource = knowledgeSourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Knowledge source not exist with id :" + id));
        return ResponseEntity.ok(knowledgeSource);
    }

    // update knowledge source rest api
    @PutMapping("/{id}")
    public ResponseEntity<KnowledgeSource> updateKnowledgeSource(@PathVariable Long id,
            @RequestBody KnowledgeSource knowledgeSourceDetails) {
        KnowledgeSource knowledgeSource = knowledgeSourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Knowledge source not exist with id :" + id));

        knowledgeSource.setName(knowledgeSourceDetails.getName());
        knowledgeSource.setDescription(knowledgeSourceDetails.getDescription());
        knowledgeSource.setUrl(knowledgeSourceDetails.getUrl());

        KnowledgeSource updatedKnowledgeSource = knowledgeSourceRepository.save(knowledgeSource);
        return ResponseEntity.ok(updatedKnowledgeSource);
    }

    // delete knowledge source rest api
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteKnowledgeSource(@PathVariable Long id) {
        KnowledgeSource knowledgeSource = knowledgeSourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Knowledge source not exist with id :" + id));

        knowledgeSourceRepository.delete(knowledgeSource);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
