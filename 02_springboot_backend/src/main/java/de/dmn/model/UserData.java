package de.dmn.model;

import java.util.List;

public class UserData {
    List<String> businessObjectives;
    List<String> userInputData;
    List<String> userOutputData;
    public List<String> getBusinessObjectives() {
        return businessObjectives;
    }
    public void setBusinessObjectives(List<String> businessObjectives) {
        this.businessObjectives = businessObjectives;
    }
    public List<String> getUserInputData() {
        return userInputData;
    }
    public void setUserInputData(List<String> userInputData) {
        this.userInputData = userInputData;
    }
    public List<String> getUserOutputData() {
        return userOutputData;
    }
    public void setUserOutputData(List<String> userOutputData) {
        this.userOutputData = userOutputData;
    }
}
