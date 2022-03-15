import React, { Component } from 'react'
import KnowledgeSourceService from '../../services/KnowledgeSourceService'

class ViewKnowledgeSourceComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {
            id: this.props.match.params.id,
            knowledgeSource: {}
        }
    }

    componentDidMount(){
        KnowledgeSourceService.getKnowledgeSourceById(this.state.id).then( res => {
            this.setState({knowledgeSource: res.data});
        })
    }

    render() {
        return (
            <div>
                <br></br>
                <div className = "card col-md-6 offset-md-3">
                    <h3 className = "text-center"> View KnowledgeSource Details</h3>
                    <div className = "card-body">
                        <div className = "row">
                            <label> Name: </label>
                            <div> <a href={ this.state.knowledgeSource.url }>{ this.state.knowledgeSource.name }</a></div>
                        </div>
                        <div className = "row">
                            <label> Description: </label>
                            <div> { this.state.knowledgeSource.description }</div>
                        </div>
                    </div>

                </div>
            </div>
        )
    }
}

export default ViewKnowledgeSourceComponent