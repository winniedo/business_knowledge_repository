import React, { Component } from 'react'
import OutputDataService from '../../services/OutputDataService';

class CreateOutputDataComponent extends Component {
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
            OutputDataService.getOutputDataById(this.state.id).then( (res) =>{
                let outputData = res.data;
                console.log(outputData.name)
                this.setState({name: outputData.name,
                    description: outputData.description
                });
            });
        }        
    }
    saveOrUpdateOutputData = (e) => {
        e.preventDefault();
        let outputData = {name: this.state.name, description: this.state.description};
        console.log('outputData => ' + JSON.stringify(outputData));

        // step 5
        if(this.state.id === '_add'){
            OutputDataService.createOutputData(outputData).then(res =>{
                this.props.history.push('/3');
            });
        }else{
            OutputDataService.updateOutputData(outputData, this.state.id).then( res => {
                this.props.history.push('/3');
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
        this.props.history.push('/3');
    }

    getTitle(){
        if(this.state.id === '_add'){
            return <h3 className="text-center">Add Output data</h3>
        }else{
            return <h3 className="text-center">Update Output data</h3>
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
                                            <input placeholder="Output data name" name="name" className="form-control" 
                                                value={this.state.name} onChange={this.changeNameHandler}/>
                                        </div>
                                        <div className = "form-group">
                                            <label> Description: </label>
                                            <input placeholder="Output data description" name="description" className="form-control" 
                                                value={this.state.description} onChange={this.changeDescriptionHandler}/>
                                        </div>

                                        <button className="btn btn-success" onClick={this.saveOrUpdateOutputData}>Save</button>
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

export default CreateOutputDataComponent