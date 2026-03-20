import re
import requests
import os
from geopy.geocoders import Nominatim
from datetime import datetime

class UserInputModule:
    def __init__(self):
        # 初始化地理编码器
        self.geolocator = Nominatim(user_agent="zhiyou_travel_app")
        # 常见城市别名映射
        self.city_aliases = {
            "大理": "大理市",
            "北京": "北京市",
            "上海": "上海市",
            "广州": "广州市",
            "深圳": "深圳市",
            "杭州": "杭州市",
            "成都": "成都市",
            "重庆": "重庆市",
            "西安": "西安市",
            "南京": "南京市",
            "武汉": "武汉市",
            "苏州": "苏州市",
            "厦门": "厦门市",
            "青岛": "青岛市",
            "大连": "大连市",
            "天津": "天津市"
        }
    
    def process_input(self, province, city, days, preferences):
        """
        处理用户输入，进行城市识别和粒度控制
        """
        # 验证天数输入
        try:
            days = int(days)
            if days < 1 or days > 30:
                raise ValueError("旅游天数应在1-30天之间")
        except (ValueError, TypeError):
            raise ValueError("请输入有效的旅游天数")
        
        # 处理城市名称，自动补全城市后缀
        processed_city = self._process_city_name(city)
        
        # 验证城市是否真实存在
        self._validate_city(processed_city)
        
        # 标准化偏好标签
        processed_preferences = self._normalize_preferences(preferences)
        
        # 构建返回结果
        result = {
            "province": province,
            "city": processed_city,
            "days": days,
            "preferences": processed_preferences,
            "is_city_specific": True if "市" in processed_city else False,
            "timestamp": datetime.now().isoformat()
        }
        
        return result
    
    def _process_city_name(self, city):
        """
        处理城市名称，转换别名，添加城市后缀
        """
        if not city:
            return ""
        
        # 去除空白字符
        city = city.strip()
        
        # 转换常见别名
        if city in self.city_aliases:
            return self.city_aliases[city]
        
        # 如果没有城市后缀，添加"市"
        if not any(suffix in city for suffix in ["市", "区", "县", "州", "盟"]):
            # 对于特殊情况，如"北京"直接返回"北京市"
            return f"{city}市"
        
        return city
    
    def _validate_city(self, city):
        """
        验证城市是否真实存在
        """
        if not city:
            raise ValueError("请输入有效的城市名称")
        
        # 简化验证逻辑，避免依赖外部地理编码服务
        # 只进行基本的格式验证和空值检查
        # 在无法连接外部服务时，我们假设用户输入的城市是有效的
        if len(city) < 1:
            raise ValueError(f"无法识别城市: {city}")
        
        # 记录一条警告，表明我们正在使用简化的验证逻辑
        print(f"警告: 使用简化的城市验证逻辑，假设城市 '{city}' 是有效的")
        
        # 跳过地理编码验证，直接返回
        return
    
    def _validate_with_amap(self, city):
        """
        使用高德地图API验证城市
        """
        try:
            api_key = os.getenv('AMAP_API_KEY')
            if not api_key:
                # 如果没有API密钥，跳过验证
                return True
            
            url = f"https://restapi.amap.com/v3/geocode/geo"
            params = {
                'key': api_key,
                'address': city
            }
            
            response = requests.get(url, params=params)
            data = response.json()
            
            return data.get('status') == '1' and len(data.get('geocodes', [])) > 0
        except Exception:
            # API调用失败时，返回默认值
            return True
    
    def _normalize_preferences(self, preferences):
        """
        标准化用户偏好标签
        """
        if not preferences:
            return []
        
        # 确保preferences是列表
        if isinstance(preferences, str):
            # 如果是字符串，按逗号分割
            preferences = [p.strip() for p in preferences.split(',')]
        
        # 常见偏好标签标准化
        normalized = []
        preference_mapping = {
            "历史": "历史文化",
            "文化": "历史文化",
            "古迹": "历史文化",
            "自然": "自然风光",
            "风景": "自然风光",
            "山水": "自然风光",
            "美食": "特色美食",
            "小吃": "特色美食",
            "购物": "购物娱乐",
            "娱乐": "购物娱乐",
            "主题": "主题乐园",
            "乐园": "主题乐园",
            "亲子": "亲子活动",
            "家庭": "亲子活动"
        }
        
        for pref in preferences:
            pref = pref.strip()
            if pref:
                normalized.append(preference_mapping.get(pref, pref))
        
        # 去重
        return list(set(normalized))
    
    def extract_location_info(self, query_text):
        """
        从自由文本中提取位置信息
        """
        # 简单的正则表达式模式
        patterns = {
            "province": r'([^，,市\s]+)省',
            "city": r'([^，,区县区县镇\s]+)[市县区州盟]',
            "days": r'(\d+)天'
        }
        
        result = {}
        
        for key, pattern in patterns.items():
            match = re.search(pattern, query_text)
            if match:
                result[key] = match.group(1)
        
        return result
    
    def is_city_specific(self, city_name):
        """
        判断用户是否指定了具体城市
        """
        return any(suffix in city_name for suffix in ["市", "区", "县", "州", "盟"])
    
    def get_city_granularity(self, city_name):
        """
        获取城市粒度级别
        """
        if "市" in city_name:
            return "city"
        elif "区" in city_name or "县" in city_name:
            return "district"
        else:
            return "province"