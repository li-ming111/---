import unittest
import sys
import os
import json
import datetime

# 添加项目根目录到Python路径
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

# 导入测试模块
from modules.user_input_module import UserInputModule
from modules.api_integration import APIIntegration
from modules.scenic_spot_module import ScenicSpotModule
from modules.route_planning_module import RoutePlanningModule
from modules.seasonal_optimization_module import SeasonalOptimizationModule
from modules.itinerary_output_module import ItineraryOutputModule
from modules.visualization_module import VisualizationModule

class TestUserInputModule(unittest.TestCase):
    """测试用户输入与识别模块"""
    
    def setUp(self):
        """设置测试环境"""
        self.user_input = UserInputModule()
    
    def test_identify_city(self):
        """测试城市识别功能"""
        # 测试直接城市名
        result = self.user_input.identify_city("大理市")
        self.assertEqual(result.get('city_name'), "大理市")
        self.assertEqual(result.get('is_precise_city'), True)
        
        # 测试省份+城市
        result = self.user_input.identify_city("云南省大理市")
        self.assertEqual(result.get('city_name'), "大理市")
        self.assertEqual(result.get('is_precise_city'), True)
        
        # 测试简称城市
        result = self.user_input.identify_city("大理")
        self.assertEqual(result.get('city_name'), "大理市")
        self.assertEqual(result.get('is_precise_city'), True)
    
    def test_validate_travel_days(self):
        """测试旅行天数验证"""
        # 有效天数
        self.assertTrue(self.user_input.validate_travel_days(3))
        self.assertTrue(self.user_input.validate_travel_days(7))
        
        # 无效天数
        self.assertFalse(self.user_input.validate_travel_days(0))
        self.assertFalse(self.user_input.validate_travel_days(-1))
        self.assertFalse(self.user_input.validate_travel_days(30))
    
    def test_standardize_preferences(self):
        """测试偏好标准化"""
        preferences = ["自然风光", "历史文化", "美食"]
        standardized = self.user_input.standardize_preferences(preferences)
        
        self.assertIn("自然风光", standardized)
        self.assertIn("历史文化", standardized)
        self.assertIn("美食", standardized)
        
        # 测试同义词转换
        preferences_synonyms = ["自然景观", "古迹", "当地小吃"]
        standardized = self.user_input.standardize_preferences(preferences_synonyms)
        
        self.assertIn("自然风光", standardized)
        self.assertIn("历史文化", standardized)
        self.assertIn("美食", standardized)

class TestScenicSpotModule(unittest.TestCase):
    """测试景点数据库模块"""
    
    def setUp(self):
        """设置测试环境"""
        self.spot_module = ScenicSpotModule()
    
    def test_get_spots_by_city(self):
        """测试获取城市景点功能"""
        spots = self.spot_module.get_spots_by_city("大理市")
        
        # 验证返回格式
        self.assertIsInstance(spots, list)
        
        # 验证景点包含必要字段
        if spots:
            spot = spots[0]
            self.assertIn('id', spot)
            self.assertIn('name', spot)
            self.assertIn('type', spot)
            self.assertIn('city', spot)
            self.assertEqual(spot['city'], "大理市")
    
    def test_filter_spots_by_preference(self):
        """测试按偏好筛选景点"""
        # 首先获取大理市景点
        spots = self.spot_module.get_spots_by_city("大理市")
        
        # 按自然风光筛选
        nature_spots = self.spot_module.filter_spots_by_preference(spots, ["自然风光"])
        
        self.assertIsInstance(nature_spots, list)
        if nature_spots:
            # 验证筛选后的景点包含自然风光类型
            self.assertTrue(any(spot.get('type') in ["自然景观", "湖泊", "山岳"] for spot in nature_spots))

class TestRoutePlanningModule(unittest.TestCase):
    """测试智能路线规划引擎"""
    
    def setUp(self):
        """设置测试环境"""
        self.route_module = RoutePlanningModule()
        self.spot_module = ScenicSpotModule()
    
    def test_plan_route(self):
        """测试路线规划功能"""
        # 获取测试景点
        spots = self.spot_module.get_spots_by_city("大理市")
        
        # 测试规划3天行程
        plan = self.route_module.plan_route(spots, 3)
        
        self.assertIsInstance(plan, list)
        self.assertEqual(len(plan), 3)
        
        # 验证每天行程包含必要字段
        for daily_plan in plan:
            self.assertIn('day', daily_plan)
            self.assertIn('spots', daily_plan)
            self.assertIn('schedule', daily_plan)
            self.assertIn('summary', daily_plan)
            
            # 验证行程安排
            for item in daily_plan.get('schedule', []):
                self.assertIn('arrival_time', item)
                self.assertIn('departure_time', item)
                self.assertIn('spot', item)
                self.assertIn('duration_hours', item)
    
    def test_optimize_route_by_proximity(self):
        """测试基于地理邻近性优化"""
        # 创建测试景点数据
        test_spots = [
            {'id': '1', 'name': '景点A', 'type': '历史文化', 'latitude': 25.6, 'longitude': 100.2},
            {'id': '2', 'name': '景点B', 'type': '自然风光', 'latitude': 25.61, 'longitude': 100.21},
            {'id': '3', 'name': '景点C', 'type': '美食', 'latitude': 25.62, 'longitude': 100.22}
        ]
        
        optimized = self.route_module._optimize_route_by_proximity(test_spots)
        
        self.assertEqual(len(optimized), 3)
        # 验证优化后的顺序是基于距离的
        self.assertEqual(optimized[0]['id'], '1')
        self.assertEqual(optimized[1]['id'], '2')
        self.assertEqual(optimized[2]['id'], '3')

class TestSeasonalOptimizationModule(unittest.TestCase):
    """测试周期性优化模块"""
    
    def setUp(self):
        """设置测试环境"""
        self.season_module = SeasonalOptimizationModule()
    
    def test_get_current_season_info(self):
        """测试获取当前季节信息"""
        # 获取当前月份
        current_month = datetime.datetime.now().month
        
        # 根据月份确定预期季节
        if 3 <= current_month <= 5:
            expected_season = "春季"
        elif 6 <= current_month <= 8:
            expected_season = "夏季"
        elif 9 <= current_month <= 11:
            expected_season = "秋季"
        else:
            expected_season = "冬季"
        
        # 测试获取季节信息
        season_info = self.season_module.get_current_season_info()
        
        self.assertIn('current_season', season_info)
        self.assertEqual(season_info.get('current_season'), expected_season)
        self.assertIn('month', season_info)
        self.assertEqual(season_info.get('month'), current_month)
    
    def test_get_city_season_score(self):
        """测试获取城市季节适宜度评分"""
        # 测试大理市在不同季节的评分
        score_spring = self.season_module.get_city_season_score("大理市", "春季")
        score_summer = self.season_module.get_city_season_score("大理市", "夏季")
        score_autumn = self.season_module.get_city_season_score("大理市", "秋季")
        score_winter = self.season_module.get_city_season_score("大理市", "冬季")
        
        # 验证评分在0-100之间
        self.assertTrue(0 <= score_spring <= 100)
        self.assertTrue(0 <= score_summer <= 100)
        self.assertTrue(0 <= score_autumn <= 100)
        self.assertTrue(0 <= score_winter <= 100)
        
        # 大理市的最佳季节应该是春季或秋季
        self.assertGreaterEqual(max(score_spring, score_autumn), min(score_summer, score_winter))

class TestItineraryOutputModule(unittest.TestCase):
    """测试行程详情输出模块"""
    
    def setUp(self):
        """设置测试环境"""
        self.output_module = ItineraryOutputModule()
        
        # 创建测试行程数据
        self.test_daily_plans = [
            {
                'day': 1,
                'spots': [
                    {'id': '1', 'name': '大理古城', 'type': '古城', 'latitude': 25.6, 'longitude': 100.2},
                    {'id': '2', 'name': '崇圣寺三塔', 'type': '历史文化', 'latitude': 25.61, 'longitude': 100.21}
                ],
                'schedule': [
                    {
                        'arrival_time': '09:00',
                        'departure_time': '12:00',
                        'spot': {'id': '1', 'name': '大理古城', 'type': '古城'},
                        'duration_hours': 3,
                        'transportation': '步行'
                    },
                    {
                        'arrival_time': '13:30',
                        'departure_time': '16:30',
                        'spot': {'id': '2', 'name': '崇圣寺三塔', 'type': '历史文化'},
                        'duration_hours': 3,
                        'transportation': '公交车'
                    }
                ],
                'summary': '大理古城和崇圣寺三塔一日游'
            }
        ]
    
    def test_generate_daily_itinerary_details(self):
        """测试生成每日行程详情"""
        details = self.output_module.generate_daily_itinerary_details(self.test_daily_plans[0], "大理市")
        
        # 验证必要字段
        self.assertIn('day', details)
        self.assertIn('schedule', details)
        self.assertIn('accommodation_suggestion', details)
        self.assertIn('dining_suggestions', details)
        self.assertIn('dressing_advice', details)
        
        # 验证行程安排增强
        for item in details.get('schedule', []):
            self.assertIn('spot', item)
            if item['spot'].get('type') == '古城':
                self.assertIn('visit_tips', item['spot'])
    
    def test_generate_full_itinerary_report(self):
        """测试生成完整行程报告"""
        report = self.output_module.generate_full_itinerary_report(
            self.test_daily_plans, 
            "大理市",
            preferences=["历史文化", "自然风光"],
            seasonal_info={"current_season": "秋季", "season_suggestion": "秋季适合旅游"}
        )
        
        # 验证报告结构
        self.assertIn('destination', report)
        self.assertEqual(report.get('destination'), "大理市")
        self.assertIn('total_days', report)
        self.assertEqual(report.get('total_days'), 1)
        self.assertIn('daily_itineraries', report)
        self.assertIn('travel_tips', report)
        self.assertIn('packing_suggestions', report)
    
    def test_format_itinerary_for_display(self):
        """测试行程格式化显示"""
        report = self.output_module.generate_full_itinerary_report(
            self.test_daily_plans, "大理市"
        )
        formatted = self.output_module.format_itinerary_for_display(report)
        
        self.assertIsInstance(formatted, str)
        self.assertIn("智游中国 - 行程规划报告", formatted)
        self.assertIn("大理市", formatted)
        self.assertIn("第1天", formatted)

class TestVisualizationModule(unittest.TestCase):
    """测试可视化与导出模块"""
    
    def setUp(self):
        """设置测试环境"""
        self.visual_module = VisualizationModule(api_key="test_api_key")
        
        # 创建测试行程数据
        self.test_itinerary = {
            'destination': '大理市',
            'total_days': 1,
            'generation_time': '2024-01-01 10:00:00',
            'daily_itineraries': [
                {
                    'day': 1,
                    'summary': '大理古城一日游',
                    'schedule': [
                        {
                            'arrival_time': '09:00',
                            'departure_time': '12:00',
                            'spot': {
                                'id': '1',
                                'name': '大理古城',
                                'type': '古城',
                                'latitude': 25.6,
                                'longitude': 100.2
                            },
                            'duration_hours': 3
                        }
                    ]
                }
            ]
        }
    
    def test_generate_map_html(self):
        """测试生成地图HTML"""
        html = self.visual_module.generate_map_html(self.test_itinerary)
        
        self.assertIsInstance(html, str)
        self.assertIn('<!DOCTYPE html>', html)
        self.assertIn('AMap.Map', html)  # 检查高德地图初始化
        self.assertIn('大理市', html)
    
    def test_export_to_json(self):
        """测试JSON导出功能"""
        # 测试返回JSON字符串
        json_str = self.visual_module.export_to_json(self.test_itinerary)
        
        # 解析JSON验证结构
        data = json.loads(json_str)
        self.assertIn('type', data)
        self.assertEqual(data.get('type'), '智游中国行程规划')
        self.assertIn('data', data)
        self.assertEqual(data['data'].get('destination'), '大理市')
    
    def test_export_to_text(self):
        """测试文本导出功能"""
        text = self.visual_module.export_to_text(self.test_itinerary)
        
        self.assertIsInstance(text, str)
        self.assertIn('智游中国 - 行程规划报告', text)
        self.assertIn('大理市', text)
        self.assertIn('第1天', text)

class TestAPIIntegration(unittest.TestCase):
    """测试API集成模块"""
    
    def setUp(self):
        """设置测试环境"""
        # 使用模拟API调用，避免实际API调用
        os.environ['MOCK_API_CALLS'] = '1'
        self.api_module = APIIntegration()
    
    def test_get_weather_info(self):
        """测试获取天气信息"""
        weather = self.api_module.get_weather_info("大理市")
        
        self.assertIsInstance(weather, dict)
        self.assertIn('condition', weather)
        self.assertIn('temperature', weather)
    
    def test_search_scenic_spots(self):
        """测试搜索景点"""
        spots = self.api_module.search_scenic_spots("大理市")
        
        self.assertIsInstance(spots, list)
        if spots:
            spot = spots[0]
            self.assertIn('name', spot)
            self.assertIn('location', spot)
    
    def test_get_local_food(self):
        """测试获取当地美食"""
        foods = self.api_module.get_local_food("大理市")
        
        self.assertIsInstance(foods, list)
        if foods:
            food = foods[0]
            self.assertIn('name', food)
            self.assertIn('description', food)

class TestIntegration(unittest.TestCase):
    """测试系统集成功能"""
    
    def setUp(self):
        """设置测试环境"""
        # 使用模拟API调用
        os.environ['MOCK_API_CALLS'] = '1'
        
        self.user_input = UserInputModule()
        self.spot_module = ScenicSpotModule()
        self.route_module = RoutePlanningModule()
        self.season_module = SeasonalOptimizationModule()
        self.output_module = ItineraryOutputModule()
        self.visual_module = VisualizationModule()
    
    def test_full_workflow(self):
        """测试完整工作流"""
        # 1. 处理用户输入
        city_info = self.user_input.identify_city("大理市")
        self.assertTrue(city_info.get('is_precise_city'))
        
        # 2. 获取景点数据
        spots = self.spot_module.get_spots_by_city("大理市")
        self.assertIsInstance(spots, list)
        
        # 3. 规划行程
        daily_plans = self.route_module.plan_route(spots, 2)
        self.assertEqual(len(daily_plans), 2)
        
        # 4. 添加季节信息
        seasonal_info = self.season_module.get_current_season_info()
        
        # 5. 生成行程报告
        report = self.output_module.generate_full_itinerary_report(
            daily_plans, 
            "大理市",
            preferences=["自然风光", "历史文化"],
            seasonal_info=seasonal_info
        )
        
        # 6. 生成可视化内容
        html = self.visual_module.generate_map_html(report)
        self.assertIn('AMap.Map', html)
        
        # 7. 导出JSON
        json_data = self.visual_module.export_to_json(report)
        self.assertIn('智游中国行程规划', json_data)

if __name__ == '__main__':
    unittest.main()