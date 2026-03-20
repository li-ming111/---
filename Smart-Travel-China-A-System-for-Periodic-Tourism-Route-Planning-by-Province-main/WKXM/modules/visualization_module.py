import json
import os
import base64
from datetime import datetime

class VisualizationModule:
    def __init__(self, api_key=None):
        """
        初始化可视化模块
        api_key: 高德地图API密钥
        """
        self.api_key = api_key
    
    def generate_map_html(self, itinerary, api_key=None):
        """
        生成高德地图HTML页面
        itinerary: 行程数据
        api_key: 高德地图API密钥（优先使用实例中设置的）
        """
        key = api_key or self.api_key
        
        # 提取景点坐标和信息
        markers = []
        paths = []
        
        # 处理每日行程
        for day_index, daily in enumerate(itinerary.get('daily_itineraries', []), 1):
            day_markers = []
            
            # 处理每个景点
            for item in daily.get('schedule', []):
                spot = item.get('spot', {})
                if spot:
                    marker = {
                        'id': spot.get('id', f"spot_{day_index}_{len(day_markers) + 1}"),
                        'name': spot.get('name', '未命名景点'),
                        'type': spot.get('type', '其他'),
                        'latitude': spot.get('latitude', 39.9042),  # 默认北京坐标
                        'longitude': spot.get('longitude', 116.4074),
                        'day': day_index,
                        'arrival_time': item.get('arrival_time', ''),
                        'departure_time': item.get('departure_time', ''),
                        'duration': item.get('duration_hours', 0)
                    }
                    day_markers.append(marker)
                    markers.append(marker)
            
            # 添加路径
            if len(day_markers) > 1:
                day_path = {
                    'path': [[m['longitude'], m['latitude']] for m in day_markers],
                    'day': day_index
                }
                paths.append(day_path)
        
        # 生成HTML内容
        html_content = self._create_map_html(markers, paths, key, itinerary)
        
        return html_content
    
    def export_to_json(self, itinerary, file_path=None):
        """
        导出行程为JSON格式
        itinerary: 行程数据
        file_path: 保存路径，如果为None则返回JSON字符串
        """
        # 准备导出数据
        export_data = {
            'type': '智游中国行程规划',
            'version': '1.0',
            'export_time': datetime.now().strftime('%Y-%m-%d %H:%M:%S'),
            'data': itinerary
        }
        
        json_data = json.dumps(export_data, ensure_ascii=False, indent=2)
        
        if file_path:
            # 确保目录存在
            os.makedirs(os.path.dirname(os.path.abspath(file_path)), exist_ok=True)
            # 写入文件
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(json_data)
            return True
        else:
            return json_data
    
    def export_to_text(self, itinerary, file_path=None):
        """
        导出行程为纯文本格式
        itinerary: 行程数据
        file_path: 保存路径，如果为None则返回文本字符串
        """
        text_lines = []
        
        # 标题
        text_lines.append("智游中国 - 行程规划报告")
        text_lines.append("=" * 50)
        
        # 基本信息
        text_lines.append(f"目的地: {itinerary.get('destination', '未知')}")
        text_lines.append(f"行程天数: {itinerary.get('total_days', 0)}天")
        text_lines.append(f"生成时间: {itinerary.get('generation_time', '')}")
        text_lines.append(f"导出时间: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
        text_lines.append("")
        
        # 季节信息
        if itinerary.get('seasonal_info'):
            text_lines.append("【季节信息】")
            seasonal = itinerary['seasonal_info']
            if seasonal.get('season_suggestion'):
                text_lines.append(f"  {seasonal['season_suggestion']}")
            text_lines.append("")
        
        # 行程概览
        text_lines.append("【行程概览】")
        text_lines.append(itinerary.get('overall_summary', '暂无概述'))
        text_lines.append("")
        
        # 每日行程
        text_lines.append("【每日行程】")
        for daily in itinerary.get('daily_itineraries', []):
            text_lines.append(f"\n第{daily.get('day', 0)}天 - {daily.get('summary', '')}")
            text_lines.append(f"日期建议: {daily.get('date_suggestion', '')}")
            text_lines.append(f"天气提示: {daily.get('weather_tips', '请关注天气预报')}")
            
            text_lines.append("\n行程安排:")
            for item in daily.get('schedule', []):
                text_lines.append(f"  {item.get('arrival_time', '')} - {item.get('departure_time', '')}: {item.get('spot', {}).get('name', '')}")
                text_lines.append(f"    景点类型: {item.get('spot', {}).get('type', '')}")
                text_lines.append(f"    游玩时长: {item.get('duration_hours', 0)}小时")
                if 'transportation' in item:
                    text_lines.append(f"    交通方式: {item['transportation']}")
            
            text_lines.append("\n餐饮推荐:")
            for dining in daily.get('dining_suggestions', []):
                text_lines.append(f"  {dining.get('type', '')}: {dining.get('name', '')} ({dining.get('reason', '')})")
            
            if daily.get('accommodation_suggestion'):
                text_lines.append(f"\n住宿建议: {daily['accommodation_suggestion']}")
            
            if daily.get('dressing_advice'):
                text_lines.append(f"穿衣建议: {daily['dressing_advice']}")
            
            if daily.get('daily_tips'):
                text_lines.append(f"今日贴士: {daily['daily_tips']}")
        
        # 旅行贴士
        text_lines.append("\n【旅行贴士】")
        for tip in itinerary.get('travel_tips', []):
            text_lines.append(f"- {tip}")
        
        # 紧急联系
        text_lines.append("\n【紧急联系】")
        for contact in itinerary.get('essential_contacts', []):
            text_lines.append(f"- {contact}")
        
        # 打包建议
        text_lines.append("\n【打包建议】")
        for category, items in itinerary.get('packing_suggestions', {}).items():
            text_lines.append(f"{category}:")
            for item in items:
                text_lines.append(f"  - {item}")
        
        text_content = '\n'.join(text_lines)
        
        if file_path:
            # 确保目录存在
            os.makedirs(os.path.dirname(os.path.abspath(file_path)), exist_ok=True)
            # 写入文件
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(text_content)
            return True
        else:
            return text_content
    
    def generate_shareable_link(self, itinerary):
        """
        生成可分享的行程链接（基础实现，可以扩展为实际的分享功能）
        itinerary: 行程数据
        """
        # 这里只是一个示例实现
        # 实际应用中，可以将行程数据保存到服务器，然后生成唯一的访问链接
        
        # 简单生成行程摘要
        destination = itinerary.get('destination', 'unknown')
        days = itinerary.get('total_days', 0)
        
        # 生成基础64编码的行程ID（实际应用中应该使用服务器端存储）
        itinerary_id = f"{destination}_{days}d_{datetime.now().strftime('%Y%m%d%H%M%S')}"
        encoded_id = base64.urlsafe_b64encode(itinerary_id.encode()).decode()[:16]
        
        # 生成分享链接
        share_link = f"https://zhiyou.example.com/share/{encoded_id}"
        
        return {
            'link': share_link,
            'qr_code_data': f"行程: {destination}{days}日游",
            'expire_time': None  # 可以设置过期时间
        }
    
    def _create_map_html(self, markers, paths, api_key, itinerary_data):
        """
        创建高德地图HTML页面
        """
        # 准备JavaScript数据
        markers_json = json.dumps(markers, ensure_ascii=False)
        paths_json = json.dumps(paths, ensure_ascii=False)
        destination = itinerary_data.get('destination', '未知城市')
        
        html_template = f"""
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>智游中国 - {destination}行程地图</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/antd@5.0.0/dist/reset.css">
    <style>
        body {{
            margin: 0;
            padding: 0;
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
            background-color: #f5f5f5;
        }}
        .container {{
            display: flex;
            height: 100vh;
            width: 100%;
        }}
        .sidebar {{
            width: 320px;
            background-color: white;
            box-shadow: 2px 0 8px rgba(0,0,0,0.1);
            overflow-y: auto;
            padding: 20px;
        }}
        .map-container {{
            flex: 1;
            height: 100%;
        }}
        .header {{
            margin-bottom: 20px;
            padding-bottom: 15px;
            border-bottom: 1px solid #eee;
        }}
        .header h2 {{
            margin: 0;
            font-size: 20px;
            color: #333;
        }}
        .header p {{
            margin: 5px 0 0 0;
            color: #666;
            font-size: 14px;
        }}
        .day-tab {{
            cursor: pointer;
            padding: 10px 15px;
            margin-bottom: 10px;
            background-color: #f0f0f0;
            border-radius: 4px;
            transition: all 0.3s;
            border: 1px solid transparent;
        }}
        .day-tab:hover {{
            background-color: #e6f7ff;
            border-color: #91d5ff;
        }}
        .day-tab.active {{
            background-color: #e6f7ff;
            border-color: #1890ff;
            color: #1890ff;
        }}
        .day-info h3 {{
            margin: 0 0 10px 0;
            font-size: 16px;
            color: #333;
        }}
        .spot-item {{
            padding: 8px 0;
            border-bottom: 1px solid #f0f0f0;
            font-size: 14px;
        }}
        .spot-item:last-child {{
            border-bottom: none;
        }}
        .spot-name {{
            font-weight: 500;
            color: #333;
        }}
        .spot-time {{
            color: #666;
            font-size: 12px;
            margin: 2px 0;
        }}
        .spot-type {{
            display: inline-block;
            background-color: #f0f0f0;
            padding: 2px 8px;
            border-radius: 10px;
            font-size: 12px;
            color: #666;
            margin-top: 2px;
        }}
        .export-buttons {{
            margin-top: 20px;
            display: flex;
            gap: 10px;
        }}
        .export-btn {{
            flex: 1;
            padding: 8px 12px;
            background-color: #1890ff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
            transition: background-color 0.3s;
        }}
        .export-btn:hover {{
            background-color: #40a9ff;
        }}
        .export-btn.secondary {{
            background-color: #52c41a;
        }}
        .export-btn.secondary:hover {{
            background-color: #73d13d;
        }}
    </style>
    <script type="text/javascript" src="https://webapi.amap.com/maps?v=1.4.15&key={api_key}"></script>
    <script src="https://cdn.jsdelivr.net/npm/antd@5.0.0/dist/antd.min.js"></script>
</head>
<body>
    <div class="container">
        <div class="sidebar">
            <div class="header">
                <h2>智游中国</h2>
                <p>{destination} {itinerary_data.get('total_days', 0)}日游</p>
            </div>
            
            <div class="export-buttons">
                <button class="export-btn" onclick="exportAsText()">导出文本</button>
                <button class="export-btn secondary" onclick="exportAsJSON()">导出JSON</button>
            </div>
            
            <div class="day-tabs">
                <h3 style="font-size: 16px; margin-top: 20px; margin-bottom: 10px;">行程天数</h3>
                {self._generate_day_tabs(itinerary_data.get('daily_itineraries', []))}
            </div>
        </div>
        
        <div class="map-container" id="mapContainer"></div>
    </div>

    <script>
        // 行程数据
        const markers = {markers_json};
        const paths = {paths_json};
        const itineraryData = {json.dumps(itinerary_data, ensure_ascii=False)};
        
        // 初始化地图
        let map = new AMap.Map('mapContainer', {{
            zoom: 12,
            center: markers.length > 0 ? [markers[0].longitude, markers[0].latitude] : [116.4074, 39.9042],
            resizeEnable: true
        }});
        
        // 添加控件
        AMap.plugin(['AMap.ToolBar', 'AMap.Scale', 'AMap.OverView'], function() {{
            map.addControl(new AMap.ToolBar());
            map.addControl(new AMap.Scale());
            map.addControl(new AMap.OverView({{isOpen: true}}));
        }});
        
        // 当前显示的天数
        let currentDay = 1;
        let currentMarkers = [];
        let currentLines = [];
        
        // 显示特定天的行程
        function showDay(day) {{
            currentDay = day;
            
            // 更新标签状态
            document.querySelectorAll('.day-tab').forEach(tab => {{
                if (parseInt(tab.dataset.day) === day) {{
                    tab.classList.add('active');
                }} else {{
                    tab.classList.remove('active');
                }}
            }});
            
            // 清除当前标记和线路
            currentMarkers.forEach(marker => marker.setMap(null));
            currentLines.forEach(line => line.setMap(null));
            currentMarkers = [];
            currentLines = [];
            
            // 添加当天的标记
            const dayMarkers = markers.filter(marker => marker.day === day);
            dayMarkers.forEach((marker, index) => {{
                const content = `
                    <div style="padding: 10px; min-width: 150px;">
                        <div style="font-weight: bold; margin-bottom: 5px;">${{marker.name}}</div>
                        <div style="font-size: 12px; color: #666;">
                            <div>${{marker.arrival_time}} - ${{marker.departure_time}}</div>
                            <div>游玩时长: ${{marker.duration}}小时</div>
                            <div>类型: ${{marker.type}}</div>
                        </div>
                    </div>
                `;
                
                const mapMarker = new AMap.Marker({{
                    position: [marker.longitude, marker.latitude],
                    title: marker.name,
                    label: {{
                        content: `${{index + 1}}`,
                        direction: 'top',
                        offset: new AMap.Pixel(0, -30)
                    }}
                }});
                
                // 添加信息窗口
                const infoWindow = new AMap.InfoWindow({{
                    content: content,
                    offset: new AMap.Pixel(0, -30)
                }});
                
                mapMarker.on('click', function() {{
                    infoWindow.open(map, mapMarker.getPosition());
                }});
                
                mapMarker.setMap(map);
                currentMarkers.push(mapMarker);
            }});
            
            // 添加当天的线路
            const dayPath = paths.find(path => path.day === day);
            if (dayPath && dayPath.path.length > 1) {{
                const polyline = new AMap.Polyline({{
                    path: dayPath.path,
                    strokeColor: '#1890ff',
                    strokeWeight: 4,
                    strokeStyle: 'solid',
                    lineJoin: 'round'
                }});
                polyline.setMap(map);
                currentLines.push(polyline);
                
                // 自动调整地图视野
                map.setFitView([...currentMarkers, polyline]);
            }} else if (dayMarkers.length > 0) {{
                // 调整视野到标记点
                map.setFitView(currentMarkers);
            }}
        }}
        
        // 生成线路动画
        function animatePath(path, color = '#1890ff') {{
            // 这里可以实现线路动画效果
            console.log('路径动画:', path);
        }}
        
        // 导出为文本
        function exportAsText() {{
            const textContent = generateTextContent();
            const blob = new Blob([textContent], {{ type: 'text/plain;charset=utf-8' }});
            const url = URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = '{destination}_' + itinerary_data.get("total_days", 0) + '日游.txt';
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            URL.revokeObjectURL(url);
        }}
        
        // 导出为JSON
        function exportAsJSON() {{
            const jsonContent = JSON.stringify(itineraryData, null, 2);
            const blob = new Blob([jsonContent], {{ type: 'application/json;charset=utf-8' }});
            const url = URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = '{destination}_' + itinerary_data.get("total_days", 0) + '日游.json';
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            URL.revokeObjectURL(url);
        }}
        
        // 生成文本内容
        function generateTextContent() {{
            let text = [];
            text.push('智游中国 - 行程规划报告');
            text.push('='.repeat(50));
            text.push('');
            
            text.push(`目的地: ${{itineraryData.destination || '未知'}}`);
            text.push(`行程天数: ${{itineraryData.total_days || 0}}天`);
            text.push(`生成时间: ${{itineraryData.generation_time || ''}}`);
            text.push('');
            
            if (itineraryData.seasonal_info) {{
                text.push('【季节信息】');
                if (itineraryData.seasonal_info.season_suggestion) {{
                    text.push(`  ${{itineraryData.seasonal_info.season_suggestion}}`);
                }}
                text.push('');
            }}
            
            text.push('【行程概览】');
            text.push(itineraryData.overall_summary || '暂无概述');
            text.push('');
            
            text.push('【每日行程】');
            itineraryData.daily_itineraries.forEach(daily => {{
                text.push(`\n第${{daily.day || 0}}天 - ${{daily.summary || ''}}`);
                text.push(`日期建议: ${{daily.date_suggestion || ''}}`);
                text.push(`天气提示: ${{daily.weather_tips || '请关注天气预报'}}`);
                
                text.push('\n行程安排:');
                daily.schedule.forEach(item => {{
                    text.push(`  ${{item.arrival_time || ''}} - ${{item.departure_time || ''}}: ${{item.spot ? item.spot.name : ''}}`);
                    text.push(`    景点类型: ${{item.spot ? item.spot.type : ''}}`);
                    text.push(`    游玩时长: ${{item.duration_hours || 0}}小时`);
                    if (item.transportation) {{
                        text.push(`    交通方式: ${{item.transportation}}`);
                    }}
                }});
            }});
            
            return text.join('\n');
        }}
        
        // 初始化显示第一天
        showDay(1);
        
        // 添加窗口事件监听器
        window.addEventListener('resize', function() {{
            map.resize();
        }});
    </script>
</body>
</html>
"""
        
        return html_template
    
    def _generate_day_tabs(self, daily_itineraries):
        """
        生成天数标签的HTML
        """
        tabs_html = []
        
        for index, daily in enumerate(daily_itineraries, 1):
            tab_class = 'day-tab' + (' active' if index == 1 else '')
            
            tab_html = f"""
            <div class="{tab_class}" data-day="{index}" onclick="showDay({index})">
                <div class="day-info">
                    <h3>第{index}天</h3>
                    <p style="margin: 0; font-size: 12px; color: #666;">{daily.get('summary', '')}</p>
                    <div style="margin-top: 5px;">
                        {self._generate_spot_list_preview(daily.get('schedule', []))}
                    </div>
                </div>
            </div>
            """
            
            tabs_html.append(tab_html)
        
        return '\n'.join(tabs_html)
    
    def _generate_spot_list_preview(self, schedule):
        """
        生成景点列表预览
        """
        spot_items = []
        
        # 最多显示前3个景点
        for item in schedule[:3]:
            spot = item.get('spot', {})
            if spot:
                item_html = f"""
                <div class="spot-item">
                    <div class="spot-name">{spot.get('name', '')}</div>
                    <div class="spot-time">{item.get('arrival_time', '')} - {item.get('departure_time', '')}</div>
                    <span class="spot-type">{spot.get('type', '')}</span>
                </div>
                """
                spot_items.append(item_html)
        
        # 如果景点超过3个，显示更多提示
        if len(schedule) > 3:
            spot_items.append(f"\n<div style='font-size: 12px; color: #666; text-align: center; padding: 5px;'>+{len(schedule) - 3} 个景点</div>")
        
        return ''.join(spot_items)
    
    def generate_simple_map_html(self, itinerary, api_key=None):
        """
        生成简化版的地图HTML（不包含复杂的界面，只显示地图和行程）
        itinerary: 行程数据
        api_key: 高德地图API密钥
        """
        key = api_key or self.api_key
        
        # 提取所有景点坐标
        all_points = []
        spot_names = []
        
        for daily in itinerary.get('daily_itineraries', []):
            for item in daily.get('schedule', []):
                spot = item.get('spot', {})
                if 'latitude' in spot and 'longitude' in spot:
                    all_points.append([spot['longitude'], spot['latitude']])
                    spot_names.append(spot.get('name', ''))
        
        # 生成简化的HTML
        simple_html = f"""
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>行程地图 - {itinerary.get('destination', '')}</title>
    <style>
        body, html {{
            margin: 0;
            padding: 0;
            height: 100%;
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
        }}
        #simpleMap {{
            height: 100%;
            width: 100%;
        }}
        .title-bar {{
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            background: rgba(255, 255, 255, 0.95);
            padding: 10px 20px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.1);
            z-index: 100;
        }}
        .title-bar h3 {{
            margin: 0;
            font-size: 18px;
            color: #333;
        }}
        .title-bar p {{
            margin: 5px 0 0 0;
            font-size: 14px;
            color: #666;
        }}
    </style>
    <script type="text/javascript" src="https://webapi.amap.com/maps?v=1.4.15&key={key}"></script>
</head>
<body>
    <div class="title-bar">
        <h3>智游中国 - {itinerary.get('destination', '')}</h3>
        <p>{itinerary.get('total_days', 0)}日游 · 共{len(all_points)}个景点</p>
    </div>
    <div id="simpleMap" style="margin-top: 70px;"></div>

    <script>
        // 初始化地图
        var map = new AMap.Map('simpleMap', {{
            zoom: 12,
            center: {all_points[0] or [116.4074, 39.9042]},
            resizeEnable: true
        }});
        
        // 添加控件
        AMap.plugin(['AMap.ToolBar', 'AMap.Scale'], function() {{
            map.addControl(new AMap.ToolBar());
            map.addControl(new AMap.Scale());
        }});
        
        // 添加景点标记和线路
        var markers = [];
        
        // 添加景点标记
        var points = {json.dumps(all_points)};
        var names = {json.dumps(spot_names)};
        
        // 绘制线路
        if (points.length > 1) {{
            var polyline = new AMap.Polyline({{
                path: points,
                strokeColor: '#1890ff',
                strokeWeight: 4,
                strokeStyle: 'solid'
            }});
            polyline.setMap(map);
        }}
        
        // 添加标记点
        for (var i = 0; i < points.length; i++) {{
            var marker = new AMap.Marker({{
                position: points[i], // 修正为正确的坐标格式
                title: names[i],
                label: {{
                    content: String(i + 1),
                    direction: 'top'
                }}
            }});
            
            // 添加信息窗口
            var infoWindow = new AMap.InfoWindow({{
                content: '<div style="padding: 10px;">' + names[i] + '</div>',
                offset: new AMap.Pixel(0, -30)
            }});
            
            marker.on('click', function(e) {{
                infoWindow.open(map, e.target.getPosition());
            }});
            
            marker.setMap(map);
            markers.push(marker);
        }}
        
        // 调整地图视野
        if (markers.length > 0) {{
            map.setFitView(markers);
        }}
    </script>
</body>
</html>
        """
        
        return simple_html

# 示例使用
# if __name__ == "__main__":
#     # 假设的行程数据
#     mock_itinerary = {{
#         'destination': '大理市',
#         'total_days': 3,
#         'generation_time': '2024-01-01 10:00:00',
#         'daily_itineraries': [
#             {{
#                 'day': 1,
#                 'summary': '大理古城一日游',
#                 'schedule': [
#                     {{
#                         'arrival_time': '09:00',
#                         'departure_time': '12:00',
#                         'spot': {{'name': '大理古城', 'type': '古城', 'longitude': 100.2386, 'latitude': 25.6057}},
#                         'duration_hours': 3
#                     }}
#                 ]
#             }}
#         ]
#     }}
#     
#     viz = VisualizationModule(api_key='your_amap_api_key')
#     html = viz.generate_map_html(mock_itinerary)
#     print(html)