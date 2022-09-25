import React from 'react'

import Figure from 'react-bootstrap/Figure';
import Col from 'react-bootstrap/Col';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import Container from 'react-bootstrap/Container';


function ProfileForm({ loggedUser, isLoggedUserLoading }) {
  if (isLoggedUserLoading) {
    return <h1>Loading...</h1>
  } else {
    return (
      <Container>
        <Form>
          <Row className="mb-3">
            <Form.Group as={Col} controlId="formId">
              <Form.Label>Id</Form.Label>
              <Form.Control type="text" value={loggedUser.id} disabled readOnly />
            </Form.Group>

            <Form.Group as={Col} controlId="formName">
              <Form.Label>Name</Form.Label>
              <Form.Control type="text" value={loggedUser.name} disabled readOnly />
            </Form.Group>

            <Form.Group as={Col} controlId="formEmail">
              <Form.Label>Email</Form.Label>
              <Form.Control type="email" value={loggedUser.email} disabled readOnly />
            </Form.Group>
          </Row>
          <Row className="mb-3">
            <Figure>
              <Figure.Image src={loggedUser.imageUrl} />
              <Figure.Caption>
                {loggedUser.imageUrl}
              </Figure.Caption>
            </Figure>
          </Row>
        </Form>
      </Container>
    )
  }
}

export default ProfileForm