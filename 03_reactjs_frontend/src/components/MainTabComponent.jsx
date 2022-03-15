import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
// import "react-tabs/style/react-tabs.css";
import React, { Component } from 'react'
import 'react-tabs/style/react-tabs.css';
import ListDecisionsComponent from './decision/ListDecisionsComponent';
import ListInputDataComponent from './inputData/ListInputDataComponent';
import ListOutputDataComponent from './outputData/ListOutputDataComponent';
import ListBusinessKnowledgeComponent from './businessKnowledge/ListBusinessKnowledgeComponent';
import ListKnowledgeSourceComponent from './knowledgeSource/ListKnowledgeSourceComponent';
import ListOrganisationalUnitComponent from './organisationalUnit/ListOrganisationalUnitComponent';
import Graph from './graph/Graph';
import Test from './graph/Test';

class MainTabComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {
        }
    }

    render() {
        return (
            <div className="App">
                <Tabs defaultIndex={parseInt(this.props.match.params.id)} onSelect={index => console.log(index)}>
                    <TabList>
                        <Tab>eDRD (Business knowledge)</Tab>
                        <Tab>Decisions</Tab>
                        <Tab>Input data</Tab>
                        <Tab>Output data</Tab>
                        <Tab>Business knowledge</Tab>
                        <Tab>Knowledge sources</Tab>
                        <Tab>Organisational units</Tab>
                    </TabList>
                    <TabPanel>
                        <Graph />
                    </TabPanel>
                    <TabPanel>
                        <ListDecisionsComponent />
                    </TabPanel>
                    <TabPanel>
                        <ListInputDataComponent />
                    </TabPanel>
                    <TabPanel>
                        <ListOutputDataComponent />
                    </TabPanel>
                    <TabPanel>
                        <ListBusinessKnowledgeComponent />
                    </TabPanel>
                    <TabPanel>
                        <ListKnowledgeSourceComponent />
                    </TabPanel>
                    <TabPanel>
                        <ListOrganisationalUnitComponent />
                    </TabPanel>
                </Tabs>
            </div>
        );
    }
}

export default MainTabComponent