import { httpClient, bearerAuth } from '../http/HttpClient'

export const userApi = {
  getLoggedUser,
  numberOfUsers,
  getUsers,
  deleteUser,
}

function getLoggedUser(user) {
  return httpClient.get('/api/users/me', {
    headers: { 'Authorization': bearerAuth(user) }
  })
}

function numberOfUsers() {
  return httpClient.get('/public/numberOfUsers')
}

function getUsers(user, username) {
  const url = username ? `/api/users?text=${username}` : '/api/users'
  return httpClient.get(url, {
    headers: { 'Authorization': bearerAuth(user) }
  })
}

function deleteUser(user, id) {
  return httpClient.delete(`/api/users/${id}`, {
    headers: { 'Authorization': bearerAuth(user) }
  })
}
