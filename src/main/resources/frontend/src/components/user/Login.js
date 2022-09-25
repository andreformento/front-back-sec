import React, { Component } from 'react';
import { Navigate } from 'react-router-dom';
import AuthContext from '../context/AuthContext';
import { config } from '../../Constants';

import { GoMarkGithub } from 'react-icons/go';
import { FcGoogle } from 'react-icons/fc';

import Button from 'react-bootstrap/Button';
import ButtonGroup from 'react-bootstrap/ButtonGroup';
import Container from 'react-bootstrap/Container';

class Login extends Component {
  static contextType = AuthContext

  state = {
    isLoggedIn: false
  }

  componentDidMount() {
    const Auth = this.context
    const isLoggedIn = Auth.userIsAuthenticated()
    this.setState({ isLoggedIn })
  }

  handleInputChange = (e, { name, value }) => {
    this.setState({ [name]: value })
  }

  getReferer = () => {
    const locationState = this.props.location ? this.props.location.state : null
    return locationState && locationState.referer ? locationState.referer : '/'
  }

  getSocialLoginUrl(name) {
    return `${config.url.REACT_APP_API_BASE_URL}/oauth2/authorization/${name}?redirect_uri=${config.url.REACT_APP_OAUTH2_REDIRECT_URI}`
  }

  render() {
    const { isLoggedIn } = this.state

    const referer = this.getReferer()
    if (isLoggedIn) {
      return <Navigate to={referer} />
    } else {
      return (
        <div>
          <Container>
            <div className="text-center">
              <ButtonGroup aria-label="Login options" size="lg" vertical>
                <Button variant="light" href={this.getSocialLoginUrl('google')}>
                  Login with Google <FcGoogle size={70} />
                </Button>
                <Button variant="light" href={this.getSocialLoginUrl('github')}>
                  Login with Github <GoMarkGithub size={70} />
                </Button>
              </ButtonGroup>
            </div>
          </Container>
        </div>
      )
    }
  }
}

export default Login