module.exports = {
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8084',
        changeOrigin: true,
        pathRewrite: {
          '^/api': '/api'
        }
      }
    }
  }
}