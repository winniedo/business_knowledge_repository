import React, { Component } from 'react'
import { withRouter } from 'react-router-dom';
import BusinessKnowledgeService from '../../services/BusinessKnowledgeService'

class ListBusinessKnowledgeComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {
            businessKnowledge: []
        }
        this.addBusinessKnowledge = this.addBusinessKnowledge.bind(this);
        this.editBusinessKnowledge = this.editBusinessKnowledge.bind(this);
        this.deleteBusinessKnowledge = this.deleteBusinessKnowledge.bind(this);
    }

    addBusinessKnowledge() {
        this.props.history.push('/add-businessKnowledge/_add');
    }    
    editBusinessKnowledge(id) {
        this.props.history.push(`/add-businessKnowledge/${id}`);
    }
    deleteBusinessKnowledge(id) {
        BusinessKnowledgeService.deleteBusinessKnowledge(id).then(res => {
            this.setState({ businessKnowledge: this.state.businessKnowledge.filter(businessKnowledge => businessKnowledge.id !== id) });
        });
    }
    viewBusinessKnowledge(id) {
        this.props.history.push(`/view-businessKnowledge/${id}`);
    }
    componentDidMount() {
        BusinessKnowledgeService.getBusinessKnowledge().then((res) => {
            console.log(res.data)
            this.setState({ businessKnowledge: res.data });
        });
    }

    render() {
        return (
            <div>
                <div className="row">
                    <button className="btn btn-primary" onClick={this.addBusinessKnowledge}> Add BusinessKnowledge</button>
                </div>
                <br></br>
                <div className="row">
                    <table className="table table-striped table-bordered">

                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Description</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                this.state.businessKnowledge.map(
                                    businessKnowledge =>
                                        <tr key={businessKnowledge.id}>
                                            <td width="20%"> <a href={businessKnowledge.url}>{businessKnowledge.name}</a></td>
                                            <td width="50%"> {businessKnowledge.description}</td>
                                            <td width="30%">
                                                <button onClick={() => this.editBusinessKnowledge(businessKnowledge.id)} className="btn btn-info">Update </button>
                                                <button style={{ marginLeft: "10px" }} onClick={() => this.deleteBusinessKnowledge(businessKnowledge.id)} className="btn btn-danger">Delete </button>
                                                <button style={{ marginLeft: "10px" }} onClick={() => this.viewBusinessKnowledge(businessKnowledge.id)} className="btn btn-info">View </button>
                                            </td>
                                        </tr>
                                )
                            }
                        </tbody>
                    </table>

                </div>

            </div>
        )
    }
}

export default withRouter(ListBusinessKnowledgeComponent)