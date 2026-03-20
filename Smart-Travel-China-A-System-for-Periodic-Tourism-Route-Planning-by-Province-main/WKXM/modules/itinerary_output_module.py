import json
import datetime
import copy
from .api_integration import APIIntegration

class ItineraryOutputModule:
    def __init__(self):
        # 初始化API集成模块
        self.api = APIIntegration()
        # 生成时间
        self.generation_time = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
    
    def generate_daily_itinerary_details(self, daily_plan, city, weather_info=None):
        """
        生成每日行程详细信息
        """
        if not daily_plan:
            return {}
        
        # 创建详细行程信息
        detailed_itinerary = {
            'day': daily_plan.get('day', 1),
            'date_suggestion': self._suggest_date(daily_plan.get('day', 1)),
            'summary': daily_plan.get('summary', '当日行程'),
            'start_time': daily_plan.get('start_time', '08:30'),
            'end_time': daily_plan.get('end_time', '17:00'),
            'weather_tips': self._generate_weather_tips(weather_info) if weather_info else '',
            'schedule': self._enhance_schedule(daily_plan.get('schedule', []), city),
            'transportation_summary': self._generate_transportation_summary(daily_plan.get('schedule', [])),
            'accommodation_suggestion': self._generate_accommodation_suggestion(city),
            'dining_suggestions': self._generate_dining_suggestions(city, daily_plan.get('spots', [])),
            'dressing_advice': self._generate_dressing_advice(city, weather_info),
            'daily_tips': self._generate_daily_tips(city, daily_plan.get('spots', []))
        }
        
        return detailed_itinerary
    
    def generate_full_itinerary_report(self, daily_plans, city, preferences=None, seasonal_info=None):
        """
        生成完整的行程报告
        """
        if not daily_plans:
            return {}
        
        # 基本信息
        report = {
            'generation_time': self.generation_time,
            'destination': city,
            'total_days': len(daily_plans),
            'travel_preferences': preferences or {},
            'seasonal_info': seasonal_info or {},
            'overall_summary': self._generate_overall_summary(daily_plans, city),
            'daily_itineraries': [],
            'travel_tips': self._generate_travel_tips(city),
            'essential_contacts': self._generate_essential_contacts(city),
            'packing_suggestions': self._generate_packing_suggestions(city, seasonal_info)
        }
        
        # 生成每日行程详情
        for daily_plan in daily_plans:
            detailed_daily = self.generate_daily_itinerary_details(daily_plan, city)
            report['daily_itineraries'].append(detailed_daily)
        
        # 计算统计信息
        report['statistics'] = self._calculate_itinerary_statistics(daily_plans)
        
        return report
    
    def format_itinerary_for_display(self, report):
        """
        格式化行程报告，用于友好显示
        """
        if not report:
            return "暂无行程信息"
        
        formatted_output = []
        
        # 标题
        formatted_output.append("=== 智游中国 - 行程规划报告 ===")
        formatted_output.append(f"\n【目的地】{report.get('destination', '未知城市')}")
        formatted_output.append(f"【行程天数】{report.get('total_days', 0)}天")
        formatted_output.append(f"【生成时间】{report.get('generation_time', '')}")
        
        # 季节信息
        if report.get('seasonal_info'):
            seasonal_info = report['seasonal_info']
            formatted_output.append(f"\n【季节信息】")
            if 'season_suggestion' in seasonal_info:
                formatted_output.append(f"  {seasonal_info['season_suggestion']}")
        
        # 总体概述
        formatted_output.append(f"\n【行程概览】")
        formatted_output.append(report.get('overall_summary', '暂无概述'))
        
        # 每日行程详情
        formatted_output.append("\n【每日行程】")
        for daily in report.get('daily_itineraries', []):
            formatted_output.append(f"\n第{daily.get('day', 0)}天")
            formatted_output.append(f"  日期建议: {daily.get('date_suggestion', '')}")
            formatted_output.append(f"  行程概要: {daily.get('summary', '')}")
            formatted_output.append(f"  当天天气提示: {daily.get('weather_tips', '请关注天气预报')}")
            
            # 行程安排
            formatted_output.append("  行程安排:")
            for item in daily.get('schedule', []):
                formatted_output.append(f"    {item.get('arrival_time', '')} - {item.get('departure_time', '')}: {item.get('spot', {}).get('name', '未命名景点')}")
                formatted_output.append(f"      景点类型: {item.get('spot', {}).get('type', '未知')}")
                formatted_output.append(f"      游玩时长: {item.get('duration_hours', 0)}小时")
                if 'transportation' in item:
                    formatted_output.append(f"      交通方式: {item['transportation']}")
                
            # 餐饮推荐
            formatted_output.append("  餐饮推荐:")
            for dining in daily.get('dining_suggestions', []):
                formatted_output.append(f"    {dining.get('type', '')}: {dining.get('name', '')}")
                if 'address' in dining:
                    formatted_output.append(f"      推荐理由: {dining.get('reason', '')}")
                
            # 住宿建议
            if daily.get('accommodation_suggestion'):
                formatted_output.append(f"  住宿建议: {daily['accommodation_suggestion']}")
            
            # 穿衣建议
            if daily.get('dressing_advice'):
                formatted_output.append(f"  穿衣建议: {daily['dressing_advice']}")
            
            # 每日贴士
            if daily.get('daily_tips'):
                formatted_output.append(f"  今日贴士: {daily['daily_tips']}")
        
        # 旅行贴士
        formatted_output.append("\n【旅行贴士】")
        for tip in report.get('travel_tips', []):
            formatted_output.append(f"- {tip}")
        
        # 联系方式
        formatted_output.append("\n【紧急联系】")
        for contact in report.get('essential_contacts', []):
            formatted_output.append(f"- {contact}")
        
        # 打包建议
        formatted_output.append("\n【打包建议】")
        for category, items in report.get('packing_suggestions', {}).items():
            formatted_output.append(f"{category}:")
            for item in items:
                formatted_output.append(f"  - {item}")
        
        return '\n'.join(formatted_output)
    
    def export_itinerary_as_json(self, report, file_path=None):
        """
        导出行程为JSON格式
        """
        if file_path:
            try:
                with open(file_path, 'w', encoding='utf-8') as f:
                    json.dump(report, f, ensure_ascii=False, indent=2)
                return True
            except Exception as e:
                print(f"导出JSON失败: {e}")
                return False
        else:
            # 返回JSON字符串
            return json.dumps(report, ensure_ascii=False, indent=2)
    
    def _suggest_date(self, day_number):
        """
        建议日期
        """
        # 从明天开始计算
        start_date = datetime.datetime.now() + datetime.timedelta(days=1)
        suggested_date = start_date + datetime.timedelta(days=day_number - 1)
        return suggested_date.strftime('%Y年%m月%d日')
    
    def _enhance_schedule(self, schedule, city):
        """
        增强行程安排信息
        """
        enhanced_schedule = []
        
        for item in schedule:
            enhanced_item = copy.deepcopy(item)
            
            # 增强景点信息
            if 'spot' in enhanced_item:
                spot = enhanced_item['spot']
                # 补充景点详细信息
                spot['opening_hours'] = spot.get('opening_hours', self._get_default_opening_hours(spot.get('type', '')))
                spot['ticket_info'] = spot.get('ticket_info', self._get_default_ticket_info(spot.get('type', '')))
                spot['visit_tips'] = spot.get('visit_tips', self._generate_visit_tips(spot, city))
            
            # 增强交通信息
            if 'transportation' in enhanced_item:
                # 补充更详细的交通建议
                enhanced_item['transportation_details'] = self._enhance_transportation_info(
                    enhanced_item['transportation'], city
                )
            
            enhanced_schedule.append(enhanced_item)
        
        return enhanced_schedule
    
    def _generate_weather_tips(self, weather_info):
        """
        生成天气提示
        """
        if not weather_info:
            return "请提前查看天气预报"
        
        condition = weather_info.get('condition', '').lower()
        temperature = weather_info.get('temperature', '')
        
        tips = []
        
        if '雨' in condition:
            tips.append("今日有雨，请携带雨具")
            tips.append("雨天路滑，注意安全")
        elif '雪' in condition:
            tips.append("今日有雪，注意保暖防滑")
            tips.append("道路可能结冰，出行请小心")
        elif '晴' in condition:
            tips.append("今日天气晴好，适合出行")
            tips.append("阳光强烈，注意防晒")
        elif '阴' in condition:
            tips.append("今日天气阴沉，气温适中")
        
        if temperature:
            tips.append(f"今日温度：{temperature}")
        
        return ' '.join(tips)
    
    def _generate_transportation_summary(self, schedule):
        """
        生成交通方式总结
        """
        if not schedule:
            return "暂无交通信息"
        
        # 统计各种交通方式
        transport_counts = {}
        
        for item in schedule:
            if 'transportation' in item:
                transport_text = item['transportation']
                # 简单分类交通方式
                if '步行' in transport_text:
                    transport_counts['步行'] = transport_counts.get('步行', 0) + 1
                elif '共享' in transport_text or '单车' in transport_text:
                    transport_counts['共享单车'] = transport_counts.get('共享单车', 0) + 1
                elif '公交' in transport_text:
                    transport_counts['公交车'] = transport_counts.get('公交车', 0) + 1
                elif '打车' in transport_text or '出租' in transport_text:
                    transport_counts['出租车'] = transport_counts.get('出租车', 0) + 1
                else:
                    transport_counts['其他'] = transport_counts.get('其他', 0) + 1
        
        # 生成总结
        summary_parts = []
        for transport, count in transport_counts.items():
            summary_parts.append(f"{transport} {count}次")
        
        summary = f"今日交通以{', '.join(summary_parts)}为主，建议"
        
        # 添加建议
        if transport_counts.get('共享单车', 0) > 0:
            summary += "提前下载当地共享单车APP"
        if transport_counts.get('公交车', 0) > 0:
            summary += "，准备公交卡或移动支付"
        if transport_counts.get('出租车', 0) > 0:
            summary += "，预留充足的打车时间"
        
        return summary
    
    def _generate_accommodation_suggestion(self, city):
        """
        生成住宿建议
        """
        # 城市住宿区域建议
        city_accommodation = {
            '大理市': {
                'areas': ['大理古城内', '大理古城周边', '洱海边上'],
                'price_range': '¥200-600/晚',
                'types': ['特色客栈', '精品民宿', '度假酒店'],
                'recommendation': '推荐选择大理古城内的特色客栈，交通便利且能体验当地文化'
            },
            '丽江市': {
                'areas': ['丽江古城内', '束河古镇', '玉龙雪山脚下'],
                'price_range': '¥200-800/晚',
                'types': ['纳西特色客栈', '精品酒店', '度假别墅'],
                'recommendation': '建议入住丽江古城内的纳西特色客栈，体验传统文化氛围'
            },
            '昆明市': {
                'areas': ['翠湖周边', '南屏步行街附近', '滇池路'],
                'price_range': '¥300-800/晚',
                'types': ['商务酒店', '民宿', '国际连锁酒店'],
                'recommendation': '推荐翠湖周边或市中心区域住宿，交通便利'
            },
            '杭州市': {
                'areas': ['西湖周边', '河坊街附近', '武林广场'],
                'price_range': '¥400-1200/晚',
                'types': ['精品酒店', '民宿', '国际酒店'],
                'recommendation': '建议选择西湖周边住宿，方便游览主要景点'
            },
            '苏州市': {
                'areas': ['拙政园附近', '平江路历史街区', '观前街'],
                'price_range': '¥350-1000/晚',
                'types': ['园林式酒店', '精品民宿', '商务酒店'],
                'recommendation': '推荐入住园林式酒店或平江路历史街区的民宿'
            }
        }
        
        if city in city_accommodation:
            info = city_accommodation[city]
            return f"{info['recommendation']}。价格区间：{info['price_range']}。推荐区域：{', '.join(info['areas'])}。"
        else:
            return f"建议选择{city}市中心或主要景点附近的住宿，交通便利且方便游览。"
    
    def _generate_dining_suggestions(self, city, spots):
        """
        生成餐饮建议
        """
        # 城市特色美食
        city_food = {
            '大理市': [
                {'name': '大理砂锅鱼', 'type': '午餐/晚餐', 'reason': '大理特色美食，鱼肉鲜嫩'}, 
                {'name': '白族三道茶', 'type': '下午茶', 'reason': '白族传统茶艺表演'}, 
                {'name': '喜洲粑粑', 'type': '小吃', 'reason': '传统面食，甜咸两种口味'}, 
                {'name': '乳扇', 'type': '小吃', 'reason': '白族特色奶制品'}, 
                {'name': '永平黄焖鸡', 'type': '晚餐', 'reason': '肉质鲜嫩，味道独特'}
            ],
            '丽江市': [
                {'name': '丽江纳西烤鱼', 'type': '午餐/晚餐', 'reason': '纳西族特色美食'}, 
                {'name': '鸡豆凉粉', 'type': '小吃', 'reason': '丽江特色小吃'}, 
                {'name': '丽江粑粑', 'type': '早餐', 'reason': '传统面食'}, 
                {'name': '野生菌火锅', 'type': '晚餐', 'reason': '云南特色，鲜美可口'}, 
                {'name': '摩梭猪膘肉', 'type': '特色菜', 'reason': '摩梭族传统美食'}
            ],
            '昆明市': [
                {'name': '过桥米线', 'type': '早餐/午餐', 'reason': '云南招牌美食'}, 
                {'name': '汽锅鸡', 'type': '午餐/晚餐', 'reason': '云南传统名菜'}, 
                {'name': '官渡粑粑', 'type': '小吃', 'reason': '昆明传统小吃'}, 
                {'name': '烧豆腐', 'type': '小吃', 'reason': '云南特色小吃'}, 
                {'name': '宣威火腿', 'type': '特色菜', 'reason': '云南著名特产'}
            ],
            '杭州市': [
                {'name': '西湖醋鱼', 'type': '午餐/晚餐', 'reason': '杭州名菜，酸甜可口'}, 
                {'name': '龙井虾仁', 'type': '午餐/晚餐', 'reason': '杭州特色，清香鲜嫩'}, 
                {'name': '叫花鸡', 'type': '晚餐', 'reason': '传统名菜，肉质酥烂'}, 
                {'name': '东坡肉', 'type': '午餐/晚餐', 'reason': '肥而不腻，入口即化'}, 
                {'name': '杭州小笼包', 'type': '早餐', 'reason': '皮薄馅多，汤汁鲜美'}
            ],
            '苏州市': [
                {'name': '松鼠桂鱼', 'type': '午餐/晚餐', 'reason': '苏州名菜，造型美观'}, 
                {'name': '碧螺春虾仁', 'type': '午餐/晚餐', 'reason': '苏州特色，清香可口'}, 
                {'name': '响油鳝糊', 'type': '晚餐', 'reason': '传统苏菜，鲜美可口'}, 
                {'name': '太湖银鱼羹', 'type': '汤品', 'reason': '太湖特色，鲜嫩细腻'}, 
                {'name': '苏州汤包', 'type': '早餐', 'reason': '皮薄馅多，汤汁丰富'}
            ]
        }
        
        suggestions = []
        
        # 根据城市获取美食推荐
        if city in city_food:
            suggestions = city_food[city]
        
        # 根据景点调整推荐
        adjusted_suggestions = self._adjust_dining_suggestions_based_on_spots(suggestions, spots)
        
        return adjusted_suggestions
    
    def _generate_dressing_advice(self, city, weather_info):
        """
        生成穿衣建议
        """
        current_month = datetime.datetime.now().month
        
        # 基础穿衣建议
        season_clothing = {
            1: '冬季着装，厚外套、毛衣、围巾、手套等保暖衣物',
            2: '冬季着装，厚外套、保暖内衣、帽子等',
            3: '春装，外套、长袖衣物，早晚温差大',
            4: '春秋装，薄外套、长袖衬衫，舒适为主',
            5: '夏装为主，准备薄外套应对空调环境',
            6: '夏装，短袖、短裤、防晒衣物',
            7: '夏装，轻薄透气衣物，防晒用品必备',
            8: '夏装，准备雨具应对雷雨天气',
            9: '秋装，长袖衣物、薄外套',
            10: '秋装，外套、长袖衣物，注意保暖',
            11: '秋冬装，厚外套、毛衣等',
            12: '冬装，厚外套、保暖内衣、围巾等'
        }
        
        base_advice = season_clothing.get(current_month, '舒适衣物为主')
        
        # 根据天气信息调整
        if weather_info:
            condition = weather_info.get('condition', '').lower()
            if '雨' in condition:
                base_advice += '，请携带雨具'
            elif '雪' in condition:
                base_advice += '，注意防滑保暖'
            elif '晴' in condition:
                base_advice += '，注意防晒'
        
        # 城市特定建议
        city_specific = {
            '大理市': '大理昼夜温差较大，无论何时都建议携带外套',
            '丽江市': '丽江海拔较高，紫外线强，注意防晒，昼夜温差大',
            '昆明市': '昆明四季如春，但早晚温差大，建议携带薄外套',
            '杭州市': '杭州气候湿润，夏季炎热潮湿，冬季湿冷',
            '苏州市': '苏州气候温和，但梅雨季节潮湿，冬季湿冷'
        }
        
        if city in city_specific:
            return f"{base_advice}。{city_specific[city]}"
        else:
            return base_advice
    
    def _generate_daily_tips(self, city, spots):
        """
        生成每日小贴士
        """
        tips = []
        
        # 通用贴士
        tips.append("携带身份证、手机等必备物品")
        tips.append("景区内注意环保，不乱扔垃圾")
        
        # 根据景点类型添加贴士
        spot_types = set(spot.get('type', '') for spot in spots)
        
        if any(t in ['古城', '古镇'] for t in spot_types):
            tips.append("古城内人流较大，注意保管个人财物")
        
        if any(t in ['寺庙', '教堂'] for t in spot_types):
            tips.append("参观宗教场所请尊重当地习俗")
        
        if any(t in ['山岳', '国家公园'] for t in spot_types):
            tips.append("山区温差大，建议穿着舒适的鞋子")
        
        # 城市特定贴士
        city_tips = {
            '大理市': '大理紫外线强烈，请注意防晒',
            '丽江市': '丽江高原地区氧气稀薄，避免剧烈运动',
            '昆明市': '昆明气候干燥，注意补充水分',
            '杭州市': '西湖周边景区较大，建议合理安排时间',
            '苏州市': '苏州园林游客较多，建议避开节假日高峰'
        }
        
        if city in city_tips:
            tips.append(city_tips[city])
        
        return ' '.join(tips)
    
    def _generate_overall_summary(self, daily_plans, city):
        """
        生成总体行程概述
        """
        if not daily_plans:
            return "暂无行程安排"
        
        total_spots = sum(len(plan.get('spots', [])) for plan in daily_plans)
        total_days = len(daily_plans)
        
        # 统计景点类型
        spot_types = {}
        for plan in daily_plans:
            for spot in plan.get('spots', []):
                spot_type = spot.get('type', '其他')
                spot_types[spot_type] = spot_types.get(spot_type, 0) + 1
        
        # 生成概述
        summary = f"本次{city}之行共安排{total_days}天，涵盖{total_spots}个景点。"
        
        # 添加景点类型分布
        if spot_types:
            types_str = '、'.join([f"{t}({spot_types[t]}个)" for t in sorted(spot_types.keys())])
            summary += f"景点类型包括{types_str}等。"
        
        # 添加特色说明
        city_features = {
            '大理市': '行程将带您领略大理古城的历史文化和洱海的自然风光，体验白族特色文化。',
            '丽江市': '您将游览丽江古城、玉龙雪山等著名景点，感受纳西族传统文化的魅力。',
            '昆明市': '行程涵盖昆明市区主要景点，体验春城的独特魅力和丰富的民族文化。',
            '杭州市': '您将游览西湖周边著名景点，感受杭州的自然风光和人文历史。',
            '苏州市': '行程将带您游览苏州园林、平江路等景点，体验江南水乡的独特韵味。'
        }
        
        if city in city_features:
            summary += city_features[city]
        
        return summary
    
    def _generate_travel_tips(self, city):
        """
        生成旅行贴士
        """
        base_tips = [
            '请提前预订机票/火车票和住宿，特别是节假日期间',
            '出发前检查身份证、驾驶证等证件是否齐全',
            '准备常用药品，如感冒药、肠胃药、创可贴等',
            '注意保管个人财物，特别是在人多拥挤的地方',
            '尊重当地风俗习惯，遵守景区规定',
            '保持手机电量充足，下载离线地图',
            '旅行期间保持联系，告知家人行程安排'
        ]
        
        # 城市特定贴士
        city_tips = {
            '大理市': [
                '大理紫外线强烈，务必做好防晒措施',
                '洱海周边景点分散，建议合理安排交通',
                '白族有独特的茶文化，可以体验三道茶'
            ],
            '丽江市': [
                '丽江海拔较高，初到可能有高原反应，建议多休息',
                '玉龙雪山气温较低，需准备保暖衣物',
                '古城内商业氛围浓厚，购物请理性消费'
            ],
            '昆明市': [
                '昆明气候干燥，注意多喝水补充水分',
                '石林景区较大，建议穿着舒适的鞋子',
                '红嘴鸥季节（11月-3月）可到海埂大坝观赏'
            ],
            '杭州市': [
                '西湖周边停车位紧张，建议使用公共交通',
                '杭州多雨，出行前请查看天气预报',
                '龙井村茶叶购买请选择正规店铺'
            ],
            '苏州市': [
                '苏州园林众多，建议提前了解园林历史背景',
                '平江路、山塘街等古街人流量大，注意安全',
                '苏州评弹是当地特色，可以体验'
            ]
        }
        
        all_tips = base_tips.copy()
        if city in city_tips:
            all_tips.extend(city_tips[city])
        
        return all_tips
    
    def _generate_essential_contacts(self, city):
        """
        生成紧急联系方式
        """
        # 通用紧急电话
        contacts = [
            '紧急救援：120',
            '报警：110',
            '消防：119',
            '旅游投诉：12345'
        ]
        
        # 城市旅游咨询电话
        city_tourism_phones = {
            '大理市': '大理市旅游咨询：0872-2121246',
            '丽江市': '丽江市旅游咨询：0888-5111118',
            '昆明市': '昆明市旅游咨询：0871-63163620',
            '杭州市': '杭州市旅游咨询：0571-85171292',
            '苏州市': '苏州市旅游咨询：0512-65223377'
        }
        
        if city in city_tourism_phones:
            contacts.append(city_tourism_phones[city])
        
        return contacts
    
    def _generate_packing_suggestions(self, city, seasonal_info):
        """
        生成打包建议
        """
        suggestions = {
            '必备证件': ['身份证', '银行卡', '现金少量'],
            '电子设备': ['手机', '充电器', '移动电源', '相机（可选）'],
            '个人护理': ['牙刷', '牙膏', '毛巾', '洗发水', '沐浴露'],
            '药品': ['感冒药', '肠胃药', '创可贴', '晕车药', '防晒用品']
        }
        
        # 根据季节信息调整
        if seasonal_info and 'current_season' in seasonal_info:
            season = seasonal_info['current_season']
            if season == '夏季':
                suggestions['夏季必备'] = ['防晒霜', '墨镜', '遮阳帽', '防蚊液']
            elif season == '冬季':
                suggestions['冬季必备'] = ['保暖内衣', '厚外套', '围巾', '手套']
            elif season == '春季':
                suggestions['春季必备'] = ['薄外套', '雨伞', '过敏药（可选）']
            elif season == '秋季':
                suggestions['秋季必备'] = ['薄外套', '长袖衣物']
        
        # 城市特定建议
        city_specific = {
            '大理市': ['防晒霜（紫外线强）', '雨具（雨季）', '舒适徒步鞋'],
            '丽江市': ['防晒霜（高原紫外线）', '保暖衣物（昼夜温差大）', '补水护肤品'],
            '昆明市': ['薄外套（早晚温差）', '防晒用品'],
            '杭州市': ['雨具', '舒适的鞋子（步行较多）'],
            '苏州市': ['舒适的鞋子（园林游览）', '轻便雨具']
        }
        
        if city in city_specific:
            suggestions[f'{city}特色必备'] = city_specific[city]
        
        return suggestions
    
    def _calculate_itinerary_statistics(self, daily_plans):
        """
        计算行程统计信息
        """
        stats = {
            'total_days': len(daily_plans),
            'total_spots': 0,
            'spots_by_type': {},
            'daily_spot_count': [],
            'total_distance_estimate': 0  # 估算总距离
        }
        
        for plan in daily_plans:
            spots = plan.get('spots', [])
            stats['total_spots'] += len(spots)
            stats['daily_spot_count'].append(len(spots))
            
            # 统计景点类型
            for spot in spots:
                spot_type = spot.get('type', '其他')
                stats['spots_by_type'][spot_type] = stats['spots_by_type'].get(spot_type, 0) + 1
        
        return stats
    
    # 辅助方法
    def _get_default_opening_hours(self, spot_type):
        """
        获取默认开放时间
        """
        default_hours = {
            '古城': '全天开放',
            '古镇': '全天开放',
            '公园': '06:00-22:00',
            '博物馆': '09:00-17:00（周一闭馆）',
            '寺庙': '08:00-17:00',
            '主题乐园': '09:00-18:00',
            '海滩': '全天开放',
            '山岳': '08:00-17:00'
        }
        
        return default_hours.get(spot_type, '09:00-17:00')
    
    def _get_default_ticket_info(self, spot_type):
        """
        获取默认门票信息
        """
        default_tickets = {
            '古城': '免费',
            '古镇': '免费',
            '公园': '免费',
            '博物馆': '免费或20-100元',
            '寺庙': '10-50元',
            '主题乐园': '100-300元',
            '海滩': '免费',
            '山岳': '50-200元'
        }
        
        return default_tickets.get(spot_type, '具体以景区公告为准')
    
    def _generate_visit_tips(self, spot, city):
        """
        生成参观贴士
        """
        spot_type = spot.get('type', '')
        tips = {
            '古城': '建议清晨或傍晚参观，避开人流高峰',
            '古镇': '可以深入小巷，发现不一样的风景',
            '博物馆': '建议租用讲解器，了解更多历史文化',
            '寺庙': '参观时请保持安静，尊重宗教习俗',
            '主题乐园': '建议工作日前往，避开周末人流高峰',
            '山岳': '穿着舒适的鞋子，带足饮用水',
            '海滩': '注意防晒，不要在非游泳区游泳'
        }
        
        return tips.get(spot_type, '祝您参观愉快！')
    
    def _enhance_transportation_info(self, basic_info, city):
        """
        增强交通信息
        """
        # 补充城市公共交通信息
        city_transport_info = {
            '大理市': '大理市内可使用公交车（票价1-2元）、出租车（起步价8元）或共享单车',
            '丽江市': '丽江古城内禁止机动车，可步行或使用共享单车；古城外可乘坐公交车或出租车',
            '昆明市': '昆明公交系统发达，可使用支付宝或微信扫码乘车',
            '杭州市': '杭州公共交通便利，地铁和公交覆盖主要景点，可使用支付宝扫码乘车',
            '苏州市': '苏州交通便利，地铁和公交网络完善，可使用苏州市民卡或移动支付'
        }
        
        # 根据基础交通信息补充详细建议
        details = []
        if '步行' in basic_info:
            details.append('步行是游览城市景点的好方式，可以更好地感受当地氛围')
        elif '共享单车' in basic_info:
            details.append('使用共享单车前请确认停车区域，遵守当地规定')
        elif '公交' in basic_info:
            details.append('公交车是经济实惠的出行方式，建议提前了解路线')
        elif '打车' in basic_info:
            details.append('高峰期打车可能需要等待，建议提前安排')
        
        # 添加城市特定信息
        if city in city_transport_info:
            details.append(city_transport_info[city])
        
        return ' '.join(details)
    
    def _adjust_dining_suggestions_based_on_spots(self, suggestions, spots):
        """
        根据景点调整餐饮建议
        """
        if not spots:
            return suggestions
        
        # 分析景点分布，调整餐饮推荐
        spot_types = [spot.get('type', '') for spot in spots]
        
        # 如果有古城或古镇，优先推荐当地特色小吃
        if any(t in ['古城', '古镇'] for t in spot_types):
            # 确保小吃类推荐靠前
            adjusted = sorted(suggestions, key=lambda x: 0 if x['type'] == '小吃' else 1)
            return adjusted
        
        return suggestions