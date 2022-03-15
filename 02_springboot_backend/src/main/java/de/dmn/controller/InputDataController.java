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
import de.dmn.model.InputData;
import de.dmn.repository.InputDataRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/dmnapi/v1/inputData/")
public class InputDataController {

    @Autowired
    private InputDataRepository inputDataRepository;

    // get all input data
    @GetMapping("/")
    public List<InputData> getAllInputData() {
        return inputDataRepository.findAll();
    }

    // create input data rest api
    @PostMapping("/")
    public InputData createInputData(@RequestBody InputData inputData) {
        List<InputData> elt = inputDataRepository.findByName(inputData.getName());
        if (elt.size() != 0) {
            return inputDataRepository.save(elt.get(0));
        } else {
            return inputDataRepository.save(inputData);
        }
    }

    // get input data by id rest api
    @GetMapping("/{id}")
    public ResponseEntity<InputData> getInputDataById(@PathVariable Long id) {
        InputData inputData = inputDataRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Input data not exist with id :" + id));
        return ResponseEntity.ok(inputData);
    }

    // update input data rest api
    @PutMapping("/{id}")
    public ResponseEntity<InputData> updateInputData(@PathVariable Long id, @RequestBody InputData inputDataDetails) {
        InputData inputData = inputDataRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Input data not exist with id :" + id));

        inputData.setName(inputDataDetails.getName());
        inputData.setDescription(inputDataDetails.getDescription());

        InputData updatedInputData = inputDataRepository.save(inputData);
        return ResponseEntity.ok(updatedInputData);
    }

    // delete input data rest api
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteInputData(@PathVariable Long id) {
        InputData inputData = inputDataRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Input data not exist with id :" + id));

        inputDataRepository.delete(inputData);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
