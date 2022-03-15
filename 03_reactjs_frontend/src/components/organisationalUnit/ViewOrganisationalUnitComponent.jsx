import React, { Component } from 'react'
import OrganisationalUnitService from '../../services/OrganisationalUnitService'

class ViewOrganisationalUnitComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {
            id: this.props.match.params.id,
            organisationalUnit: {}
        }
    }

    componentDidMount(){
        OrganisationalUnitService.getOrganisationalUnitById(this.state.id).then( res => {
            this.setState({organisationalUnit: res.data});
        })
    }

    render() {
        return (
            <div>
                <br></br>
                <div className = "card col-md-6 offset-md-3">
                    <h3 className = "text-center"> View OrganisationalUnit Details</h3>
                    <div className = "card-body">
                        <div className = "row">
                            <label> Name: </label>
                            <div> <a href={ this.state.organisationalUnit.url }>{ this.state.organisationalUnit.name }</a></div>
                        </div>
                        <div className = "row">
                            <label> Description: </label>
                            <div> { this.state.organisationalUnit.description }</div>
                        </div>
                    </div>

                </div>
            </div>
        )
    }
}

export default ViewOrganisationalUnitComponent