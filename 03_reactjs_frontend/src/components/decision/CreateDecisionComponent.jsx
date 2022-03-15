import React, { Component } from 'react'
import DecisionService from '../../services/DecisionService';

class CreateDecisionComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {
            // step 2
            id: this.props.match.params.id,
            name: '',
            description: ''
        }
        this.changeNameHandler = this.changeNameHandler.bind(this);
        this.changeDescriptionHandler = this.changeDescriptionHandler.bind(this);
    }

    // step 3
    componentDidMount(){

        // step 4
        if(this.state.id === '_add'){
            return
        }else{
            DecisionService.getDecisionById(this.state.id).then( (res) =>{
                let decision = res.data;
                this.setState({name: decision.name,
                    description: decision.description
                });
            });
        }        
    }
    saveOrUpdateDecision = (e) => {
        e.preventDefault();
        let decision = {name: this.state.name, description: this.state.description};
        console.log('decision => ' + JSON.stringify(decision));

        // step 5
        if(this.state.id === '_add'){
            DecisionService.createDecision(decision).then(res =>{
                this.props.history.push('/1');
            });
        }else{
            DecisionService.updateDecision(decision, this.state.id).then( res => {
                this.props.history.push('/view-decision/' + this.state.id);
            });
        }
    }
    
    changeNameHandler= (event) => {
        this.setState({name: event.target.value});
    }

    changeDescriptionHandler= (event) => {
        this.setState({description: event.target.value});
    }

    cancel(){
        if(this.state.id === '_add'){
            this.props.history.push('/1');
        }else{
            this.props.history.push('/view-decision/' + this.state.id);
        }
    }

    getTitle(){
        if(this.state.id === '_add'){
            return <h3 className="text-center">Add Decision</h3>
        }else{
            return <h3 className="text-center">Update Decision</h3>
        }
    }
    render() {
        return (
            <div>
                <br></br>
                   <div className = "container">
                        <div className = "row">
                            <div className = "card col-md-6 offset-md-3 offset-md-3">
                                {
                                    this.getTitle()
                                }
                                <div className = "card-body">
                                    <form>
                                        <div className = "form-group">
                                            <label> Name: </label>
                                            <input placeholder="Decision name" name="name" className="form-control" 
                                                value={this.state.name} onChange={this.changeNameHandler}/>
                                        </div>
                                        <div className = "form-group">
                                            <label> Description: </label>
                                            <input placeholder="Decision description" name="description" className="form-control" 
                                                value={this.state.description} onChange={this.changeDescriptionHandler}/>
                                        </div>

                                        <button className="btn btn-success" onClick={this.saveOrUpdateDecision}>Save</button>
                                        <button className="btn btn-danger" onClick={this.cancel.bind(this)} style={{marginLeft: "10px"}}>Cancel</button>
                                    </form>
                                </div>
                            </div>
                        </div>

                   </div>
            </div>
        )
    }
}

export default CreateDecisionComponent