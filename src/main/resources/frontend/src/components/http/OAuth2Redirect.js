import React, { Component } from 'react'
import { Navigate } from 'react-router-dom'
import AuthContext from '../context/AuthContext'
import { parseJwt } from './HttpClient'

class OAuth2Redirect extends Component {
  static contextType = AuthContext

  state = {
    redirectTo: '/login'
  }

  componentDidMount() {
    const accessToken = this.extractUrlParameter('token')
    // const error = this.extractUrlParameter('error')
    if (accessToken) {
      this.handleLogin(accessToken)
      const redirect = "/"
      this.setState({ redirect })
    }
  }

  extractUrlParameter = (key) => {
    return new URLSearchParams(window.location.search).get(key)
  }

  handleLogin = (accessToken) => {
    const data = parseJwt(accessToken)
    const user = { data, accessToken }

    const Auth = this.context
    Auth.userLogin(user)
  }

  render() {
    return <Navigate to={this.state.redirectTo} />
  }
}

export default OAuth2Redirect