export default {
  projectId: 'wdj3ha',
  allowCypressEnv: false,
  chromeWebSecurity: false,

  e2e: {
    baseUrl: 'http://localhost:3000',
    setupNodeEvents(on, config) {
      // implement node event listeners here
    },
  },
};
