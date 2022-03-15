import React, { Component } from 'react'
import InputDataService from '../../services/InputDataService'

class ViewInputDataComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {
            id: this.props.match.params.id,
            inputData: {}
        }
    }

    componentDidMount(){
        InputDataService.getInputDataById(this.state.id).then( res => {
            this.setState({inputData: res.data});
        })
    }

    render() {
        return (
            <div>
                <br></br>
                <div className = "card col-md-6 offset-md-3">
                    <h3 className = "text-center"> View InputData Details</h3>
                    <div className = "card-body">
                        <div className = "row">
                            <label> Name: </label>
                            <div> { this.state.inputData.name }</div>
                        </div>
                        <div className = "row">
                            <label> Description: </label>
                            <div> { this.state.inputData.description }</div>
                        </div>
                    </div>

                </div>
            </div>
        )
    }
}

export default ViewInputDataComponent