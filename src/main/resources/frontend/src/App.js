import React from 'react'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import { AuthProvider } from './components/context/AuthContext'
import Navbar from './components/home/AppNavbar'
import Home from './components/home/Home'
import Login from './components/user/Login'
import OAuth2Redirect from './components/http/OAuth2Redirect'
import OrganizationListPage from './components/organization/OrganizationListPage'
import OrganizationFormPage from './components/organization/OrganizationFormPage'
import ProfilePage from './components/user/ProfilePage'

import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.min.js';
import './App.css';

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Navbar />
        <Routes>
          <Route path='/' exact element={<Home />}  />
          <Route path='/login' element={<Login />}  />
          <Route path='/oauth2/redirect' element={<OAuth2Redirect />}  />

          <Route path='/organizations' element={ <OrganizationListPage /> } />
          <Route path='/organizations/new' element={ <OrganizationFormPage /> } />
          <Route path='/organizations/:organizationId' element={ <OrganizationFormPage /> } />
          <Route path='/profile' element={ <ProfilePage /> } />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  )
}

export default App
