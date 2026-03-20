# 智游中国 - 省域周期性旅游路线规划系统
# Flask 应用主程序

import os
import json
import time
import random
import logging
from datetime import datetime
from flask import Flask, render_template, request, jsonify, redirect, url_for, session
from dotenv import load_dotenv

# 加载环境变量
load_dotenv()

# 创建Flask应用
app = Flask(__name__)
app.config['SECRET_KEY'] = os.urandom(24)  # 用于会话安全
app.config['JSON_AS_ASCII'] = False  # 确保JSON响应中的中文字符正确显示

# 导入自定义模块
from modules.user_input_module import UserInputModule
from modules.api_integration import APIIntegration
from modules.scenic_spot_module import ScenicSpotModule
from modules.route_planning_module import RoutePlanningModule
from modules.seasonal_optimization_module import SeasonalOptimizationModule
from modules.itinerary_output_module import ItineraryOutputModule
from modules.visualization_module import VisualizationModule
from modules.unicode_decoder import ensure_chinese_display, safe_json_dumps

# 配置日志
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler('app.log'),
        logging.StreamHandler()
    ]
)
logger = logging.getLogger(__name__)

# 初始化各个模块
user_input_module = UserInputModule()
api_integration = APIIntegration()
scenic_spot_module = ScenicSpotModule()
route_planning_module = RoutePlanningModule()
seasonal_optimization_module = SeasonalOptimizationModule()
itinerary_output_module = ItineraryOutputModule()
visualization_module = VisualizationModule()

@app.route('/')
def index():
    """首页路由"""
    return render_template('index.html')

# 模拟数据：不同城市的景点和行程信息
MOCK_CITY_DATA = {
    'beijing': {
        'destination': '北京',
        'city_name': '北京',
        'spots': [
            {'name': '故宫博物院', 'type': '历史文化', 'description': '中国明清两代的皇家宫殿', 'highlights': ['天安门', '太和殿', '御花园']},
            {'name': '长城', 'type': '自然风光', 'description': '中国古代伟大的防御工程', 'highlights': ['八达岭', '烽火台', '壮丽景观']},
            {'name': '天坛', 'type': '历史文化', 'description': '明清两朝皇帝祭天祈谷之地', 'highlights': ['祈年殿', '回音壁', '圜丘坛']},
            {'name': '颐和园', 'type': '历史文化', 'description': '中国清朝时期皇家园林', 'highlights': ['万寿山', '昆明湖', '长廊']},
            {'name': '什刹海', 'type': '特色体验', 'description': '北京著名的历史文化街区', 'highlights': ['胡同文化', '四合院', '夜景']}
        ],
        'food_recommendations': [
            {'name': '北京烤鸭', 'description': '北京最著名的传统美食'},
            {'name': '炸酱面', 'description': '老北京传统面食'},
            {'name': '豆汁焦圈', 'description': '北京特色小吃'},
            {'name': '涮羊肉', 'description': '冬季保暖佳肴'}
        ]
    },
    'shanghai': {
        'destination': '上海',
        'city_name': '上海',
        'spots': [
            {'name': '外滩', 'type': '特色体验', 'description': '上海的标志性景观', 'highlights': ['万国建筑群', '黄浦江夜景', '东方明珠']},
            {'name': '东方明珠', 'type': '特色体验', 'description': '上海地标性建筑', 'highlights': ['观光层', '旋转餐厅', '城市全景']},
            {'name': '豫园', 'type': '历史文化', 'description': '江南古典园林', 'highlights': ['湖心亭', '九曲桥', '传统商铺']},
            {'name': '迪士尼乐园', 'type': '特色体验', 'description': '大型主题乐园', 'highlights': ['城堡', '游行表演', '游乐设施']},
            {'name': '田子坊', 'type': '特色体验', 'description': '文艺小资聚集地', 'highlights': ['创意店铺', '咖啡馆', '石库门建筑']}
        ],
        'food_recommendations': [
            {'name': '小笼包', 'description': '上海传统点心'},
            {'name': '生煎包', 'description': '底部焦脆的美味包子'},
            {'name': '外滩牛排', 'description': '高端西式料理'},
            {'name': '本帮菜', 'description': '上海本地特色菜系'}
        ]
    },
    'hangzhou': {
        'destination': '杭州',
        'city_name': '杭州',
        'spots': [
            {'name': '西湖', 'type': '自然风光', 'description': '中国著名的风景名胜区', 'highlights': ['断桥残雪', '平湖秋月', '三潭印月']},
            {'name': '灵隐寺', 'type': '历史文化', 'description': '江南古刹', 'highlights': ['飞来峰', '石刻造像', '千年古寺']},
            {'name': '千岛湖', 'type': '自然风光', 'description': '人工湖泊风景区', 'highlights': ['千岛景观', '游船', '水质清澈']},
            {'name': '宋城', 'type': '历史文化', 'description': '仿宋主题公园', 'highlights': ['千古情表演', '宋代建筑', '民俗体验']},
            {'name': '西溪湿地', 'type': '自然风光', 'description': '国家湿地公园', 'highlights': ['水乡风光', '生态环境', '休闲徒步']}
        ],
        'food_recommendations': [
            {'name': '西湖醋鱼', 'description': '杭州传统名菜'},
            {'name': '龙井虾仁', 'description': '鲜嫩可口的特色菜'},
            {'name': '叫花鸡', 'description': '传统烹饪技艺'},
            {'name': '东坡肉', 'description': '肥而不腻的经典'}
        ]
    }
}

# 生成模拟行程数据
def generate_mock_itinerary(city, days, preferences):
    """根据城市、天数和偏好生成模拟行程"""
    # 确保city是字符串
    city_str = str(city) if city else ''
    city_lower = city_str.lower()
    
    # 尝试找到完全匹配的城市数据
    city_data = MOCK_CITY_DATA.get(city_lower)
    
    # 如果找不到完全匹配，尝试查找最接近的匹配
    if not city_data:
        # 检查是否包含已知城市名称的一部分
        for city_key, data in MOCK_CITY_DATA.items():
            if city_key in city_lower or data['destination'] in city_str:
                city_data = data
                break
    
    # 如果仍然找不到，使用北京作为默认值，但更新目的地名称为用户输入的城市
    if not city_data:
        city_data = MOCK_CITY_DATA['beijing'].copy()
        city_data['destination'] = city_str or '未知城市'
    spots = city_data['spots']
    
    # 根据偏好过滤景点
    if preferences:
        preferred_spots = []
        other_spots = []
        for spot in spots:
            if spot['type'] in preferences or any(p in spot['description'] for p in preferences):
                preferred_spots.append(spot)
            else:
                other_spots.append(spot)
        spots = preferred_spots + other_spots
    
    # 计算每天的景点数量
    spots_per_day = max(1, len(spots) // days)
    daily_spots = []
    
    for i in range(days):
        start_idx = i * spots_per_day
        end_idx = min(start_idx + spots_per_day + 1, len(spots))
        day_spots = spots[start_idx:end_idx]
        
        # 生成当天的行程安排
        schedule = []
        current_time = 9  # 从9点开始
        for j, spot in enumerate(day_spots):
            arrival_time = f"{int(current_time):02d}:00"
            duration = 1 if j < len(day_spots) - 1 else 2  # 最后一个景点停留时间更长
            current_time += duration + 0.5  # 景点间移动时间30分钟
            departure_time = f"{int(current_time):02d}:{(current_time % 1) * 60:0.0f}"
            
            schedule.append({
                'arrival_time': arrival_time,
                'departure_time': departure_time,
                'spot': spot,
                'duration_hours': duration,
                'transportation': '地铁' if j < len(day_spots) - 1 else None
            })
        
        daily_spots.append({
            'day': i + 1,
            'summary': f"{'文化之旅' if any(s['type'] == '历史文化' for s in day_spots) else '自然风光'}一日游",
            'description': f"参观{len(day_spots)}个景点，包括{', '.join([s['name'] for s in day_spots[:2]])}等",
            'spots': day_spots,
            'schedule': schedule,
            'accommodation_suggestion': '市中心酒店'
        })
    
    # 生成统计数据
    stats = {
        'total_spots': len(spots),
        'nature_spots': sum(1 for s in spots if s['type'] == '自然风光'),
        'culture_spots': sum(1 for s in spots if s['type'] == '历史文化'),
        'experience_spots': sum(1 for s in spots if s['type'] == '特色体验')
    }
    
    return {
        'destination': city_data['destination'],
        'total_days': days,
        'preferences': preferences,
        'itinerary': {
            'daily_itineraries': daily_spots,
            'food_recommendations': city_data['food_recommendations']
        },
        'stats': stats
    }

@app.route('/plan', methods=['POST'])
@app.route('/plan_trip', methods=['POST'])  # 添加别名路由，保持兼容性
def plan_trip():
    """行程规划API - 增强版"""
    try:
        # 1. 获取请求数据 - 增强的JSON处理
        data = None
        
        # 尝试多种方式获取数据，确保能够正确处理JSON请求
        try:
            # 首先尝试直接获取JSON数据（自动处理Content-Type）
            data = request.get_json()
            if data:
                logger.info("成功从JSON请求体获取数据")
        except Exception as json_err:
            logger.warning(f"JSON解析错误: {json_err}")
            data = None
        
        # 如果JSON解析失败，尝试强制获取
        if data is None:
            try:
                data = request.get_json(force=True)  # 强制解析JSON
                logger.info("成功从JSON请求体强制获取数据")
            except Exception as json_err:
                logger.warning(f"强制JSON解析失败: {json_err}")
                data = None
        
        # 如果JSON解析仍然失败，尝试从表单数据获取
        if data is None:
            try:
                data = dict(request.form)
                logger.info("从表单数据获取数据")
            except Exception as form_err:
                logger.warning(f"表单数据获取失败: {form_err}")
                data = {}
        
        # 确保data是字典
        if not isinstance(data, dict):
            data = {}
            logger.warning("请求数据格式不正确，使用空数据")
        
        logger.info(f"接收到行程规划请求: {data}")
        
        # 2. 处理和验证用户输入
        # 处理preferences字段 - 增强版处理
        preferences = data.get('preferences', [])
        if isinstance(preferences, str):
            try:
                # 尝试解析JSON字符串
                preferences = json.loads(preferences)
                logger.info("成功解析preferences JSON字符串")
            except:
                # 如果JSON解析失败，尝试按逗号分隔或使用原始值
                if ',' in preferences:
                    preferences = [p.strip() for p in preferences.split(',')]
                    logger.info("按逗号分隔解析preferences")
                else:
                    preferences = [preferences] if preferences else []
                    logger.info("使用原始值作为单个preference")
        
        # 确保preferences是数组
        if not isinstance(preferences, list):
            preferences = [str(preferences)]
        
        # 构建用户输入数据
        user_input_data = {
            'province': data.get('province', '').strip(),
            'city': data.get('city', '').strip(),
            'days': int(data.get('days', 3)) if data.get('days') else 3,
            'preferences': preferences,
            'travel_date': data.get('travel_date', datetime.now().strftime('%Y-%m-%d'))
        }
        
        # 验证必要字段 - 与前端保持一致，允许只输入省份或只输入城市
        if not user_input_data['province'] and not user_input_data['city']:
            return jsonify({
                'success': False,
                'message': "请输入省份或城市"
            }), 400
        
        if user_input_data['days'] < 1 or user_input_data['days'] > 30:
            return jsonify({
                'success': False,
                'message': "行程天数必须在1-30天之间"
            }), 400
        
        logger.info(f"处理后的用户输入数据: {user_input_data}")
        
        # 3. 处理用户输入，标准化城市名称
        parsed_input = user_input_module.process_input(
            user_input_data['province'],
            user_input_data['city'],
            user_input_data['days'],
            user_input_data['preferences']
        )
        logger.info(f"解析后的用户输入: {parsed_input}")
        
        # 4. 获取目标城市的景点数据 - 增强版
        city_name = parsed_input.get('target_city', '') or user_input_data['city']
        if not city_name:
            raise ValueError("无法识别目标城市")
        
        logger.info(f"开始为城市 {city_name} 获取景点数据")
        
        # 获取景点数据 - 多级尝试策略
        spot_data = []
        
        # 策略1: 使用scenic_spot_module获取景点
        try:
            spot_data = scenic_spot_module.get_spots_by_city(city_name)
            logger.info(f"策略1: 从scenic_spot_module获取到{len(spot_data)}个景点")
        except Exception as e:
            logger.error(f"策略1失败: {e}")
        
        # 策略2: 如果景点不足，使用api_integration直接获取
        if len(spot_data) < 5:
            try:
                spot_data = api_integration.get_scenic_spots(city_name)
                logger.info(f"策略2: 从api_integration获取到{len(spot_data)}个景点")
            except Exception as e:
                logger.error(f"策略2失败: {e}")
        
        # 策略3: 如果仍不足，使用LLM获取景点信息
        if len(spot_data) < 5:
            try:
                logger.info(f"策略3: 使用LLM生成{city_name}的景点信息")
                # 直接调用LLM获取景点信息
                prompt = f"请列出{city_name}的主要旅游景点，至少15个。" \
                         f"每个景点需要包含：名称(name)、类型(type)、地址(address)、" \
                         f"经纬度(location格式为'经度,纬度')、评分(rating)、" \
                         f"推荐游玩时长(visit_duration)、简介(description)。" \
                         f"请以JSON数组格式返回，确保数据真实准确。"
                
                llm_response = api_integration.call_deepseek_api(prompt, max_tokens=2000)
                if llm_response and 'choices' in llm_response:
                    content = llm_response['choices'][0]['message']['content']
                    # 提取JSON部分
                    if '[' in content and ']' in content:
                        json_str = content[content.find('['):content.rfind(']')+1]
                        spot_data = json.loads(json_str)
                        logger.info(f"策略3成功: 获取到{len(spot_data)}个景点")
            except Exception as e:
                logger.error(f"策略3失败: {e}")
        
        # 策略4: 使用默认模拟数据
        if len(spot_data) < 5:
            spot_data = get_default_spot_data(city_name)
            logger.info(f"策略4: 使用默认模拟数据，获取到{len(spot_data)}个景点")
        
        # 验证景点数据质量
        if not spot_data:
            raise ValueError(f"无法获取{city_name}的景点数据，请尝试其他城市")
        
        # 5. 增强景点数据
        enhanced_spots = []
        for spot in spot_data:
            # 确保景点有必要的字段
            enhanced_spot = {
                'name': spot.get('name', '未知景点'),
                'type': spot.get('type', '景点'),
                'address': spot.get('address', ''),
                'location': spot.get('location', '116.397428,39.90923'),  # 默认北京坐标
                'rating': float(spot.get('rating', '4.0')),
                'visit_duration': spot.get('visit_duration', '约2小时'),
                'description': spot.get('description', '')
            }
            
            # 处理经纬度格式
            if isinstance(enhanced_spot['location'], dict):
                # 如果是字典格式，转换为字符串
                enhanced_spot['location'] = f"{enhanced_spot['location'].get('lng', 116.397428)},{enhanced_spot['location'].get('lat', 39.90923)}"
            
            enhanced_spots.append(enhanced_spot)
        
        spot_data = enhanced_spots
        logger.info(f"增强后景点数据: {len(spot_data)}个景点")
        
        # 6. 根据用户偏好筛选景点
        if preferences:
            filtered_spots = []
            for spot in spot_data:
                # 检查景点类型或名称是否匹配用户偏好
                spot_type = spot.get('type', '').lower()
                spot_name = spot.get('name', '').lower()
                
                # 检查是否匹配任一偏好
                matched = False
                for pref in preferences:
                    pref_lower = str(pref).lower()
                    if pref_lower in spot_type or pref_lower in spot_name:
                        matched = True
                        break
                
                if matched:
                    filtered_spots.append(spot)
            
            # 如果筛选后景点太少，保留原始列表
            if len(filtered_spots) >= 3:
                spot_data = filtered_spots
                logger.info(f"根据偏好筛选后景点数量: {len(spot_data)}")
            else:
                logger.info(f"偏好筛选结果太少，保留原始景点列表")
        
        # 7. 季节性优化
        # 使用optimize_for_season方法替代不存在的方法
        optimized_spots = seasonal_optimization_module.optimize_for_season(
            spot_data, 
            parsed_input.get('province'),
            city_name
        )
        logger.info(f"季节性优化后景点数量: {len(optimized_spots)}")
        
        # 获取季节性推荐信息
        seasonal_info = seasonal_optimization_module.get_seasonal_recommendations(
            spot_data,
            parsed_input.get('province'),
            city_name
        )
        logger.info(f"季节性优化信息: {seasonal_info}")
        
        # 8. 路线规划 - 增强版
        days = user_input_data['days']
        logger.info(f"开始规划{days}天的行程")
        
        # 使用RoutePlanningModule进行路线规划
        route_planner = route_planning_module.RoutePlanningModule() if hasattr(route_planning_module, 'RoutePlanningModule') else route_planning_module
        
        # 根据是否有足够景点选择不同的规划策略
        if hasattr(route_planner, 'plan_route'):
            daily_plans = route_planner.plan_route(optimized_spots, days, preferences)
        else:
            daily_plans = route_planning_module.generate_route(
                optimized_spots,
                days,
                preferences
            )
        
        logger.info(f"生成的每日行程数量: {len(daily_plans) if isinstance(daily_plans, list) else 0}")
        
        # 9. 获取天气信息
        weather_info = None
        try:
            weather_info = api_integration.get_weather(city_name)
            logger.info("成功获取天气信息")
        except Exception as e:
            logger.warning(f"获取天气信息失败: {e}")
        
        # 10. 生成详细行程信息
        daily_itineraries = []
        for daily_plan in daily_plans:
            if isinstance(daily_plan, dict) and 'day' in daily_plan:
                # 生成每日详细行程
                detailed_plan = itinerary_output_module.generate_daily_itinerary_details(
                    daily_plan, city_name, weather_info
                )
                daily_itineraries.append(detailed_plan)
        
        # 11. 生成行程概览
        total_spots = sum(len(plan.get('spots', [])) for plan in daily_plans)
        overall_summary = f"为您规划了{days}天的行程，共包含{total_spots}个景点。"
        
        if preferences:
            overall_summary += f" 根据您的偏好{'、'.join(preferences)}，我们为您精心挑选了最适合的景点。"
        
        # 12. 生成完整的行程数据
        itinerary_data = {
            'destination': city_name,
            'total_days': days,
            'generation_time': datetime.now().strftime('%Y-%m-%d %H:%M:%S'),
            'seasonal_info': seasonal_info,
            'overall_summary': overall_summary,
            'daily_itineraries': daily_itineraries,
            'statistics': {
                'total_spots': total_spots,
                'spots_per_day': total_spots / days if days > 0 else 0,
                'preferences': preferences
            }
        }
        
        # 13. 获取额外城市信息
        try:
            city_info = api_integration.get_city_info(city_name)
            itinerary_data['city_info'] = city_info
            logger.info("成功获取城市信息")
        except Exception as e:
            logger.warning(f"获取城市信息失败: {e}")
        
        # 14. 计算路线统计信息
        try:
            if hasattr(route_planner, 'calculate_route_statistics'):
                stats = route_planner.calculate_route_statistics(daily_plans)
                itinerary_data['route_statistics'] = stats
                logger.info("成功计算路线统计信息")
        except Exception as e:
            logger.warning(f"计算路线统计信息失败: {e}")
        
        # 使用模拟数据生成行程（在实际应用中，这里会调用真实的行程规划算法）
        # 确保城市名称有效，如果city_name为空，尝试使用省份名称
        destination_name = city_name
        if not destination_name and user_input_data.get('province'):
            destination_name = user_input_data['province']
        
        # 生成模拟行程数据
        itinerary_data = generate_mock_itinerary(destination_name, days, preferences)
        
        # 15. 存储到会话中供后续使用 - 添加Unicode解码处理
        session['latest_itinerary'] = ensure_chinese_display(itinerary_data)
        logger.info("行程数据已存储到会话并进行Unicode解码处理")
        
        # 16. 返回结果 - 添加Unicode解码处理
        # 确保所有数据中的中文都能正确显示，不会被转义
        result = {
            'success': True,
            'message': f"成功为{city_name}生成{days}天行程规划",
            'itinerary_data': ensure_chinese_display(itinerary_data),
            'input_data': ensure_chinese_display(user_input_data)
        }
        
        logger.info("行程规划完成，准备返回结果")
        # 使用我们的安全JSON序列化函数，确保中文正确显示
        return app.response_class(
            response=safe_json_dumps(result),
            status=200,
            mimetype='application/json'
        )
        
    except Exception as e:
        logger.error(f"行程规划错误: {str(e)}", exc_info=True)
        return jsonify({
            'success': False,
            'message': f"行程规划失败: {str(e)}"
        }), 500

@app.route('/result')
def show_result():
    """显示行程规划结果页面"""
    # 从会话中获取行程数据
    itinerary_data = session.get('latest_itinerary')
    
    if not itinerary_data:
        # 如果会话中没有数据，检查URL参数中是否有行程ID
        itinerary_id = request.args.get('id')
        if itinerary_id:
            # 这里可以实现从数据库加载行程的逻辑
            # 暂时返回一个空页面
            return render_template('itinerary_result.html', data={'success': False})
        else:
            # 重定向到首页
            return redirect(url_for('index'))
    
    # 确保数据中的中文都能正确显示
    decoded_data = ensure_chinese_display(itinerary_data)
    
    # 确保数据格式符合模板期望
    processed_data = {
        'success': True,
        'destination': decoded_data.get('destination', '未知目的地'),
        'total_days': decoded_data.get('total_days', 1),
        'preferences_str': ', '.join(decoded_data.get('preferences', [])),
        'generation_time': datetime.now().strftime('%Y-%m-%d %H:%M'),
        'itinerary': decoded_data.get('itinerary', {}),
        'stats': decoded_data.get('stats', {}),
        'seasonal_info': decoded_data.get('seasonal_info', {}),
        'amap_key': 'YOUR_AMAP_KEY'  # 这里应该从配置中获取
    }
    
    return render_template('itinerary_result.html', data=processed_data)

@app.route('/shared/<share_id>')
def shared_itinerary(share_id):
    """显示分享的行程"""
    # 在实际应用中，这里会从数据库或缓存中获取分享的行程
    # 暂时返回一个模拟的共享页面
    mock_data = {
        'success': True,
        'share_id': share_id,
        'message': '共享行程功能将在未来版本中完全实现'
    }
    return render_template('shared.html', data=mock_data)

@app.route('/api/city-suggestions')
def city_suggestions():
    """获取城市建议API"""
    try:
        query = request.args.get('q', '').strip()
        suggestions = user_input_module.get_city_suggestions(query)
        return jsonify({'suggestions': suggestions})
    except Exception as e:
        logger.error(f"城市建议API错误: {str(e)}")
        return jsonify({'suggestions': []})

@app.route('/api/map-data/<city>')
def get_map_data(city):
    """获取地图数据API"""
    try:
        spots = scenic_spot_module.get_spots_by_city(city)
        if len(spots) < 3:
            spots = get_default_spot_data(city)
        
        map_data = visualization_module.prepare_map_data(spots)
        return jsonify({'map_data': map_data})
    except Exception as e:
        logger.error(f"地图数据API错误: {str(e)}")
        return jsonify({'map_data': []})

@app.route('/about')
def about():
    """关于页面"""
    return render_template('about.html')

@app.route('/help')
def help_page():
    """帮助页面"""
    return render_template('help.html')

# 辅助函数
def get_default_spot_data(city_name):
    """获取默认模拟景点数据"""
    # 基于城市名称生成一些模拟景点
    mock_spots = []
    
    # 通用景点数据模板
    spot_templates = [
        {'type': '自然风景', 'tags': ['自然', '山水']},
        {'type': '历史古迹', 'tags': ['历史', '文化']},
        {'type': '博物馆', 'tags': ['文化', '教育']},
        {'type': '公园', 'tags': ['自然', '休闲']},
        {'type': '商业街', 'tags': ['购物', '美食']},
        {'type': '主题乐园', 'tags': ['娱乐', '亲子']},
        {'type': '宗教场所', 'tags': ['文化', '历史']},
        {'type': '城市地标', 'tags': ['观光', '摄影']}
    ]
    
    # 根据城市名称生成一些特定景点
    if '大理' in city_name or '大理市' in city_name:
        mock_spots = [
            {
                'name': '大理古城',
                'city': '大理市',
                'type': '历史古迹',
                'location': {'longitude': 100.2519, 'latitude': 25.6023},
                'opening_hours': '全天开放',
                'ticket_price': '免费',
                'best_season': '3-5月，10-11月',
                'duration_hours': 3,
                'description': '大理古城是中国历史文化名城，有着深厚的历史文化底蕴。',
                'tags': ['历史', '文化', '古城']
            },
            {
                'name': '洱海',
                'city': '大理市',
                'type': '自然风景',
                'location': {'longitude': 100.2656, 'latitude': 25.6474},
                'opening_hours': '全天开放',
                'ticket_price': '免费（景区内部分景点收费）',
                'best_season': '全年',
                'duration_hours': 4,
                'description': '洱海是云南省第二大淡水湖，风景如画，被誉为"高原明珠"。',
                'tags': ['自然', '湖泊', '摄影']
            },
            {
                'name': '崇圣寺三塔',
                'city': '大理市',
                'type': '宗教场所',
                'location': {'longitude': 100.2647, 'latitude': 25.6147},
                'opening_hours': '07:30-18:30',
                'ticket_price': '120元',
                'best_season': '3-5月，10-11月',
                'duration_hours': 2,
                'description': '崇圣寺三塔是大理的标志性建筑，是中国南方最古老的建筑之一。',
                'tags': ['历史', '宗教', '文化']
            },
            {
                'name': '双廊古镇',
                'city': '大理市',
                'type': '历史古迹',
                'location': {'longitude': 100.3914, 'latitude': 25.7056},
                'opening_hours': '全天开放',
                'ticket_price': '免费',
                'best_season': '3-5月，10-11月',
                'duration_hours': 3,
                'description': '双廊古镇位于洱海东北岸，是一个历史悠久的渔村古镇。',
                'tags': ['历史', '古镇', '摄影']
            },
            {
                'name': '喜洲古镇',
                'city': '大理市',
                'type': '历史古迹',
                'location': {'longitude': 100.1892, 'latitude': 25.6570},
                'opening_hours': '全天开放',
                'ticket_price': '免费',
                'best_season': '3-5月，10-11月',
                'duration_hours': 2,
                'description': '喜洲古镇是白族文化的重要传承地，保存有完好的白族建筑群。',
                'tags': ['历史', '民族文化', '古镇']
            },
            {
                'name': '苍山',
                'city': '大理市',
                'type': '自然风景',
                'location': {'longitude': 100.2271, 'latitude': 25.6612},
                'opening_hours': '08:00-18:00',
                'ticket_price': '30元（进山费）',
                'best_season': '5-10月',
                'duration_hours': 5,
                'description': '苍山是云岭山脉南端的主峰，与洱海共同构成大理的自然景观核心。',
                'tags': ['自然', '登山', '风景']
            },
            {
                'name': '南诏风情岛',
                'city': '大理市',
                'type': '自然风景',
                'location': {'longitude': 100.3734, 'latitude': 25.6911},
                'opening_hours': '08:30-17:00',
                'ticket_price': '50元',
                'best_season': '3-5月，10-11月',
                'duration_hours': 2,
                'description': '南诏风情岛位于洱海中，是一个融合了南诏文化与自然风光的景点。',
                'tags': ['自然', '文化', '岛屿']
            },
            {
                'name': '蝴蝶泉',
                'city': '大理市',
                'type': '自然风景',
                'location': {'longitude': 100.1852, 'latitude': 25.6761},
                'opening_hours': '08:00-17:00',
                'ticket_price': '60元',
                'best_season': '4-5月',
                'duration_hours': 1.5,
                'description': '蝴蝶泉因每年春天大量蝴蝶聚集而闻名，是大理著名的爱情主题景点。',
                'tags': ['自然', '浪漫', '科普']
            }
        ]
    elif '丽江' in city_name or '丽江市' in city_name:
        mock_spots = [
            {
                'name': '丽江古城',
                'city': '丽江市',
                'type': '历史古迹',
                'location': {'longitude': 100.2326, 'latitude': 26.8637},
                'opening_hours': '全天开放',
                'ticket_price': '80元（古城维护费）',
                'best_season': '4-5月，9-11月',
                'duration_hours': 4,
                'description': '丽江古城是世界文化遗产，保存有完好的纳西族传统建筑群。',
                'tags': ['历史', '文化', '古城']
            },
            {
                'name': '玉龙雪山',
                'city': '丽江市',
                'type': '自然风景',
                'location': {'longitude': 100.1452, 'latitude': 27.0557},
                'opening_hours': '07:30-18:00',
                'ticket_price': '105元（进山费）',
                'best_season': '11-4月（雪景最佳）',
                'duration_hours': 6,
                'description': '玉龙雪山是北半球最南端的雪山，以其壮丽的冰川景观著称。',
                'tags': ['自然', '雪山', '摄影']
            }
        ]
    elif '哈尔滨' in city_name or '哈尔滨市' in city_name:
        mock_spots = [
            {
                'name': '中央大街',
                'city': '哈尔滨市',
                'type': '商业街',
                'location': {'longitude': 126.6418, 'latitude': 45.7552},
                'opening_hours': '全天开放',
                'ticket_price': '免费',
                'best_season': '6-8月（夏季），12-2月（冰雪节）',
                'duration_hours': 3,
                'description': '哈尔滨最著名的商业街，融合了欧式建筑风格和各种美食。',
                'tags': ['商业', '美食', '建筑']
            },
            {
                'name': '圣索菲亚大教堂',
                'city': '哈尔滨市',
                'type': '宗教场所',
                'location': {'longitude': 126.6425, 'latitude': 45.7594},
                'opening_hours': '08:30-17:00',
                'ticket_price': '20元',
                'best_season': '全年',
                'duration_hours': 1.5,
                'description': '哈尔滨的标志性建筑，拜占庭式建筑风格，是拍照打卡的好地方。',
                'tags': ['历史', '宗教', '建筑']
            },
            {
                'name': '松花江',
                'city': '哈尔滨市',
                'type': '自然风景',
                'location': {'longitude': 126.6360, 'latitude': 45.7770},
                'opening_hours': '全天开放',
                'ticket_price': '免费',
                'best_season': '12-2月（冬季），6-8月（夏季）',
                'duration_hours': 4,
                'description': '哈尔滨的母亲河，冬季可体验冰雪运动，夏季可乘船游览。',
                'tags': ['自然', '运动', '旅游']
            },
            {
                'name': '太阳岛',
                'city': '哈尔滨市',
                'type': '公园',
                'location': {'longitude': 126.6178, 'latitude': 45.7812},
                'opening_hours': '08:00-17:00',
                'ticket_price': '30元',
                'best_season': '6-8月（夏季），12-2月（冰雪节）',
                'duration_hours': 3,
                'description': '哈尔滨著名的风景名胜区，冬季举办国际雪雕博览会。',
                'tags': ['自然', '摄影', '文化']
            },
            {
                'name': '亚布力滑雪旅游度假区',
                'city': '哈尔滨市',
                'type': '自然风景',
                'location': {'longitude': 128.7889, 'latitude': 44.6044},
                'opening_hours': '07:30-16:30',
                'ticket_price': '150-350元',
                'best_season': '12-3月（冬季）',
                'duration_hours': 6,
                'description': '中国最大的滑雪胜地，拥有世界级的滑雪设施和雪景。',
                'tags': ['运动', '滑雪', '自然']
            }
        ]
    elif '北京市' in city_name or '北京' in city_name:
        mock_spots = [
            {
                'name': '故宫博物院',
                'city': '北京市',
                'type': '博物馆',
                'location': {'longitude': 116.3970, 'latitude': 39.9176},
                'opening_hours': '08:30-17:00',
                'ticket_price': '60元',
                'best_season': '春秋两季',
                'duration_hours': 4,
                'description': '明清两代的皇家宫殿，中国古代宫廷建筑之精华。',
                'tags': ['历史', '文化', '建筑']
            },
            {
                'name': '天安门广场',
                'city': '北京市',
                'type': '城市地标',
                'location': {'longitude': 116.3917, 'latitude': 39.9062},
                'opening_hours': '全天开放',
                'ticket_price': '免费',
                'best_season': '全年',
                'duration_hours': 2,
                'description': '世界上最大的城市广场，中国的重要象征。',
                'tags': ['历史', '政治', '观光']
            },
            {
                'name': '长城',
                'city': '北京市',
                'type': '历史古迹',
                'location': {'longitude': 116.5704, 'latitude': 40.4319},
                'opening_hours': '07:30-18:00',
                'ticket_price': '45元',
                'best_season': '春秋两季',
                'duration_hours': 5,
                'description': '世界文化遗产，中国古代军事防御工程的伟大杰作。',
                'tags': ['历史', '军事', '自然']
            },
            {
                'name': '颐和园',
                'city': '北京市',
                'type': '公园',
                'location': {'longitude': 116.2755, 'latitude': 39.9998},
                'opening_hours': '06:30-18:00',
                'ticket_price': '30元',
                'best_season': '春秋两季',
                'duration_hours': 4,
                'description': '中国古典园林之首，慈禧太后的夏宫。',
                'tags': ['园林', '历史', '文化']
            },
            {
                'name': '天坛公园',
                'city': '北京市',
                'type': '公园',
                'location': {'longitude': 116.4074, 'latitude': 39.8824},
                'opening_hours': '06:00-22:00',
                'ticket_price': '15元',
                'best_season': '春秋两季',
                'duration_hours': 3,
                'description': '明清两朝皇帝祭天祈谷的圣地。',
                'tags': ['宗教', '历史', '文化']
            }
        ]
    elif '上海市' in city_name or '上海' in city_name:
        mock_spots = [
            {
                'name': '外滩',
                'city': '上海市',
                'type': '城市地标',
                'location': {'longitude': 121.4944, 'latitude': 31.2396},
                'opening_hours': '全天开放',
                'ticket_price': '免费',
                'best_season': '春秋两季',
                'duration_hours': 2,
                'description': '上海的标志性景观，万国建筑博览群。',
                'tags': ['建筑', '观光', '夜景']
            },
            {
                'name': '东方明珠塔',
                'city': '上海市',
                'type': '城市地标',
                'location': {'longitude': 121.4994, 'latitude': 31.2397},
                'opening_hours': '08:00-21:30',
                'ticket_price': '220元',
                'best_season': '全年',
                'duration_hours': 3,
                'description': '上海的象征，468米高的电视塔。',
                'tags': ['观景', '现代', '地标']
            },
            {
                'name': '豫园',
                'city': '上海市',
                'type': '公园',
                'location': {'longitude': 121.4920, 'latitude': 31.2257},
                'opening_hours': '08:30-17:30',
                'ticket_price': '40元',
                'best_season': '春秋两季',
                'duration_hours': 2,
                'description': '明代私人花园，上海古典园林的代表作。',
                'tags': ['园林', '历史', '文化']
            },
            {
                'name': '上海博物馆',
                'city': '上海市',
                'type': '博物馆',
                'location': {'longitude': 121.4760, 'latitude': 31.2275},
                'opening_hours': '09:00-17:00',
                'ticket_price': '免费',
                'best_season': '全年',
                'duration_hours': 3,
                'description': '中国古代艺术品的宝库，收藏了大量珍贵文物。',
                'tags': ['文物', '艺术', '教育']
            },
            {
                'name': '南京路步行街',
                'city': '上海市',
                'type': '商业街',
                'location': {'longitude': 121.4780, 'latitude': 31.2339},
                'opening_hours': '全天开放',
                'ticket_price': '免费',
                'best_season': '全年',
                'duration_hours': 2,
                'description': '中华商业第一街，购物天堂。',
                'tags': ['购物', '商业', '美食']
            }
        ]
    elif '成都市' in city_name or '成都' in city_name:
        mock_spots = [
            {
                'name': '大熊猫繁育研究基地',
                'city': '成都市',
                'type': '博物馆',
                'location': {'longitude': 104.1547, 'latitude': 30.7318},
                'opening_hours': '07:30-18:00',
                'ticket_price': '58元',
                'best_season': '全年',
                'duration_hours': 4,
                'description': '世界最大的大熊猫人工繁育基地。',
                'tags': ['动物', '亲子', '自然']
            },
            {
                'name': '宽窄巷子',
                'city': '成都市',
                'type': '商业街',
                'location': {'longitude': 104.0592, 'latitude': 30.6707},
                'opening_hours': '全天开放',
                'ticket_price': '免费',
                'best_season': '春秋两季',
                'duration_hours': 3,
                'description': '成都历史文化名片，川西民居建筑群。',
                'tags': ['文化', '美食', '历史']
            },
            {
                'name': '锦里古街',
                'city': '成都市',
                'type': '商业街',
                'location': {'longitude': 104.0430, 'latitude': 30.6474},
                'opening_hours': '全天开放',
                'ticket_price': '免费',
                'best_season': '春秋两季',
                'duration_hours': 3,
                'description': '三国文化主题街区，体验成都传统文化。',
                'tags': ['文化', '美食', '娱乐']
            },
            {
                'name': '武侯祠',
                'city': '成都市',
                'type': '历史古迹',
                'location': {'longitude': 104.0447, 'latitude': 30.6484},
                'opening_hours': '08:00-18:00',
                'ticket_price': '50元',
                'best_season': '春秋两季',
                'duration_hours': 2,
                'description': '纪念蜀汉丞相诸葛亮的祠庙。',
                'tags': ['历史', '文化', '宗教']
            },
            {
                'name': '都江堰',
                'city': '成都市',
                'type': '历史古迹',
                'location': {'longitude': 103.6403, 'latitude': 31.0207},
                'opening_hours': '08:00-18:00',
                'ticket_price': '90元',
                'best_season': '春秋两季',
                'duration_hours': 4,
                'description': '世界文化遗产，李冰父子修建的水利工程。',
                'tags': ['历史', '工程', '自然']
            }
        ]
    elif '广州市' in city_name or '广州' in city_name:
        mock_spots = [
            {
                'name': '广州塔',
                'city': '广州市',
                'type': '城市地标',
                'location': {'longitude': 113.3202, 'latitude': 23.1069},
                'opening_hours': '09:30-22:30',
                'ticket_price': '150元',
                'best_season': '全年',
                'duration_hours': 2,
                'description': '广州的新地标，俗称小蛮腰。',
                'tags': ['观景', '现代', '地标']
            },
            {
                'name': '长隆野生动物世界',
                'city': '广州市',
                'type': '主题乐园',
                'location': {'longitude': 113.3306, 'latitude': 23.0000},
                'opening_hours': '09:30-18:30',
                'ticket_price': '300元',
                'best_season': '春秋两季',
                'duration_hours': 6,
                'description': '中国最大的野生动物园。',
                'tags': ['动物', '亲子', '娱乐']
            },
            {
                'name': '陈家祠',
                'city': '广州市',
                'type': '历史古迹',
                'location': {'longitude': 113.2453, 'latitude': 23.1150},
                'opening_hours': '08:30-17:30',
                'ticket_price': '10元',
                'best_season': '春秋两季',
                'duration_hours': 2,
                'description': '广东现存规模最大的古建筑群。',
                'tags': ['建筑', '文化', '历史']
            },
            {
                'name': '沙面岛',
                'city': '广州市',
                'type': '公园',
                'location': {'longitude': 113.2457, 'latitude': 23.1084},
                'opening_hours': '全天开放',
                'ticket_price': '免费',
                'best_season': '春秋两季',
                'duration_hours': 2,
                'description': '珠江上的欧式建筑小岛。',
                'tags': ['建筑', '休闲', '摄影']
            },
            {
                'name': '上下九步行街',
                'city': '广州市',
                'type': '商业街',
                'location': {'longitude': 113.2553, 'latitude': 23.1180},
                'opening_hours': '全天开放',
                'ticket_price': '免费',
                'best_season': '全年',
                'duration_hours': 3,
                'description': '广州最繁华的商业步行街。',
                'tags': ['购物', '美食', '商业']
            }
        ]
    elif '深圳市' in city_name or '深圳' in city_name:
        mock_spots = [
            {
                'name': '深圳湾公园',
                'city': '深圳市',
                'type': '公园',
                'location': {'longitude': 114.0307, 'latitude': 22.4780},
                'opening_hours': '全天开放',
                'ticket_price': '免费',
                'best_season': '春秋两季',
                'duration_hours': 3,
                'description': '深圳最著名的海滨公园，看日落的绝佳地点。',
                'tags': ['自然', '摄影', '休闲']
            },
            {
                'name': '欢乐谷',
                'city': '深圳市',
                'type': '主题乐园',
                'location': {'longitude': 114.0674, 'latitude': 22.5398},
                'opening_hours': '09:30-21:30',
                'ticket_price': '230元',
                'best_season': '全年',
                'duration_hours': 6,
                'description': '中国著名的现代化主题乐园。',
                'tags': ['娱乐', '刺激', '亲子']
            },
            {
                'name': '世界之窗',
                'city': '深圳市',
                'type': '主题乐园',
                'location': {'longitude': 113.9785, 'latitude': 22.5398},
                'opening_hours': '09:00-22:30',
                'ticket_price': '220元',
                'best_season': '全年',
                'duration_hours': 4,
                'description': '微缩景观主题公园，展示世界名胜古迹。',
                'tags': ['文化', '教育', '观光']
            },
            {
                'name': '大梅沙海滨公园',
                'city': '深圳市',
                'type': '自然风景',
                'location': {'longitude': 114.3250, 'latitude': 22.5960},
                'opening_hours': '06:00-23:00',
                'ticket_price': '免费',
                'best_season': '夏季',
                'duration_hours': 4,
                'description': '深圳最著名的海滨浴场。',
                'tags': ['海滨', '度假', '游泳']
            },
            {
                'name': '东部华侨城',
                'city': '深圳市',
                'type': '主题乐园',
                'location': {'longitude': 114.2678, 'latitude': 22.6072},
                'opening_hours': '09:30-20:00',
                'ticket_price': '200元',
                'best_season': '春秋两季',
                'duration_hours': 5,
                'description': '集休闲娱乐旅游于一体的大型度假区。',
                'tags': ['度假', '娱乐', '文化']
            }
        ]
    elif '西安市' in city_name or '西安' in city_name:
        mock_spots = [
            {
                'name': '兵马俑',
                'city': '西安市',
                'type': '历史古迹',
                'location': {'longitude': 109.2766, 'latitude': 34.3849},
                'opening_hours': '08:30-17:30',
                'ticket_price': '120元',
                'best_season': '春秋两季',
                'duration_hours': 3,
                'description': '世界第八大奇迹，秦始皇陵的陪葬坑。',
                'tags': ['历史', '文化', '考古']
            },
            {
                'name': '大雁塔',
                'city': '西安市',
                'type': '历史古迹',
                'location': {'longitude': 108.9648, 'latitude': 34.2186},
                'opening_hours': '08:30-19:00',
                'ticket_price': '50元',
                'best_season': '春秋两季',
                'duration_hours': 2,
                'description': '唐代的佛教建筑，保存有佛经和佛像。',
                'tags': ['佛教', '建筑', '文化']
            },
            {
                'name': '华清池',
                'city': '西安市',
                'type': '自然风景',
                'location': {'longitude': 109.2124, 'latitude': 34.3620},
                'opening_hours': '07:00-19:00',
                'ticket_price': '150元',
                'best_season': '春秋两季',
                'duration_hours': 3,
                'description': '唐代温泉度假胜地，贵妃洗澡的地方。',
                'tags': ['温泉', '历史', '文化']
            },
            {
                'name': '西安城墙',
                'city': '西安市',
                'type': '历史古迹',
                'location': {'longitude': 108.9520, 'latitude': 34.2658},
                'opening_hours': '08:30-22:00',
                'ticket_price': '54元',
                'best_season': '春秋两季',
                'duration_hours': 3,
                'description': '中国现存最完整的古代城垣建筑。',
                'tags': ['建筑', '历史', '观光']
            },
            {
                'name': '钟楼',
                'city': '西安市',
                'type': '城市地标',
                'location': {'longitude': 108.9489, 'latitude': 34.2616},
                'opening_hours': '08:30-22:00',
                'ticket_price': '30元',
                'best_season': '全年',
                'duration_hours': 1,
                'description': '西安市中心的地标建筑，中国现存最大的钟楼。',
                 'tags': ['建筑', '历史', '地标']
             }
         ]
    elif '重庆市' in city_name or '重庆' in city_name:
         mock_spots = [
             {
                 'name': '解放碑',
                 'city': '重庆市',
                 'type': '城市地标',
                 'location': {'longitude': 106.5806, 'latitude': 29.5587},
                 'opening_hours': '全天开放',
                 'ticket_price': '免费',
                 'best_season': '春秋两季',
                 'duration_hours': 2,
                 'description': '重庆的标志性建筑和商业中心。',
                 'tags': ['商业', '购物', '地标']
             },
             {
                 'name': '洪崖洞',
                 'city': '重庆市',
                 'type': '商业街',
                 'location': {'longitude': 106.5825, 'latitude': 29.5638},
                 'opening_hours': '全天开放',
                 'ticket_price': '免费',
                 'best_season': '全年',
                 'duration_hours': 3,
                 'description': '重庆最具巴渝传统建筑特色的吊脚楼群。',
                 'tags': ['文化', '建筑', '美食']
             },
             {
                 'name': '磁器口古镇',
                 'city': '重庆市',
                 'type': '历史古迹',
                 'location': {'longitude': 106.4597, 'latitude': 29.5480},
                 'opening_hours': '08:30-20:00',
                 'ticket_price': '免费',
                 'best_season': '春秋两季',
                 'duration_hours': 3,
                 'description': '嘉陵江畔的千年古镇，保存完好的古建筑群。',
                 'tags': ['历史', '文化', '古镇']
             },
             {
                 'name': '长江索道',
                 'city': '重庆市',
                 'type': '交通景观',
                 'location': {'longitude': 106.6066, 'latitude': 29.5592},
                 'opening_hours': '07:30-22:30',
                 'ticket_price': '20元',
                 'best_season': '春秋两季',
                 'duration_hours': 0.5,
                 'description': '重庆的空中交通工具，欣赏江景的绝佳方式。',
                 'tags': ['观光', '交通', '江景']
             },
             {
                 'name': '南山一棵树观景台',
                 'city': '重庆市',
                 'type': '观景台',
                 'location': {'longitude': 106.6329, 'latitude': 29.5448},
                 'opening_hours': '09:00-22:00',
                 'ticket_price': '30元',
                 'best_season': '全年',
                 'duration_hours': 1.5,
                 'description': '重庆夜景观赏的最佳地点，俯瞰山城全景。',
                 'tags': ['夜景', '观景', '摄影']
             }
         ]
    elif '天津市' in city_name or '天津' in city_name:
        mock_spots = [
            {
                'name': '天津之眼',
                'city': '天津市',
                'type': '城市地标',
                'location': {'longitude': 117.1967, 'latitude': 39.1230},
                'opening_hours': '09:30-21:30',
                'ticket_price': '70元',
                'best_season': '春秋两季',
                'duration_hours': 1,
                'description': '世界唯一的桥上摩天轮，天津的地标建筑。',
                'tags': ['观景', '地标', '现代']
            },
            {
                'name': '五大道',
                'city': '天津市',
                'type': '历史街区',
                'location': {'longitude': 117.1967, 'latitude': 39.1153},
                'opening_hours': '全天开放',
                'ticket_price': '免费',
                'best_season': '春秋两季',
                'duration_hours': 3,
                'description': '万国建筑博览区，中国保留最完整的洋楼建筑群。',
                'tags': ['建筑', '历史', '文化']
            },
            {
                'name': '意式风情区',
                'city': '天津市',
                'type': '商业街',
                'location': {'longitude': 117.1994, 'latitude': 39.1506},
                'opening_hours': '全天开放',
                'ticket_price': '免费',
                'best_season': '全年',
                'duration_hours': 2,
                'description': '保存完整的意大利风貌建筑群，充满异域风情。',
                'tags': ['异域', '建筑', '购物']
            },
            {
                'name': '古文化街',
                'city': '天津市',
                'type': '商业街',
                'location': {'longitude': 117.1937, 'latitude': 39.1510},
                'opening_hours': '08:30-18:00',
                'ticket_price': '免费',
                'best_season': '春秋两季',
                'duration_hours': 2,
                'description': '天津文化的发源地，传统手工艺品和美食的集中地。',
                'tags': ['文化', '传统', '美食']
            },
            {
                'name': '瓷房子',
                'city': '天津市',
                'type': '博物馆',
                'location': {'longitude': 117.2024, 'latitude': 39.1130},
                'opening_hours': '09:00-19:00',
                'ticket_price': '50元',
                'best_season': '全年',
                'duration_hours': 1.5,
                'description': '用瓷器装饰的艺术建筑，独特而奢华。',
                'tags': ['艺术', '独特', '文化']
            }
        ]
    elif '南京市' in city_name or '南京' in city_name:
        mock_spots = [
            {
                'name': '中山陵',
                'city': '南京市',
                'type': '历史古迹',
                'location': {'longitude': 118.8484, 'latitude': 32.0658},
                'opening_hours': '08:30-17:00',
                'ticket_price': '免费',
                'best_season': '春秋两季',
                'duration_hours': 2,
                'description': '孙中山先生的陵墓，中国近代建筑的杰作。',
                'tags': ['历史', '纪念', '建筑']
            },
            {
                'name': '夫子庙',
                'city': '南京市',
                'type': '历史古迹',
                'location': {'longitude': 118.7952, 'latitude': 32.0210},
                'opening_hours': '09:00-21:00',
                'ticket_price': '70元',
                'best_season': '春秋两季',
                'duration_hours': 3,
                'description': '秦淮河畔的古代文化教育中心，南京的文化象征。',
                'tags': ['文化', '历史', '传统']
            },
            {
                'name': '明孝陵',
                'city': '南京市',
                'type': '历史古迹',
                'location': {'longitude': 118.8376, 'latitude': 32.0609},
                'opening_hours': '06:30-18:30',
                'ticket_price': '70元',
                'best_season': '春秋两季',
                'duration_hours': 2,
                'description': '明代开国皇帝朱元璋的陵墓，明清皇家陵寝的代表。',
                'tags': ['历史', '皇家', '文化']
            },
            {
                'name': '秦淮河',
                'city': '南京市',
                'type': '自然风景',
                'location': {'longitude': 118.7900, 'latitude': 32.0200},
                'opening_hours': '全天开放',
                'ticket_price': '免费',
                'best_season': '春秋两季',
                'duration_hours': 2,
                'description': '南京的母亲河，夜晚灯火辉煌的秦淮夜景。',
                'tags': ['自然', '夜景', '文化']
            },
            {
                'name': '南京长江大桥',
                'city': '南京市',
                'type': '城市地标',
                'location': {'longitude': 118.7390, 'latitude': 32.0970},
                'opening_hours': '全天开放',
                'ticket_price': '免费',
                'best_season': '全年',
                'duration_hours': 1,
                'description': '中国第一座自主设计建造的双层铁路公路两用桥。',
                'tags': ['工程', '历史', '地标']
            }
        ]
    elif '杭州市' in city_name or '杭州' in city_name:
        mock_spots = [
             {
                 'name': '西湖',
                 'city': '杭州市',
                 'type': '自然风景',
                 'location': {'longitude': 120.1551, 'latitude': 30.2741},
                 'opening_hours': '全天开放',
                 'ticket_price': '免费',
                 'best_season': '春秋两季',
                 'duration_hours': 4,
                 'description': '世界文化遗产，中国最著名的湖泊之一。',
                 'tags': ['自然', '文化', '浪漫']
             },
             {
                 'name': '灵隐寺',
                 'city': '杭州市',
                 'type': '宗教场所',
                 'location': {'longitude': 120.1017, 'latitude': 30.2388},
                 'opening_hours': '07:00-18:00',
                 'ticket_price': '45元',
                 'best_season': '春秋两季',
                 'duration_hours': 3,
                 'description': '中国佛教著名寺院，有1600多年的历史。',
                 'tags': ['佛教', '历史', '文化']
             },
             {
                 'name': '雷峰塔',
                 'city': '杭州市',
                 'type': '历史古迹',
                 'location': {'longitude': 120.1517, 'latitude': 30.2317},
                 'opening_hours': '08:00-20:30',
                 'ticket_price': '40元',
                 'best_season': '春秋两季',
                 'duration_hours': 1.5,
                 'description': '因白娘子传说而闻名的古塔。',
                 'tags': ['传说', '历史', '文化']
             },
             {
                 'name': '千岛湖',
                 'city': '杭州市',
                 'type': '自然风景',
                 'location': {'longitude': 119.0313, 'latitude': 29.6084},
                 'opening_hours': '全天开放',
                 'ticket_price': '150元',
                 'best_season': '春秋两季',
                 'duration_hours': 6,
                 'description': '中国最大的人工湖，1078个岛屿各具特色。',
                 'tags': ['自然', '湖泊', '度假']
             },
             {
                 'name': '宋城',
                 'city': '杭州市',
                 'type': '主题乐园',
                 'location': {'longitude': 120.1030, 'latitude': 30.1796},
                 'opening_hours': '09:00-21:00',
                 'ticket_price': '350元',
                 'best_season': '全年',
                 'duration_hours': 4,
                 'description': '再现宋代文化的主题公园，《宋城千古情》表演著名。',
                 'tags': ['文化', '表演', '娱乐']
             }
         ]
    elif '武汉市' in city_name or '武汉' in city_name:
         mock_spots = [
             {
                 'name': '黄鹤楼',
                 'city': '武汉市',
                 'type': '历史古迹',
                 'location': {'longitude': 114.3054, 'latitude': 30.5531},
                 'opening_hours': '08:00-18:00',
                 'ticket_price': '70元',
                 'best_season': '春秋两季',
                 'duration_hours': 2,
                 'description': '江南三大名楼之一，武汉的标志性建筑。',
                 'tags': ['历史', '文化', '地标']
             },
             {
                 'name': '东湖',
                 'city': '武汉市',
                 'type': '自然风景',
                 'location': {'longitude': 114.4158, 'latitude': 30.5832},
                 'opening_hours': '06:00-22:00',
                 'ticket_price': '免费',
                 'best_season': '春秋两季',
                 'duration_hours': 3,
                 'description': '中国最大的城中湖，风景秀丽。',
                 'tags': ['自然', '湖泊', '休闲']
             },
             {
                 'name': '户部巷',
                 'city': '武汉市',
                 'type': '美食街',
                 'location': {'longitude': 114.2896, 'latitude': 30.5550},
                 'opening_hours': '全天开放',
                 'ticket_price': '免费',
                 'best_season': '全年',
                 'duration_hours': 2,
                 'description': '武汉著名的美食街，"汉味早点第一巷"。',
                 'tags': ['美食', '小吃', '文化']
             },
             {
                 'name': '武汉大学',
                 'city': '武汉市',
                 'type': '校园景点',
                 'location': {'longitude': 114.3663, 'latitude': 30.5432},
                 'opening_hours': '08:30-17:30',
                 'ticket_price': '免费',
                 'best_season': '春季',
                 'duration_hours': 2,
                 'description': '中国最美大学之一，以樱花闻名。',
                 'tags': ['教育', '樱花', '建筑']
             },
             {
                 'name': '长江大桥',
                 'city': '武汉市',
                 'type': '城市地标',
                 'location': {'longitude': 114.2896, 'latitude': 30.5387},
                 'opening_hours': '全天开放',
                 'ticket_price': '免费',
                 'best_season': '全年',
                 'duration_hours': 1,
                 'description': '万里长江上的第一座大桥，武汉的象征。',
                 'tags': ['工程', '历史', '地标']
             }
         ]
    elif '昆明市' in city_name or '昆明' in city_name:
         mock_spots = [
             {
                 'name': '滇池',
                 'city': '昆明市',
                 'type': '自然风景',
                 'location': {'longitude': 102.6486, 'latitude': 24.9917},
                 'opening_hours': '全天开放',
                 'ticket_price': '免费',
                 'best_season': '春秋两季',
                 'duration_hours': 3,
                 'description': '云南最大的淡水湖，被誉为"高原明珠"。',
                 'tags': ['自然', '湖泊', '观鸟']
             },
             {
                 'name': '石林',
                 'city': '昆明市',
                 'type': '自然风景',
                 'location': {'longitude': 103.2696, 'latitude': 24.8167},
                 'opening_hours': '07:00-19:00',
                 'ticket_price': '175元',
                 'best_season': '春秋两季',
                 'duration_hours': 4,
                 'description': '世界自然遗产，典型的喀斯特地貌奇观。',
                 'tags': ['自然', '地质', '奇观']
             },
             {
                 'name': '翠湖',
                 'city': '昆明市',
                 'type': '公园',
                 'location': {'longitude': 102.6947, 'latitude': 25.0413},
                 'opening_hours': '全天开放',
                 'ticket_price': '免费',
                 'best_season': '冬季',
                 'duration_hours': 2,
                 'description': '昆明市中心的人工湖，每年冬季有大量红嘴鸥栖息。',
                 'tags': ['湖泊', '红嘴鸥', '城市']
             },
             {
                 'name': '西山森林公园',
                 'city': '昆明市',
                 'type': '自然风景',
                 'location': {'longitude': 102.6264, 'latitude': 24.9658},
                 'opening_hours': '08:00-18:00',
                 'ticket_price': '30元',
                 'best_season': '春秋两季',
                 'duration_hours': 3,
                 'description': '昆明市郊的天然氧吧，可俯瞰滇池全景。',
                 'tags': ['自然', '登山', '观景']
             },
             {
                 'name': '九乡',
                 'city': '昆明市',
                 'type': '自然风景',
                 'location': {'longitude': 103.2337, 'latitude': 25.0667},
                 'opening_hours': '08:30-18:00',
                 'ticket_price': '120元',
                 'best_season': '春秋两季',
                 'duration_hours': 4,
                 'description': '喀斯特溶洞景观，有"溶洞博物馆"之称。',
                 'tags': ['溶洞', '地质', '自然']
             }
         ]
    elif '石家庄市' in city_name or '石家庄' in city_name:
         mock_spots = [
             {
                 'name': '正定古城',
                 'city': '石家庄市',
                 'type': '历史古迹',
                 'location': {'longitude': 114.5814, 'latitude': 38.1468},
                 'opening_hours': '08:30-17:30',
                 'ticket_price': '60元',
                 'best_season': '春秋两季',
                 'duration_hours': 4,
                 'description': '1600多年历史的古城，保存有大量古建筑。',
                 'tags': ['古城', '历史', '文化']
             },
             {
                 'name': '赵州桥',
                 'city': '石家庄市',
                 'type': '历史古迹',
                 'location': {'longitude': 114.7742, 'latitude': 37.7936},
                 'opening_hours': '08:00-17:30',
                 'ticket_price': '40元',
                 'best_season': '春秋两季',
                 'duration_hours': 2,
                 'description': '世界现存最早的石拱桥，有1400多年历史。',
                 'tags': ['工程', '历史', '建筑']
             },
             {
                 'name': '抱犊寨',
                 'city': '石家庄市',
                 'type': '自然风景',
                 'location': {'longitude': 114.3864, 'latitude': 38.0289},
                 'opening_hours': '08:00-18:00',
                 'ticket_price': '50元',
                 'best_season': '春秋两季',
                 'duration_hours': 3,
                 'description': '石家庄周边最高峰，有"天下奇寨"之称。',
                 'tags': ['山峰', '自然', '登山']
             }
         ]
    elif '太原市' in city_name or '太原' in city_name:
         mock_spots = [
             {
                 'name': '晋祠',
                 'city': '太原市',
                 'type': '历史古迹',
                 'location': {'longitude': 112.5631, 'latitude': 37.7097},
                 'opening_hours': '08:30-17:00',
                 'ticket_price': '80元',
                 'best_season': '春秋两季',
                 'duration_hours': 3,
                 'description': '山西现存最早的古典祠堂建筑群。',
                 'tags': ['祠堂', '历史', '文化']
             },
             {
                 'name': '双塔寺',
                 'city': '太原市',
                 'type': '历史古迹',
                 'location': {'longitude': 112.5628, 'latitude': 37.8831},
                 'opening_hours': '08:30-17:00',
                 'ticket_price': '30元',
                 'best_season': '春秋两季',
                 'duration_hours': 2,
                 'description': '太原的地标建筑，明代砖塔。',
                 'tags': ['塔', '地标', '历史']
             }
         ]
    elif '呼和浩特市' in city_name or '呼和浩特' in city_name:
         mock_spots = [
             {
                 'name': '大召寺',
                 'city': '呼和浩特市',
                 'type': '宗教场所',
                 'location': {'longitude': 111.7503, 'latitude': 40.8425},
                 'opening_hours': '08:30-18:00',
                 'ticket_price': '35元',
                 'best_season': '夏季',
                 'duration_hours': 2,
                 'description': '呼和浩特最大的黄教寺庙。',
                 'tags': ['藏传佛教', '寺庙', '文化']
             },
             {
                 'name': '内蒙古博物院',
                 'city': '呼和浩特市',
                 'type': '博物馆',
                 'location': {'longitude': 111.7593, 'latitude': 40.8391},
                 'opening_hours': '09:00-17:00',
                 'ticket_price': '免费',
                 'best_season': '全年',
                 'duration_hours': 3,
                 'description': '了解蒙古文化的最佳场所。',
                 'tags': ['文化', '历史', '民族']
             }
         ]
    elif '银川市' in city_name or '银川' in city_name:
         mock_spots = [
             {
                 'name': '镇北堡西部影视城',
                 'city': '银川市',
                 'type': '主题乐园',
                 'location': {'longitude': 106.1022, 'latitude': 38.7344},
                 'opening_hours': '08:00-19:00',
                 'ticket_price': '120元',
                 'best_season': '春秋两季',
                 'duration_hours': 4,
                 'description': '《大话西游》等著名电影的拍摄地。',
                 'tags': ['电影', '文化', '娱乐']
             },
             {
                 'name': '西夏王陵',
                 'city': '银川市',
                 'type': '历史古迹',
                 'location': {'longitude': 106.1022, 'latitude': 38.6939},
                 'opening_hours': '08:00-18:00',
                 'ticket_price': '75元',
                 'best_season': '春秋两季',
                 'duration_hours': 3,
                 'description': '西夏王朝的皇家陵园，"东方金字塔"。',
                 'tags': ['陵墓', '历史', '考古']
             }
         ]
    elif '乌鲁木齐市' in city_name or '乌鲁木齐' in city_name:
         mock_spots = [
             {
                 'name': '天山',
                 'city': '乌鲁木齐市',
                 'type': '自然风景',
                 'location': {'longitude': 88.0689, 'latitude': 43.8145},
                 'opening_hours': '08:00-20:00',
                 'ticket_price': '185元',
                 'best_season': '夏季',
                 'duration_hours': 6,
                 'description': '天池美景，雪山湖泊交相辉映。',
                 'tags': ['天山', '天池', '自然']
             },
             {
                 'name': '红山',
                 'city': '乌鲁木齐市',
                 'type': '公园',
                 'location': {'longitude': 87.6163, 'latitude': 43.8256},
                 'opening_hours': '08:00-22:00',
                 'ticket_price': '40元',
                 'best_season': '春秋两季',
                 'duration_hours': 2,
                 'description': '乌鲁木齐的象征，可俯瞰全城。',
                 'tags': ['观景', '地标', '城市']
             }
         ]
    elif '拉萨市' in city_name or '拉萨' in city_name:
         mock_spots = [
             {
                 'name': '布达拉宫',
                 'city': '拉萨市',
                 'type': '历史古迹',
                 'location': {'longitude': 91.1409, 'latitude': 29.6456},
                 'opening_hours': '09:00-16:00',
                 'ticket_price': '200元',
                 'best_season': '5-10月',
                 'duration_hours': 3,
                 'description': '世界文化遗产，西藏的象征。',
                 'tags': ['宫殿', '藏传佛教', '世界遗产']
             },
             {
                 'name': '大昭寺',
                 'city': '拉萨市',
                 'type': '宗教场所',
                 'location': {'longitude': 91.1171, 'latitude': 29.6516},
                 'opening_hours': '09:00-18:30',
                 'ticket_price': '85元',
                 'best_season': '5-10月',
                 'duration_hours': 2,
                 'description': '藏传佛教的圣地，佛教徒朝拜的终点。',
                 'tags': ['佛教', '圣地', '文化']
             }
         ]
    elif '兰州市' in city_name or '兰州' in city_name:
         mock_spots = [
             {
                 'name': '中山桥',
                 'city': '兰州市',
                 'type': '城市地标',
                 'location': {'longitude': 103.8236, 'latitude': 36.0581},
                 'opening_hours': '全天开放',
                 'ticket_price': '免费',
                 'best_season': '全年',
                 'duration_hours': 1,
                 'description': '黄河上的第一座桥，兰州的标志。',
                 'tags': ['黄河', '桥梁', '地标']
             },
             {
                 'name': '白塔山公园',
                 'city': '兰州市',
                 'type': '公园',
                 'location': {'longitude': 103.8269, 'latitude': 36.0650},
                 'opening_hours': '06:00-20:00',
                 'ticket_price': '免费',
                 'best_season': '春秋两季',
                 'duration_hours': 3,
                 'description': '兰州的制高点，可俯瞰黄河两岸。',
                 'tags': ['观景', '登山', '黄河']
             }
         ]
    elif '西宁市' in city_name or '西宁' in city_name:
         mock_spots = [
             {
                 'name': '塔尔寺',
                 'city': '西宁市',
                 'type': '宗教场所',
                 'location': {'longitude': 101.5778, 'latitude': 36.6189},
                 'opening_hours': '08:00-18:00',
                 'ticket_price': '70元',
                 'best_season': '夏季',
                 'duration_hours': 3,
                 'description': '藏传佛教格鲁派六大寺院之一。',
                 'tags': ['藏传佛教', '寺院', '文化']
             },
             {
                 'name': '青海湖',
                 'city': '西宁市',
                 'type': '自然风景',
                 'location': {'longitude': 99.9372, 'latitude': 36.9569},
                 'opening_hours': '全天开放',
                 'ticket_price': '90元',
                 'best_season': '7-8月',
                 'duration_hours': 6,
                 'description': '中国最大的内陆湖，碧波万顷。',
                 'tags': ['湖泊', '自然', '观鸟']
             }
         ]
    elif '贵阳市' in city_name or '贵阳' in city_name:
         mock_spots = [
             {
                 'name': '黄果树瀑布',
                 'city': '贵阳市',
                 'type': '自然风景',
                 'location': {'longitude': 105.6694, 'latitude': 25.9726},
                 'opening_hours': '07:30-18:00',
                 'ticket_price': '180元',
                 'best_season': '夏季',
                 'duration_hours': 4,
                 'description': '中国最大的瀑布，气势磅礴。',
                 'tags': ['瀑布', '自然', '奇观']
             },
             {
                 'name': '青岩古镇',
                 'city': '贵阳市',
                 'type': '历史古迹',
                 'location': {'longitude': 106.7805, 'latitude': 26.5532},
                 'opening_hours': '08:30-18:00',
                 'ticket_price': '80元',
                 'best_season': '春秋两季',
                 'duration_hours': 3,
                 'description': '贵州四大古镇之一，明清古建筑群。',
                 'tags': ['古镇', '历史', '文化']
             }
         ]
    elif '海口市' in city_name or '海口' in city_name:
         mock_spots = [
             {
                 'name': '假日海滩',
                 'city': '海口市',
                 'type': '自然风景',
                 'location': {'longitude': 110.1997, 'latitude': 20.0311},
                 'opening_hours': '全天开放',
                 'ticket_price': '免费',
                 'best_season': '全年',
                 'duration_hours': 4,
                 'description': '海口最著名的海滨浴场。',
                 'tags': ['海滨', '度假', '游泳']
             },
             {
                 'name': '骑楼老街',
                 'city': '海口市',
                 'type': '历史街区',
                 'location': {'longitude': 110.3312, 'latitude': 20.0311},
                 'opening_hours': '全天开放',
                 'ticket_price': '免费',
                 'best_season': '全年',
                 'duration_hours': 2,
                 'description': '南洋风格的骑楼建筑群。',
                 'tags': ['建筑', '历史', '文化']
             }
         ]
    elif '三亚市' in city_name or '三亚' in city_name:
         mock_spots = [
             {
                 'name': '亚龙湾',
                 'city': '三亚市',
                 'type': '自然风景',
                 'location': {'longitude': 109.6419, 'latitude': 18.2365},
                 'opening_hours': '全天开放',
                 'ticket_price': '免费',
                 'best_season': '全年',
                 'duration_hours': 6,
                 'description': '天下第一湾，水清沙白。',
                 'tags': ['海滨', '度假', '潜水']
             },
             {
                 'name': '天涯海角',
                 'city': '三亚市',
                 'type': '自然风景',
                 'location': {'longitude': 109.5083, 'latitude': 18.3092},
                 'opening_hours': '08:00-19:00',
                 'ticket_price': '95元',
                 'best_season': '全年',
                 'duration_hours': 3,
                 'description': '海南最著名的景区，"天涯海角"石刻。',
                 'tags': ['石刻', '海岸', '文化']
             },
             {
                 'name': '南山文化旅游区',
                 'city': '三亚市',
                 'type': '宗教场所',
                 'location': {'longitude': 109.2044, 'latitude': 18.2950},
                 'opening_hours': '08:30-17:30',
                 'ticket_price': '150元',
                 'best_season': '全年',
                 'duration_hours': 4,
                 'description': '108米高的南山海上观音。',
                 'tags': ['佛教', '观音', '文化']
             }
         ]
    else:
        # 为其他城市生成一些通用景点
        base_longitude, base_latitude = get_default_city_coordinates(city_name)
        
        for i in range(8):
            spot_type_data = random.choice(spot_templates)
            mock_spot = {
                'name': f'{city_name}{spot_type_data["type"]}{i+1}',
                'city': city_name,
                'type': spot_type_data['type'],
                'location': {
                    'longitude': round(base_longitude + random.uniform(-0.1, 0.1), 4),
                    'latitude': round(base_latitude + random.uniform(-0.1, 0.1), 4)
                },
                'opening_hours': '09:00-17:00',
                'ticket_price': f'{random.randint(0, 200)}元',
                'best_season': random.choice(['春季', '夏季', '秋季', '冬季', '全年']),
                'duration_hours': random.randint(1, 4),
                'description': f'{city_name}著名的{spot_type_data["type"]}，深受游客喜爱。',
                'tags': spot_type_data['tags']
            }
            mock_spots.append(mock_spot)
    
    return mock_spots

def get_default_city_coordinates(city_name):
    """获取默认城市坐标"""
    # 中国省会城市和主要旅游城市坐标数据库
    city_coordinates = {
        # 直辖市
        '北京市': (116.3974, 39.9093),
        '上海市': (121.4737, 31.2304),
        '天津市': (117.2010, 39.0842),
        '重庆市': (106.5349, 29.5630),
        
        # 省会城市
        '石家庄市': (114.5149, 38.0423),  # 河北
        '太原市': (112.5573, 37.8706),    # 山西
        '呼和浩特市': (111.7519, 40.8414), # 内蒙古
        '沈阳市': (123.4332, 41.8043),    # 辽宁
        '长春市': (125.3245, 43.8868),    # 吉林
        '哈尔滨市': (126.6424, 45.7567),  # 黑龙江
        '南京市': (118.7674, 32.0415),    # 江苏
        '杭州市': (120.1551, 30.2741),    # 浙江
        '合肥市': (117.2272, 31.8206),    # 安徽
        '福州市': (119.3062, 26.0745),    # 福建
        '南昌市': (115.8642, 28.6894),    # 江西
        '济南市': (117.0009, 36.6758),    # 山东
        '郑州市': (113.6253, 34.7466),    # 河南
        '武汉市': (114.3055, 30.5928),    # 湖北
        '长沙市': (112.9822, 28.1941),    # 湖南
        '广州市': (113.2644, 23.1291),    # 广东
        '南宁市': (108.3200, 22.8170),    # 广西
        '海口市': (110.3312, 20.0319),    # 海南
        '成都市': (104.0665, 30.5728),    # 四川
        '贵阳市': (106.7097, 26.5994),    # 贵州
        '昆明市': (102.7122, 25.0453),    # 云南
        '拉萨市': (91.1142, 29.6500),     # 西藏
        '西安市': (108.9480, 34.3416),    # 陕西
        '兰州市': (103.8236, 36.0581),    # 甘肃
        '西宁市': (101.7779, 36.6233),    # 青海
        '银川市': (106.2586, 38.4680),    # 宁夏
        '乌鲁木齐市': (87.6177, 43.7928), # 新疆
        
        # 特别行政区
        '香港特别行政区': (114.1095, 22.3964),
        '澳门特别行政区': (113.5430, 22.1868),
        '台北市': (121.5244, 25.0481),    # 台湾
        
        # 主要旅游城市
        '苏州市': (120.5853, 31.2989),
        '无锡市': (120.3019, 31.5733),
        '深圳市': (114.0579, 22.5431),
        '珠海市': (113.5769, 22.2707),
        '厦门市': (118.1104, 24.4905),
        '大连市': (121.6147, 38.9140),
        '青岛市': (120.3826, 36.0670),
        '烟台市': (121.4479, 37.4638),
        '威海市': (122.1201, 37.5127),
        '三亚市': (109.5119, 18.2529),
        '桂林市': (110.2993, 25.2342),
        '北海市': (109.1195, 21.4735),
        '丽江市': (100.2326, 26.8637),
        '大理市': (100.2519, 25.6023),
        '西双版纳': (100.7969, 22.0015),
        '九寨沟县': (103.9188, 33.1917),
        '张家界市': (110.4790, 29.1239),
        '凤凰县': (109.5904, 27.9506),
        '厦门市': (118.1104, 24.4905),
        '武夷山市': (117.9916, 27.7519),
        '三亚市': (109.5119, 18.2529),
        '五指山市': (109.5168, 18.7750),
        '琼海市': (110.4666, 19.2469),
        '阿坝州': (102.2210, 31.9001),
        '甘孜州': (101.9634, 30.0499),
        '凉山州': (102.2583, 27.8861),
        '呼伦贝尔市': (119.7655, 49.2117),
        '阿尔山市': (119.9434, 47.1776),
        '满洲里市': (117.3786, 49.5976),
        '二连浩特市': (111.9844, 43.6520),
        '腾冲市': (98.4546, 25.3107),
        '香格里拉市': (99.7083, 27.8284),
        '瑞丽市': (97.8550, 24.0154),
        '景洪市': (100.7969, 22.0015),
        '个旧市': (103.1526, 23.3617),
        '芒市': (98.5903, 24.4107),
        '文山市': (104.2442, 23.3695),
        '普者黑': (104.0889, 24.1085),
        '弥勒市': (103.2628, 24.4127),
        '建水县': (102.8277, 23.6106),
        '元阳县': (102.8354, 23.1600),
        '哈尼梯田': (102.8477, 23.1379),
        '梅里雪山': (98.8774, 28.4476),
        '泸沽湖': (100.7733, 27.6914),
        '西昌市': (102.2596, 27.8924),
        '稻城县': (100.2964, 29.0376),
        '康定市': (101.9615, 30.0503),
        '理塘县': (100.2681, 29.9963),
        '亚丁村': (100.3604, 28.9571),
        '丹巴县': (101.8933, 30.8795),
        '四姑娘山': (102.8347, 31.8949),
        '毕节市': (105.2850, 27.3017),
        '安顺市': (105.9472, 26.2452),
        '荔波县': (107.8752, 25.2944),
        '镇远县': (108.4249, 27.0506),
        '肇兴侗寨': (109.1636, 25.8402),
        '西江千户苗寨': (108.0834, 26.5786),
        '荔浦市': (110.3988, 24.4733),
        '阳朔县': (110.4747, 24.7769),
        '龙脊梯田': (109.9925, 25.7786),
        '涠洲岛': (109.1205, 21.0486),
        '德天瀑布': (106.7581, 22.8671),
        '通灵大峡谷': (106.6204, 22.9363),
        '北海市': (109.1195, 21.4735),
        '防城港市': (108.3533, 21.6178),
        '钦州市': (108.6244, 21.9613),
        '玉林市': (110.1828, 22.6432),
        '百色市': (106.6168, 23.9007),
        '崇左市': (107.3539, 22.4154),
        '贺州市': (111.5661, 24.7017),
        '河池市': (108.0622, 24.6929),
        '来宾市': (109.1746, 23.7340),
        '贵港市': (109.5989, 23.1073),
        '梧州市': (111.3059, 23.4786)
    }
    
    # 城市名称别名映射
    city_aliases = {
        # 简称映射
        '北京': '北京市',
        '上海': '上海市', 
        '天津': '天津市',
        '重庆': '重庆市',
        '石家庄': '石家庄市',
        '太原': '太原市',
        '呼和浩特': '呼和浩特市',
        '沈阳': '沈阳市',
        '长春': '长春市',
        '哈尔滨': '哈尔滨市',
        '南京': '南京市',
        '杭州': '杭州市',
        '合肥': '合肥市',
        '福州': '福州市',
        '南昌': '南昌市',
        '济南': '济南市',
        '郑州': '郑州市',
        '武汉': '武汉市',
        '长沙': '长沙市',
        '广州': '广州市',
        '南宁': '南宁市',
        '海口': '海口市',
        '成都': '成都市',
        '贵阳': '贵阳市',
        '昆明': '昆明市',
        '拉萨': '拉萨市',
        '西安': '西安市',
        '兰州': '兰州市',
        '西宁': '西宁市',
        '银川': '银川市',
        '乌鲁木齐': '乌鲁木齐市',
        
        # 特殊地区
        '香港': '香港特别行政区',
        '澳门': '澳门特别行政区',
        '台北': '台北市',
        '高雄': '高雄市',
        '台中': '台中市',
        
        # 主要城市别名
        '深圳': '深圳市',
        '苏州': '苏州市',
        '无锡': '无锡市',
        '珠海': '珠海市',
        '厦门': '厦门市',
        '大连': '大连市',
        '青岛': '青岛市',
        '烟台': '烟台市',
        '威海': '威海市',
        '三亚': '三亚市',
        '桂林': '桂林市',
        '北海': '北海市',
        '丽江': '丽江市',
        '大理': '大理市',
        '张家界': '张家界市',
        '凤凰': '凤凰县',
        '西双版纳': '西双版纳',
        '九寨沟': '九寨沟县',
        '稻城': '稻城县',
        '香格里拉': '香格里拉市',
        '亚丁': '亚丁村',
        '阳朔': '阳朔县',
        '涠洲岛': '涠洲岛',
        '德天': '德天瀑布',
        '满洲里': '满洲里市',
        '阿尔山': '阿尔山市',
        '呼伦贝尔': '呼伦贝尔市',
        '梅里雪山': '梅里雪山',
        '泸沽湖': '泸沽湖',
        '四姑娘山': '四姑娘山',
        '丹巴': '丹巴县',
        '毕节': '毕节市',
        '安顺': '安顺市',
        '荔波': '荔波县',
        '镇远': '镇远县',
        '西江': '西江千户苗寨',
        '肇兴': '肇兴侗寨',
        '龙脊': '龙脊梯田',
        '通灵': '通灵大峡谷',
        '防城港': '防城港市',
        '钦州': '钦州市',
        '玉林': '玉林市',
        '百色': '百色市',
        '崇左': '崇左市',
        '贺州': '贺州市',
        '河池': '河池市',
        '来宾': '来宾市',
        '贵港': '贵港市',
        '梧州': '梧州市',
        '弥勒': '弥勒市',
        '建水': '建水县',
        '元阳': '元阳县',
        '个旧': '个旧市',
        '芒市': '芒市',
        '文山': '文山市',
        '普者黑': '普者黑',
        '哈尼梯田': '哈尼梯田'
    }
    
    # 城市名称标准化和清理
    city_name = city_name.strip()
    original_name = city_name
    
    # 移除常见的修饰词和后缀
    clean_name = city_name.replace('市', '').replace('省', '').replace('自治区', '').replace('特别行政区', '')
    clean_name = clean_name.replace('州', '').replace('地区', '').replace('县', '').replace('区', '')
    
    # 同义词和美称映射
    synonym_mapping = {
        '京城': '北京市', '燕京': '北京市', '紫禁城': '北京市', '首都': '北京市',
        '魔都': '上海市', '申城': '上海市', '沪': '上海市',
        '津门': '天津市', '津': '天津市',
        '山城': '重庆市', '渝中': '重庆市', '巴渝': '重庆市',
        '冰城': '哈尔滨市', '春城': '长春市', '东方莫斯科': '哈尔滨市',
        '奉天': '沈阳市', '盛京': '沈阳市',
        '泉城': '济南市', '历下': '济南市',
        '蓉城': '成都市', '锦官城': '成都市', '蜀都': '成都市',
        '羊城': '广州市', '花城': '广州市', '穗城': '广州市',
        '榕城': '福州市', '三山': '福州市',
        '邕城': '南宁市', '绿城': '南宁市',
        '椰城': '海口市', '海府': '海口市',
        '江城': '武汉市', '武昌': '武汉市', '汉口': '武汉市', '汉阳': '武汉市',
        '星城': '长沙市', '潭城': '长沙市',
        '庐州': '合肥市', '庐阳': '合肥市',
        '金陵': '南京市', '建康': '南京市', '江宁': '南京市',
        '临安': '杭州市', '钱塘': '杭州市',
        '晋阳': '太原市', '并州': '太原市',
        '青城': '呼和浩特市', '呼市': '呼和浩特市',
        '日光城': '拉萨市', '圣城': '拉萨市', '逻些': '拉萨市',
        '长安': '西安市', '雍城': '西安市', '镐京': '西安市',
        '金城': '兰州市', '黄河城': '兰州市', '陆都': '兰州市',
        '夏都': '西宁市', '湟中': '西宁市',
        '凤城': '银川市', '塞上江南': '银川市',
        '迪化': '乌鲁木齐市', '红山': '乌鲁木齐市',
        '鹏城': '深圳市', '改革开放前沿': '深圳市',
        '鹭岛': '厦门市', '海上花园': '厦门市',
        '岛城': '青岛市', '琴岛': '青岛市', '啤酒城': '青岛市',
        '滨城': '大连市', '浪漫之都': '大连市',
        '甬城': '宁波市', '海港城市': '宁波市',
        '姑苏': '苏州市', '吴门': '苏州市', '水城': '苏州市',
        '太湖明珠': '无锡市', '锡城': '无锡市',
        '鹿城': '温州市', '山海城市': '温州市',
        '鲤城': '泉州市', '刺桐城': '泉州市',
        '百岛之市': '珠海市', '浪漫之城': '珠海市',
        '山水甲天下': '桂林市', '漓江': '桂林市',
        '世界文化遗产': '丽江市',
        '沙州': '敦煌市', '丝路明珠': '敦煌市',
        '世外桃源': '香格里拉', '迪庆': '香格里拉',
        '香江': '香港特别行政区', '东方之珠': '香港特别行政区',
        '濠江': '澳门特别行政区', '赌城': '澳门特别行政区',
        '宝岛': '台北市'
    }
    
    # 拼音和英文映射（简化版）
    pinyin_mapping = {
        'beijing': '北京市', 'shanghai': '上海市', 'tianjin': '天津市', 'chongqing': '重庆市',
        'guangzhou': '广州市', 'shenzhen': '深圳市', 'hangzhou': '杭州市', 'nanjing': '南京市',
        'wuhan': '武汉市', 'xian': '西安市', 'chengdu': '成都市', 'harbin': '哈尔滨市',
        'kunming': '昆明市', 'lasa': '拉萨市', 'lijiang': '丽江市'
    }
    
    # 多阶段匹配策略
    matched_coordinates = None
    
    # 第一阶段：直接完整匹配
    if city_name in city_coordinates:
        matched_coordinates = city_coordinates[city_name]
    
    # 第二阶段：清理后名称匹配
    elif clean_name in city_coordinates:
        matched_coordinates = city_coordinates[clean_name]
    
    # 第三阶段：同义词和美称匹配
    elif city_name in synonym_mapping:
        synonym = synonym_mapping[city_name]
        if synonym in city_coordinates:
            matched_coordinates = city_coordinates[synonym]
    
    # 第四阶段：别名映射
    elif city_name in city_aliases:
        alias_name = city_aliases[city_name]
        if alias_name in city_coordinates:
            matched_coordinates = city_coordinates[alias_name]
    
    # 第五阶段：标准城市名后缀匹配
    elif not matched_coordinates:
        for city_key in city_coordinates.keys():
            city_clean = city_key.replace('市', '').replace('省', '').replace('自治区', '').replace('特别行政区', '')
            if (city_name == city_clean or clean_name == city_key or 
                city_name == city_key or clean_name == city_clean):
                matched_coordinates = city_coordinates[city_key]
                break
    
    # 第六阶段：部分匹配（包含关系）- 增强版本
    if not matched_coordinates:
        for city_key in city_coordinates.keys():
            city_clean = city_key.replace('市', '').replace('省', '').replace('自治区', '').replace('特别行政区', '')
            # 检查各种变体
            if (city_name in city_clean or city_clean in city_name or
                clean_name in city_key or city_key in clean_name or
                city_name in city_key or city_key in city_name):
                matched_coordinates = city_coordinates[city_key]
                break
    
    # 第七阶段：拼音匹配
    if not matched_coordinates:
        city_lower = city_name.lower()
        if city_lower in pinyin_mapping:
            pinyin_city = pinyin_mapping[city_lower]
            if pinyin_city in city_coordinates:
                matched_coordinates = city_coordinates[pinyin_city]
    
    # 第八阶段：基于地理区域的智能匹配
    if not matched_coordinates:
        region_keywords = {
            '东北': ['哈尔滨', '长春', '沈阳', '大连'],
            '华北': ['北京', '天津', '石家庄', '太原', '呼和浩特'],
            '华东': ['上海', '杭州', '南京', '合肥', '福州', '南昌', '济南'],
            '华中': ['武汉', '郑州', '长沙'],
            '华南': ['广州', '南宁', '海口', '深圳'],
            '西南': ['成都', '重庆', '贵阳', '昆明', '拉萨'],
            '西北': ['西安', '兰州', '西宁', '银川', '乌鲁木齐'],
            '港澳台': ['香港', '澳门', '台湾']
        }
        
        for region, cities in region_keywords.items():
            if region in city_name:
                # 返回该区域的主要城市（北京作为默认）
                matched_coordinates = (116.4074, 39.9042)  # 北京坐标
                break
    
    # 如果都找不到，返回智能默认坐标（根据城市名称特征）
    if not matched_coordinates:
        # 根据城市名称的特征选择合适的默认坐标
        if any(keyword in city_name for keyword in ['西', '藏', '高原', '雪山']):
            matched_coordinates = (91.1409, 29.6456)  # 拉萨
        elif any(keyword in city_name for keyword in ['南', '海', '岛', '热带']):
            matched_coordinates = (110.3312, 20.0319)  # 海口
        elif any(keyword in city_name for keyword in ['北', '雪', '冰', '东北']):
            matched_coordinates = (126.6424, 45.7567)  # 哈尔滨
        else:
            matched_coordinates = (116.4074, 39.9042)  # 北京作为全国默认
    
    return matched_coordinates

# 错误处理
@app.errorhandler(404)
def page_not_found(error):
    return render_template('error.html', error_code=404, error_message='页面未找到'), 404

@app.errorhandler(500)
def internal_server_error(error):
    logger.error(f"服务器内部错误: {str(error)}", exc_info=True)
    return render_template('error.html', error_code=500, error_message='服务器内部错误'), 500

# 应用配置类
class Config:
    DEBUG = False
    TESTING = False
    SECRET_KEY = os.environ.get('SECRET_KEY', 'hard-to-guess-string')

class DevelopmentConfig(Config):
    DEBUG = True

class ProductionConfig(Config):
    pass

# 根据环境变量选择配置
config_by_name = {
    'development': DevelopmentConfig,
    'production': ProductionConfig,
    'default': DevelopmentConfig
}

# 应用工厂函数
def create_app(config_name='default'):
    app = Flask(__name__)
    app.config.from_object(config_by_name[config_name])
    
    # 注册路由和错误处理
    register_blueprints(app)
    register_error_handlers(app)
    
    return app

def register_blueprints(app):
    """注册蓝图"""
    # 这里可以注册模块化的蓝图
    # 例如: from blueprints.planning import planning_bp
    # app.register_blueprint(planning_bp)
    pass

def register_error_handlers(app):
    """注册全局错误处理"""
    @app.errorhandler(Exception)
    def handle_exception(error):
        logger.error(f"未处理的异常: {str(error)}", exc_info=True)
        return jsonify({
            'success': False,
            'message': '发生未处理的异常'
        }), 500

# 应用启动入口
if __name__ == '__main__':
    load_dotenv()  # 加载.env文件中的环境变量
    
    # 获取环境配置
    config_name = os.environ.get('FLASK_CONFIG', 'default')
    
    # 启动应用
    host = '0.0.0.0'  # 允许所有主机访问
    port = int(os.environ.get('PORT', 5000))  # 默认端口5000
    
    logger.info(f"应用启动于 {host}:{port}，环境: {config_name}")
    app.run(host=host, port=port, debug=app.config['DEBUG'])