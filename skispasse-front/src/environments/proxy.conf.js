const PROXY_CONFIG = [
  {
    context: [
      '/account',
      '/newsFacts',
      '/newsCategories',
      '/users',
      '/maps'
    ],
    target: "http://localhost:8080",
    secure: false,
    changeOrigin: false
  }
];

module.exports = PROXY_CONFIG;
