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
import de.dmn.model.OutputData;
import de.dmn.repository.OutputDataRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/dmnapi/v1/outputData/")
public class OutputDataController {

    @Autowired
    private OutputDataRepository outputDataRepository;

    // get all output data
    @GetMapping("/")
    public List<OutputData> getAllOutputData() {
        return outputDataRepository.findAll();
    }

    // create output data rest api
    @PostMapping("/")
    public OutputData createOutputData(@RequestBody OutputData outputData) {
        List<OutputData> elt = outputDataRepository.findByName(outputData.getName());
        if (elt.size() != 0) {
            return outputDataRepository.save(elt.get(0));
        } else {
            return outputDataRepository.save(outputData);
        }
    }

    // get output data by id rest api
    @GetMapping("/{id}")
    public ResponseEntity<OutputData> getOutputDataById(@PathVariable Long id) {
        OutputData outputData = outputDataRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Output data not exist with id :" + id));
        return ResponseEntity.ok(outputData);
    }

    // update output data rest api
    @PutMapping("/{id}")
    public ResponseEntity<OutputData> updateOutputData(@PathVariable Long id,
            @RequestBody OutputData outputDataDetails) {
        OutputData outputData = outputDataRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Output data not exist with id :" + id));

        outputData.setName(outputDataDetails.getName());
        outputData.setDescription(outputDataDetails.getDescription());

        OutputData updatedOutputData = outputDataRepository.save(outputData);
        return ResponseEntity.ok(updatedOutputData);
    }

    // delete output data rest api
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteOutputData(@PathVariable Long id) {
        OutputData outputData = outputDataRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Input data not exist with id :" + id));

        outputDataRepository.delete(outputData);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
