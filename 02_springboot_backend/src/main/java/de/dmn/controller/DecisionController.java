package de.dmn.controller;

import java.time.Instant;
import java.util.ArrayList;
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
import de.dmn.repository.BusinessKnowledgeModelRepository;
import de.dmn.repository.DecisionRepository;
import de.dmn.repository.InputDataRepository;
import de.dmn.repository.KnowledgeSourceRepository;
import de.dmn.repository.OrganisationalUnitRepository;
import de.dmn.repository.OutputDataRepository;
import de.dmn.repository.PerformanceIndicatorRepository;
import de.dmn.repository.PerformanceMeasureRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/dmnapi/v1/decision/")
public class DecisionController {

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

    // get all performance indicator
    @GetMapping("/")
    public List<Decision> getAllDecision() {
        return decisionRepository.findAll();
    }

    // create performance indicator rest api
    @PostMapping("/")
    public Decision createDecision(@RequestBody Decision decision) {
        List<Decision> elt = decisionRepository.findByName(decision.getName());
        if (elt.size() != 0) {
            return decisionRepository.save(elt.get(0));
        } else {
            return decisionRepository.save(decision);
        }
    }

    // get performance indicator by id rest api
    @GetMapping("/{id}")
    public ResponseEntity<Decision> getDecisionId(@PathVariable Long id) {
        Decision decision = decisionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Decision not exist with id :" + id));
        return ResponseEntity.ok(decision);
    }

    // get performance indicator by id rest api
    @GetMapping("/extended/{id}")
    public ResponseEntity<DecisionExtended> getDecisionExtendedId(@PathVariable Long id) {
        Decision decision = decisionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Decision not exist with id :" + id));
        DecisionExtended decisionExtended = new DecisionExtended(decision);

        List<KnowledgeSource> allKnowledgeSources = knowledgeSourceRepository.findAll();
        List<KnowledgeSource> allNotKnowledgeSources = new ArrayList<KnowledgeSource>();
        for (KnowledgeSource knowledgeSource : allKnowledgeSources) {
            if (!decision.getKnowledgeSources().contains(knowledgeSource)) {
                allNotKnowledgeSources.add(knowledgeSource);
            }
        }
        decisionExtended.setNotKnowledgeSources(allNotKnowledgeSources);

        List<OrganisationalUnit> allOwners = organisationalUnitRepository.findAll();
        List<OrganisationalUnit> allNotOwners = new ArrayList<OrganisationalUnit>();
        for (OrganisationalUnit Owner : allOwners) {
            if (!decision.getOwners().contains(Owner)) {
                allNotOwners.add(Owner);
            }
        }
        decisionExtended.setNotOwners(allNotOwners);
        decisionExtended.setAllDecisionMakers(allOwners);

        List<PerformanceIndicator> allPerformanceIndicators = performanceIndicatorRepository.findAll();
        decisionExtended.setAllPerformanceIndicators(allPerformanceIndicators);

        List<BusinessKnowledgeModel> allBusinessKnowledges = businessKnowledgeModelRepository.findAll();
        List<BusinessKnowledgeModel> allNotBusinessKnowledges = new ArrayList<BusinessKnowledgeModel>();
        for (BusinessKnowledgeModel businessKnowledge : allBusinessKnowledges) {
            if (!decision.getBusinessKnowledges().contains(businessKnowledge)) {
                allNotBusinessKnowledges.add(businessKnowledge);
            }
        }
        decisionExtended.setNotBusinessKnowledges(allNotBusinessKnowledges);

        List<InputData> allInputDatas = inputDataRepository.findAll();
        List<InputData> allNotInputDatas = new ArrayList<InputData>();
        for (InputData inputData : allInputDatas) {
            if (!decision.getInputData().contains(inputData)) {
                allNotInputDatas.add(inputData);
            }
        }
        decisionExtended.setNotInputData(allNotInputDatas);

        List<OutputData> allOutputDatas = outputDataRepository.findAll();
        List<OutputData> allNotOutputAsInputData = new ArrayList<OutputData>();
        for (OutputData outputAsInputData : allOutputDatas) {
            if (!decision.getOutputAsInputData().contains(outputAsInputData)) {
                allNotOutputAsInputData.add(outputAsInputData);
            }
        }
        decisionExtended.setNotOutputAsInputData(allNotOutputAsInputData);

        allOutputDatas = outputDataRepository.findAll();
        List<OutputData> allNotOutputDatas = new ArrayList<OutputData>();
        for (OutputData outputData : allOutputDatas) {
            if (!decision.getOutputData().contains(outputData)) {
                allNotOutputDatas.add(outputData);
            }
        }
        decisionExtended.setNotOutputData(allNotOutputDatas);

        return ResponseEntity.ok(decisionExtended);
    }

    // update performance indicator rest api
    @PutMapping("/{id}")
    public ResponseEntity<Decision> updateDecision(@PathVariable Long id, @RequestBody Decision decisionDetails) {
        Decision decision = decisionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Decision not exist with id :" + id));

        decision.setName(decisionDetails.getName());
        decision.setDescription(decisionDetails.getDescription());

        Decision updatedDecision = decisionRepository.save(decision);
        return ResponseEntity.ok(updatedDecision);
    }

    // add knowledge source to decision rest api
    @PutMapping("/knowledgeSource/")
    public Decision addKnowledgeSource(@RequestParam Long decisionId, @RequestParam Long knowledgeSourceId) {
        Decision decision = decisionRepository.findById(decisionId)
                .orElseThrow(() -> new ResourceNotFoundException("Decision unit not exist with id :" + decisionId));
        KnowledgeSource knowledgeSource = knowledgeSourceRepository.findById(knowledgeSourceId).orElseThrow(
                () -> new ResourceNotFoundException("Knowledge source not exist with id :" + knowledgeSourceId));

        decision.addKnowledgeSource(knowledgeSource);

        return decisionRepository.save(decision);
    }

    // add owner to decision rest api
    @PutMapping("/owner/")
    public Decision addOwner(@RequestParam Long decisionId, @RequestParam Long ownerId) {
        Decision decision = decisionRepository.findById(decisionId)
                .orElseThrow(() -> new ResourceNotFoundException("Decision unit not exist with id :" + decisionId));
        OrganisationalUnit owner = organisationalUnitRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Organisational unit not exist with id :" + ownerId));

        decision.addOwner(owner);

        return decisionRepository.save(decision);
    }

    // add performance measure to decision rest api
    @PutMapping("/performanceMeasure/")
    public Decision addPerformanceMeasure(@RequestParam Long decisionId, @RequestParam Long decisionMakerId,
            @RequestParam String performanceIndicatorName, @RequestParam double rating) {
        Decision decision = decisionRepository.findById(decisionId)
                .orElseThrow(() -> new ResourceNotFoundException("Decision unit not exist with id :" + decisionId));
        OrganisationalUnit decisionMaker = organisationalUnitRepository.findById(decisionMakerId).orElseThrow(
                () -> new ResourceNotFoundException("Organisational unit not exist with id :" + decisionMakerId));

        List<PerformanceIndicator> performanceIndicators = decision.getPerformanceIndicators();

        PerformanceIndicator foundPerformanceIndicator = null;

        for (PerformanceIndicator performanceIndicator : performanceIndicators) {
            if (performanceIndicator.getName().toLowerCase().equals(performanceIndicatorName.toLowerCase())) {
                foundPerformanceIndicator = performanceIndicator;
            }
        }

        if (foundPerformanceIndicator == null) {
            PerformanceIndicator newPerformanceIndicator = new PerformanceIndicator();
            newPerformanceIndicator.setName(performanceIndicatorName);
            foundPerformanceIndicator = performanceIndicatorRepository.save(newPerformanceIndicator);
            decision.addPerformanceIndicator(foundPerformanceIndicator);
        }

        PerformanceMeasure performanceMeasure = new PerformanceMeasure();
        performanceMeasure.setDecisionMaker(decisionMaker);
        performanceMeasure.setRating(rating);
        performanceMeasure.setDatetime(Instant.now().toString());

        performanceMeasure = performanceMeasureRepository.save(performanceMeasure);

        foundPerformanceIndicator.addPerformanceMeasures(performanceMeasure);

        foundPerformanceIndicator = performanceIndicatorRepository.save(foundPerformanceIndicator);

        return decisionRepository.save(decision);
    }

    // add business knowledge to decision rest api
    @PutMapping("/businessKnowledge/")
    public Decision addBusinessKnowledge(@RequestParam Long decisionId, @RequestParam Long businessKnowledgeId) {
        Decision decision = decisionRepository.findById(decisionId)
                .orElseThrow(() -> new ResourceNotFoundException("Decision unit not exist with id :" + decisionId));
        BusinessKnowledgeModel businessKnowledge = businessKnowledgeModelRepository.findById(businessKnowledgeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Business knowledge not exist with id :" + businessKnowledgeId));

        decision.addBusinessKnowledge(businessKnowledge);

        return decisionRepository.save(decision);
    }

    // add input data to decision rest api
    @PutMapping("/inputData/")
    public Decision addInputData(@RequestParam Long decisionId, @RequestParam Long inputDataId) {
        Decision decision = decisionRepository.findById(decisionId)
                .orElseThrow(() -> new ResourceNotFoundException("Decision unit not exist with id :" + decisionId));
        InputData inputData = inputDataRepository.findById(inputDataId)
                .orElseThrow(() -> new ResourceNotFoundException("Input data not exist with id :" + inputDataId));

        decision.addInputData(inputData);

        return decisionRepository.save(decision);
    }

    // add output data to decision rest api
    @PutMapping("/outputAsInputData/")
    public Decision addOutputAsInputData(@RequestParam Long decisionId, @RequestParam Long outputAsInputDataId) {
        Decision decision = decisionRepository.findById(decisionId)
                .orElseThrow(() -> new ResourceNotFoundException("Decision unit not exist with id :" + decisionId));
        OutputData outputAsInputData = outputDataRepository.findById(outputAsInputDataId)
                .orElseThrow(() -> new ResourceNotFoundException("Output data not exist with id :" + outputAsInputDataId));

        decision.addOutputAsInputData(outputAsInputData);

        return decisionRepository.save(decision);
    }

    // add output data to decision rest api
    @PutMapping("/outputData/")
    public Decision addOutputData(@RequestParam Long decisionId, @RequestParam Long outputDataId) {
        Decision decision = decisionRepository.findById(decisionId)
                .orElseThrow(() -> new ResourceNotFoundException("Decision unit not exist with id :" + decisionId));
        OutputData outputData = outputDataRepository.findById(outputDataId)
                .orElseThrow(() -> new ResourceNotFoundException("Output data not exist with id :" + outputDataId));

        decision.addOutputData(outputData);

        return decisionRepository.save(decision);
    }

    // delete performance indicator rest api
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteDecision(@PathVariable Long id) {
        Decision decision = decisionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Decision not exist with id :" + id));

        decisionRepository.delete(decision);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    // remove owner from decision rest api
    @DeleteMapping("/knowledgeSource/")
    public Decision removeKnowledgeSource(@RequestParam Long decisionId, @RequestParam Long knowledgeSourceId) {
        Decision decision = decisionRepository.findById(decisionId)
                .orElseThrow(() -> new ResourceNotFoundException("Decision unit not exist with id :" + decisionId));
        KnowledgeSource knowledgeSource = knowledgeSourceRepository.findById(knowledgeSourceId).orElseThrow(
                () -> new ResourceNotFoundException("Knowledge source not exist with id :" + knowledgeSourceId));

        decision.removeKnowledgeSource(knowledgeSource);

        return decisionRepository.save(decision);
    }

    // remove knowledge source from decision rest api
    @DeleteMapping("/owner/")
    public Decision removeOwner(@RequestParam Long decisionId, @RequestParam Long ownerId) {
        Decision decision = decisionRepository.findById(decisionId)
                .orElseThrow(() -> new ResourceNotFoundException("Decision unit not exist with id :" + decisionId));
        OrganisationalUnit owner = organisationalUnitRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Organisational unit not exist with id :" + ownerId));

        decision.removeOwner(owner);

        return decisionRepository.save(decision);
    }

    // remove performance measure to decision rest api
    @DeleteMapping("/performanceMeasure/")
    public Decision removePerformanceMeasure(@RequestParam Long decisionId, @RequestParam Long performanceMeasuresId) {
        Decision decision = decisionRepository.findById(decisionId)
                .orElseThrow(() -> new ResourceNotFoundException("Decision unit not exist with id :" + decisionId));

        performanceMeasureRepository.deleteById(performanceMeasuresId);

        decision.updateCombinedRatings();

        return decisionRepository.save(decision);
    }

    // remove performance measure to decision rest api
    @DeleteMapping("/performancIndicator/")
    public Decision removePerformanceIndicator(@RequestParam Long decisionId,
            @RequestParam Long performanceIndicatorId) {
        Decision decision = decisionRepository.findById(decisionId)
                .orElseThrow(() -> new ResourceNotFoundException("Decision unit not exist with id :" + decisionId));
        PerformanceIndicator performanceIndicator = performanceIndicatorRepository.findById(performanceIndicatorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Performance indicator not exist with id :" + performanceIndicatorId));

        List<PerformanceMeasure> measures = performanceIndicator.getPerformanceMeasures();

        for (PerformanceMeasure performanceMeasure : measures) {
            performanceMeasureRepository.delete(performanceMeasure);
        }

        performanceIndicatorRepository.deleteById(performanceIndicatorId);

        return decisionRepository.save(decision);
    }

    // remove business knowledge from decision rest api
    @DeleteMapping("/businessKnowledge/")
    public Decision removeBusinessKnowledge(@RequestParam Long decisionId, @RequestParam Long businessKnowledgeId) {
        Decision decision = decisionRepository.findById(decisionId)
                .orElseThrow(() -> new ResourceNotFoundException("Decision unit not exist with id :" + decisionId));
        BusinessKnowledgeModel businessKnowledge = businessKnowledgeModelRepository.findById(businessKnowledgeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Business knowledge not exist with id :" + businessKnowledgeId));

        decision.removeBusinessKnowledge(businessKnowledge);

        return decisionRepository.save(decision);
    }

    // remove input data from decision rest api
    @DeleteMapping("/inputData/")
    public Decision removeInputData(@RequestParam Long decisionId, @RequestParam Long inputDataId) {
        Decision decision = decisionRepository.findById(decisionId)
                .orElseThrow(() -> new ResourceNotFoundException("Decision unit not exist with id :" + decisionId));
        InputData inputData = inputDataRepository.findById(inputDataId)
                .orElseThrow(() -> new ResourceNotFoundException("Input data not exist with id :" + inputDataId));

        decision.removeInputData(inputData);

        return decisionRepository.save(decision);
    }

    // remove output data from decision rest api
    @DeleteMapping("/outputData/")
    public Decision removeOutputData(@RequestParam Long decisionId, @RequestParam Long outputDataId) {
        Decision decision = decisionRepository.findById(decisionId)
                .orElseThrow(() -> new ResourceNotFoundException("Decision unit not exist with id :" + decisionId));
        OutputData outputData = outputDataRepository.findById(outputDataId)
                .orElseThrow(() -> new ResourceNotFoundException("Output data not exist with id :" + outputDataId));

        decision.removeOutputData(outputData);

        return decisionRepository.save(decision);
    }
    
    // remove output data from decision rest api
    @DeleteMapping("/outputAsInputData/")
    public Decision removeOutputAsInputData(@RequestParam Long decisionId, @RequestParam Long outputAsInputDataId) {
        Decision decision = decisionRepository.findById(decisionId)
                .orElseThrow(() -> new ResourceNotFoundException("Decision unit not exist with id :" + decisionId));
        OutputData outputAsInputData = outputDataRepository.findById(outputAsInputDataId)
                .orElseThrow(() -> new ResourceNotFoundException("Output data not exist with id :" + outputAsInputDataId));

        decision.removeOutputAsInputData(outputAsInputData);

        return decisionRepository.save(decision);
    }
}
