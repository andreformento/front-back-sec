import { httpClient, bearerAuth } from '../http/HttpClient'

export const organizationApi = {
  getOrganizations,
  getOrganizationById,
  addOrganization,
  updateOrganization,
  deleteOrganization,
}

function getOrganizations(user) {
  return httpClient.get('/api/organizations', {
    headers: { 'Authorization': bearerAuth(user) }
  })
}

function getOrganizationById(user, id) {
  return httpClient.get(`/api/organizations/${id}`, {
    headers: { 'Authorization': bearerAuth(user) }
  })
}

function addOrganization(user, organization) {
  return httpClient.post('/api/organizations', organization, {
    headers: {
      'Content-type': 'application/json',
      'Authorization': bearerAuth(user)
    }
  })
}

function updateOrganization(user, id, organization) {
  return httpClient.put(`/api/organizations/${id}`, organization, {
    headers: {
      'Content-type': 'application/json',
      'Authorization': bearerAuth(user)
    }
  })
}

function deleteOrganization(user, organizationId) {
  return httpClient.delete(`/api/organizations/${organizationId}`, {
    headers: { 'Authorization': bearerAuth(user) }
  })
}
