import os

# 检查文件是否存在
docx_path = '学涯助手.docx'

print(f"当前目录: {os.getcwd()}")
print(f"文件是否存在: {os.path.exists(docx_path)}")

if os.path.exists(docx_path):
    print(f"文件大小: {os.path.getsize(docx_path)} bytes")
    print(f"文件修改时间: {os.path.getmtime(docx_path)}")

# 尝试以二进制模式读取文件
try:
    with open(docx_path, 'rb') as f:
        content = f.read(100)
        print(f"文件前100字节: {content[:20]}...")
    print("文件可以正常打开")
except Exception as e:
    print(f"打开文件时出错: {e}")
