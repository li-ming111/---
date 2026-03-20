// Unicode解码测试脚本
// 测试用户提供的示例数据

// 导入解码函数（在实际使用时，这些函数已经在main.js中定义）
function decodeUnicodeEscapes(str) {
    return str.replace(/\\u([0-9a-fA-F]{4})/g, function(match, code) {
        return String.fromCharCode(parseInt(code, 16));
    });
}

function decodeUnicodeEscapesInObject(obj) {
    if (typeof obj !== 'object' || obj === null) {
        if (typeof obj === 'string') {
            return decodeUnicodeEscapes(obj);
        }
        return obj;
    }
    
    if (Array.isArray(obj)) {
        return obj.map(item => decodeUnicodeEscapesInObject(item));
    }
    
    const decodedObj = {};
    for (const key in obj) {
        if (obj.hasOwnProperty(key)) {
            decodedObj[key] = decodeUnicodeEscapesInObject(obj[key]);
        }
    }
    return decodedObj;
}

// 测试用户提供的示例数据
const testData = { "input_data": { "city": "\u54c8\u5c14\u6ee8", "days": 1, "preferences": [ "\u5386\u53f2\u6587\u5316" ], "province": "", "travel_date": "2025-11-24" }, "itinerary_data": { "destination": "\u54c8\u5c14\u6ee8", "itinerary": { "daily_itineraries": [ { "accommodation_suggestion": "\u5e02\u4e2d\u5fc3\u9152\u5e97", "day": 1, "description": "\u53c2\u89c25\u4e2a\u666f\u70b9\uff0c\u5305\u62ec\u6545\u5bab\u535a\u7269\u9662, \u5929\u575b\u7b49", "schedule": [ { "arrival_time": "09:00", "departure_time": "10:30", "duration_hours": 1, "spot": { "description": "\u4e2d\u56fd\u660e\u6e05\u4e24\u4ee3\u7684\u7687\u5bb6\u5bab\u6bbf", "highlights": [ "\u5929\u5b89\u95e8", "\u592a\u548c\u6bbf", "\u5fa1\u82b1\u56ed" ], "name": "\u6545\u5bab\u535a\u7269\u9662", "type": "\u5386\u53f2\u6587\u5316" }, "transportation": "\u5730\u94c1" }, { "arrival_time": "10:00", "departure_time": "12:0", "duration_hours": 1, "spot": { "description": "\u660e\u6e05\u4e24\u671d\u7687\u5e1d\u796d\u5929\u7948\u8c37\u4e4b\u5730", "highlights": [ "\u7948\u5e74\u6bbf", "\u56de\u97f3\u58c1", "\u571c\u4e18\u575b" ], "name": "\u5929\u575b", "type": "\u5386\u53f2\u6587\u5316" }, "transportation": "\u5730\u94c1" }, { "arrival_time": "12:00", "departure_time": "13:30", "duration_hours": 1, "spot": { "description": "\u4e2d\u56fd\u6e05\u671d\u65f6\u671f\u7687\u5bb6\u56ed\u6797", "highlights": [ "\u4e07\u5bff\u5c71", "\u6606\u660e\u6e56", "\u957f\u5eca" ], "name": "\u9890\u548c\u56ed", "type": "\u5386\u53f2\u6587\u5316" }, "transportation": "\u5730\u94c1" }, { "arrival_time": "13:00", "departure_time": "15:0", "duration_hours": 1, "spot": { "description": "\u5317\u4eac\u8457\u540d\u7684\u5386\u53f2\u6587\u5316\u8857\u533a", "highlights": [ "\u80e1\u540c\u6587\u5316", "\u56db\u5408\u9662", "\u591c\u666f" ], "name": "\u4ec0\u5239\u6d77", "type": "\u7279\u8272\u4f53\u9a8c" }, "transportation": "\u5730\u94c1" }, { "arrival_time": "15:00", "departure_time": "17:30", "duration_hours": 2, "spot": { "description": "\u4e2d\u56fd\u53e4\u4ee3\u4f1f\u5927\u7684\u9632\u5fa1\u5de5\u7a0b", "highlights": [ "\u516b\u8fbe\u5cad", "\u70fd\u706b\u53f0", "\u58ee\u4e3d\u666f\u89c2" ], "name": "\u957f\u57ce", "type": "\u81ea\u7136\u98ce\u5149" }, "transportation": null } ], "spots": [ { "description": "\u4e2d\u56fd\u660e\u6e05\u4e24\u4ee3\u7684\u7687\u5bbf\u5bab\u6bbf", "highlights": [ "\u5929\u5b89\u95e8", "\u592a\u548c\u6bbf", "\u5fa1\u82b1\u56ed" ], "name": "\u6545\u5bab\u535a\u7269\u9662", "type": "\u5386\u53f2\u6587\u5316" }, { "description": "\u660e\u6e05\u4e24\u671d\u7687\u5e1d\u796d\u5929\u7948\u8c37\u4e4b\u5730", "highlights": [ "\u7948\u5e74\u6bbf", "\u56de\u97f3\u58c1", "\u571c\u4e18\u575b" ], "name": "\u5929\u575b", "type": "\u5386\u53f2\u6587\u5316" }, { "description": "\u4e2d\u56fd\u6e05\u671d\u65f6\u671f\u7687\u5bbf\u56ed\u6797", "highlights": [ "\u4e07\u5bff\u5c71", "\u6606\u660e\u6e56", "\u957f\u5eca" ], "name": "\u9890\u548c\u56ed", "type": "\u5386\u53f2\u6587\u5316" }, { "description": "\u5317\u4eac\u8457\u540d\u7684\u5386\u53f2\u6587\u5316\u8857\u533a", "highlights": [ "\u80e1\u540c\u6587\u5316", "\u56db\u5408\u9662", "\u591c\u666f" ], "name": "\u4ec0\u5239\u6d77", "type": "\u7279\u8272\u4f53\u9a8c" }, { "description": "\u4e2d\u56fd\u53e4\u4ee3\u4f1f\u5927\u7684\u9632\u5fa1\u5de5\u7a0b", "highlights": [ "\u516b\u8fbe\u5cad", "\u70fd\u706b\u53f0", "\u58ee\u4e3d\u666f\u89c2" ], "name": "\u957f\u57ce", "type": "\u81ea\u7136\u98ce\u5149" } ], "summary": "\u6587\u5316\u4e4b\u65c5\u4e00\u65e5\u6e38" } ], "food_recommendations": [ { "description": "\u5317\u4eac\u6700\u8457\u540d\u7684\u4f20\u7edf\u7f8e\u98df", "name": "\u5317\u4eac\u70e4\u9e2d" }, { "description": "\u8001\u5317\u4eac\u4f20\u7edf\u9762\u98df", "name": "\u70b8\u9171\u9762" }, { "description": "\u5317\u4eac\u7279\u8272\u5c0f\u5403", "name": "\u8c46\u6c41\u7126\u5708" }, { "description": "\u51ac\u5b63\u4fdd\u6696\u4f73\u80b4", "name": "\u6dae\u7f8a\u8089" } ] }, "preferences": [ "\u5386\u53f2\u6587\u5316" ], "stats": { "culture_spots": 3, "experience_spots": 1, "nature_spots": 1, "total_spots": 5 }, "total_days": 1 }, "message": "\u6210\u529f\u4e3a\u54c8\u5c14\u6ee8\u751f\u62101\u5929\u884c\u7a0b\u89c4\u5212", "success": true };

// 执行解码测试
const decodedTestData = decodeUnicodeEscapesInObject(testData);

// 测试结果验证
console.log('=== Unicode解码测试结果 ===');
console.log('原始数据城市名称:', testData.input_data.city);
console.log('解码后城市名称:', decodedTestData.input_data.city);
console.log('\n原始数据景点名称示例:', testData.itinerary_data.itinerary.daily_itineraries[0].schedule[0].spot.name);
console.log('解码后景点名称示例:', decodedTestData.itinerary_data.itinerary.daily_itineraries[0].schedule[0].spot.name);
console.log('\n原始数据消息:', testData.message);
console.log('解码后消息:', decodedTestData.message);

// 验证解码功能是否正常工作
function validateDecoding() {
    const originalCity = testData.input_data.city;
    const decodedCity = decodedTestData.input_data.city;
    
    // 检查是否成功解码为中文
    if (originalCity.includes('\\u') && !decodedCity.includes('\\u')) {
        console.log('\n✅ 解码测试通过: Unicode转义字符已成功转换为中文');
        
        // 验证特定的中文字符
        if (decodedCity === '哈尔滨') {
            console.log('✅ 城市名称正确解码为: 哈尔滨');
        }
        
        return true;
    } else {
        console.log('\n❌ 解码测试失败: Unicode转义字符未能正确转换');
        return false;
    }
}

// 运行验证
validateDecoding();

// 将此脚本添加到HTML页面中，或在浏览器控制台中运行以测试解码功能
// 在实际集成中，main.js中的解码函数将自动处理所有Unicode转义字符
