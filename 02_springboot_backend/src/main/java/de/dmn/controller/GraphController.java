package de.dmn.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.index.CorruptIndexException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.dmn.exception.ResourceNotFoundException;
import de.dmn.model.BusinessKnowledgeModel;
import de.dmn.model.Decision;
import de.dmn.model.DecisionExtended;
import de.dmn.model.InputData;
import de.dmn.model.KnowledgeSource;
import de.dmn.model.OrganisationalUnit;
import de.dmn.model.OutputData;
import de.dmn.model.PerformanceIndicator;
import de.dmn.model.PerformanceMeasure;
import de.dmn.model.ReactGraph;
import de.dmn.model.ReactNode;
import de.dmn.model.ReactOutPutAndGraph;
import de.dmn.model.ReactResults;
import de.dmn.model.TestResults;
import de.dmn.model.UserData;
import de.dmn.repository.BusinessKnowledgeModelRepository;
import de.dmn.repository.DecisionRepository;
import de.dmn.repository.InputDataRepository;
import de.dmn.repository.KnowledgeSourceRepository;
import de.dmn.repository.OrganisationalUnitRepository;
import de.dmn.repository.OutputDataRepository;
import de.dmn.repository.PerformanceIndicatorRepository;
import de.dmn.repository.PerformanceMeasureRepository;
import de.dmn.service.graph.GraphManagement;
import de.linguatools.disco.CorruptConfigFileException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/dmnapi/v1/graph/")
public class GraphController {

    @Autowired
    private DecisionRepository decisionRepository;

    @Autowired
    private KnowledgeSourceRepository knowledgeSourceRepository;

    @Autowired
    private BusinessKnowledgeModelRepository businessKnowledgeModelRepository;

    @Autowired
    private InputDataRepository inputDataRepository;

    @Autowired
    private OrganisationalUnitRepository organisationalUnitRepository;

    @Autowired
    private OutputDataRepository outputDataRepository;

    @Autowired
    private PerformanceIndicatorRepository performanceIndicatorRepository;

    @Autowired
    private PerformanceMeasureRepository performanceMeasureRepository;

    @Autowired
    private GraphManagement graphManagement;

    // create performance test api
    @PostMapping("/performanceTest/")
    public TestResults performanceTest(@RequestParam Integer depth, @RequestParam Integer decToOutputEdges, @RequestParam Integer inputToDecEdges, @RequestParam Integer outputToDecEdges) throws IOException, InterruptedException, CorruptConfigFileException {
        return graphManagement.performanceTest(depth, decToOutputEdges, inputToDecEdges, outputToDecEdges);
    }

    // create performance test api
    @PostMapping("/bruteForcePerformanceTest/")
    public TestResults bruteForcePerformanceTest(@RequestParam Integer depth, @RequestParam Integer decToOutputEdges, @RequestParam Integer inputToDecEdges, @RequestParam Integer outputToDecEdges) throws IOException, InterruptedException, CorruptConfigFileException {
        return graphManagement.bruteForcePerformanceTest(depth, decToOutputEdges, inputToDecEdges, outputToDecEdges);
    }

    @PostMapping("/matchingAlgorithm/")
    public ReactResults matchingAlgorithm(@RequestBody UserData userData, @RequestParam Double threshold) throws CorruptIndexException, FileNotFoundException, IOException, CorruptConfigFileException, InterruptedException {
        return graphManagement.matchingAlgorithm(userData.getBusinessObjectives(), userData.getUserInputData(), userData.getUserOutputData(), threshold);
    }

    // get all performance indicator
    @GetMapping("/allInputData/")
    public List < InputData > getAllInputData() {
        return inputDataRepository.findAll();
    }

    // get all performance indicator
    @GetMapping("/allOutputData/")
    public List < OutputData > getAllOutputData() {
        return outputDataRepository.findAll();
    }

    // get all performance indicator
    @GetMapping("/initialGraph/")
    public ReactGraph getInitialGraph() {
        return graphManagement.getInitialGraph();
    }

}
