from docx import Document
import os

# 读取docx文件
docx_path = '学涯助手.docx'

# 检查文件是否存在
if not os.path.exists(docx_path):
    print(f"文件不存在: {docx_path}")
    exit(1)

print(f"正在读取文件: {docx_path}")

# 使用python-docx读取文件
try:
    doc = Document(docx_path)
    print("文件读取成功")
    
    # 提取文本内容
    text_content = []
    for paragraph in doc.paragraphs:
        if paragraph.text:
            text_content.append(paragraph.text)
    
    print(f"提取到 {len(text_content)} 个段落")
    
    # 打印内容
    if text_content:
        print("\n文件内容:")
        for i, para in enumerate(text_content, 1):
            print(f"段落 {i}: {para}")
    else:
        print("文件中没有找到文本内容")
        
except Exception as e:
    print(f"读取文件时出错: {e}")
    import traceback
    traceback.print_exc()
