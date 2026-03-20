import zipfile
import xml.etree.ElementTree as ET
import os

# 读取docx文件
docx_path = '学涯助手.docx'

# 检查文件是否存在
if not os.path.exists(docx_path):
    print(f"文件不存在: {docx_path}")
    print(f"当前目录: {os.getcwd()}")
    print(f"目录内容: {os.listdir('.')}")
    exit(1)

print(f"正在读取文件: {docx_path}")

# 解压docx文件
try:
    with zipfile.ZipFile(docx_path, 'r') as zip_ref:
        # 查看zip文件内容
        print("ZIP文件内容:")
        for file in zip_ref.namelist():
            print(f"  - {file}")
        
        # 读取document.xml文件
        if 'word/document.xml' in zip_ref.namelist():
            with zip_ref.open('word/document.xml') as f:
                xml_content = f.read()
            print(f"document.xml大小: {len(xml_content)} bytes")
        else:
            print("word/document.xml不存在于ZIP文件中")
            exit(1)
except Exception as e:
    print(f"读取ZIP文件时出错: {e}")
    exit(1)

# 解析XML
try:
    root = ET.fromstring(xml_content)
    print("XML解析成功")
except Exception as e:
    print(f"XML解析出错: {e}")
    exit(1)

# 定义命名空间
ns = {'w': 'http://schemas.openxmlformats.org/wordprocessingml/2006/main'}

# 提取文本内容
text_content = []
try:
    for elem in root.findall('.//w:t', ns):
        if elem.text:
            text_content.append(elem.text)
    print(f"提取到 {len(text_content)} 个文本元素")
except Exception as e:
    print(f"提取文本时出错: {e}")
    exit(1)

# 打印内容
if text_content:
    print("\n文件内容:")
    print('\n'.join(text_content))
else:
    print("文件中没有找到文本内容")

