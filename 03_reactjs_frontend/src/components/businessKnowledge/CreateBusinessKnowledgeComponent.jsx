import React, { Component } from 'react'
import BusinessKnowledgeService from '../../services/BusinessKnowledgeService';

class CreateBusinessKnowledgeComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {
            // step 2
            id: this.props.match.params.id,
            name: '',
            description: '',
            url: ''
        }
        this.changeNameHandler = this.changeNameHandler.bind(this);
        this.changeDescriptionHandler = this.changeDescriptionHandler.bind(this);
        this.changeUrlHandler = this.changeUrlHandler.bind(this);
    }

    // step 3
    componentDidMount(){

        // step 4
        if(this.state.id === '_add'){
            return
        }else{
            BusinessKnowledgeService.getBusinessKnowledgeById(this.state.id).then( (res) =>{
                let businessKnowledge = res.data;
                this.setState({name: businessKnowledge.name,
                    description: businessKnowledge.description, url: businessKnowledge.url
                });
            });
        }        
    }
    saveOrUpdateBusinessKnowledge = (e) => {
        e.preventDefault();
        let businessKnowledge = {name: this.state.name, description: this.state.description, url: this.state.url};
        console.log('businessKnowledge => ' + JSON.stringify(businessKnowledge));

        // step 5
        if(this.state.id === '_add'){
            BusinessKnowledgeService.createBusinessKnowledge(businessKnowledge).then(res =>{
                this.props.history.push('/4');
            });
        }else{
            BusinessKnowledgeService.updateBusinessKnowledge(businessKnowledge, this.state.id).then( res => {
                this.props.history.push('/4');
            });
        }
    }
    
    changeNameHandler= (event) => {
        this.setState({name: event.target.value});
    }

    changeDescriptionHandler= (event) => {
        this.setState({description: event.target.value});
    }

    changeUrlHandler= (event) => {
        this.setState({url: event.target.value});
    }

    cancel(){
        this.props.history.push('/4');
    }

    getTitle(){
        if(this.state.id === '_add'){
            return <h3 className="text-center">Add Business knowledge</h3>
        }else{
            return <h3 className="text-center">Update Business knowledge</h3>
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
                                            <input placeholder="Business knowledge name" name="name" className="form-control" 
                                                value={this.state.name} onChange={this.changeNameHandler}/>
                                        </div>
                                        <div className = "form-group">
                                            <label> Description: </label>
                                            <input placeholder="Business knowledge description" name="description" className="form-control" 
                                                value={this.state.description} onChange={this.changeDescriptionHandler}/>
                                        </div>
                                        <div className = "form-group">
                                            <label> URL: </label>
                                            <input placeholder="Business knowledge URL" name="url" className="form-control" 
                                                value={this.state.url} onChange={this.changeUrlHandler}/>
                                        </div>

                                        <button className="btn btn-success" onClick={this.saveOrUpdateBusinessKnowledge}>Save</button>
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

export default CreateBusinessKnowledgeComponent