import React, { Component } from 'react'
import AuthContext from '../context/AuthContext'
import { userApi } from '../user/UserApi'
import { handleLogError } from '../http/HttpClient'

import ProfileForm from './ProfileForm'

class ProfilePage extends Component {
  static contextType = AuthContext

  state = {
    loggedUser: {
      id: 0,
      name: '',
      email: '',
      role: '',
      imageUrl: '',
    },
    isLoggedIn: false,
    isLoggedUserLoading: false,
  }

  componentDidMount() {
    const Auth = this.context
    this.setState({ isLoggedIn: Auth.userIsAuthenticated() })

    this.handleGetUserMe()
  }

  handleGetUserMe = () => {
    const Auth = this.context
    const user = Auth.getUser()

    this.setState({ isLoggedUserLoading: true })
    userApi.getLoggedUser(user)
      .then(response => {
        this.setState({ loggedUser: response.data })
      })
      .catch(error => {
        handleLogError(error)
      })
      .finally(() => {
        // setTimeout(() => {
        //   console.log("blah!");
        //   this.setState({ isLoggedUserLoading: false })
        // }, 2000);

        this.setState({ isLoggedUserLoading: false })
      })
  }

  render() {
    const { isLoggedIn, loggedUser, isLoggedUserLoading } = this.state
    if (isLoggedIn) {
      return (
        <ProfileForm
          loggedUser={loggedUser}
          isLoggedUserLoading={isLoggedUserLoading}
        />
      )
    }
  }
}

export default ProfilePage