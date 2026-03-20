import json
import os
import pandas as pd
import requests
from datetime import datetime
from .api_integration import APIIntegration

class ScenicSpotModule:
    def __init__(self):
        # 初始化API集成模块
        self.api = APIIntegration()
        # 缓存目录
        self.cache_dir = 'cache/scenic_spots'
        os.makedirs(self.cache_dir, exist_ok=True)
        # 景点类型映射
        self.spot_type_mapping = {
            '110100': '旅游景点',
            '110101': '综合景点',
            '110102': '城市公园',
            '110103': '植物园',
            '110104': '动物园',
            '110105': '主题乐园',
            '110106': '游乐园',
            '110107': '水族馆',
            '110108': '海洋馆',
            '110109': '博物馆',
            '110110': '纪念馆',
            '110111': '展览馆',
            '110112': '寺庙',
            '110113': '教堂',
            '110114': '清真寺',
            '110115': '遗址',
            '110116': '古镇',
            '110117': '古城',
            '110118': '古村落',
            '110119': '自然保护区',
            '110120': '森林公园',
            '110121': '风景名胜',
            '110122': '海滩',
            '110123': '湖泊',
            '110124': '河流',
            '110125': '温泉',
            '110126': '瀑布',
            '110127': '山洞',
            '110128': '山岳'
        }
    
    def get_city_spots(self, city_name):
        """
        获取指定城市的景点列表
        """
        # 检查缓存
        cache_file = os.path.join(self.cache_dir, f'{city_name}_spots.json')
        if os.path.exists(cache_file):
            # 检查缓存是否过期（24小时）
            if self._is_cache_valid(cache_file):
                with open(cache_file, 'r', encoding='utf-8') as f:
                    return json.load(f)
        
        # 从API获取景点数据
        spots = self._fetch_spots_from_api(city_name)
        
        # 丰富景点信息
        enriched_spots = self._enrich_spot_info(spots, city_name)
        
        # 保存到缓存
        with open(cache_file, 'w', encoding='utf-8') as f:
            json.dump(enriched_spots, f, ensure_ascii=False, indent=2)
        
        return enriched_spots
    
    def _fetch_spots_from_api(self, city_name):
        """
        从API获取景点基础数据
        """
        try:
            # 使用API集成模块获取景点
            spots = self.api.get_scenic_spots(city_name)
            
            # 标准化景点数据结构
            standardized_spots = []
            for spot in spots:
                standardized_spot = {
                    'name': spot.get('name', ''),
                    'city': city_name,
                    'address': spot.get('address', ''),
                    'location': spot.get('location', ''),  # lng,lat格式
                    'type_code': spot.get('type', '').split(';')[0] if spot.get('type') else '110100',
                    'type': self._get_spot_type(spot.get('type', '')),
                    'rating': spot.get('biz_ext', {}).get('rating', '0'),
                    'pname': spot.get('pname', ''),  # 省份
                    'adname': spot.get('adname', ''),  # 行政区域
                    'description': spot.get('description', ''),
                    'distance': spot.get('distance', 0),
                    'fetch_time': datetime.now().isoformat()
                }
                standardized_spots.append(standardized_spot)
            
            # 按评分排序
            standardized_spots.sort(key=lambda x: float(x['rating']) if x['rating'] else 0, reverse=True)
            
            return standardized_spots
        except Exception as e:
            print(f"获取景点数据失败: {e}")
            # 返回空列表或模拟数据
            return self._get_mock_spots(city_name)
    
    def _get_spot_type(self, type_str):
        """
        根据类型代码获取景点类型名称
        """
        if not type_str:
            return '旅游景点'
        
        # 尝试从类型代码获取
        for code in type_str.split(';'):
            if code in self.spot_type_mapping:
                return self.spot_type_mapping[code]
        
        # 简单的关键词匹配
        type_keywords = {
            '公园': '城市公园',
            '植物园': '植物园',
            '动物园': '动物园',
            '主题': '主题乐园',
            '游乐': '游乐园',
            '海洋': '海洋馆',
            '水族': '水族馆',
            '博物馆': '博物馆',
            '纪念馆': '纪念馆',
            '展览': '展览馆',
            '寺庙': '寺庙',
            '教堂': '教堂',
            '清真寺': '清真寺',
            '遗址': '遗址',
            '古镇': '古镇',
            '古城': '古城',
            '古村': '古村落',
            '森林': '森林公园',
            '风景': '风景名胜',
            '海滩': '海滩',
            '湖': '湖泊',
            '河': '河流',
            '温泉': '温泉',
            '瀑布': '瀑布',
            '山洞': '山洞',
            '山': '山岳'
        }
        
        for keyword, type_name in type_keywords.items():
            if keyword in type_str:
                return type_name
        
        return '旅游景点'
    
    def _enrich_spot_info(self, spots, city_name):
        """
        丰富景点信息，包括开放时间、门票、游玩时长等
        """
        enriched_spots = []
        
        # 准备批量查询的景点名称列表
        spot_names = [spot['name'] for spot in spots]
        
        # 使用LLM批量获取景点详细信息
        detailed_info = self._get_spots_detail_from_llm(city_name, spot_names[:20])  # 限制一次性查询数量
        
        # 合并详细信息
        for spot in spots:
            spot_name = spot['name']
            # 查找对应的详细信息
            spot_detail = next((info for info in detailed_info if info.get('name') == spot_name), {})
            
            # 合并基础信息和详细信息
            enriched_spot = {
                **spot,
                'opening_hours': spot_detail.get('opening_hours', '暂无信息'),
                'ticket_price': spot_detail.get('ticket_price', '免费'),
                'visit_duration': spot_detail.get('visit_duration', '约1小时'),
                'best_season': spot_detail.get('best_season', '全年适宜'),
                'description': spot_detail.get('description', spot.get('description', '')),
                'tips': spot_detail.get('tips', ''),
                'review_count': spot_detail.get('review_count', '0'),
                'latitude': self._get_latitude(spot.get('location', '')),
                'longitude': self._get_longitude(spot.get('location', ''))
            }
            enriched_spots.append(enriched_spot)
        
        return enriched_spots
    
    def _get_spots_detail_from_llm(self, city_name, spot_names):
        """
        使用LLM获取景点详细信息
        """
        if not spot_names:
            return []
        
        spots_str = '、'.join(spot_names)
        prompt = f"请提供{city_name}以下景点的详细信息：{spots_str}。" \
                f"每个景点需要包含：名称(name)、开放时间(opening_hours)、门票价格(ticket_price)、建议游玩时长(visit_duration)、最佳游玩季节(best_season)、景点介绍(description)、游玩贴士(tips)。" \
                f"请以JSON数组格式返回，确保每个景点都有完整的信息。如果某个景点信息未知，请提供合理的默认值。"
        
        try:
            result = self.api.call_deepseek_api(prompt, max_tokens=2000)
            if 'choices' in result and result['choices']:
                content = result['choices'][0]['message']['content']
                # 提取JSON部分
                if '[' in content and ']' in content:
                    json_str = content[content.find('['):content.rfind(']')+1]
                    return json.loads(json_str)
        except Exception as e:
            print(f"获取景点详细信息失败: {e}")
        
        return []
    
    def _get_latitude(self, location_str):
        """
        从location字符串中提取纬度
        """
        if not location_str or ',' not in location_str:
            return None
        try:
            # 高德地图location格式为lng,lat
            return float(location_str.split(',')[1])
        except:
            return None
    
    def _get_longitude(self, location_str):
        """
        从location字符串中提取经度
        """
        if not location_str or ',' not in location_str:
            return None
        try:
            # 高德地图location格式为lng,lat
            return float(location_str.split(',')[0])
        except:
            return None
    
    def _is_cache_valid(self, cache_file):
        """
        检查缓存是否有效（24小时内）
        """
        try:
            mtime = os.path.getmtime(cache_file)
            current_time = datetime.now().timestamp()
            # 缓存有效期24小时
            return (current_time - mtime) < 86400
        except:
            return False
    
    def _get_mock_spots(self, city_name):
        """
        返回模拟景点数据，用于测试或API调用失败时
        """
        # 一些常见城市的模拟景点数据
        mock_spots_data = {
            '大理市': [
                {
                    'name': '大理古城',
                    'city': '大理市',
                    'address': '云南省大理白族自治州大理市古城路',
                    'type': '古城',
                    'rating': '4.7',
                    'opening_hours': '全天开放',
                    'ticket_price': '免费',
                    'visit_duration': '约3小时',
                    'best_season': '3-5月，9-11月',
                    'description': '大理古城是古代南诏国和大理国的都城，保存着完好的白族建筑风格和历史文化。'
                },
                {
                    'name': '洱海',
                    'city': '大理市',
                    'address': '云南省大理白族自治州大理市',
                    'type': '湖泊',
                    'rating': '4.8',
                    'opening_hours': '全天开放',
                    'ticket_price': '免费（部分景点收费）',
                    'visit_duration': '约4小时',
                    'best_season': '3-5月，9-11月',
                    'description': '洱海是云南省第二大淡水湖，以"银苍玉洱"闻名，是大理"风花雪月"四景之一。'
                },
                {
                    'name': '崇圣寺三塔',
                    'city': '大理市',
                    'address': '云南省大理白族自治州大理市崇圣路',
                    'type': '寺庙',
                    'rating': '4.6',
                    'opening_hours': '08:00-18:00',
                    'ticket_price': '75元',
                    'visit_duration': '约2小时',
                    'best_season': '全年适宜',
                    'description': '崇圣寺三塔是大理的标志性建筑，由一大二小三座佛塔组成，是中国南方最古老最雄伟的建筑之一。'
                },
                {
                    'name': '双廊古镇',
                    'city': '大理市',
                    'address': '云南省大理白族自治州大理市双廊镇',
                    'type': '古镇',
                    'rating': '4.5',
                    'opening_hours': '全天开放',
                    'ticket_price': '免费',
                    'visit_duration': '约3小时',
                    'best_season': '3-5月，9-11月',
                    'description': '双廊古镇位于洱海东北岸，是一个集历史文化、自然风光于一体的古镇。'
                },
                {
                    'name': '喜洲古镇',
                    'city': '大理市',
                    'address': '云南省大理白族自治州大理市喜洲镇',
                    'type': '古镇',
                    'rating': '4.4',
                    'opening_hours': '全天开放',
                    'ticket_price': '免费',
                    'visit_duration': '约2.5小时',
                    'best_season': '3-5月，9-11月',
                    'description': '喜洲古镇是大理白族文化的重要发源地，保存着大量白族传统民居建筑。'
                }
            ]
        }
        
        # 返回对应城市的模拟数据或空列表
        return mock_spots_data.get(city_name, [])
    
    def filter_spots_by_preferences(self, spots, preferences):
        """
        根据用户偏好筛选景点
        """
        if not preferences:
            return spots
        
        filtered_spots = []
        
        # 偏好与景点类型的映射关系
        preference_type_mapping = {
            '自然风光': ['湖泊', '山岳', '海滩', '瀑布', '森林', '河流', '温泉', '自然保护区'],
            '历史文化': ['古城', '古镇', '古村落', '寺庙', '博物馆', '纪念馆', '遗址', '教堂'],
            '特色美食': [],  # 美食偏好不直接对应景点类型
            '购物娱乐': ['主题乐园', '游乐园', '购物中心'],
            '主题乐园': ['主题乐园', '游乐园', '海洋馆', '水族馆'],
            '亲子活动': ['主题乐园', '动物园', '植物园', '海洋馆', '科技馆']
        }
        
        for spot in spots:
            # 检查景点是否符合任一偏好
            for pref in preferences:
                # 获取该偏好对应的景点类型列表
                preferred_types = preference_type_mapping.get(pref, [])
                
                # 检查景点类型是否匹配
                if spot.get('type') in preferred_types:
                    filtered_spots.append(spot)
                    break
                
                # 检查景点描述是否包含偏好相关关键词
                if any(keyword in spot.get('description', '').lower() for keyword in pref.lower().split()):
                    filtered_spots.append(spot)
                    break
        
        # 如果没有符合偏好的景点，返回原始列表
        return filtered_spots if filtered_spots else spots
    
    def search_spots(self, keyword, city=None, limit=20):
        """
        搜索景点
        """
        # 如果指定了城市，先获取该城市的景点
        if city:
            spots = self.get_city_spots(city)
        else:
            # 如果没有指定城市，需要从缓存或数据库中搜索
            spots = self._search_all_cities(keyword)
        
        # 根据关键词过滤
        filtered_spots = [
            spot for spot in spots 
            if keyword.lower() in spot.get('name', '').lower() or 
               keyword.lower() in spot.get('description', '').lower() or
               keyword.lower() in spot.get('address', '').lower()
        ]
        
        return filtered_spots[:limit]
    
    def _search_all_cities(self, keyword):
        """
        搜索所有城市的景点（从缓存中）
        """
        all_spots = []
        
        # 遍历缓存文件
        for filename in os.listdir(self.cache_dir):
            if filename.endswith('_spots.json'):
                try:
                    with open(os.path.join(self.cache_dir, filename), 'r', encoding='utf-8') as f:
                        spots = json.load(f)
                        all_spots.extend(spots)
                except Exception as e:
                    print(f"读取缓存文件失败: {e}")
        
        return all_spots
    
    def get_nearby_spots(self, lat, lng, radius=5000, limit=10):
        """
        获取指定坐标附近的景点
        """
        try:
            # 使用高德地图POI周边搜索
            url = 'https://restapi.amap.com/v3/place/around'
            params = {
                'key': os.getenv('AMAP_API_KEY'),
                'location': f'{lng},{lat}',
                'keywords': '',
                'types': '110100',
                'radius': radius,
                'offset': limit,
                'page': 1,
                'extensions': 'all'
            }
            
            response = requests.get(url, params=params, timeout=10)
            data = response.json()
            
            if data.get('status') == '1' and data.get('pois'):
                # 转换为标准格式
                spots = []
                for poi in data['pois']:
                    spot = self._convert_poi_to_spot(poi)
                    spots.append(spot)
                return spots
            
            return []
        except Exception as e:
            print(f"获取附近景点失败: {e}")
            return []
    
    def _convert_poi_to_spot(self, poi):
        """
        将POI数据转换为景点标准格式
        """
        return {
            'name': poi.get('name', ''),
            'city': poi.get('cityname', ''),
            'address': poi.get('address', ''),
            'location': poi.get('location', ''),
            'type_code': poi.get('type', '').split(';')[0] if poi.get('type') else '110100',
            'type': self._get_spot_type(poi.get('type', '')),
            'rating': poi.get('biz_ext', {}).get('rating', '0'),
            'distance': poi.get('distance', 0),
            'latitude': self._get_latitude(poi.get('location', '')),
            'longitude': self._get_longitude(poi.get('location', ''))
        }
    
    def _get_latitude(self, location_str):
        """
        从location字符串提取纬度
        """
        if not location_str or ',' not in location_str:
            return None
        try:
            # location格式为"lng,lat"
            return float(location_str.split(',')[1])
        except:
            return None
    
    def _get_longitude(self, location_str):
        """
        从location字符串提取经度
        """
        if not location_str or ',' not in location_str:
            return None
        try:
            # location格式为"lng,lat"
            return float(location_str.split(',')[0])
        except:
            return None
    
    def get_popular_spots(self, city_name, limit=10):
        """
        获取城市热门景点
        """
        spots = self.get_city_spots(city_name)
        # 按评分和评论数量排序
        spots.sort(key=lambda x: (float(x['rating']) if x['rating'] else 0, 
                                  int(x.get('review_count', '0')) if x.get('review_count') else 0), 
                   reverse=True)
        return spots[:limit]
    
    def export_spots_to_csv(self, spots, filename='spots.csv'):
        """
        将景点数据导出为CSV文件
        """
        if not spots:
            print("没有数据可导出")
            return False
        
        try:
            # 转换为DataFrame
            df = pd.DataFrame(spots)
            # 保存为CSV
            df.to_csv(filename, index=False, encoding='utf-8-sig')
            print(f"景点数据已导出到: {filename}")
            return True
        except Exception as e:
            print(f"导出CSV失败: {e}")
            return False
    
    def clear_cache(self, city_name=None):
        """
        清除缓存数据
        """
        try:
            if city_name:
                # 清除指定城市的缓存
                cache_file = os.path.join(self.cache_dir, f'{city_name}_spots.json')
                if os.path.exists(cache_file):
                    os.remove(cache_file)
                    print(f"已清除{city_name}的缓存")
            else:
                # 清除所有缓存
                for filename in os.listdir(self.cache_dir):
                    if filename.endswith('_spots.json'):
                        os.remove(os.path.join(self.cache_dir, filename))
                print("已清除所有缓存")
            return True
        except Exception as e:
            print(f"清除缓存失败: {e}")
            return False