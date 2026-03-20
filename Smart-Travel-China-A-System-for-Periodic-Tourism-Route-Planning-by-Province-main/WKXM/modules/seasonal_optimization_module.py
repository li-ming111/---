import datetime
import calendar
import json
import copy
from .api_integration import APIIntegration

class SeasonalOptimizationModule:
    def __init__(self):
        # 初始化API集成模块
        self.api = APIIntegration()
        # 当前日期
        self.current_date = datetime.datetime.now()
        # 当前月份
        self.current_month = self.current_date.month
        # 当前季节
        self.current_season = self._get_season(self.current_month)
        # 节假日数据
        self.holidays = self._load_holidays()
    
    def optimize_for_season(self, spots, province=None, city=None):
        """
        根据季节优化景点推荐
        """
        if not spots:
            return []
        
        # 复制景点列表，避免修改原始数据
        spots_copy = copy.deepcopy(spots)
        
        # 为每个景点计算季节适宜度分数
        spots_with_scores = []
        for spot in spots_copy:
            # 基础分数
            base_score = 5.0
            
            # 最佳季节匹配分数
            season_score = self._calculate_season_score(spot, self.current_season, province, city)
            
            # 月份特定推荐分数
            month_score = self._calculate_month_score(spot, self.current_month, province, city)
            
            # 综合季节分数
            seasonal_score = base_score + season_score + month_score
            
            # 确保分数在0-10之间
            seasonal_score = max(0, min(10, seasonal_score))
            
            # 添加季节分数到景点信息
            spot['seasonal_score'] = seasonal_score
            spot['best_visit_season'] = spot.get('best_visit_season', self._get_suggested_best_season(spot))
            spots_with_scores.append(spot)
        
        # 根据季节分数排序景点
        spots_with_scores.sort(key=lambda x: x.get('seasonal_score', 0), reverse=True)
        
        return spots_with_scores
    
    def optimize_for_holidays(self, spots, travel_dates=None):
        """
        根据节假日优化景点推荐
        """
        if not spots:
            return []
        
        # 复制景点列表，避免修改原始数据
        spots_copy = copy.deepcopy(spots)
        
        # 如果没有提供旅行日期，假设是未来30天
        if not travel_dates:
            start_date = datetime.datetime.now()
            end_date = start_date + datetime.timedelta(days=30)
            travel_dates = self._generate_date_range(start_date, end_date)
        
        # 检查旅行日期是否包含节假日
        has_holiday = any(self._is_holiday_or_weekend(date) for date in travel_dates)
        
        # 为每个景点计算节假日适宜度分数
        spots_with_scores = []
        for spot in spots_copy:
            # 基础分数
            base_score = 5.0
            
            # 节假日人流拥挤度分数
            crowd_score = self._calculate_crowd_score(spot, has_holiday)
            
            # 避开热门景点分数
            popular_score = self._calculate_popularity_score(spot, has_holiday)
            
            # 综合节假日分数
            holiday_score = base_score + crowd_score + popular_score
            
            # 确保分数在0-10之间
            holiday_score = max(0, min(10, holiday_score))
            
            # 添加节假日分数到景点信息
            spot['holiday_score'] = holiday_score
            spots_with_scores.append(spot)
        
        # 根据节假日分数排序景点
        spots_with_scores.sort(key=lambda x: x.get('holiday_score', 0), reverse=True)
        
        return spots_with_scores
    
    def get_seasonal_recommendations(self, spots, province=None, city=None):
        """
        获取季节性推荐信息
        """
        # 优化景点
        optimized_spots = self.optimize_for_season(spots, province, city)
        
        # 找出最适合当前季节的景点
        best_season_spots = [spot for spot in optimized_spots if spot.get('seasonal_score', 0) >= 7.5]
        
        # 找出不适合当前季节的景点
        not_recommended_spots = [spot for spot in optimized_spots if spot.get('seasonal_score', 0) <= 3.5]
        
        # 生成季节建议
        season_suggestion = self._generate_season_suggestion(self.current_season, province, city)
        
        recommendations = {
            'best_season_spots': best_season_spots[:5],  # 最多返回5个
            'not_recommended_spots': not_recommended_spots,
            'season_suggestion': season_suggestion,
            'current_season': self.current_season,
            'current_month': self.current_month
        }
        
        return recommendations
    
    def get_holiday_advice(self, spots, travel_dates=None):
        """
        获取节假日出行建议
        """
        # 确定旅行日期
        if not travel_dates:
            start_date = datetime.datetime.now()
            end_date = start_date + datetime.timedelta(days=7)
            travel_dates = self._generate_date_range(start_date, end_date)
        
        # 检查是否包含节假日
        holidays_in_range = [date for date in travel_dates if self._is_holiday(date)]
        weekends_in_range = [date for date in travel_dates if self._is_weekend(date)]
        
        # 获取替代景点建议
        alternative_spots = self._suggest_alternative_spots(spots, bool(holidays_in_range or weekends_in_range))
        
        # 生成出行建议
        travel_advice = self._generate_travel_advice(holidays_in_range, weekends_in_range)
        
        advice = {
            'holidays_in_range': holidays_in_range,
            'weekends_in_range': weekends_in_range,
            'alternative_spots': alternative_spots[:5],  # 最多返回5个
            'travel_advice': travel_advice,
            'crowd_level': self._estimate_crowd_level(holidays_in_range, weekends_in_range)
        }
        
        return advice
    
    def _get_season(self, month):
        """
        根据月份获取季节
        """
        if month in [3, 4, 5]:
            return '春季'
        elif month in [6, 7, 8]:
            return '夏季'
        elif month in [9, 10, 11]:
            return '秋季'
        else:
            return '冬季'
    
    def _calculate_season_score(self, spot, current_season, province=None, city=None):
        """
        计算景点与当前季节的匹配度分数
        """
        best_season = spot.get('best_visit_season', '').strip()
        
        # 如果没有最佳季节信息，返回基础分数
        if not best_season:
            return 0
        
        # 检查当前季节是否是最佳季节
        if current_season in best_season:
            return 3.0
        
        # 检查是否是次佳季节
        secondary_seasons = {
            '春季': ['秋季'],
            '夏季': ['春季', '秋季'],
            '秋季': ['春季'],
            '冬季': ['秋季']
        }
        
        if any(season in best_season for season in secondary_seasons.get(current_season, [])):
            return 1.0
        
        # 如果是不适宜的季节
        if current_season in ['春季', '夏季', '秋季', '冬季']:
            opposite_seasons = {
                '春季': ['冬季'],
                '夏季': ['冬季'],
                '秋季': ['冬季'],
                '冬季': ['夏季']
            }
            
            if any(season in best_season for season in opposite_seasons.get(current_season, [])):
                return -2.0
        
        # 城市特定的季节调整
        city_season_boost = self._get_city_season_boost(city, current_season)
        return city_season_boost
    
    def _calculate_month_score(self, spot, current_month, province=None, city=None):
        """
        计算特定月份的推荐分数
        """
        # 基于城市和月份的特殊事件或自然现象
        special_events_bonus = self._get_special_events_bonus(city, current_month)
        
        # 基于城市的最佳月份调整
        city_month_boost = self._get_city_month_boost(city, current_month)
        
        return special_events_bonus + city_month_boost
    
    def _calculate_crowd_score(self, spot, during_holiday):
        """
        计算节假日人流拥挤度分数
        """
        # 获取景点类型
        spot_type = spot.get('type', '').strip()
        
        # 热门景点在节假日的拥挤度惩罚
        popular_spot_penalty = {
            '古城': -2.0,
            '古镇': -2.0,
            '主题乐园': -3.0,
            '海滩': -1.5,
            '寺庙': -1.5,
            '国家公园': -1.0
        }
        
        # 冷门景点在节假日的推荐加成
        less_known_bonus = {
            '博物馆': 1.0,
            '历史遗迹': 0.5,
            '文化村': 1.5
        }
        
        score = 0
        
        # 只有在节假日期间才应用这些分数
        if during_holiday:
            # 热门景点惩罚
            for spot_cat, penalty in popular_spot_penalty.items():
                if spot_cat in spot_type:
                    score += penalty
                    break
            
            # 冷门景点加成
            for spot_cat, bonus in less_known_bonus.items():
                if spot_cat in spot_type:
                    score += bonus
                    break
        
        return score
    
    def _calculate_popularity_score(self, spot, during_holiday):
        """
        计算景点知名度分数（用于避开人潮）
        """
        # 获取景点等级或知名度信息
        spot_level = spot.get('level', '').strip()
        
        # AAAAA级景点在节假日人流较多
        if during_holiday and 'AAAAA' in spot_level:
            return -1.5
        elif during_holiday and 'AAAA' in spot_level:
            return -1.0
        
        # 低级别景点在节假日可能是更好的选择
        if during_holiday and spot_level in ['AAA', 'AA']:
            return 1.0
        
        return 0
    
    def _get_suggested_best_season(self, spot):
        """
        根据景点类型建议最佳游览季节
        """
        spot_type = spot.get('type', '').strip()
        
        # 不同类型景点的建议季节
        seasonal_recommendations = {
            '海滩': '夏季（6-9月）',
            '山岳': '春季（4-5月）和秋季（9-10月）',
            '湖泊': '春季（4-5月）和秋季（9-10月）',
            '古城': '春季（3-5月）和秋季（9-11月）',
            '古镇': '春季（3-5月）和秋季（9-11月）',
            '寺庙': '春季（3-5月）和秋季（9-11月）',
            '主题乐园': '春季（3-5月）和秋季（9-11月）',
            '博物馆': '四季皆宜',
            '动物园': '春季（4-5月）和秋季（9-10月）',
            '温泉': '冬季（11-2月）'
        }
        
        # 查找最匹配的类型
        for spot_cat, season in seasonal_recommendations.items():
            if spot_cat in spot_type:
                return season
        
        # 默认推荐
        return '春季（3-5月）和秋季（9-11月）'
    
    def _generate_season_suggestion(self, season, province=None, city=None):
        """
        生成季节旅行建议
        """
        # 基础季节建议
        base_suggestions = {
            '春季': '春天是赏花和户外活动的好时节，气温宜人，风景优美。',
            '夏季': '夏季天气炎热，建议携带防晒用品，注意防暑降温，可选择水上活动或高海拔地区。',
            '秋季': '秋天是旅游的黄金季节，气温适中，景色迷人，适合各类户外活动。',
            '冬季': '冬季天气寒冷，北方地区可能有雪景，南方地区相对温暖，可根据目的地准备合适衣物。'
        }
        
        base_suggestion = base_suggestions.get(season, '祝您旅途愉快！')
        
        # 城市特定的季节建议
        city_suggestion = self._get_city_specific_suggestion(city, season)
        
        # 结合建议
        full_suggestion = f"{base_suggestion} {city_suggestion}"
        
        # 月份特定提示
        month_tip = self._get_month_specific_tip(self.current_month, province, city)
        if month_tip:
            full_suggestion += f" {month_tip}"
        
        return full_suggestion.strip()
    
    def _suggest_alternative_spots(self, spots, during_holiday):
        """
        推荐节假日替代景点
        """
        if not during_holiday or not spots:
            return []
        
        # 复制景点列表
        spots_copy = copy.deepcopy(spots)
        
        # 为每个景点计算替代推荐分数
        for spot in spots_copy:
            # 计算综合分数
            crowd_score = self._calculate_crowd_score(spot, True)
            popularity_score = self._calculate_popularity_score(spot, True)
            
            # 假设已有时长信息（从游玩时长判断是否适合避开人群）
            duration = spot.get('visit_duration', '约1小时')
            duration_bonus = 0
            if '小时' in duration and any(x in duration for x in ['2', '3', '4']):
                duration_bonus = 1.0  # 中等游玩时长的景点更适合避开人流
            
            # 综合替代分数
            alternative_score = crowd_score + popularity_score + duration_bonus
            spot['alternative_score'] = alternative_score
        
        # 排序，找出最适合作为替代的景点
        spots_copy.sort(key=lambda x: x.get('alternative_score', 0), reverse=True)
        
        # 只返回适合作为替代的景点
        return [spot for spot in spots_copy if spot.get('alternative_score', 0) >= 1.0]
    
    def _generate_travel_advice(self, holidays_in_range, weekends_in_range):
        """
        生成节假日出行建议
        """
        advice = []
        
        # 节假日建议
        if holidays_in_range:
            advice.append('节假日期间景区人流量大，建议提前预订门票和住宿。')
            advice.append('出行前关注目的地天气和交通情况，合理安排行程。')
            advice.append('考虑错峰出行，避开假期首尾两天的交通高峰。')
            advice.append('热门景点建议早去或晚去，避开中午人流高峰。')
        
        # 周末建议
        if weekends_in_range:
            advice.append('周末市区景点人流量较大，可考虑周边郊区景点。')
            advice.append('提前规划交通路线，避开周末拥堵路段。')
        
        # 通用建议
        if advice:
            advice.append('随身携带必要证件、常用药品和防晒雨具。')
            advice.append('尊重当地风俗习惯，文明旅游。')
        else:
            # 如果既不是节假日也不是周末
            advice.append('当前时间段是非高峰旅游期，是游览的好时机。')
            advice.append('景区人流量适中，可灵活安排行程。')
        
        return ' '.join(advice)
    
    def _estimate_crowd_level(self, holidays_in_range, weekends_in_range):
        """
        估算人流量水平
        """
        if holidays_in_range:
            return 'high'  # 高
        elif weekends_in_range:
            return 'medium'  # 中
        else:
            return 'low'  # 低
    
    def _get_city_season_boost(self, city, season):
        """
        获取特定城市在特定季节的推荐加成
        """
        # 城市季节加成字典
        city_boosts = {
            '大理市': {
                '春季': 2.0,  # 春季大理气候宜人
                '夏季': -1.0,  # 夏季多雨
                '秋季': 2.5,  # 秋季是最佳季节
                '冬季': 0.5   # 冬季阳光充足
            },
            '丽江市': {
                '春季': 1.5,
                '夏季': 0.0,
                '秋季': 2.0,
                '冬季': 0.0
            },
            '昆明市': {
                '春季': 2.0,  # 春城名不虚传
                '夏季': 1.0,
                '秋季': 1.5,
                '冬季': 1.0
            },
            '三亚市': {
                '春季': 1.0,
                '夏季': -2.0,  # 夏季炎热
                '秋季': -1.0,  # 秋季可能有台风
                '冬季': 3.0   # 冬季避寒胜地
            },
            '厦门市': {
                '春季': 1.5,
                '夏季': -1.0,
                '秋季': 2.0,
                '冬季': 1.0
            },
            '杭州市': {
                '春季': 2.5,  # 春季西湖最美
                '夏季': -1.5,  # 夏季炎热潮湿
                '秋季': 1.5,
                '冬季': -1.0   # 冬季较冷
            },
            '苏州市': {
                '春季': 2.0,
                '夏季': -1.0,
                '秋季': 1.5,
                '冬季': -0.5
            }
        }
        
        if city and city in city_boosts:
            return city_boosts[city].get(season, 0.0)
        
        return 0.0
    
    def _get_city_month_boost(self, city, month):
        """
        获取特定城市在特定月份的推荐加成
        """
        # 城市月份加成字典
        city_month_boosts = {
            '大理市': {
                3: 1.5,  # 春季花期
                4: 2.0,
                5: 1.0,
                10: 2.0,  # 秋季最佳
                11: 1.5
            },
            '丽江市': {
                3: 1.0,
                4: 1.5,
                5: 1.0,
                9: 1.5,
                10: 1.0
            },
            '杭州市': {
                3: 2.0,  # 春季
                4: 2.5,  # 春季
                9: 1.0,
                10: 1.5
            }
        }
        
        if city and city in city_month_boosts:
            return city_month_boosts[city].get(month, 0.0)
        
        return 0.0
    
    def _get_special_events_bonus(self, city, month):
        """
        获取特定城市在特定月份的特殊活动加成
        """
        # 特殊活动加成
        special_events = {
            '大理市': {
                3: 1.0,  # 大理三月街
                4: 1.0,  # 大理国际马拉松
                9: 1.0,  # 大理国际影会
                11: 1.0  # 大理国际兰花茶花博览会
            },
            '昆明市': {
                2: 1.5,  # 昆明国际花卉展
                3: 1.5,  # 昆明樱花节
                12: 1.0  # 昆明国际旅游交易会
            }
        }
        
        if city and city in special_events:
            return special_events[city].get(month, 0.0)
        
        return 0.0
    
    def _get_city_specific_suggestion(self, city, season):
        """
        获取特定城市的季节旅行建议
        """
        # 城市特定建议
        city_suggestions = {
            '大理市': {
                '春季': '大理春季气候宜人，适合游览大理古城和洱海，可欣赏油菜花田。',
                '夏季': '大理夏季多雨，建议携带雨具，苍山可能有云海景观。',
                '秋季': '大理秋季天高云淡，是游览的最佳季节，可观赏红枫。',
                '冬季': '大理冬季阳光充足，适合晒晒太阳，感受冬日温暖。'
            },
            '丽江市': {
                '春季': '丽江春季百花盛开，玉龙雪山积雪未化，景色壮观。',
                '夏季': '丽江夏季凉爽，是避暑胜地，可前往泸沽湖。',
                '秋季': '丽江秋季天气晴朗，适合徒步和摄影。',
                '冬季': '丽江冬季较冷，玉龙雪山是不错的选择。'
            },
            '昆明市': {
                '春季': '昆明春季鲜花盛开，圆通山樱花、海埂大坝赏海鸥都是不错的选择。',
                '夏季': '昆明夏季凉爽，可前往石林、九乡等景区。',
                '秋季': '昆明秋季天空湛蓝，滇池周边风景优美。',
                '冬季': '昆明冬季是红嘴鸥迁徙的季节，海埂大坝观鸟正当时。'
            }
        }
        
        if city and city in city_suggestions:
            return city_suggestions[city].get(season, '')
        
        return ''
    
    def _get_month_specific_tip(self, month, province, city):
        """
        获取特定月份的旅行提示
        """
        # 月份特定提示
        month_tips = {
            1: '1月份天气寒冷，北方地区注意保暖，南方地区较为温暖。',
            2: '2月份春节期间旅游人数多，注意错峰出行。',
            3: '3月份是赏花的好时节，可关注各地花期信息。',
            4: '4月份清明假期出行人数增多，注意交通安全。',
            5: '5月份天气转暖，是户外活动的好时机。',
            6: '6月份南方进入梅雨季，注意携带雨具。',
            7: '7月份暑假开始，亲子游增多，热门景区人流量大。',
            8: '8月份天气炎热，注意防暑降温，高海拔地区温差大。',
            9: '9月份开学季，旅游人数相对减少，是错峰出行的好时机。',
            10: '10月份国庆假期旅游高峰，建议提前规划行程。',
            11: '11月份北方地区开始降温，南方地区气候宜人。',
            12: '12月份冬季来临，北方雪景优美，南方可避寒。'
        }
        
        return month_tips.get(month, '')
    
    def _is_holiday(self, date):
        """
        判断某一天是否是节假日
        """
        # 简化版节假日判断
        # 实际应用中，应该从API获取最新的节假日数据
        holiday_dates = self.holidays
        
        # 判断日期是否在节假日列表中
        date_str = date.strftime('%Y-%m-%d')
        return date_str in holiday_dates
    
    def _is_weekend(self, date):
        """
        判断某一天是否是周末
        """
        return date.weekday() >= 5  # 5是周六，6是周日
    
    def _is_holiday_or_weekend(self, date):
        """
        判断某一天是否是节假日或周末
        """
        return self._is_holiday(date) or self._is_weekend(date)
    
    def _load_holidays(self):
        """
        加载节假日数据
        """
        # 简化版节假日数据
        # 实际应用中，应该从API获取最新的节假日数据
        holidays = {
            # 2024年节假日
            '2024-01-01': '元旦',
            '2024-01-20': '春节',
            '2024-01-21': '春节',
            '2024-01-22': '春节',
            '2024-01-23': '春节',
            '2024-01-24': '春节',
            '2024-01-25': '春节',
            '2024-01-26': '春节',
            '2024-02-10': '元宵节',
            '2024-04-04': '清明节',
            '2024-04-05': '清明节',
            '2024-04-06': '清明节',
            '2024-05-01': '劳动节',
            '2024-05-02': '劳动节',
            '2024-05-03': '劳动节',
            '2024-05-04': '劳动节',
            '2024-05-05': '劳动节',
            '2024-06-10': '端午节',
            '2024-09-15': '中秋节',
            '2024-09-16': '中秋节',
            '2024-09-17': '中秋节',
            '2024-10-01': '国庆节',
            '2024-10-02': '国庆节',
            '2024-10-03': '国庆节',
            '2024-10-04': '国庆节',
            '2024-10-05': '国庆节',
            '2024-10-06': '国庆节',
            '2024-10-07': '国庆节'
        }
        
        return holidays
    
    def _generate_date_range(self, start_date, end_date):
        """
        生成日期范围
        """
        date_range = []
        current_date = start_date
        
        while current_date <= end_date:
            date_range.append(current_date)
            current_date += datetime.timedelta(days=1)
        
        return date_range
    
    def update_holidays_data(self, new_holidays):
        """
        更新节假日数据
        """
        if isinstance(new_holidays, dict):
            self.holidays.update(new_holidays)
    
    def set_travel_dates(self, start_date, end_date):
        """
        设置旅行日期，用于更准确的季节性和节假日推荐
        """
        # 更新当前日期为旅行开始日期
        self.current_date = start_date
        self.current_month = start_date.month
        self.current_season = self._get_season(start_date.month)
    
    def optimize_plan_for_weather(self, daily_plans, weather_forecast=None):
        """
        根据天气优化行程安排
        """
        if not daily_plans or not weather_forecast:
            return daily_plans
        
        optimized_plans = []
        
        for day_plan in daily_plans:
            # 获取当天的天气预报
            day_weather = weather_forecast.get(f'day_{day_plan["day"]}', {})
            weather_condition = day_weather.get('condition', '').lower()
            
            # 根据天气调整行程
            if '雨' in weather_condition or '雪' in weather_condition:
                # 下雨天调整：优先安排室内景点
                optimized_spots = self._reorder_spots_for_rainy_day(day_plan['spots'])
                optimized_day_plan = self._regenerate_plan_with_new_order(day_plan, optimized_spots)
                optimized_plans.append(optimized_day_plan)
            elif '晴' in weather_condition:
                # 晴天：优先安排需要好天气的景点
                optimized_spots = self._reorder_spots_for_sunny_day(day_plan['spots'])
                optimized_day_plan = self._regenerate_plan_with_new_order(day_plan, optimized_spots)
                optimized_plans.append(optimized_day_plan)
            elif '阴' in weather_condition:
                # 阴天：平衡安排室内外景点
                optimized_spots = self._reorder_spots_for_cloudy_day(day_plan['spots'])
                optimized_day_plan = self._regenerate_plan_with_new_order(day_plan, optimized_spots)
                optimized_plans.append(optimized_day_plan)
            else:
                # 保持原计划
                optimized_plans.append(day_plan)
        
        return optimized_plans
    
    def _reorder_spots_for_rainy_day(self, spots):
        """
        雨天景点重排序
        """
        # 室内景点在前，室外景点在后
        indoor_spots = []
        outdoor_spots = []
        
        indoor_types = ['博物馆', '美术馆', '展览馆', '商场', '剧院', '寺庙', '室内']
        
        for spot in spots:
            spot_type = spot.get('type', '').lower()
            is_indoor = any(indoor_type in spot_type for indoor_type in indoor_types)
            
            if is_indoor:
                indoor_spots.append(spot)
            else:
                outdoor_spots.append(spot)
        
        # 室内景点中，也按照地理邻近性排序
        # 这里简化处理，实际应该调用路线规划模块的排序方法
        return indoor_spots + outdoor_spots
    
    def _reorder_spots_for_sunny_day(self, spots):
        """
        晴天景点重排序
        """
        # 室外景点在前，室内景点在后
        outdoor_spots = []
        indoor_spots = []
        
        indoor_types = ['博物馆', '美术馆', '展览馆', '商场', '剧院', '室内']
        
        for spot in spots:
            spot_type = spot.get('type', '').lower()
            is_indoor = any(indoor_type in spot_type for indoor_type in indoor_types)
            
            if is_indoor:
                indoor_spots.append(spot)
            else:
                outdoor_spots.append(spot)
        
        # 室外景点优先安排需要好天气的
        return outdoor_spots + indoor_spots
    
    def _reorder_spots_for_cloudy_day(self, spots):
        """
        阴天景点重排序
        """
        # 平衡安排，将部分室外景点和室内景点交替
        indoor_spots = []
        outdoor_spots = []
        
        indoor_types = ['博物馆', '美术馆', '展览馆', '商场', '剧院', '室内']
        
        for spot in spots:
            spot_type = spot.get('type', '').lower()
            is_indoor = any(indoor_type in spot_type for indoor_type in indoor_types)
            
            if is_indoor:
                indoor_spots.append(spot)
            else:
                outdoor_spots.append(spot)
        
        # 平衡排列
        result = []
        i, j = 0, 0
        
        # 优先安排一个室外景点
        if outdoor_spots:
            result.append(outdoor_spots[i])
            i += 1
        
        # 交替安排
        while i < len(outdoor_spots) and j < len(indoor_spots):
            if len(result) % 2 == 1:
                # 奇数位置后放室内景点
                result.append(indoor_spots[j])
                j += 1
            else:
                # 偶数位置后放室外景点
                result.append(outdoor_spots[i])
                i += 1
        
        # 添加剩余的景点
        result.extend(outdoor_spots[i:])
        result.extend(indoor_spots[j:])
        
        return result
    
    def _regenerate_plan_with_new_order(self, day_plan, new_spots_order):
        """
        根据新的景点顺序重新生成行程
        """
        # 这里简化处理，实际应该调用路线规划模块的方法重新生成详细行程
        # 这里仅更新景点顺序和摘要
        
        new_day_plan = copy.deepcopy(day_plan)
        new_day_plan['spots'] = new_spots_order
        
        # 简化的摘要更新
        new_day_plan['summary'] = f"根据天气调整后的行程，包含{len(new_spots_order)}个景点"
        
        return new_day_plan