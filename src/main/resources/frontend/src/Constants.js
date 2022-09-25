export const config = {
  url: {
    REACT_APP_API_BASE_URL: process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080',
    REACT_APP_OAUTH2_REDIRECT_URI: process.env.REACT_APP_OAUTH2_REDIRECT_URI || 'http://localhost:8080/oauth2/redirect'
  }
}
