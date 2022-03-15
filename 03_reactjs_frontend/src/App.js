import React from 'react';
import logo from './logo.svg';
import './App.css';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom'
import HeaderComponent from './components/HeaderComponent';
import FooterComponent from './components/FooterComponent';
import MainTabComponent from './components/MainTabComponent';

import CreateInputDataComponent from './components/inputData/CreateInputDataComponent';
import ViewInputDataComponent from './components/inputData/ViewInputDataComponent';
import CreateOutputDataComponent from './components/outputData/CreateOutputDataComponent';
import ViewOutputDataComponent from './components/outputData/ViewOutputDataComponent';
import CreateBusinessKnowledgeComponent from './components/businessKnowledge/CreateBusinessKnowledgeComponent';
import ViewBusinessKnowledgeComponent from './components/businessKnowledge/ViewBusinessKnowledgeComponent';
import CreateKnowledgeSourceComponent from './components/knowledgeSource/CreateKnowledgeSourceComponent';
import ViewKnowledgeSourceComponent from './components/knowledgeSource/ViewKnowledgeSourceComponent';
import CreateOrganisationalUnitComponent from './components/organisationalUnit/CreateOrganisationalUnitComponent';
import ViewOrganisationalUnitComponent from './components/organisationalUnit/ViewOrganisationalUnitComponent';
import CreateDecisionComponent from './components/decision/CreateDecisionComponent';
import ViewDecisionComponent from './components/decision/ViewDecisionComponent';


function App() {
  return (
    <div>
      <Router>
        <HeaderComponent />
        <div>
          <Switch>
            <Route path="/" exact component={MainTabComponent}></Route>
            <Route path="/:id" exact component={MainTabComponent}></Route>
            <Route path="/add-inputData/:id" component={CreateInputDataComponent}></Route>
            <Route path="/view-inputData/:id" component={ViewInputDataComponent}></Route>
            <Route path="/add-outputData/:id" component={CreateOutputDataComponent}></Route>
            <Route path="/view-outputData/:id" component={ViewOutputDataComponent}></Route>
            <Route path="/add-businessKnowledge/:id" component={CreateBusinessKnowledgeComponent}></Route>
            <Route path="/view-businessKnowledge/:id" component={ViewBusinessKnowledgeComponent}></Route>
            <Route path="/add-knowledgeSource/:id" component={CreateKnowledgeSourceComponent}></Route>
            <Route path="/view-knowledgeSource/:id" component={ViewKnowledgeSourceComponent}></Route>
            <Route path="/add-organisationalUnit/:id" component={CreateOrganisationalUnitComponent}></Route>
            <Route path="/view-organisationalUnit/:id" component={ViewOrganisationalUnitComponent}></Route>            
            <Route path="/add-decision/:id" component={CreateDecisionComponent}></Route>
            <Route path="/view-decision/:id" component={ViewDecisionComponent}></Route>
            {/*<Route path = "/employees" component = {ListEmployeeComponent}></Route>
                          <Route path = "/add-employee/:id" component = {CreateEmployeeComponent}></Route>
                           */}
            {/* <Route path = "/update-employee/:id" component = {UpdateEmployeeComponent}></Route> */}
          </Switch>
        </div>
        <FooterComponent />
      </Router>
    </div>

  );
}

export default App;