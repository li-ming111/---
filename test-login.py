import requests

# 测试后端API
print("测试后端API...")
response = requests.get('http://localhost:8082/api/auth/schools')
print(f"后端API状态码: {response.status_code}")
print(f"后端API响应: {response.json()}")

# 测试前端代理
print("\n测试前端代理...")
response = requests.get('http://localhost:3001/api/auth/schools')
print(f"前端代理状态码: {response.status_code}")
print(f"前端代理响应: {response.json()}")

print("\n测试完成")
