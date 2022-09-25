import React from 'react';

import Table from 'react-bootstrap/Table';
import Button from 'react-bootstrap/Button';
import ButtonGroup from 'react-bootstrap/ButtonGroup';
import Container from 'react-bootstrap/Container';
import { BsTrash, BsPlusLg, BsPencil } from 'react-icons/bs';

import { Link } from "react-router-dom";

function OrganizationList({ organanizations, handleDeleteOrganization }) {
    let organizationMenu = (
        <div>
            <Link to="/organizations/new">
                <BsPlusLg /> New organization
            </Link>
        </div>
    )

    if (organanizations.length === 0) {
        return (
            <Container>
                {organizationMenu}
                <h1>no organizations registered</h1>
            </Container>
        )
    } else {
        let organanizationItems = organanizations.map(organization => {
            return (
                <tr key={organization.id}>
                    <td>{organization.name}</td>
                    <td>
                        <ButtonGroup aria-label="Login options" size="lg">
                            <Button variant="light" href={`/organizations/${organization.id}`}>
                                <BsPencil />
                            </Button>
                            <Button variant="danger" onClick={() => handleDeleteOrganization(organization.id)}>
                                <BsTrash />
                            </Button>
                        </ButtonGroup>
                    </td>
                </tr>
            )
        })

        return (
            <Container>
                {organizationMenu}

                <Table responsive="sm" striped bordered hover>
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        {organanizationItems}
                    </tbody>
                </Table>
            </Container>
        )
    }
}

export default OrganizationList
