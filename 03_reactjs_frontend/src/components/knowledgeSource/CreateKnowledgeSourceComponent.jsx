import React, { Component } from 'react'
import KnowledgeSourceService from '../../services/KnowledgeSourceService';

class CreateKnowledgeSourceComponent extends Component {
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
            KnowledgeSourceService.getKnowledgeSourceById(this.state.id).then( (res) =>{
                let knowledgeSource = res.data;
                this.setState({name: knowledgeSource.name,
                    description: knowledgeSource.description, url: knowledgeSource.url
                });
            });
        }        
    }
    saveOrUpdateKnowledgeSource = (e) => {
        e.preventDefault();
        let knowledgeSource = {name: this.state.name, description: this.state.description, url: this.state.url};
        console.log('knowledgeSource => ' + JSON.stringify(knowledgeSource));

        // step 5
        if(this.state.id === '_add'){
            KnowledgeSourceService.createKnowledgeSource(knowledgeSource).then(res =>{
                this.props.history.push('/5');
            });
        }else{
            KnowledgeSourceService.updateKnowledgeSource(knowledgeSource, this.state.id).then( res => {
                this.props.history.push('/5');
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
        this.props.history.push('/5');
    }

    getTitle(){
        if(this.state.id === '_add'){
            return <h3 className="text-center">Add Knowledge source</h3>
        }else{
            return <h3 className="text-center">Update Knowledge source</h3>
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
                                            <input placeholder="Knowledge source name" name="name" className="form-control" 
                                                value={this.state.name} onChange={this.changeNameHandler}/>
                                        </div>
                                        <div className = "form-group">
                                            <label> Description: </label>
                                            <input placeholder="Knowledge source description" name="description" className="form-control" 
                                                value={this.state.description} onChange={this.changeDescriptionHandler}/>
                                        </div>
                                        <div className = "form-group">
                                            <label> URL: </label>
                                            <input placeholder="Knowledge source URL" name="url" className="form-control" 
                                                value={this.state.url} onChange={this.changeUrlHandler}/>
                                        </div>

                                        <button className="btn btn-success" onClick={this.saveOrUpdateKnowledgeSource}>Save</button>
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

export default CreateKnowledgeSourceComponent