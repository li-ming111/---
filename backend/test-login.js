const https = require('https');
const http = require('http');

const options = {
  hostname: 'localhost',
  port: 8082,
  path: '/api/auth/login',
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Content-Length': Buffer.byteLength(JSON.stringify({
