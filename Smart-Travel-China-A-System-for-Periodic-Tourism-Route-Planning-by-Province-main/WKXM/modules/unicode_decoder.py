# Unicode转义字符解码器模块
# 用于将Python中的Unicode转义字符(如 '\u5317\u4eac') 转换为实际的中文字符(如 '北京')

import json
import re

def decode_unicode_escapes(text):
    """
    将字符串中的Unicode转义字符转换为实际字符
    
    Args:
        text: 包含Unicode转义字符的字符串
        
    Returns:
        解码后的字符串，其中Unicode转义字符被替换为对应的字符
    """
    if not isinstance(text, str):
        return text
    
    # 使用正则表达式匹配并替换Unicode转义字符
    def replace_unicode(match):
        code = match.group(1)
        return chr(int(code, 16))
    
    # 匹配 \u 后跟4位十六进制数字的模式
    decoded_text = re.sub(r'\\u([0-9a-fA-F]{4})', replace_unicode, text)
    return decoded_text

def decode_unicode_escapes_in_dict(data):
    """
    递归遍历字典，解码所有字符串值中的Unicode转义字符
    
    Args:
        data: 包含Unicode转义字符的字典或列表
        
    Returns:
        解码后的数据结构，其中所有字符串中的Unicode转义字符被替换
    """
    if isinstance(data, dict):
        return {key: decode_unicode_escapes_in_dict(value) for key, value in data.items()}
    elif isinstance(data, list):
        return [decode_unicode_escapes_in_dict(item) for item in data]
    elif isinstance(data, str):
        return decode_unicode_escapes(data)
    else:
        return data

def safe_json_dumps(obj, ensure_ascii=False, **kwargs):
    """
    安全地将Python对象序列化为JSON字符串，默认不转义非ASCII字符
    
    Args:
        obj: 要序列化的Python对象
        ensure_ascii: 是否将所有非ASCII字符转义为Unicode转义序列，默认为False
        **kwargs: 传递给json.dumps的其他参数
        
    Returns:
        序列化后的JSON字符串
    """
    return json.dumps(obj, ensure_ascii=ensure_ascii, **kwargs)

def decode_json_with_escapes(json_str):
    """
    解码包含Unicode转义字符的JSON字符串，然后返回Python对象
    
    Args:
        json_str: 可能包含Unicode转义字符的JSON字符串
        
    Returns:
        解码后的Python对象，其中所有Unicode转义字符已被替换为对应字符
    """
    # 先解码Unicode转义字符
    decoded_str = decode_unicode_escapes(json_str)
    # 然后解析JSON
    return json.loads(decoded_str)

def ensure_chinese_display(data):
    """
    确保数据中的中文字符能够正确显示，处理各种可能的编码问题
    
    Args:
        data: 要处理的数据（字典、列表、字符串等）
        
    Returns:
        处理后的数据，确保中文能正确显示
    """
    # 如果是字符串，尝试解析为JSON（应对双重编码情况）
    if isinstance(data, str):
        try:
            parsed = json.loads(data)
            return decode_unicode_escapes_in_dict(parsed)
        except (json.JSONDecodeError, TypeError):
            # 如果不是有效的JSON，直接解码字符串
            return decode_unicode_escapes(data)
    
    # 递归处理数据结构
    return decode_unicode_escapes_in_dict(data)
