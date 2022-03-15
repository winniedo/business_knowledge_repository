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
import de.dmn.model.OrganisationalUnit;
import de.dmn.repository.OrganisationalUnitRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/dmnapi/v1/organisationalUnit/")
public class OrganisationalUnitController {

    @Autowired
    private OrganisationalUnitRepository organisationalUnitRepository;

    // get all organisational unit
    @GetMapping("/")
    public List<OrganisationalUnit> getAllOrganisationalUnit() {
        return organisationalUnitRepository.findAll();
    }

    // create organisational unit rest api
    @PostMapping("/")
    public OrganisationalUnit createOrganisationalUnit(@RequestBody OrganisationalUnit organisationalUnit) {
        List<OrganisationalUnit> elt = organisationalUnitRepository.findByName(organisationalUnit.getName());
        if (elt.size() != 0) {
            return organisationalUnitRepository.save(elt.get(0));
        } else {
            return organisationalUnitRepository.save(organisationalUnit);
        }
    }

    // get organisational unit by id rest api
    @GetMapping("/{id}")
    public ResponseEntity<OrganisationalUnit> getOrganisationalUnitById(@PathVariable Long id) {
        OrganisationalUnit organisationalUnit = organisationalUnitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organisational unit not exist with id :" + id));
        return ResponseEntity.ok(organisationalUnit);
    }

    // update organisational unit rest api
    @PutMapping("/{id}")
    public ResponseEntity<OrganisationalUnit> updateOrganisationalUnit(@PathVariable Long id,
            @RequestBody OrganisationalUnit organisationalUnitDetails) {
        OrganisationalUnit organisationalUnit = organisationalUnitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organisational unit not exist with id :" + id));

        organisationalUnit.setName(organisationalUnitDetails.getName());
        organisationalUnit.setDescription(organisationalUnitDetails.getDescription());
        organisationalUnit.setUrl(organisationalUnitDetails.getUrl());

        OrganisationalUnit updatedOrganisationalUnit = organisationalUnitRepository.save(organisationalUnit);
        return ResponseEntity.ok(updatedOrganisationalUnit);
    }

    // delete organisational unit rest api
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteOrganisationalUnit(@PathVariable Long id) {
        OrganisationalUnit organisationalUnit = organisationalUnitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organisational unit not exist with id :" + id));

        organisationalUnitRepository.delete(organisationalUnit);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
