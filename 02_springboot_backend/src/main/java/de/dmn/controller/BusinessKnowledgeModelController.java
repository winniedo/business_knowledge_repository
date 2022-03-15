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
import de.dmn.model.BusinessKnowledgeModel;
import de.dmn.repository.BusinessKnowledgeModelRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/dmnapi/v1/businessKnowledge/")
public class BusinessKnowledgeModelController {

    @Autowired
    private BusinessKnowledgeModelRepository businessKnowledgeModelRepository;

    // get all business knowledge
    @GetMapping("/")
    public List<BusinessKnowledgeModel> getAllBusinessKnowledge() {
        return businessKnowledgeModelRepository.findAll();
    }

    // create business knowledge rest api
    @PostMapping("/")
    public BusinessKnowledgeModel createBusinessKnowledge(@RequestBody BusinessKnowledgeModel businessKnowledge) {
        List<BusinessKnowledgeModel> elt = businessKnowledgeModelRepository.findByName(businessKnowledge.getName());
        if (elt.size() != 0) {
            return businessKnowledgeModelRepository.save(elt.get(0));
        } else {
            return businessKnowledgeModelRepository.save(businessKnowledge);
        }
    }

    // get business knowledge by id rest api
    @GetMapping("/{id}")
    public ResponseEntity<BusinessKnowledgeModel> getBusinessKnowledgeById(@PathVariable Long id) {
        BusinessKnowledgeModel businessKnowledge = businessKnowledgeModelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Business knowledge not exist with id :" + id));
        return ResponseEntity.ok(businessKnowledge);
    }

    // update business knowledge rest api
    @PutMapping("/{id}")
    public ResponseEntity<BusinessKnowledgeModel> updateBusinessKnowledge(@PathVariable Long id,
            @RequestBody BusinessKnowledgeModel businessKnowledgeDetails) {
        BusinessKnowledgeModel businessKnowledge = businessKnowledgeModelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Business knowledge not exist with id :" + id));

        businessKnowledge.setName(businessKnowledgeDetails.getName());
        businessKnowledge.setDescription(businessKnowledgeDetails.getDescription());
        businessKnowledge.setUrl(businessKnowledgeDetails.getUrl());

        BusinessKnowledgeModel updatedBusinessKnowledge = businessKnowledgeModelRepository.save(businessKnowledge);
        return ResponseEntity.ok(updatedBusinessKnowledge);
    }

    // delete business knowledge rest api
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteBusinessKnowledge(@PathVariable Long id) {
        BusinessKnowledgeModel businessKnowledge = businessKnowledgeModelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Business knowledge not exist with id :" + id));

        businessKnowledgeModelRepository.delete(businessKnowledge);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
