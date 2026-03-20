import requests
import json

# 测试API的URL
url = 'http://localhost:5000/plan'

# 请求数据
data = {
    "city": "北京",
    "days": 2,
    "preferences": ["历史文化"]
}

# 发送POST请求
response = requests.post(url, json=data)

# 打印响应状态码
print(f"响应状态码: {response.status_code}")

# 尝试解析JSON响应
if response.status_code == 200:
    try:
        result = response.json()
        # 使用ensure_ascii=False打印中文字符
        print(json.dumps(result, ensure_ascii=False, indent=2))
    except Exception as e:
        print(f"解析JSON失败: {e}")
        print("原始响应内容:")
        print(response.text)
else:
    print("请求失败，响应内容:")
    print(response.text)
