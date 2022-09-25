import React, { Component } from 'react'

import AuthContext from '../context/AuthContext'
import { organizationApi } from './OrganizationApi'
import OrganizationList from './OrganizationList'
import { handleLogError } from '../http/HttpClient'


class OrganizationListPage extends Component {
    static contextType = AuthContext

    state = {
        isLoggedIn: false,
        isLoading: false,
        organanizations: [],
    }

    componentDidMount() {
        const Auth = this.context
        this.setState({ isLoggedIn: Auth.userIsAuthenticated() })

        this.handleGetUserMe()
    }

    handleGetUserMe = () => {
        const Auth = this.context
        const user = Auth.getUser()

        this.setState({ isLoading: true })

        if (user != null) {
            organizationApi.getOrganizations(user)
                .then(response => {
                    this.setState({ organanizations: response.data })
                })
                .catch(error => {
                    handleLogError(error)
                })
                .finally(() => {
                    this.setState({ isLoading: false })
                })
        }
    }

    handleDeleteOrganization = (organizationId) => {
        const Auth = this.context
        const user = Auth.getUser()

        let deleteFilter = (org => org.id !== organizationId)

        const { organanizations } = this.state

        organizationApi.deleteOrganization(user, organizationId)
            .then(response => {
                this.setState({ organanizations: organanizations.filter(deleteFilter) })
            })
            .catch(error => {
                handleLogError(error)
            })
            .finally(() => {
                this.setState({ isLoading: false })
            })
    }

    render() {
        const { isLoggedIn, isLoading, organanizations } = this.state
        if (isLoggedIn) {
            if (isLoading) {
                return (
                    <h1>loading...</h1>
                )
            } else {
                return <OrganizationList
                    organanizations={organanizations}
                    handleDeleteOrganization={this.handleDeleteOrganization}
                />
            }
        } else {
            return (
                <h1>not logged in</h1>
            )
        }
    }

}

export default OrganizationListPage