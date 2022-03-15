import React, { Component } from 'react'
import { withRouter } from 'react-router-dom';
import KnowledgeSourceService from '../../services/KnowledgeSourceService'

class ListKnowledgeSourceComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {
            knowledgeSource: []
        }
        this.addKnowledgeSource = this.addKnowledgeSource.bind(this);
        this.editKnowledgeSource = this.editKnowledgeSource.bind(this);
        this.deleteKnowledgeSource = this.deleteKnowledgeSource.bind(this);
    }

    addKnowledgeSource() {
        this.props.history.push('/add-knowledgeSource/_add');
    }    
    editKnowledgeSource(id) {
        this.props.history.push(`/add-knowledgeSource/${id}`);
    }
    deleteKnowledgeSource(id) {
        KnowledgeSourceService.deleteKnowledgeSource(id).then(res => {
            this.setState({ knowledgeSource: this.state.knowledgeSource.filter(knowledgeSource => knowledgeSource.id !== id) });
        });
    }
    viewKnowledgeSource(id) {
        this.props.history.push(`/view-knowledgeSource/${id}`);
    }
    componentDidMount() {
        KnowledgeSourceService.getKnowledgeSource().then((res) => {
            console.log(res.data)
            this.setState({ knowledgeSource: res.data });
        });
    }

    render() {
        return (
            <div>
                <div className="row">
                    <button className="btn btn-primary" onClick={this.addKnowledgeSource}> Add KnowledgeSource</button>
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
                                this.state.knowledgeSource.map(
                                    knowledgeSource =>
                                        <tr key={knowledgeSource.id}>
                                            <td width="20%"> <a href={knowledgeSource.url}>{knowledgeSource.name}</a></td>
                                            <td width="50%"> {knowledgeSource.description}</td>
                                            <td width="30%">
                                                <button onClick={() => this.editKnowledgeSource(knowledgeSource.id)} className="btn btn-info">Update </button>
                                                <button style={{ marginLeft: "10px" }} onClick={() => this.deleteKnowledgeSource(knowledgeSource.id)} className="btn btn-danger">Delete </button>
                                                <button style={{ marginLeft: "10px" }} onClick={() => this.viewKnowledgeSource(knowledgeSource.id)} className="btn btn-info">View </button>
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

export default withRouter(ListKnowledgeSourceComponent)