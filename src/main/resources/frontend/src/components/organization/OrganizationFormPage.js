import React, { Component } from 'react'

import AuthContext from '../context/AuthContext'
import { withParams } from '../util/Util'
import { organizationApi } from './OrganizationApi'
import { handleLogError } from '../http/HttpClient'
import { Navigate } from 'react-router-dom'

import Col from 'react-bootstrap/Col';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import Container from 'react-bootstrap/Container';
import Button from 'react-bootstrap/Button';

class OrganizationFormPage extends Component {
    static contextType = AuthContext

    state = {
        validated: false,
        setValidated: false,
        isLoggedIn: false,
        isOrganizationLoading: true,
        organization: null,
        navigate: false
    }

    componentDidMount() {
        const Auth = this.context
        this.setState({ isLoggedIn: Auth.userIsAuthenticated() })

        this.handleChange = this.handleChange.bind(this);

        let { organizationId } = this.props.params;

        if (organizationId) {
            const user = Auth.getUser()

            organizationApi
                .getOrganizationById(user, organizationId)
                .then(response => {
                    this.setState({
                        organization: response.data,
                        isOrganizationLoading: false,
                    })
                })
                .catch(error => {
                    handleLogError(error)
                })
                .finally(() => {
                })
        } else {
            let organization = {name: ''}
            this.setState({
                organization: organization,
                isOrganizationLoading: false,
            })
        }
    }

    handleChange(event) {
        const { organization } = this.state

        const name = event.target.name;
        const value = event.target.value;

        organization[name] = value
        this.setState({ organization: organization })
    }

    onSaved(organization) {
        this.setState({ navigate: true })
    }

    save(user, id, organization) {
        if (user != null) {
            const operation = id ?
                organizationApi.updateOrganization(user, id, organization) :
                organizationApi.addOrganization(user, organization)

            operation
                .then(response => {
                    this.onSaved(response.data)
                })
                .catch(error => {
                    handleLogError(error)
                })
                .finally(() => {
                })
        }
    }

    handleSubmit = (event) => {
        const form = event.currentTarget;
        if (form.checkValidity() === false) {
            event.preventDefault();
            event.stopPropagation();
        } else {
            const Auth = this.context
            const user = Auth.getUser()

            const { organization } = this.state

            this.save(user, organization.id, organization)

            event.preventDefault();
            event.stopPropagation();
        }
    };

    render() {
        const {
            validated,
            isOrganizationLoading,
            organization,
            navigate,
            isLoggedIn,
        } = this.state

        if (navigate) {
            return <Navigate to="/organizations" />
        } else if (!isLoggedIn) {
            return <h1>go to login please</h1>
        } else if (isOrganizationLoading) {
            return <h1>Loading...</h1>
        } else {
            return (
                <Container>
                    <Form noValidate validated={validated} onSubmit={this.handleSubmit}>
                        <Row className="mb-3">
                            <Form.Group as={Col} controlId="formName">
                                <Form.Label>Name</Form.Label>
                                <Form.Control
                                    name="name"
                                    type="text"
                                    value={organization?.name}
                                    onChange={this.handleChange}
                                    required
                                />
                            </Form.Group>
                        </Row>
                        <Button variant="primary" type="submit">
                            Save
                        </Button>
                    </Form>
                </Container>
            )
        }
    }

}

export default withParams(OrganizationFormPage)
