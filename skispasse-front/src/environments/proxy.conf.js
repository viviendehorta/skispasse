const PROXY_CONFIG = [
  {
    context: [
      '/api',
      '/newsFact',
      '/newsCategory',
    ],
    target: "http://localhost:8080",
    secure: false,
    changeOrigin: false
  }
];

module.exports = PROXY_CONFIG;
