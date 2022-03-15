import React, { Component } from 'react'
import OutputDataService from '../../services/OutputDataService'

class ViewOutputDataComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {
            id: this.props.match.params.id,
            outputData: {}
        }
    }

    componentDidMount(){
        OutputDataService.getOutputDataById(this.state.id).then( res => {
            this.setState({outputData: res.data});
        })
    }

    render() {
        return (
            <div>
                <br></br>
                <div className = "card col-md-6 offset-md-3">
                    <h3 className = "text-center"> View OutputData Details</h3>
                    <div className = "card-body">
                        <div className = "row">
                            <label> Name: </label>
                            <div> { this.state.outputData.name }</div>
                        </div>
                        <div className = "row">
                            <label> Description: </label>
                            <div> { this.state.outputData.description }</div>
                        </div>
                    </div>

                </div>
            </div>
        )
    }
}

export default ViewOutputDataComponent