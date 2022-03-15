package de.dmn.model;

import java.util.List;

public class ReactOutPutAndGraph {
    ReactGraph userData;
    List<ReactGraph> reactGraphs;

    public ReactOutPutAndGraph() {
    }

    public ReactGraph getUserData() {
        return userData;
    }

    public void setUserData(ReactGraph userData) {
        this.userData = userData;
    }

    public List<ReactGraph> getReactGraphs() {
        return reactGraphs;
    }

    public void setReactGraphs(List<ReactGraph> reactGraphs) {
        this.reactGraphs = reactGraphs;
    }

}
