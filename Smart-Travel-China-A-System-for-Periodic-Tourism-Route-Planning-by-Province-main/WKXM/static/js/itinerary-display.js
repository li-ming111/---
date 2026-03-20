/**
 * 行程数据展示组件
 * 专门负责格式化显示行程数据，包括衣食住行的详细表格布局
 */

class ItineraryDisplay {
    constructor(containerId) {
        this.container = document.getElementById(containerId);
        this.init();
    }

    init() {
        if (!this.container) {
            console.error('找不到指定的容器元素');
            return;
        }
    }

    /**
     * 渲染完整的行程数据
     * @param {Object} data - 行程数据
     */
    renderItinerary(data) {
        console.log('开始渲染行程数据:', data);
        
        // 解码Unicode转义字符
        const decodedData = this.decodeUnicodeEscapesInObject(data);
        console.log('解码后的数据:', decodedData);
        
        if (!decodedData || !decodedData.success) {
            this.renderError(decodedData?.message || '数据格式错误');
            return;
        }

        const itinerary = decodedData.itinerary_data || decodedData.itinerary || decodedData;
        
        // 清空容器
        this.container.innerHTML = '';
        
        // 创建主要布局
        this.createMainLayout(decodedData, itinerary);
        
        // 添加样式
        this.addStyles();
    }

    /**
     * 创建主布局
     */
    createMainLayout(data, itinerary) {
        const mainDiv = document.createElement('div');
        mainDiv.className = 'itinerary-container';
        
        // 添加标题区域
        mainDiv.appendChild(this.createHeaderSection(data));
        
        // 添加行程概览
        mainDiv.appendChild(this.createOverviewSection(itinerary));
        
        // 添加详细行程表格
        mainDiv.appendChild(this.createDetailedItineraryTable(itinerary));
        
        // 添加衣食住行详细信息
        mainDiv.appendChild(this.createDailyDetailsTable(itinerary));
        
        this.container.appendChild(mainDiv);
    }

    /**
     * 创建标题区域
     */
    createHeaderSection(data) {
        const header = document.createElement('div');
        header.className = 'header-section mb-8 p-6 bg-gradient-to-r from-blue-600 to-purple-600 text-white rounded-xl';
        
        const destination = data.destination || data.input_data?.city || '未知目的地';
        const days = data.total_days || data.input_data?.days || 1;
        const preferences = data.preferences_str || (data.preferences ? data.preferences.join(', ') : '');
        
        header.innerHTML = `
            <div class="text-center">
                <h1 class="text-4xl font-bold mb-2">${destination} 旅行规划</h1>
                <p class="text-xl opacity-90 mb-4">${days}天精彩行程规划</p>
                ${preferences ? `<p class="text-lg opacity-80">偏好：${preferences}</p>` : ''}
                <div class="mt-4 text-sm opacity-70">
                    <i class="fa fa-clock mr-2"></i>生成时间：${data.generation_time || new Date().toLocaleString()}
                </div>
            </div>
        `;
        
        return header;
    }

    /**
     * 创建概览区域
     */
    createOverviewSection(itinerary) {
        const overview = document.createElement('div');
        overview.className = 'overview-section mb-8';
        
        const stats = itinerary.stats || {};
        const totalSpots = this.countTotalSpots(itinerary);
        
        overview.innerHTML = `
            <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
                <div class="bg-white p-6 rounded-xl shadow-md text-center">
                    <div class="text-3xl font-bold text-blue-600 mb-2">${itinerary.daily_itineraries?.length || 0}</div>
                    <div class="text-gray-600">行程天数</div>
                </div>
                <div class="bg-white p-6 rounded-xl shadow-md text-center">
                    <div class="text-3xl font-bold text-green-600 mb-2">${totalSpots}</div>
                    <div class="text-gray-600">游览景点</div>
                </div>
                <div class="bg-white p-6 rounded-xl shadow-md text-center">
                    <div class="text-3xl font-bold text-purple-600 mb-2">${stats.estimated_budget || '待定'}</div>
                    <div class="text-gray-600">预算参考</div>
                </div>
                <div class="bg-white p-6 rounded-xl shadow-md text-center">
                    <div class="text-3xl font-bold text-orange-600 mb-2">${stats.best_season || '四季皆宜'}</div>
                    <div class="text-gray-600">最佳季节</div>
                </div>
            </div>
        `;
        
        return overview;
    }

    /**
     * 创建详细行程表格
     */
    createDetailedItineraryTable(itinerary) {
        const tableSection = document.createElement('div');
        tableSection.className = 'table-section mb-8';
        
        const table = document.createElement('table');
        table.className = 'itinerary-table w-full bg-white rounded-xl shadow-lg overflow-hidden';
        
        // 表头
        const thead = document.createElement('thead');
        thead.innerHTML = `
            <tr class="bg-gradient-to-r from-blue-500 to-purple-500 text-white">
                <th class="p-4 text-left font-semibold">日期</th>
                <th class="p-4 text-left font-semibold">主要行程</th>
                <th class="p-4 text-left font-semibold">景点安排</th>
                <th class="p-4 text-left font-semibold">住宿</th>
                <th class="p-4 text-left font-semibold">用餐</th>
            </tr>
        `;
        
        // 表身
        const tbody = document.createElement('tbody');
        
        if (itinerary.daily_itineraries) {
            itinerary.daily_itineraries.forEach((day, index) => {
                const row = document.createElement('tr');
                row.className = 'border-b border-gray-100 hover:bg-gray-50 transition-colors';
                
                const spots = day.spots?.map(spot => spot.name).join(', ') || '待安排';
                const dining = day.dining_suggestions?.map(d => `${d.time_of_day}: ${d.options?.[0]?.name || '待定'}`).join('<br>') || '待安排';
                
                row.innerHTML = `
                    <td class="p-4 font-semibold text-gray-800">第${day.day}天</td>
                    <td class="p-4">
                        <div class="font-medium text-gray-800">${day.summary || '精彩行程'}</div>
                        <div class="text-sm text-gray-600 mt-1">${day.description || ''}</div>
                    </td>
                    <td class="p-4 text-sm text-gray-700">${spots}</td>
                    <td class="p-4 text-sm text-gray-700">${day.accommodation_suggestion || '待安排'}</td>
                    <td class="p-4 text-sm text-gray-700">${dining}</td>
                `;
                
                tbody.appendChild(row);
            });
        }
        
        table.appendChild(thead);
        table.appendChild(tbody);
        tableSection.appendChild(table);
        
        return tableSection;
    }

    /**
     * 创建每日详细衣食住行表格
     */
    createDailyDetailsTable(itinerary) {
        const detailsSection = document.createElement('div');
        detailsSection.className = 'details-section';
        
        const title = document.createElement('h2');
        title.className = 'text-2xl font-bold text-gray-800 mb-6';
        title.innerHTML = '<i class="fa fa-list-ul mr-2"></i>每日详细安排';
        detailsSection.appendChild(title);
        
        if (itinerary.daily_itineraries) {
            itinerary.daily_itineraries.forEach((day, index) => {
                detailsSection.appendChild(this.createDayDetailCard(day));
            });
        }
        
        return detailsSection;
    }

    /**
     * 创建每日详细卡片
     */
    createDayDetailCard(day) {
        const card = document.createElement('div');
        card.className = 'day-detail-card bg-white rounded-xl shadow-lg mb-6 overflow-hidden';
        
        // 卡片头部
        const header = document.createElement('div');
        header.className = 'bg-gradient-to-r from-green-400 to-blue-500 text-white p-6';
        header.innerHTML = `
            <div class="flex items-center justify-between">
                <h3 class="text-2xl font-bold">第${day.day}天行程</h3>
                <div class="text-right">
                    <div class="text-lg opacity-90">${day.weather?.temperature_range || '适宜温度'}</div>
                    <div class="text-sm opacity-75">${day.weather?.condition || '天气良好'}</div>
                </div>
            </div>
            <p class="mt-2 opacity-90">${day.summary || '精彩的一天即将开始'}</p>
        `;
        
        // 详细表格内容
        const content = document.createElement('div');
        content.className = 'p-6';
        
        const detailTable = document.createElement('table');
        detailTable.className = 'w-full day-details-table';
        
        detailTable.innerHTML = `
            <tbody>
                <tr class="border-b border-gray-200">
                    <td class="py-4 px-3 font-semibold text-gray-700 w-20">
                        <i class="fa fa-sun-o text-yellow-500 mr-1"></i>穿衣
                    </td>
                    <td class="py-4 px-3">
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div>
                                <p class="text-gray-800 font-medium mb-2">穿着建议</p>
                                <p class="text-gray-600 text-sm">${day.dressing_advice || '根据天气情况合理搭配'}</p>
                            </div>
                            <div>
                                <p class="text-gray-800 font-medium mb-2">推荐单品</p>
                                <div class="flex flex-wrap gap-2">
                                    ${(day.dressing_items || ['舒适衣物']).map(item => 
                                        `<span class="px-2 py-1 bg-blue-100 text-blue-700 rounded text-sm">${item}</span>`
                                    ).join('')}
                                </div>
                            </div>
                        </div>
                    </td>
                </tr>
                
                <tr class="border-b border-gray-200">
                    <td class="py-4 px-3 font-semibold text-gray-700">
                        <i class="fa fa-cutlery text-red-500 mr-1"></i>用餐
                    </td>
                    <td class="py-4 px-3">
                        <div class="space-y-3">
                            ${(day.dining_suggestions || []).map(dining => `
                                <div class="border-l-4 border-red-200 pl-4">
                                    <h5 class="font-medium text-gray-800 mb-1">${dining.time_of_day}</h5>
                                    <div class="space-y-1">
                                        ${(dining.options || []).map(option => `
                                            <div class="text-sm">
                                                <span class="font-medium text-gray-700">${option.name}</span>
                                                <span class="text-gray-500 ml-2">(${option.type})</span>
                                            </div>
                                            <p class="text-gray-600 text-xs">${option.description || ''}</p>
                                        `).join('')}
                                    </div>
                                </div>
                            `).join('') || '<p class="text-gray-500 text-sm">用餐安排待定</p>'}
                        </div>
                    </td>
                </tr>
                
                <tr class="border-b border-gray-200">
                    <td class="py-4 px-3 font-semibold text-gray-700">
                        <i class="fa fa-bed text-purple-500 mr-1"></i>住宿
                    </td>
                    <td class="py-4 px-3">
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div>
                                <p class="font-medium text-gray-800 mb-2">推荐住宿</p>
                                <p class="text-gray-600 text-sm">${day.accommodation_suggestion || '待安排'}</p>
                                <p class="text-gray-500 text-xs mt-1">${day.accommodation_details || ''}</p>
                            </div>
                            <div>
                                <p class="font-medium text-gray-800 mb-2">推荐区域</p>
                                <div class="space-y-1">
                                    ${(day.accommodation_areas || []).map(area => `
                                        <div class="flex justify-between items-center text-sm">
                                            <span class="text-gray-700">${area.name}</span>
                                            <span class="text-gray-500">${area.price_range}</span>
                                        </div>
                                    `).join('') || '<p class="text-gray-500 text-sm">区域推荐待定</p>'}
                                </div>
                            </div>
                        </div>
                    </td>
                </tr>
                
                <tr class="border-b border-gray-200">
                    <td class="py-4 px-3 font-semibold text-gray-700">
                        <i class="fa fa-car text-green-500 mr-1"></i>交通
                    </td>
                    <td class="py-4 px-3">
                        <div class="space-y-2">
                            ${(day.schedule || []).map((item, index) => {
                                const transport = item.transportation || '步行';
                                const spotName = item.spot?.name || '景点';
                                return `
                                    <div class="flex items-center text-sm">
                                        <span class="w-2 h-2 bg-green-400 rounded-full mr-3"></span>
                                        <span class="text-gray-700">${item.arrival_time || '时间待定'}</span>
                                        <span class="mx-2 text-gray-400">→</span>
                                        <span class="text-gray-800 font-medium">${spotName}</span>
                                        <span class="ml-auto text-gray-600">${transport}</span>
                                    </div>
                                `;
                            }).join('') || '<p class="text-gray-500 text-sm">交通安排待定</p>'}
                        </div>
                    </td>
                </tr>
                
                <tr>
                    <td class="py-4 px-3 font-semibold text-gray-700">
                        <i class="fa fa-map-marker text-blue-500 mr-1"></i>景点
                    </td>
                    <td class="py-4 px-3">
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div>
                                <h5 class="font-medium text-gray-800 mb-2">游览景点</h5>
                                <div class="space-y-2">
                                    ${(day.spots || []).map(spot => `
                                        <div class="border-l-4 border-blue-200 pl-3">
                                            <div class="font-medium text-gray-800">${spot.name}</div>
                                            <div class="text-xs text-gray-600">${spot.type || ''}</div>
                                            ${spot.visit_tips ? `<p class="text-xs text-gray-500 mt-1">${spot.visit_tips}</p>` : ''}
                                        </div>
                                    `).join('') || '<p class="text-gray-500 text-sm">景点安排待定</p>'}
                                </div>
                            </div>
                            <div>
                                <h5 class="font-medium text-gray-800 mb-2">游览提示</h5>
                                ${day.visit_tips ? `<p class="text-gray-600 text-sm">${day.visit_tips}</p>` : '<p class="text-gray-500 text-sm">暂无特殊提示</p>'}
                            </div>
                        </div>
                    </td>
                </tr>
            </tbody>
        `;
        
        content.appendChild(detailTable);
        
        card.appendChild(header);
        card.appendChild(content);
        
        return card;
    }

    /**
     * 渲染错误信息
     */
    renderError(message) {
        this.container.innerHTML = `
            <div class="error-container p-8 text-center bg-red-50 rounded-xl border border-red-200">
                <i class="fa fa-exclamation-triangle text-red-500 text-4xl mb-4"></i>
                <h3 class="text-xl font-semibold text-red-800 mb-2">数据加载失败</h3>
                <p class="text-red-600">${message}</p>
            </div>
        `;
    }

    /**
     * 添加CSS样式
     */
    addStyles() {
        if (document.getElementById('itinerary-display-styles')) return;
        
        const styles = document.createElement('style');
        styles.id = 'itinerary-display-styles';
        styles.textContent = `
            .itinerary-container {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                max-width: 1200px;
                margin: 0 auto;
                padding: 20px;
            }
            
            .itinerary-table th,
            .day-details-table td {
                vertical-align: top;
            }
            
            .day-detail-card {
                transition: transform 0.2s ease-in-out;
            }
            
            .day-detail-card:hover {
                transform: translateY(-2px);
            }
            
            .itinerary-table tbody tr:hover {
                background-color: #f8fafc;
            }
            
            @media (max-width: 768px) {
                .itinerary-container {
                    padding: 10px;
                }
                
                .itinerary-table {
                    font-size: 14px;
                }
                
                .day-details-table {
                    font-size: 12px;
                }
            }
        `;
        
        document.head.appendChild(styles);
    }

    /**
     * 递归解码Unicode转义字符
     */
    decodeUnicodeEscapesInObject(obj) {
        if (typeof obj === 'string') {
            return this.decodeUnicodeEscapes(obj);
        } else if (Array.isArray(obj)) {
            return obj.map(item => this.decodeUnicodeEscapesInObject(item));
        } else if (obj !== null && typeof obj === 'object') {
            const decoded = {};
            for (const key in obj) {
                decoded[key] = this.decodeUnicodeEscapesInObject(obj[key]);
            }
            return decoded;
        }
        return obj;
    }

    /**
     * 解码Unicode转义字符
     */
    decodeUnicodeEscapes(str) {
        if (typeof str !== 'string') return str;
        return str.replace(/\\\\u([\d\w]{4})/gi, (match, p1) => {
            return String.fromCharCode(parseInt(p1, 16));
        });
    }

    /**
     * 计算总景点数
     */
    countTotalSpots(itinerary) {
        if (!itinerary.daily_itineraries) return 0;
        return itinerary.daily_itineraries.reduce((total, day) => {
            return total + (day.spots?.length || 0);
        }, 0);
    }
}

// 导出类
window.ItineraryDisplay = ItineraryDisplay;