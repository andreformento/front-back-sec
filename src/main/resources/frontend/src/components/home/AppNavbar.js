import React from 'react'
import { useAuth } from '../context/AuthContext'

import { Button, Navbar, Nav, Container } from 'react-bootstrap'
import { LinkContainer } from 'react-router-bootstrap'
import { Link } from 'react-router-dom'

function AppNavbar() {
  const { getUser, userIsAuthenticated, userLogout } = useAuth()

  const logout = () => {
    userLogout()
    this.props.history.push('/')
  }

  const anonymousMenuStyle = () => {
    return userIsAuthenticated() ? { "display": "none" } : { "display": "block" }
  }

  const authenticatedMenuStyle = () => {
    return userIsAuthenticated() ? { "display": "block" } : { "display": "none" }
  }

  const getUserName = () => {
    const user = getUser()
    return user ? user.data.name : ''
  }

  return (
    <Navbar collapseOnSelect expand="lg" bg="light" variant="light">
      <Container>
        <Navbar.Brand href="/">App</Navbar.Brand>
        <Navbar.Toggle aria-controls="responsive-navbar-nav" />
        <Navbar.Collapse id="responsive-navbar-nav">
          <Nav className="me-auto">
            <LinkContainer to="/"><Nav.Link>Home</Nav.Link></LinkContainer>
            <LinkContainer style={authenticatedMenuStyle()} to="/organizations"><Nav.Link>Organizations</Nav.Link></LinkContainer>
          </Nav>

          <Nav>
            <LinkContainer style={authenticatedMenuStyle()} to="/profile"><Nav.Link>{`Hi ${getUserName()}`}</Nav.Link></LinkContainer>
            <Button style={authenticatedMenuStyle()} onClick={logout} variant="secondary" as={Link} to="/">Logout</Button>

            <Button style={anonymousMenuStyle()} variant="primary" as={Link} to="/login">Login or signup</Button>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  )
}

export default AppNavbar
