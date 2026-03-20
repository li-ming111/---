import requests
import os
import json
from datetime import datetime

class APIIntegration:
    def __init__(self):
        # 从环境变量加载API密钥
        self.deepseek_api_key = os.getenv('DEEPSEEK_API_KEY')
        self.deepseek_api_url = os.getenv('DEEPSEEK_API_URL', 'https://api.deepseek.com/v1')
        self.amap_api_key = os.getenv('AMAP_API_KEY')
        self.amap_api_url = os.getenv('AMAP_API_URL', 'https://restapi.amap.com/v3')
        self.aliyun_api_key = os.getenv('ALIYUN_API_KEY')
        self.aliyun_appcode = os.getenv('ALIYUN_APPCODE')
        self.baidu_map_api_key = os.getenv('BAIDU_MAP_API_KEY')
        
        # 设置请求超时
        self.timeout = 10
    
    def call_deepseek_api(self, prompt, max_tokens=1000, temperature=0.7):
        """
        调用DeepSeek API进行智能问答
        """
        if not self.deepseek_api_key:
            raise ValueError("DeepSeek API密钥未配置")
        
        headers = {
            'Content-Type': 'application/json',
            'Authorization': f'Bearer {self.deepseek_api_key}'
        }
        
        payload = {
            'model': 'deepseek-chat',
            'messages': [
                {'role': 'system', 'content': '你是一个智能旅游助手，专注于提供准确的旅游信息。'},                {'role': 'user', 'content': prompt}
            ],
            'max_tokens': max_tokens,
            'temperature': temperature
        }
        
        try:
            response = requests.post(
                f'{self.deepseek_api_url}/chat/completions',
                headers=headers,
                data=json.dumps(payload),
                timeout=self.timeout
            )
            response.raise_for_status()
            return response.json()
        except Exception as e:
            print(f"DeepSeek API调用失败: {e}")
            # 返回默认响应或错误信息
            return {"error": str(e)}
    
    def get_amap_geocode(self, address):
        """
        使用高德地图API进行地理编码
        """
        if not self.amap_api_key:
            raise ValueError("高德地图API密钥未配置")
        
        url = f'{self.amap_api_url}/geocode/geo'
        params = {
            'key': self.amap_api_key,
            'address': address
        }
        
        try:
            response = requests.get(url, params=params, timeout=self.timeout)
            response.raise_for_status()
            return response.json()
        except Exception as e:
            print(f"高德地图地理编码失败: {e}")
            return {"error": str(e)}
    
    def get_amap_poi(self, keywords, city, types='110100', offset=20):
        """
        使用高德地图API获取POI（兴趣点）数据
        types='110100'表示旅游景点
        """
        if not self.amap_api_key:
            raise ValueError("高德地图API密钥未配置")
        
        url = f'{self.amap_api_url}/place/text'
        params = {
            'key': self.amap_api_key,
            'keywords': keywords,
            'city': city,
            'types': types,
            'offset': offset,
            'page': 1,
            'extensions': 'all'
        }
        
        try:
            response = requests.get(url, params=params, timeout=self.timeout)
            response.raise_for_status()
            return response.json()
        except Exception as e:
            print(f"高德地图POI查询失败: {e}")
            return {"error": str(e)}
    
    def get_amap_route(self, origin, destination, mode='driving'):
        """
        使用高德地图API获取路线规划
        mode: driving(驾车), walking(步行), transit(公交), riding(骑行)
        """
        if not self.amap_api_key:
            raise ValueError("高德地图API密钥未配置")
        
        url = f'{self.amap_api_url}/direction/driving' if mode == 'driving' else \
              f'{self.amap_api_url}/direction/walking' if mode == 'walking' else \
              f'{self.amap_api_url}/direction/transit/integrated' if mode == 'transit' else \
              f'{self.amap_api_url}/direction/riding'
        
        params = {
            'key': self.amap_api_key,
            'origin': origin,  # 格式：lat,lng
            'destination': destination  # 格式：lat,lng
        }
        
        # 如果是公交，添加城市参数
        if mode == 'transit':
            params['city'] = origin.split(',')[0]  # 简单处理，实际应该从城市名称获取
        
        try:
            response = requests.get(url, params=params, timeout=self.timeout)
            response.raise_for_status()
            return response.json()
        except Exception as e:
            print(f"高德地图路线规划失败: {e}")
            return {"error": str(e)}
    
    def get_weather(self, city):
        """
        使用阿里云天气API获取城市天气
        """
        if not self.aliyun_appcode:
            raise ValueError("阿里云AppCode未配置")
        
        url = 'https://api.aliyun.com/api/weather'
        headers = {
            'Authorization': f'APPCODE {self.aliyun_appcode}'
        }
        params = {
            'city': city,
            'date': datetime.now().strftime('%Y-%m-%d')
        }
        
        try:
            response = requests.get(url, headers=headers, params=params, timeout=self.timeout)
            response.raise_for_status()
            return response.json()
        except Exception as e:
            print(f"阿里云天气API调用失败: {e}")
            # 尝试使用高德地图天气API作为备选
            return self._get_amap_weather(city)
    
    def _get_amap_weather(self, city):
        """
        使用高德地图天气API作为备选
        """
        if not self.amap_api_key:
            return {"error": "无可用的天气API"}
        
        url = f'{self.amap_api_url}/weather/weatherInfo'
        params = {
            'key': self.amap_api_key,
            'city': self._get_city_code(city),
            'extensions': 'all'
        }
        
        try:
            response = requests.get(url, params=params, timeout=self.timeout)
            response.raise_for_status()
            return response.json()
        except Exception as e:
            print(f"高德地图天气查询失败: {e}")
            return {"error": str(e)}
    
    def _get_city_code(self, city_name):
        """
        获取城市的行政区划代码
        """
        # 这里使用地理编码API间接获取城市代码
        geocode_result = self.get_amap_geocode(city_name)
        if 'geocodes' in geocode_result and geocode_result['geocodes']:
            return geocode_result['geocodes'][0].get('adcode', '')
        return ''
    
    def get_scenic_spots(self, city_name):
        """
        获取指定城市的景点列表
        """
        # 先尝试使用高德地图POI API
        poi_result = self.get_amap_poi('景点', city_name, '110100', 50)
        
        if 'pois' in poi_result:
            spots = []
            for poi in poi_result['pois']:
                spot = {
                    'name': poi.get('name'),
                    'type': poi.get('type'),
                    'address': poi.get('address'),
                    'location': poi.get('location'),  # lng,lat
                    'city': poi.get('cityname'),
                    'adname': poi.get('adname'),  # 行政区域名称
                    'distance': poi.get('distance'),
                    'biz_ext': poi.get('biz_ext', {})  # 包含评分等信息
                }
                spots.append(spot)
            return spots
        
        # 如果高德API调用失败，尝试使用DeepSeek API获取信息
        return self._get_spots_from_llm(city_name)
    
    def _get_spots_from_llm(self, city_name):
        """
        使用LLM获取城市景点信息作为备选方案
        """
        prompt = f"请列出{city_name}的主要旅游景点，每个景点需要包含：名称、类型、地址、简要介绍。" \
                f"请以JSON格式返回，字段名：name, type, address, description。最多返回10个景点。"
        
        try:
            llm_result = self.call_deepseek_api(prompt)
            if 'choices' in llm_result and llm_result['choices']:
                content = llm_result['choices'][0]['message']['content']
                # 尝试解析JSON
                spots_data = json.loads(content)
                return spots_data
        except Exception as e:
            print(f"LLM获取景点信息失败: {e}")
        
        return []
    
    def get_local_food(self, city_name):
        """
        获取城市特色美食信息
        """
        prompt = f"请列出{city_name}的特色美食，每个美食需要包含：名称、简介、推荐餐厅。" \
                f"请以JSON格式返回，字段名：name, description, recommended_restaurants。最多返回10种美食。"
        
        try:
            result = self.call_deepseek_api(prompt, max_tokens=1500)
            if 'choices' in result and result['choices']:
                content = result['choices'][0]['message']['content']
                return json.loads(content)
        except Exception as e:
            print(f"获取特色美食失败: {e}")
        
        return []
    
    def get_travel_tips(self, city_name, season=None):
        """
        获取旅行贴士，如穿衣指南等
        """
        if not season:
            season = self._get_current_season()
        
        prompt = f"请提供{city_name}在{season}的旅行贴士，包括：穿衣指南、天气特点、旅行建议、注意事项。" \
                f"请以JSON格式返回，字段名：clothing_guide, weather_features, travel_suggestions, notes。"
        
        try:
            result = self.call_deepseek_api(prompt)
            if 'choices' in result and result['choices']:
                content = result['choices'][0]['message']['content']
                return json.loads(content)
        except Exception as e:
            print(f"获取旅行贴士失败: {e}")
        
        return {}
    
    def _get_current_season(self):
        """
        获取当前季节
        """
        month = datetime.now().month
        if month in [3, 4, 5]:
            return "春季"
        elif month in [6, 7, 8]:
            return "夏季"
        elif month in [9, 10, 11]:
            return "秋季"
        else:
            return "冬季"
    
    def search_hotels(self, city_name, area=None):
        """
        搜索酒店信息
        """
        # 使用高德地图POI搜索酒店
        keywords = f'{city_name}酒店'
        if area:
            keywords = f'{area}酒店'
        
        poi_result = self.get_amap_poi(keywords, city_name, '100100', 30)
        
        if 'pois' in poi_result:
            hotels = []
            for poi in poi_result['pois']:
                hotel = {
                    'name': poi.get('name'),
                    'address': poi.get('address'),
                    'price': poi.get('biz_ext', {}).get('price', '价格待定'),
                    'rating': poi.get('biz_ext', {}).get('rating', '暂无评分'),
                    'type': poi.get('type'),
                    'location': poi.get('location')
                }
                hotels.append(hotel)
            return hotels
        
        # 备选方案：使用LLM获取酒店推荐
        prompt = f"请推荐{city_name}{area+'内' if area else ''}的酒店，包括：名称、位置、价格区间、特色。" \
                f"请以JSON格式返回，字段名：name, location, price_range, features。最多返回5家酒店。"
        
        try:
            result = self.call_deepseek_api(prompt)
            if 'choices' in result and result['choices']:
                content = result['choices'][0]['message']['content']
                return json.loads(content)
        except Exception as e:
            print(f"获取酒店推荐失败: {e}")
        
        return []
    
    def get_traffic_info(self, city_name, origin, destination):
        """
        获取交通信息建议
        """
        # 尝试使用高德地图路线规划
        try:
            # 先进行地理编码
            origin_loc = self.get_amap_geocode(f'{city_name}{origin}')
            dest_loc = self.get_amap_geocode(f'{city_name}{destination}')
            
            if origin_loc and 'geocodes' in origin_loc and dest_loc and 'geocodes' in dest_loc:
                origin_coords = origin_loc['geocodes'][0]['location']
                dest_coords = dest_loc['geocodes'][0]['location']
                
                # 尝试不同交通方式
                walking_result = self.get_amap_route(origin_coords, dest_coords, 'walking')
                riding_result = self.get_amap_route(origin_coords, dest_coords, 'riding')
                
                # 整合交通建议
                return {
                    'origin': origin,
                    'destination': destination,
                    'walking': self._parse_walking_result(walking_result),
                    'riding': self._parse_riding_result(riding_result),
                    'suggestion': self._generate_traffic_suggestion(walking_result, riding_result)
                }
        except Exception as e:
            print(f"获取交通信息失败: {e}")
        
        # 备选方案：使用LLM生成交通建议
        return self._get_traffic_suggestion_from_llm(city_name, origin, destination)
    
    def _parse_walking_result(self, result):
        """
        解析步行路线结果
        """
        if 'route' in result and 'paths' in result['route'] and result['route']['paths']:
            path = result['route']['paths'][0]
            return {
                'distance': path.get('distance', 0),
                'duration': path.get('duration', 0),
                'steps': len(path.get('steps', []))
            }
        return {}
    
    def _parse_riding_result(self, result):
        """
        解析骑行路线结果
        """
        if 'route' in result and 'paths' in result['route'] and result['route']['paths']:
            path = result['route']['paths'][0]
            return {
                'distance': path.get('distance', 0),
                'duration': path.get('duration', 0),
                'steps': len(path.get('steps', []))
            }
        return {}
    
    def _generate_traffic_suggestion(self, walking_result, riding_result):
        """
        根据步行和骑行结果生成交通建议
        """
        walking_info = self._parse_walking_result(walking_result)
        riding_info = self._parse_riding_result(riding_result)
        
        if walking_info and walking_info['duration'] < 1800:  # 小于30分钟
            return f"步行约{walking_info['duration']//60}分钟，建议步行前往"
        elif riding_info:
            return f"骑行约{riding_info['duration']//60}分钟，建议使用共享单车"
        else:
            return "建议使用公共交通或打车"
    
    def _get_traffic_suggestion_from_llm(self, city_name, origin, destination):
        """
        使用LLM生成交通建议
        """
        prompt = f"从{city_name}的{origin}到{destination}，有哪些交通方式？请推荐最合适的市内交通方式（地铁、公交、共享单车或步行），并说明理由。"
        
        try:
            result = self.call_deepseek_api(prompt)
            if 'choices' in result and result['choices']:
                content = result['choices'][0]['message']['content']
                return {
                    'suggestion': content,
                    'origin': origin,
                    'destination': destination
                }
        except Exception as e:
            print(f"LLM生成交通建议失败: {e}")
        
        return {}
    
    def get_city_info(self, city_name):
        """
        获取城市的综合信息
        """
        # 使用DeepSeek API获取城市综合信息
        prompt = f"请提供{city_name}的综合信息，包括：地理位置、气候特点、旅游季节建议、文化特色、主要景点分布区域、交通概况。" \
                f"请以JSON格式返回，字段名：location, climate, best_season, cultural_features, scenic_areas, transportation。"
        
        try:
            result = self.call_deepseek_api(prompt)
            if 'choices' in result and result['choices']:
                content = result['choices'][0]['message']['content']
                return json.loads(content)
        except Exception as e:
            print(f"获取城市信息失败: {e}")
        
        return {}