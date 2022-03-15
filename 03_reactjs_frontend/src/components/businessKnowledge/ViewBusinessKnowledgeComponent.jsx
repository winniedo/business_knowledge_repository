import React, { Component } from 'react'
import BusinessKnowledgeService from '../../services/BusinessKnowledgeService'

class ViewBusinessKnowledgeComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {
            id: this.props.match.params.id,
            businessKnowledge: {}
        }
    }

    componentDidMount(){
        BusinessKnowledgeService.getBusinessKnowledgeById(this.state.id).then( res => {
            this.setState({businessKnowledge: res.data});
        })
    }

    render() {
        return (
            <div>
                <br></br>
                <div className = "card col-md-6 offset-md-3">
                    <h3 className = "text-center"> View BusinessKnowledge Details</h3>
                    <div className = "card-body">
                        <div className = "row">
                            <label> Name: </label>
                            <div> <a href={ this.state.businessKnowledge.url }>{ this.state.businessKnowledge.name }</a></div>
                        </div>
                        <div className = "row">
                            <label> Description: </label>
                            <div> { this.state.businessKnowledge.description }</div>
                        </div>
                    </div>

                </div>
            </div>
        )
    }
}

export default ViewBusinessKnowledgeComponent