import math
import numpy as np
from datetime import datetime, timedelta
from geopy.distance import geodesic
import random
import copy
from .api_integration import APIIntegration

class RoutePlanningModule:
    def __init__(self):
        # 初始化API集成模块
        self.api = APIIntegration()
        # 每天规划的景点数量范围
        self.spots_per_day_range = (2, 4)
        # 每天的可用时间范围（小时）
        self.daily_time_range = (8.5, 17.0)  # 8:30 - 17:00
        # 景点间移动的平均时间（小时）
        self.avg_transfer_time = 0.5
        # 主题权重配置
        self.theme_weight = 0.3
        # 地理邻近性权重配置
        self.geography_weight = 0.7
        # 每天的默认起始和结束地点（用于住宿位置）
        self.daily_start_point = None
        self.daily_end_point = None
    
    def plan_route(self, spots, days, preferences=None):
        """
        规划旅游路线
        """
        if not spots or days <= 0:
            return []
        
        # 复制景点列表，避免修改原始数据
        spots_copy = copy.deepcopy(spots)
        
        # 计算每天应该安排的景点数量
        total_spots = len(spots_copy)
        daily_plans = []
        
        # 如果景点数量不足以填满天数，每天安排2-3个景点
        if total_spots <= days * self.spots_per_day_range[0]:
            daily_plan_count = min(total_spots, days * self.spots_per_day_range[0])
            spots_per_day = max(1, daily_plan_count // days)
            
            # 分配景点到每天
            remaining_spots = spots_copy
            for day in range(days):
                if not remaining_spots:
                    # 如果没有景点了，结束循环
                    break
                
                # 当天安排的景点数
                day_spots_count = min(spots_per_day, len(remaining_spots))
                # 简单分配：从剩余景点中取前N个
                day_spots = remaining_spots[:day_spots_count]
                remaining_spots = remaining_spots[day_spots_count:]
                
                # 对当天景点进行排序
                sorted_day_spots = self._sort_spots_by_proximity(day_spots)
                
                # 生成当天的行程安排
                daily_plan = self._generate_daily_plan(sorted_day_spots, day + 1)
                daily_plans.append(daily_plan)
            
            # 如果还有剩余天数，生成空的行程安排
            for day in range(len(daily_plans), days):
                daily_plan = {
                    'day': day + 1,
                    'spots': [],
                    'schedule': [],
                    'summary': '自由活动或休息'
                }
                daily_plans.append(daily_plan)
        else:
            # 使用更复杂的算法进行优化规划
            daily_plans = self._optimize_route_planning(spots_copy, days, preferences)
        
        return daily_plans
    
    def _optimize_route_planning(self, spots, days, preferences):
        """
        优化路线规划算法，考虑地理邻近性、主题一致性和时间约束
        """
        daily_plans = []
        remaining_spots = spots
        
        # 计算每天应该安排的景点数量
        min_spots_per_day = self.spots_per_day_range[0]
        max_spots_per_day = min(self.spots_per_day_range[1], len(spots) // days + 1)
        
        # 第一天的起始点设置（通常是住宿区域附近）
        if remaining_spots:
            # 选择评分最高的景点作为起始参考点
            first_day_start_spot = max(remaining_spots, key=lambda x: float(x.get('rating', '0')))
            self.daily_start_point = self._get_spot_location(first_day_start_spot)
            self.daily_end_point = self.daily_start_point  # 结束点也设为住宿区域
        
        for day in range(days):
            if not remaining_spots:
                # 如果没有景点了，生成空行程
                daily_plan = {
                    'day': day + 1,
                    'spots': [],
                    'schedule': [],
                    'summary': '自由活动或休息'
                }
                daily_plans.append(daily_plan)
                continue
            
            # 当天安排的景点数（随机选择一个合理数量）
            day_spots_count = random.randint(min_spots_per_day, max_spots_per_day)
            day_spots_count = min(day_spots_count, len(remaining_spots))
            
            # 使用贪心算法选择当天的景点
            day_spots = self._select_day_spots(remaining_spots, day_spots_count, preferences)
            
            # 从剩余景点中移除已选择的景点
            remaining_spots = [spot for spot in remaining_spots if spot not in day_spots]
            
            # 对当天景点进行排序
            sorted_day_spots = self._sort_spots_optimally(day_spots)
            
            # 生成当天的行程安排
            daily_plan = self._generate_daily_plan(sorted_day_spots, day + 1)
            daily_plans.append(daily_plan)
            
            # 更新次日的起始点（当天最后一个景点）
            if sorted_day_spots:
                self.daily_start_point = self._get_spot_location(sorted_day_spots[-1])
        
        # 如果还有剩余景点，尝试添加到现有行程中
        if remaining_spots:
            daily_plans = self._fill_remaining_spots(daily_plans, remaining_spots)
        
        return daily_plans
    
    def _select_day_spots(self, spots, count, preferences):
        """
        选择当天要游览的景点
        """
        if len(spots) <= count:
            return spots[:count]
        
        selected_spots = []
        remaining_spots = spots.copy()
        
        # 首先选择离起始点最近的景点
        if self.daily_start_point:
            closest_spot = min(remaining_spots, key=lambda x: self._calculate_distance(
                self.daily_start_point, self._get_spot_location(x)
            ))
            selected_spots.append(closest_spot)
            remaining_spots.remove(closest_spot)
            count -= 1
        
        # 继续选择景点，考虑多种因素
        while count > 0 and remaining_spots:
            # 为每个剩余景点计算分数
            spot_scores = []
            for spot in remaining_spots:
                score = 0
                
                # 1. 地理邻近性分数（与已选最后一个景点的距离）
                if selected_spots:
                    last_spot_loc = self._get_spot_location(selected_spots[-1])
                    current_spot_loc = self._get_spot_location(spot)
                    distance = self._calculate_distance(last_spot_loc, current_spot_loc)
                    # 距离越近，分数越高（0-10分）
                    proximity_score = max(0, 10 - (distance / 5) * 10)  # 5公里内满分
                    score += proximity_score * self.geography_weight
                
                # 2. 主题一致性分数
                if selected_spots and preferences:
                    theme_score = self._calculate_theme_score(spot, selected_spots, preferences)
                    score += theme_score * self.theme_weight
                
                # 3. 景点评分分数
                rating = float(spot.get('rating', '0'))
                score += rating * 2
                
                # 4. 游玩时长适宜性分数
                visit_duration = self._parse_visit_duration(spot.get('visit_duration', '约1小时'))
                # 避免选择游玩时太长的景点
                duration_score = max(0, 5 - (visit_duration - 2) * 2) if visit_duration > 2 else 5
                score += duration_score
                
                spot_scores.append((spot, score))
            
            # 选择分数最高的景点
            if spot_scores:
                spot_scores.sort(key=lambda x: x[1], reverse=True)
                best_spot = spot_scores[0][0]
                selected_spots.append(best_spot)
                remaining_spots.remove(best_spot)
                count -= 1
            else:
                break
        
        return selected_spots
    
    def _sort_spots_by_proximity(self, spots):
        """
        简单按地理邻近性排序景点
        """
        if not spots:
            return []
        
        sorted_spots = []
        remaining_spots = spots.copy()
        
        # 起始点选择（如果有起始点设置）
        if self.daily_start_point:
            current_loc = self.daily_start_point
        else:
            # 选择评分最高的景点作为起点
            start_spot = max(remaining_spots, key=lambda x: float(x.get('rating', '0')))
            sorted_spots.append(start_spot)
            remaining_spots.remove(start_spot)
            current_loc = self._get_spot_location(start_spot)
        
        # 贪心算法排序剩余景点
        while remaining_spots:
            # 找到离当前位置最近的景点
            closest_spot = min(remaining_spots, key=lambda x: self._calculate_distance(
                current_loc, self._get_spot_location(x)
            ))
            sorted_spots.append(closest_spot)
            remaining_spots.remove(closest_spot)
            current_loc = self._get_spot_location(closest_spot)
        
        return sorted_spots
    
    def _sort_spots_optimally(self, spots):
        """
        综合考虑多种因素对景点进行排序
        """
        if len(spots) <= 2:
            return self._sort_spots_by_proximity(spots)
        
        # 使用旅行商问题的启发式算法（最近邻算法）
        sorted_spots = []
        remaining_spots = spots.copy()
        
        # 起始点
        if self.daily_start_point:
            start_spot = min(remaining_spots, key=lambda x: self._calculate_distance(
                self.daily_start_point, self._get_spot_location(x)
            ))
        else:
            start_spot = max(remaining_spots, key=lambda x: float(x.get('rating', '0')))
        
        sorted_spots.append(start_spot)
        remaining_spots.remove(start_spot)
        
        # 构建路线
        while remaining_spots:
            last_spot = sorted_spots[-1]
            next_spot = min(remaining_spots, key=lambda x: self._calculate_distance(
                self._get_spot_location(last_spot), self._get_spot_location(x)
            ))
            sorted_spots.append(next_spot)
            remaining_spots.remove(next_spot)
        
        # 检查结束点是否合理（离住宿区域不要太远）
        if self.daily_end_point and len(sorted_spots) > 1:
            # 计算当前最后一个景点到结束点的距离
            last_spot_dist = self._calculate_distance(
                self._get_spot_location(sorted_spots[-1]), self.daily_end_point
            )
            # 计算倒数第二个景点到结束点的距离
            second_last_dist = self._calculate_distance(
                self._get_spot_location(sorted_spots[-2]), self.daily_end_point
            )
            # 如果倒数第二个景点更近，交换顺序
            if second_last_dist < last_spot_dist * 0.7:
                sorted_spots[-1], sorted_spots[-2] = sorted_spots[-2], sorted_spots[-1]
        
        return sorted_spots
    
    def _generate_daily_plan(self, spots, day_number):
        """
        生成当天的详细行程安排
        """
        schedule = []
        
        # 开始时间（默认8:30）
        current_time = datetime.now().replace(hour=int(self.daily_time_range[0]), 
                                             minute=int((self.daily_time_range[0] % 1) * 60), 
                                             second=0, microsecond=0)
        
        # 为每个景点分配时间
        for i, spot in enumerate(spots):
            # 景点游玩时长
            visit_duration = self._parse_visit_duration(spot.get('visit_duration', '约1小时'))
            
            # 到达时间
            arrival_time = current_time
            
            # 离开时间
            departure_time = arrival_time + timedelta(hours=visit_duration)
            
            # 添加景点到行程
            spot_schedule = {
                'spot': spot,
                'arrival_time': arrival_time.strftime('%H:%M'),
                'departure_time': departure_time.strftime('%H:%M'),
                'duration_hours': visit_duration,
                'sequence': i + 1
            }
            
            # 添加交通建议
            if i > 0:
                prev_spot = spots[i-1]
                spot_schedule['transportation'] = self._generate_transportation_suggestion(
                    prev_spot, spot
                )
            else:
                # 从住宿地到第一个景点的交通
                spot_schedule['transportation'] = '从住宿地出发，建议使用当地交通'
            
            schedule.append(spot_schedule)
            
            # 更新当前时间，加上移动时间
            if i < len(spots) - 1:
                current_time = departure_time + timedelta(hours=self.avg_transfer_time)
            else:
                current_time = departure_time
        
        # 生成当天行程摘要
        summary = self._generate_daily_summary(spots)
        
        daily_plan = {
            'day': day_number,
            'spots': spots,
            'schedule': schedule,
            'summary': summary,
            'start_time': schedule[0]['arrival_time'] if schedule else '08:30',
            'end_time': schedule[-1]['departure_time'] if schedule else '17:00'
        }
        
        return daily_plan
    
    def _generate_transportation_suggestion(self, from_spot, to_spot):
        """
        生成两个景点之间的交通建议
        """
        # 计算距离
        distance = self._calculate_distance(
            self._get_spot_location(from_spot),
            self._get_spot_location(to_spot)
        )
        
        # 根据距离生成交通建议
        if distance < 1.0:
            return '步行，约{}分钟'.format(int(distance * 15))  # 假设步行速度4km/h
        elif distance < 3.0:
            return '建议骑共享单车，约{}分钟'.format(int(distance * 8))  # 假设骑行速度7.5km/h
        elif distance < 10.0:
            return '建议乘坐公交车或打车，约{}分钟'.format(int(distance * 2))
        else:
            return '建议打车或乘坐公共交通，约{}分钟'.format(int(distance * 1.5))
    
    def _generate_daily_summary(self, spots):
        """
        生成当天行程的摘要
        """
        if not spots:
            return '当天没有安排景点'
        
        # 统计景点类型
        types = set(spot.get('type', '') for spot in spots)
        type_str = '、'.join(types) if types else '各类'
        
        # 计算总游玩时长
        total_duration = sum(self._parse_visit_duration(spot.get('visit_duration', '约1小时')) for spot in spots)
        
        # 生成摘要
        summary = f"当天安排了{len(spots)}个{type_str}景点，总游玩时长约{total_duration:.1f}小时。"
        
        # 如果有特别推荐的景点（评分高的）
        highly_rated_spots = [spot for spot in spots if float(spot.get('rating', '0')) >= 4.5]
        if highly_rated_spots:
            recommended_names = [spot['name'] for spot in highly_rated_spots]
            summary += f" 特别推荐：{'、'.join(recommended_names)}。"
        
        return summary
    
    def _calculate_distance(self, loc1, loc2):
        """
        计算两个位置之间的距离（公里）
        """
        if not all([loc1[0], loc1[1], loc2[0], loc2[1]]):
            return float('inf')  # 如果位置信息不完整，返回极大值
        
        # 使用geodesic计算实际地理距离
        try:
            distance = geodesic(loc1, loc2).kilometers
            return distance
        except:
            # 备用：使用Haversine公式
            return self._haversine_distance(loc1, loc2)
    
    def _haversine_distance(self, loc1, loc2):
        """
        使用Haversine公式计算两个经纬度点之间的距离
        """
        # 地球半径（公里）
        R = 6371.0
        
        lat1, lon1 = math.radians(loc1[0]), math.radians(loc1[1])
        lat2, lon2 = math.radians(loc2[0]), math.radians(loc2[1])
        
        dlon = lon2 - lon1
        dlat = lat2 - lat1
        
        a = math.sin(dlat / 2)**2 + math.cos(lat1) * math.cos(lat2) * math.sin(dlon / 2)**2
        c = 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))
        
        distance = R * c
        return distance
    
    def _get_spot_location(self, spot):
        """
        获取景点的位置坐标
        """
        # 优先使用明确的经纬度
        if spot.get('latitude') and spot.get('longitude'):
            return (float(spot['latitude']), float(spot['longitude']))
        
        # 尝试从location字符串解析
        location_str = spot.get('location', '')
        if location_str and ',' in location_str:
            try:
                # 注意：location格式通常是lng,lat
                lng, lat = location_str.split(',')
                return (float(lat), float(lng))  # 返回(lat, lng)
            except:
                pass
        
        # 如果无法获取位置，返回默认值（将导致距离计算不准确）
        return (0, 0)
    
    def _parse_visit_duration(self, duration_str):
        """
        解析游玩时长字符串为小时数
        """
        try:
            # 尝试直接提取数字
            import re
            numbers = re.findall(r'\d+\.?\d*', duration_str)
            if numbers:
                return float(numbers[0])
        except:
            pass
        
        # 常见时长的映射
        duration_mapping = {
            '约30分钟': 0.5,
            '约1小时': 1.0,
            '约1.5小时': 1.5,
            '约2小时': 2.0,
            '约2.5小时': 2.5,
            '约3小时': 3.0,
            '约半天': 4.0,
            '约4小时': 4.0,
            '约5小时': 5.0
        }
        
        return duration_mapping.get(duration_str, 1.0)
    
    def _calculate_theme_score(self, spot, selected_spots, preferences):
        """
        计算景点与已选景点的主题一致性分数
        """
        if not selected_spots or not preferences:
            return 0
        
        # 获取景点类型
        spot_type = spot.get('type', '')
        
        # 检查景点类型是否与任何偏好匹配
        preference_type_mapping = {
            '自然风光': ['湖泊', '山岳', '海滩', '瀑布', '森林'],
            '历史文化': ['古城', '古镇', '寺庙', '博物馆', '遗址'],
            '主题乐园': ['主题乐园', '游乐园', '海洋馆'],
            '亲子活动': ['动物园', '科技馆', '儿童乐园']
        }
        
        score = 0
        
        # 检查已选景点的主题一致性
        for selected_spot in selected_spots:
            selected_type = selected_spot.get('type', '')
            # 如果类型相同，加分
            if spot_type == selected_type:
                score += 5
            
            # 检查是否同属一个偏好类别
            for pref, types in preference_type_mapping.items():
                if spot_type in types and selected_type in types:
                    score += 3
        
        return score
    
    def _fill_remaining_spots(self, daily_plans, remaining_spots):
        """
        将剩余景点填充到现有行程中
        """
        for spot in remaining_spots:
            # 找到最合适的一天添加这个景点
            best_day_index = -1
            min_score = float('inf')
            
            for i, day_plan in enumerate(daily_plans):
                # 计算当天景点数量
                spots_count = len(day_plan['spots'])
                
                # 如果当天景点数量已达上限，跳过
                if spots_count >= self.spots_per_day_range[1]:
                    continue
                
                # 计算添加这个景点的"成本"（距离、主题多样性等）
                score = 0
                
                # 距离成本：到当天第一个和最后一个景点的平均距离
                if day_plan['spots']:
                    first_spot_dist = self._calculate_distance(
                        self._get_spot_location(spot),
                        self._get_spot_location(day_plan['spots'][0])
                    )
                    last_spot_dist = self._calculate_distance(
                        self._get_spot_location(spot),
                        self._get_spot_location(day_plan['spots'][-1])
                    )
                    score += (first_spot_dist + last_spot_dist) / 2
                
                # 多样性成本：如果类型相同，提高成本
                if any(s.get('type') == spot.get('type') for s in day_plan['spots']):
                    score += 2  # 轻微惩罚，避免同一天太多相同类型
                
                # 数量成本：优先选择景点数量少的天
                score += spots_count * 0.5
                
                # 选择成本最低的一天
                if score < min_score:
                    min_score = score
                    best_day_index = i
            
            # 如果找到合适的天，添加景点
            if best_day_index >= 0:
                # 添加景点到当天
                daily_plans[best_day_index]['spots'].append(spot)
                # 重新排序当天景点
                sorted_spots = self._sort_spots_optimally(daily_plans[best_day_index]['spots'])
                daily_plans[best_day_index]['spots'] = sorted_spots
                # 重新生成当天行程
                daily_plans[best_day_index] = self._generate_daily_plan(
                    sorted_spots, daily_plans[best_day_index]['day']
                )
        
        return daily_plans
    
    def optimize_existing_plan(self, daily_plans):
        """
        优化已有的行程计划
        """
        if not daily_plans:
            return []
        
        optimized_plans = []
        
        for day_plan in daily_plans:
            # 重新排序景点
            sorted_spots = self._sort_spots_optimally(day_plan['spots'])
            # 重新生成行程
            optimized_day_plan = self._generate_daily_plan(sorted_spots, day_plan['day'])
            optimized_plans.append(optimized_day_plan)
        
        return optimized_plans
    
    def calculate_route_statistics(self, daily_plans):
        """
        计算路线的统计信息
        """
        stats = {
            'total_days': len(daily_plans),
            'total_spots': 0,
            'total_distance_estimate': 0,
            'spots_by_type': {},
            'daily_spot_count': []
        }
        
        for day_plan in daily_plans:
            spots = day_plan['spots']
            stats['total_spots'] += len(spots)
            stats['daily_spot_count'].append(len(spots))
            
            # 计算当天的估计移动距离
            day_distance = 0
            for i in range(1, len(spots)):
                dist = self._calculate_distance(
                    self._get_spot_location(spots[i-1]),
                    self._get_spot_location(spots[i])
                )
                day_distance += dist
            stats['total_distance_estimate'] += day_distance
            
            # 统计景点类型
            for spot in spots:
                spot_type = spot.get('type', '其他')
                stats['spots_by_type'][spot_type] = stats['spots_by_type'].get(spot_type, 0) + 1
        
        return stats
    
    def validate_plan(self, daily_plans):
        """
        验证行程计划的合理性
        """
        issues = []
        
        for day_plan in daily_plans:
            spots = day_plan['spots']
            schedule = day_plan['schedule']
            
            # 检查每天的景点数量是否合理
            if len(spots) > self.spots_per_day_range[1]:
                issues.append(f"第{day_plan['day']}天安排了{len(spots)}个景点，可能过于紧凑")
            
            # 检查行程时间是否合理
            if schedule:
                start_time = datetime.strptime(schedule[0]['arrival_time'], '%H:%M')
                end_time = datetime.strptime(schedule[-1]['departure_time'], '%H:%M')
                
                # 计算行程总时长
                total_hours = (end_time - start_time).seconds / 3600
                
                # 检查是否超过合理的每天游玩时间
                if total_hours > 10:  # 超过10小时可能不合理
                    issues.append(f"第{day_plan['day']}天行程总时长为{total_hours:.1f}小时，可能过长")
                
                # 检查景点间的时间间隔是否合理
                for i in range(1, len(schedule)):
                    prev_end = datetime.strptime(schedule[i-1]['departure_time'], '%H:%M')
                    curr_start = datetime.strptime(schedule[i]['arrival_time'], '%H:%M')
                    transfer_time = (curr_start - prev_end).seconds / 3600
                    
                    if transfer_time < 0:
                        issues.append(f"第{day_plan['day']}天行程时间安排有冲突")
                    elif transfer_time > 2:  # 超过2小时的移动时间可能不合理
                        issues.append(f"第{day_plan['day']}天景点间移动时间过长（{transfer_time:.1f}小时）")
        
        return issues