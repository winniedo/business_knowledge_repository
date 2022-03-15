import React, { Component } from 'react'
import InputDataService from '../../services/InputDataService';

class CreateInputDataComponent extends Component {
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
            InputDataService.getInputDataById(this.state.id).then( (res) =>{
                let inputData = res.data;
                this.setState({name: inputData.name,
                    description: inputData.description
                });
            });
        }        
    }
    saveOrUpdateInputData = (e) => {
        e.preventDefault();
        let inputData = {name: this.state.name, description: this.state.description};
        console.log('inputData => ' + JSON.stringify(inputData));

        // step 5
        if(this.state.id === '_add'){
            InputDataService.createInputData(inputData).then(res =>{
                this.props.history.push('/2');
            });
        }else{
            InputDataService.updateInputData(inputData, this.state.id).then( res => {
                this.props.history.push('/2');
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
        this.props.history.push('/2');
    }

    getTitle(){
        if(this.state.id === '_add'){
            return <h3 className="text-center">Add Input data</h3>
        }else{
            return <h3 className="text-center">Update Input data</h3>
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
                                            <input placeholder="Input data name" name="name" className="form-control" 
                                                value={this.state.name} onChange={this.changeNameHandler}/>
                                        </div>
                                        <div className = "form-group">
                                            <label> Description: </label>
                                            <input placeholder="Input data description" name="description" className="form-control" 
                                                value={this.state.description} onChange={this.changeDescriptionHandler}/>
                                        </div>

                                        <button className="btn btn-success" onClick={this.saveOrUpdateInputData}>Save</button>
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

export default CreateInputDataComponent