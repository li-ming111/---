const express = require('express');
const cors = require('cors');

const app = express();
const port = 8082;

// 启用 CORS
app.use(cors());

// 模拟学校列表数据
const schools = [
  { id: 1, name: '哈尔滨信息工程学院', code: 'HIIE' },
  { id: 2, name: '北京大学', code: 'PKU' },
  { id: 3, name: '清华大学', code: 'THU' },
  { id: 4, name: '哈尔滨工业大学', code: 'HIT' },
  { id: 5, name: '哈尔滨工程大学', code: 'HEU' }
];

// 模拟登录接口
app.post('/api/auth/login', (req, res) => {
  res.json({
    success: true,
    token: 'mock-token-123456',
    user: {
      id: 1,
      username: '2023020616',
      name: '测试用户',
      role: '1'
    },
    school: schools[0]
  });
});

// 模拟学校列表接口
app.get('/api/auth/schools', (req, res) => {
  console.log('获取学校列表请求:', req.query);
  res.json({
    success: true,
    schools: schools
  });
});

// 模拟健康检查接口
app.get('/health', (req, res) => {
  res.send('OK');
});

app.listen(port, () => {
  console.log(`Mock server running at http://localhost:${port}`);
});
