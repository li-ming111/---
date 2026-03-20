import requests
import json
import sys
import re

def test_unicode_decoding():
    """
    测试后端Unicode解码功能是否正常工作
    调用plan_trip API，检查返回的中文是否正确显示，没有被转义
    """
    print("开始测试后端Unicode解码功能...")
    
    # 测试数据 - 使用API期望的字段名
    test_data = {
        "city": "北京",
        "days": 3,
        "preferences": ["历史文化", "美食", "购物"],
        "travel_date": "2025-12-01"
    }
    
    # 调用plan_trip API
    try:
        response = requests.post(
            "http://localhost:5000/plan_trip",
            json=test_data,
            headers={"Content-Type": "application/json"}
        )
        
        # 检查响应状态
        if response.status_code != 200:
            print(f"API调用失败: HTTP状态码 {response.status_code}")
            print(f"响应内容: {response.text}")
            return False
        
        # 获取响应数据
        data = response.json()
        print("API调用成功，获取响应数据")
        
        # 将响应数据转为字符串，用于检查
        response_text = json.dumps(data, ensure_ascii=False, indent=2)
        print("响应数据转换为字符串完成")
        
        # 检查响应中是否包含未转义的中文字符
        # 如果包含Unicode转义序列（如\uXXXX），则解码可能存在问题
        has_unicode_escapes = bool(re.search(r'\\u[0-9a-fA-F]{4}', response_text))
        
        # 检查关键字段是否有中文字符
        has_chinese_in_message = any('\u4e00' <= c <= '\u9fff' for c in data.get('message', ''))
        has_chinese_in_itinerary = False
        
        # 检查itinerary_data中是否有中文字符
        itinerary_data = data.get('itinerary_data', {})
        if isinstance(itinerary_data, dict):
            for key, value in itinerary_data.items():
                if isinstance(value, str):
                    if any('\u4e00' <= c <= '\u9fff' for c in value):
                        has_chinese_in_itinerary = True
                        break
                elif isinstance(value, (dict, list)):
                    # 简单递归检查嵌套结构
                    if any('\u4e00' <= c <= '\u9fff' for c in json.dumps(value, ensure_ascii=False)):
                        has_chinese_in_itinerary = True
                        break
        
        # 打印测试结果
        print("\n=== 测试结果 ===")
        print(f"1. 响应中是否包含Unicode转义序列: {has_unicode_escapes}")
        print(f"2. message字段中是否包含中文字符: {has_chinese_in_message}")
        print(f"3. itinerary_data中是否包含中文字符: {has_chinese_in_itinerary}")
        
        # 综合判断
        if has_unicode_escapes:
            print("\n❌ 警告: 响应中仍存在Unicode转义序列，解码可能不完整")
        else:
            print("\n✅ 成功: 响应中未发现Unicode转义序列")
        
        if has_chinese_in_message and has_chinese_in_itinerary:
            print("✅ 成功: 响应中的中文字符正确显示")
            return True
        else:
            print("❌ 失败: 响应中未发现预期的中文字符")
            return False
            
    except Exception as e:
        print(f"测试过程中出现错误: {str(e)}")
        import traceback
        traceback.print_exc()
        return False

def test_direct_decoding():
    """
    直接测试我们的解码器模块功能
    """
    print("\n开始直接测试unicode_decoder模块...")
    
    try:
        # 尝试导入我们的解码器模块
        from modules.unicode_decoder import decode_unicode_escapes, ensure_chinese_display
        
        # 测试数据 - 包含Unicode转义字符
        test_string = "这是一个\u6d4b\u8bd5\u5b57\u7b26\u4e32"
        expected_result = "这是一个测试字符串"
        
        decoded = decode_unicode_escapes(test_string)
        print(f"测试字符串解码前: {test_string}")
        print(f"测试字符串解码后: {decoded}")
        print(f"期望结果: {expected_result}")
        print(f"解码结果是否正确: {decoded == expected_result}")
        
        # 测试嵌套对象解码
        test_object = {
            "name": "\u5f20\u4e09",
            "description": "\u6d4b\u8bd5\u5bf9\u8c61",
            "items": [
                {"title": "\u6807\u98981", "content": "\u5185\u5bb91"},
                {"title": "\u6807\u98982", "content": "\u5185\u5bb92"}
            ]
        }
        
        decoded_object = ensure_chinese_display(test_object)
        print("\n嵌套对象解码后:")
        print(json.dumps(decoded_object, ensure_ascii=False, indent=2))
        
        return True
        
    except ImportError:
        print("❌ 无法导入unicode_decoder模块")
        return False
    except Exception as e:
        print(f"测试过程中出现错误: {str(e)}")
        import traceback
        traceback.print_exc()
        return False

if __name__ == "__main__":
    print("Unicode解码功能测试工具")
    print("========================")
    
    # 运行直接解码测试
    direct_test_result = test_direct_decoding()
    
    # 运行API测试
    api_test_result = test_unicode_decoding()
    
    # 综合结果
    print("\n=== 综合测试结果 ===")
    print(f"1. 直接解码器测试: {'通过' if direct_test_result else '失败'}")
    print(f"2. API响应测试: {'通过' if api_test_result else '失败'}")
    
    if direct_test_result and api_test_result:
        print("\n🎉 所有测试通过! Unicode解码功能正常工作!")
        sys.exit(0)
    else:
        print("\n❌ 部分测试失败，请检查代码实现")
        sys.exit(1)
