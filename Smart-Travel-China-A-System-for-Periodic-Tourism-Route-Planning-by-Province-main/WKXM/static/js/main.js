// 主应用JavaScript文件 - 支持对话式交互

// 解码Unicode转义字符为中文字符
function decodeUnicodeEscapes(str) {
    // 使用正则表达式匹配Unicode转义字符并解码
    return str.replace(/\\u([0-9a-fA-F]{4})/g, function(match, code) {
        return String.fromCharCode(parseInt(code, 16));
    });
}

// 递归处理对象中的所有Unicode转义字符
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

// 全局变量存储旅行规划数据
let travelData = {
    city: '',
    days: '',
    preferences: [],
    date: ''
};

// 对话状态管理
let conversationState = {
    awaitingCity: false,
    awaitingDays: false,
    awaitingPreferences: false,
    awaitingConfirmation: false,
    planningCompleted: false
};

// 支持的旅行偏好选项
const availablePreferences = {
    'nature': '自然风光',
    'culture': '历史文化',
    'food': '美食探索',
    'shopping': '购物娱乐',
    'adventure': '冒险体验',
    'relax': '休闲度假',
    'family': '亲子活动',
    'photography': '摄影观光'
};

// 等待DOM内容加载完成
document.addEventListener('DOMContentLoaded', function() {
    // 初始化函数
    initApp();
    
    // 初始化对话界面（如果存在聊天容器）
    if (document.getElementById('chat-container')) {
        initChatInterface();
    }
    // 初始化用户输入表单（向后兼容）
    if (document.getElementById('planning-form')) {
        initInputForm();
    }
    
    // 初始化行程结果页面（如果在结果页面）
    if (document.getElementById('itinerary-result')) {
        initItineraryResult();
    }
});

// 应用初始化函数
function initApp() {
    // 初始化导航栏滚动效果
    initNavScrollEffect();
    
    // 初始化移动端菜单
    initMobileMenu();
    
    // 初始化返回顶部按钮
    initBackToTopButton();
    
    // 初始化页面元素动画
    initElementAnimations();
}

// 导航栏滚动效果
function initNavScrollEffect() {
    const header = document.querySelector('header');
    let lastScrollTop = 0;
    
    window.addEventListener('scroll', function() {
        const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
        
        if (scrollTop > 100) {
            header.classList.add('py-2');
            header.classList.add('shadow-md');
        } else {
            header.classList.remove('py-2');
            header.classList.remove('shadow-md');
        }
        
        lastScrollTop = scrollTop;
    });
}

// 移动端菜单
function initMobileMenu() {
    const mobileMenuButton = document.getElementById('mobile-menu-button');
    const mobileMenu = document.getElementById('mobile-menu');
    
    if (mobileMenuButton && mobileMenu) {
        mobileMenuButton.addEventListener('click', function() {
            mobileMenu.classList.toggle('hidden');
            
            // 切换图标
            const icon = mobileMenuButton.querySelector('i');
            if (mobileMenu.classList.contains('hidden')) {
                icon.classList.remove('fa-times');
                icon.classList.add('fa-bars');
            } else {
                icon.classList.remove('fa-bars');
                icon.classList.add('fa-times');
            }
        });
    }
}

// 返回顶部按钮
function initBackToTopButton() {
    const backToTopButton = document.getElementById('back-to-top');
    
    if (backToTopButton) {
        window.addEventListener('scroll', function() {
            if (window.scrollY > 300) {
                backToTopButton.classList.remove('opacity-0', 'invisible');
                backToTopButton.classList.add('opacity-100', 'visible');
            } else {
                backToTopButton.classList.remove('opacity-100', 'visible');
                backToTopButton.classList.add('opacity-0', 'invisible');
            }
        });
        
        backToTopButton.addEventListener('click', function() {
            window.scrollTo({
                top: 0,
                behavior: 'smooth'
            });
        });
    }
}

// 页面元素动画
function initElementAnimations() {
    // 检测元素是否在视口中
    function isInViewport(element) {
        const rect = element.getBoundingClientRect();
        return (
            rect.top <= (window.innerHeight || document.documentElement.clientHeight) * 0.8 &&
            rect.bottom >= 0
        );
    }
    
    // 为元素添加进入视口时的动画
    function animateOnScroll() {
        const animatedElements = document.querySelectorAll('.animate-on-scroll');
        
        animatedElements.forEach(element => {
            if (isInViewport(element) && !element.classList.contains('animated')) {
                element.classList.add('animated');
                element.classList.add('fadeIn');
            }
        });
    }
    
    // 初始执行一次
    animateOnScroll();
    
    // 滚动时执行
    window.addEventListener('scroll', animateOnScroll);
}

// 初始化对话界面
function initChatInterface() {
    const chatContainer = document.getElementById('chat-container');
    const messageInput = document.getElementById('user-message');
    const sendButton = document.getElementById('send-message');
    const quickActionButtons = document.querySelectorAll('.quick-action-btn');
    
    if (!chatContainer || !messageInput || !sendButton) return;
    
    // 初始化聊天界面，发送欢迎消息
    sendSystemMessage('您好！我是智能旅行规划助手。请问您想去哪个城市旅行？');
    conversationState.awaitingCity = true;
    
    // 绑定发送消息事件
    sendButton.addEventListener('click', function() {
        const userMessage = messageInput.value.trim();
        if (userMessage) {
            sendUserMessage(userMessage);
            processUserMessage(userMessage);
            messageInput.value = '';
        }
    });
    
    // 绑定回车键发送消息
    messageInput.addEventListener('keypress', function(event) {
        if (event.key === 'Enter' && !event.shiftKey) {
            event.preventDefault();
            sendButton.click();
        }
    });
    
    // 绑定快捷操作按钮事件
    quickActionButtons.forEach(button => {
        button.addEventListener('click', function() {
            const action = this.dataset.action;
            handleQuickAction(action);
        });
    });
}

// 发送系统消息
function sendSystemMessage(message) {
    const chatContainer = document.getElementById('chat-container');
    if (!chatContainer) return;
    
    const messageElement = document.createElement('div');
    messageElement.className = 'chat-message system-message';
    messageElement.innerHTML = `
        <div class="message-avatar">
            <i class="fa fa-robot"></i>
        </div>
        <div class="message-content">
            <p>${escapeHTML(message)}</p>
        </div>
    `;
    
    chatContainer.appendChild(messageElement);
    chatContainer.scrollTop = chatContainer.scrollHeight;
}

// 发送用户消息
function sendUserMessage(message) {
    const chatContainer = document.getElementById('chat-container');
    if (!chatContainer) return;
    
    const messageElement = document.createElement('div');
    messageElement.className = 'chat-message user-message';
    messageElement.innerHTML = `
        <div class="message-content">
            <p>${escapeHTML(message)}</p>
        </div>
        <div class="message-avatar">
            <i class="fa fa-user"></i>
        </div>
    `;
    
    chatContainer.appendChild(messageElement);
    chatContainer.scrollTop = chatContainer.scrollHeight;
}

// HTML转义
function escapeHTML(str) {
    return str
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#039;');
}

// 处理用户消息
function processUserMessage(message) {
    // 根据当前对话状态处理消息
    if (conversationState.awaitingCity) {
        handleCityInput(message);
    } else if (conversationState.awaitingDays) {
        handleDaysInput(message);
    } else if (conversationState.awaitingPreferences) {
        handlePreferencesInput(message);
    } else if (conversationState.awaitingConfirmation) {
        handleConfirmationInput(message);
    } else {
        // 通用消息处理
        handleGeneralMessage(message);
    }
}

// 处理城市输入
function handleCityInput(message) {
    // 简单验证城市名称
    if (message.length < 2) {
        sendSystemMessage('请输入有效的城市名称。');
        return;
    }
    
    travelData.city = message;
    conversationState.awaitingCity = false;
    conversationState.awaitingDays = true;
    
    sendSystemMessage(`好的，我们将规划${message}的旅行。您计划旅行多少天呢？`);
}

// 处理天数输入
function handleDaysInput(message) {
    // 提取数字
    const days = parseInt(message.match(/\d+/)?.[0]);
    
    if (!days || days < 1 || days > 30) {
        sendSystemMessage('请输入1-30天之间的有效天数。');
        return;
    }
    
    travelData.days = days;
    conversationState.awaitingDays = false;
    conversationState.awaitingPreferences = true;
    
    // 显示偏好选项
    let preferencesMessage = '请问您对旅行有什么偏好？可以选择以下选项（可多选）：\n';
    Object.values(availablePreferences).forEach(preference => {
        preferencesMessage += `- ${preference}\n`;
    });
    
    sendSystemMessage(preferencesMessage + '\n您可以直接输入偏好名称，或者点击下方的快捷按钮选择。');
}

// 处理偏好输入
function handlePreferencesInput(message) {
    // 从消息中提取偏好
    const lowerMessage = message.toLowerCase();
    const selectedPreferences = [];
    
    Object.entries(availablePreferences).forEach(([key, value]) => {
        if (lowerMessage.includes(key.toLowerCase()) || lowerMessage.includes(value.toLowerCase())) {
            if (!selectedPreferences.includes(key)) {
                selectedPreferences.push(key);
            }
        }
    });
    
    if (selectedPreferences.length === 0) {
        sendSystemMessage('我没有识别到您的偏好选择。请尝试从提供的选项中选择。');
        return;
    }
    
    travelData.preferences = selectedPreferences;
    conversationState.awaitingPreferences = false;
    conversationState.awaitingConfirmation = true;
    
    // 汇总信息并请求确认
    const selectedPreferencesText = selectedPreferences.map(key => availablePreferences[key]).join('、');
    sendSystemMessage(
        `我已为您记录以下旅行信息：\n` +
        `• 目的地：${travelData.city}\n` +
        `• 旅行天数：${travelData.days}天\n` +
        `• 旅行偏好：${selectedPreferencesText}\n\n` +
        `这些信息是否正确？您可以回答"是"开始生成行程，或者告诉我需要修改的部分。`
    );
}

// 处理确认输入
function handleConfirmationInput(message) {
    const lowerMessage = message.toLowerCase();
    
    if (lowerMessage.includes('是') || lowerMessage.includes('对') || lowerMessage.includes('没错')) {
        // 用户确认，开始生成行程
        conversationState.awaitingConfirmation = false;
        sendSystemMessage('太好了！我现在开始为您生成详细的旅行规划...');
        
        // 显示加载状态
        showLoadingState();
        
        // 调用后端生成行程
        generateTravelItinerary();
    } else if (lowerMessage.includes('不') || lowerMessage.includes('否') || lowerMessage.includes('修改')) {
        // 用户需要修改
        sendSystemMessage('您想修改哪部分信息？\n• 城市\n• 天数\n• 偏好');
        // 这里可以根据用户回复进一步处理修改
    }
}

// 处理通用消息
function handleGeneralMessage(message) {
    if (conversationState.planningCompleted) {
        // 行程已完成，处理后续咨询
        sendSystemMessage('关于您的行程还有其他问题吗？我可以为您调整或提供更详细的信息。');
    } else {
        // 未进入明确流程，提供帮助
        sendSystemMessage('抱歉，我不太理解您的需求。您可以尝试告诉我：\n• 想去的城市\n• 旅行天数\n• 旅行偏好');
    }
}

// 处理快捷操作按钮
function handleQuickAction(action) {
    switch(action) {
        case 'select-city':
            sendUserMessage('选择城市');
            sendSystemMessage('请告诉我您想去的城市名称。');
            conversationState.awaitingCity = true;
            break;
        case 'set-days':
            sendUserMessage('设置天数');
            sendSystemMessage('请问您计划旅行多少天？');
            conversationState.awaitingDays = true;
            break;
        case 'select-preferences':
            sendUserMessage('选择偏好');
            let preferencesMessage = '请从以下选项中选择您的旅行偏好（可多选）：\n';
            Object.values(availablePreferences).forEach(preference => {
                preferencesMessage += `- ${preference}\n`;
            });
            sendSystemMessage(preferencesMessage);
            conversationState.awaitingPreferences = true;
            break;
        case 'generate-itinerary':
            if (travelData.city && travelData.days && travelData.preferences.length > 0) {
                sendUserMessage('生成行程');
                generateTravelItinerary();
            } else {
                sendSystemMessage('请先提供完整的旅行信息：城市、天数和偏好。');
            }
            break;
    }
}

// 生成旅行行程（增强版）
function generateTravelItinerary() {
    // 准备发送到后端的数据
    const data = {
        'city': travelData.city,
        'days': travelData.days,
        'preferences': travelData.preferences,
        'date': travelData.date || new Date().toISOString().split('T')[0],
        'is_conversational': true // 标识这是对话式交互
    };
    
    // 记录开始时间
    const startTime = new Date().getTime();
    
    // 发送请求到后端API
    fetch('/plan', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        // 隐藏加载状态
        hideLoadingState();
        
        if (!response.ok) {
            throw new Error(`服务器错误: ${response.status}`);
        }
        return response.json();
    })
    .then(result => {
        // 记录结束时间
        const endTime = new Date().getTime();
        const processingTime = ((endTime - startTime) / 1000).toFixed(1);
        console.log(`行程生成耗时: ${processingTime}秒`);
        
        conversationState.planningCompleted = true;
        
        // 检查是否有AI增强的响应
        if (result && result.ai_feedback) {
            sendSystemMessage(result.ai_feedback);
        }
        
        displayItineraryResult(result);
    })
    .catch(error => {
        // 隐藏加载状态
        hideLoadingState();
        
        // 智能错误处理
        let errorMessage = '很抱歉，生成行程时出现错误。请稍后再试。';
        
        if (error.message && error.message.includes('timeout')) {
            errorMessage = '请求超时。生成复杂行程可能需要较长时间，请尝试减少旅行天数或稍后再试。';
        } else if (error.message && error.message.includes('not found')) {
            errorMessage = `我们的数据库中尚未收录${travelData.city}的详细信息。您可以尝试其他热门旅游城市。`;
        }
        
        sendSystemMessage(errorMessage);
        console.error('生成行程错误:', error);
    });
}

// 显示行程结果
function displayItineraryResult(itineraryData) {
    if (!itineraryData || !itineraryData.itinerary_data) {
        sendSystemMessage('未能生成有效的行程信息。');
        return;
    }
    
    const data = itineraryData.itinerary_data;
    let resultMessage = `您的${travelData.days}天${travelData.city}旅行规划已生成！\n\n`;
    
    // 添加行程概览
    if (data.overview) {
        resultMessage += `【行程概览】\n${data.overview}\n\n`;
    }
    
    // 添加每日行程
    if (data.daily_itinerary) {
        data.daily_itinerary.forEach((day, index) => {
            resultMessage += `【第${index + 1}天】\n`;
            if (day.activities) {
                day.activities.forEach(activity => {
                    resultMessage += `- ${activity}\n`;
                });
            }
            resultMessage += '\n';
        });
    }
    
    // 添加其他建议
    if (data.suggestions) {
        resultMessage += `【旅行建议】\n${data.suggestions}\n`;
    }
    
    sendSystemMessage(resultMessage + '\n您对这个行程安排满意吗？需要调整或者有其他问题吗？');
}

// 初始化用户输入表单（向后兼容）
function initInputForm() {
    const form = document.getElementById('planning-form');
    
    if (form) {
        // 表单验证
        form.addEventListener('submit', function(event) {
            event.preventDefault();
            
            if (validateForm()) {
                // 显示加载状态
                showLoadingState();
                
                // 提交表单数据
                submitFormData();
            }
        });
        
        // 城市自动补全
        initCityAutocomplete();
        
        // 偏好标签切换
        initPreferenceTags();
        
        // 日期选择器增强
        initDatePicker();
    }
}

// 表单验证
function validateForm() {
    const provinceInput = document.getElementById('province');
    const cityInput = document.getElementById('city');
    const daysInput = document.getElementById('travel-days');
    const preferencesContainer = document.getElementById('selected-preferences');
    
    let isValid = true;
    
    // 验证省份和城市至少有一个
    if (!provinceInput.value.trim() && !cityInput.value.trim()) {
        showError(provinceInput, '请输入省份或城市');
        isValid = false;
    } else {
        clearError(provinceInput);
    }
    
    // 验证天数
    if (!daysInput.value || daysInput.value < 1 || daysInput.value > 30) {
        showError(daysInput, '请输入1-30天之间的行程天数');
        isValid = false;
    } else {
        clearError(daysInput);
    }
    
    // 验证偏好标签
    const selectedPreferences = preferencesContainer.querySelectorAll('.tag-item');
    if (selectedPreferences.length === 0) {
        const preferencesError = document.createElement('div');
        preferencesError.className = 'error-message';
        preferencesError.textContent = '请至少选择一个旅行偏好';
        preferencesContainer.parentNode.appendChild(preferencesError);
        isValid = false;
    } else {
        const existingError = preferencesContainer.parentNode.querySelector('.error-message');
        if (existingError) {
            existingError.remove();
        }
    }
    
    return isValid;
}

// 显示错误提示
function showError(element, message) {
    // 移除已存在的错误提示
    clearError(element);
    
    // 创建错误提示元素
    const errorElement = document.createElement('div');
    errorElement.className = 'error-message';
    errorElement.textContent = message;
    
    // 添加错误类到输入框
    element.classList.add('error');
    
    // 插入错误提示
    element.parentNode.appendChild(errorElement);
}

// 清除错误提示
function clearError(element) {
    // 移除错误类
    element.classList.remove('error');
    
    // 移除错误提示元素
    const errorElement = element.parentNode.querySelector('.error-message');
    if (errorElement) {
        errorElement.remove();
    }
}

// 城市自动补全
function initCityAutocomplete() {
    const cityInput = document.getElementById('city');
    
    if (cityInput) {
        // 模拟城市数据
        const cities = [
            '北京市', '上海市', '广州市', '深圳市', '杭州市', '南京市', '成都市', 
            '武汉市', '重庆市', '西安市', '苏州市', '长沙市', 'INTEGERITY', '天津市', 
            '青岛市', '宁波市', '东莞市', '无锡市', '福州市', '厦门市', '大连市', 
            '昆明市', '合肥市', '佛山市', '南通市', '烟台市', '常州市', '太原市',
            '贵阳市', '南宁市', '南昌市', '金华市', '泉州市', '济宁市', '徐州市',
            '海口市', '三亚市', '大理市', '丽江市', '香格里拉市', '拉萨市', '西宁市'
        ];
        
        // 创建下拉建议框
        let dropdown = null;
        
        cityInput.addEventListener('input', function() {
            const inputValue = this.value.trim();
            
            // 如果输入为空，移除下拉框
            if (!inputValue) {
                if (dropdown) {
                    dropdown.remove();
                    dropdown = null;
                }
                return;
            }
            
            // 过滤城市列表
            const filteredCities = cities.filter(city => 
                city.toLowerCase().includes(inputValue.toLowerCase())
            );
            
            // 如果没有匹配结果，移除下拉框
            if (filteredCities.length === 0) {
                if (dropdown) {
                    dropdown.remove();
                    dropdown = null;
                }
                return;
            }
            
            // 创建或更新下拉框
            if (!dropdown) {
                dropdown = document.createElement('div');
                dropdown.className = 'autocomplete-dropdown';
                this.parentNode.appendChild(dropdown);
            } else {
                dropdown.innerHTML = '';
            }
            
            // 添加建议项
            filteredCities.forEach(city => {
                const item = document.createElement('div');
                item.className = 'autocomplete-item';
                item.textContent = city;
                
                // 点击选择建议项
                item.addEventListener('click', function() {
                    cityInput.value = city;
                    dropdown.remove();
                    dropdown = null;
                });
                
                dropdown.appendChild(item);
            });
        });
        
        // 点击其他地方关闭下拉框
        document.addEventListener('click', function(event) {
            if (dropdown && !cityInput.contains(event.target) && !dropdown.contains(event.target)) {
                dropdown.remove();
                dropdown = null;
            }
        });
    }
}

// 偏好标签切换
function initPreferenceTags() {
    const availableTags = document.querySelectorAll('.preference-tag');
    const selectedContainer = document.getElementById('selected-preferences');
    
    availableTags.forEach(tag => {
        tag.addEventListener('click', function() {
            const tagText = this.textContent.trim();
            const tagId = this.dataset.tag;
            
            // 检查是否已经选中
            const existingTag = document.querySelector(`.tag-item[data-tag="${tagId}"]`);
            
            if (existingTag) {
                // 如果已选中，则移除
                existingTag.remove();
                this.classList.remove('selected');
            } else {
                // 如果未选中，则添加
                const tagItem = document.createElement('div');
                tagItem.className = 'tag-item';
                tagItem.dataset.tag = tagId;
                tagItem.innerHTML = `${tagText} <button type="button" class="remove-tag">&times;</button>`;
                
                // 添加移除标签的事件监听
                const removeButton = tagItem.querySelector('.remove-tag');
                removeButton.addEventListener('click', function() {
                    tagItem.remove();
                    tag.classList.remove('selected');
                });
                
                selectedContainer.appendChild(tagItem);
                this.classList.add('selected');
            }
        });
    });
}

// 日期选择器增强
function initDatePicker() {
    const dateInput = document.getElementById('travel-date');
    
    if (dateInput) {
        // 设置最小日期为今天
        const today = new Date().toISOString().split('T')[0];
        dateInput.min = today;
        
        // 可以在这里集成第三方日期选择器库
    }
}

// 显示加载状态
function showLoadingState() {
    const formSubmitButton = document.getElementById('submit-button');
    const form = document.getElementById('planning-form');
    let progressInterval;
    
    if (formSubmitButton) {
        // 保存原始按钮内容
        formSubmitButton.dataset.originalContent = formSubmitButton.innerHTML;
        
        // 设置加载状态
        formSubmitButton.disabled = true;
        formSubmitButton.innerHTML = '<i class="fa fa-spinner fa-spin mr-2"></i> 正在生成行程...';
    }
    
    if (form) {
        // 添加加载遮罩
        const loadingOverlay = document.createElement('div');
        loadingOverlay.className = 'loading-overlay';
        loadingOverlay.innerHTML = `
            <div class="loading-content">
                <div class="loading-spinner"></div>
                <p>正在为您规划最佳行程路线</p>
                <div class="loading-progress">
                    <div class="progress-bar"></div>
                </div>
            </div>
        `;
        
        document.body.appendChild(loadingOverlay);
        
        // 模拟进度更新并保存interval ID
        progressInterval = simulateProgressUpdate();
    }
    
    // 返回progressInterval，以便后续可以清除它
    return progressInterval;
}

// 模拟进度更新
function simulateProgressUpdate() {
    const progressBar = document.querySelector('.progress-bar');
    let progress = 0;
    
    const interval = setInterval(() => {
        progress += Math.random() * 10;
        
        if (progress > 95) {
            clearInterval(interval);
        } else {
            progressBar.style.width = `${progress}%`;
        }
    }, 300);
    
    return interval; // 返回interval ID以便后续可以清除
}

// 提交表单数据（增强版）
function submitFormData() {
    // 验证表单
    if (!validateForm()) {
        showErrorMessage('请填写必要的行程信息');
        return;
    }
    
    // 显示加载状态并保存progressInterval ID
    const progressInterval = showLoadingState();
    
    const form = document.getElementById('planning-form');
    const formData = new FormData(form);
    
    // 获取选中的偏好标签，只获取已选中的标签
    const selectedPreferences = [];
    document.querySelectorAll('.tag-item.active').forEach(tag => {
        if (tag.dataset.tag) {
            selectedPreferences.push(tag.dataset.tag);
        }
    });
    
    // 创建发送到服务器的数据对象
    const data = {
        'province': formData.get('province') || '',
        'city': formData.get('city') || '',
        'days': parseInt(formData.get('days')) || 1,
        'preferences': selectedPreferences,
        'travel_date': formData.get('travel_date') || new Date().toISOString().split('T')[0],
        'is_ai_enhanced': true, // 启用AI增强功能
        'client_timestamp': new Date().getTime() // 添加客户端时间戳用于分析
    };
    
    console.log('发送数据:', data);
    
    // 启动WebSocket连接用于进度更新（如果支持）
    let wsConnection = null;
    try {
        // 尝试建立WebSocket连接用于实时进度更新
        if (window.location.protocol === 'https:') {
            wsConnection = new WebSocket(`wss://${window.location.host}/ws/progress`);
        } else {
            wsConnection = new WebSocket(`ws://${window.location.host}/ws/progress`);
        }
        
        wsConnection.onmessage = function(event) {
            try {
                const progressData = JSON.parse(event.data);
                if (progressData.type === 'progress_update' && progressData.progress) {
                    const progressBar = document.querySelector('.progress-bar');
                    if (progressBar) {
                        progressBar.style.width = `${progressData.progress}%`;
                        
                        // 如果有进度消息，显示在界面上
                        if (progressData.message) {
                            const statusText = document.querySelector('.loading-content p');
                            if (statusText) {
                                statusText.textContent = progressData.message;
                            }
                        }
                    }
                }
            } catch (e) {
                console.warn('处理WebSocket消息错误:', e);
            }
        };
        
        wsConnection.onerror = function(error) {
            console.warn('WebSocket连接错误，将使用轮询作为后备方案:', error);
            wsConnection = null;
        };
    } catch (e) {
        console.warn('无法建立WebSocket连接:', e);
    }
    
    // 使用fetch API发送数据到服务器
    fetch('/plan', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify(data),
    })
    .then(response => {
        // 清除进度条interval
        clearInterval(progressInterval);
        
        // 关闭WebSocket连接
        if (wsConnection) {
            wsConnection.close();
        }
        
        if (!response.ok) {
            // 尝试从响应中获取错误信息
            return response.json().then(errorData => {
                throw new Error(errorData.message || '服务器响应错误');
            }).catch(() => {
                throw new Error(`服务器错误: ${response.status} ${response.statusText}`);
            });
        }
        return response.json();
    })
    .then(data => {
        // 处理成功响应
        handleFormResponse(data);
    })
    .catch(error => {
        // 确保清除进度条
        clearInterval(progressInterval);
        
        // 关闭WebSocket连接
        if (wsConnection) {
            wsConnection.close();
        }
        
        // 处理错误
        handleFormError(error);
    });
}

// 处理表单响应（增强版）
function handleFormResponse(data) {
    // 隐藏加载状态
    hideLoadingState();
    
    // 确保WebSocket连接已关闭
    if (window.websocket && window.websocket.readyState === WebSocket.OPEN) {
        window.websocket.close();
        window.websocket = null;
    }
    
    // 解码所有Unicode转义字符，确保正确显示中文
    const decodedData = decodeUnicodeEscapesInObject(data);
    console.log('收到服务器响应并解码Unicode后:', decodedData);
    
    // 处理响应数据
    if (decodedData && decodedData.success) {
        try {
            // 智能处理AI增强的行程数据
            const aiEnhancedData = processAIEnhancedData(data);
            
            // 保存行程数据到会话存储和本地存储
            try {
                // 确保存储完整的行程数据结构
                const fullItineraryData = {
                    success: true,
                    message: aiEnhancedData.message || '行程规划成功',
                    itinerary_data: aiEnhancedData.itinerary_data || aiEnhancedData,
                    input_data: aiEnhancedData.input_data || {},
                    ai_enhanced_features: aiEnhancedData.ai_enhanced_features || [],
                    client_generated_at: new Date().toISOString(),
                    is_ai_enhanced: true,
                    ai_processing_time_ms: decodedData.ai_processing_time_ms || 0
                };
                
                sessionStorage.setItem('itinerary_data', JSON.stringify(fullItineraryData, null, 2));
                localStorage.setItem('last_itinerary_data', JSON.stringify(fullItineraryData, null, 2));
                console.log('行程数据已保存到存储');
            } catch (storageError) {
                console.warn('无法保存数据到存储:', storageError);
                // 存储失败不应该阻止流程
            }
            
            // 确定是否有季节性/特殊建议要显示
                let specialNotice = '';
                if (aiEnhancedData.seasonal_recommendations && aiEnhancedData.seasonal_recommendations.length > 0) {
                    specialNotice = aiEnhancedData.seasonal_recommendations[0];
                }
                
                // 显示AI生成的成功消息（如果有）
                let successMessage = null;
                if (decodedData.ai_response && decodedData.ai_response.success_message) {
                    successMessage = decodedData.ai_response.success_message;
                } else {
                    const cityName = decodedData.input_data?.city || '目的地';
                    const days = decodedData.input_data?.days || 1;
                    successMessage = `成功为${cityName}生成${days}天行程规划！`;
                }
            
            // 使用增强版的成功消息显示
            showSuccessMessage(successMessage, {
                icon: 'fa-map-marker-check',
                position: 'top-center',
                bgColor: 'success-itinerary',
                autoHide: false // 直到重定向才隐藏
            });
            
            // 如果有特殊提示，延迟显示第二个消息
            if (specialNotice) {
                setTimeout(() => {
                    showSuccessMessage(specialNotice, {
                        icon: 'fa-lightbulb',
                        position: 'bottom-center',
                        bgColor: 'info-tip',
                        autoHide: false
                    });
                }, 500);
            }
            
            // 智能重定向到结果页面
            // 检查是否有AI推荐的特殊结果页面
            const redirectUrl = data.ai_response?.recommended_view || '/result';
            
            // 给用户足够时间看到成功消息
            setTimeout(() => {
                console.log('重定向到结果页面:', redirectUrl);
                window.location.href = redirectUrl;
            }, 2000); // 稍长一点的延迟，让用户看到成功消息和可能的特殊提示
        } catch (processingError) {
            console.error('处理响应数据时出错:', processingError);
            // 即使处理出错，也尝试显示基本成功消息
            showSuccessMessage('行程已生成，正在跳转到结果页面...', {
                position: 'top-center'
            });
            
            // 仍然尝试跳转到结果页面
            setTimeout(() => {
                window.location.href = '/result';
            }, 1500);
        }
    } else {
        // 显示错误信息
            const errorMsg = decodedData && decodedData.message ? decodedData.message : '生成行程失败，请重试';
        showErrorMessage(errorMsg, {
            level: 'warning',
            position: 'top-center'
        });
    }
}

// 处理AI增强的数据
function processAIEnhancedData(data) {
    try {
        // 检查输入数据是否有效
        if (!data || typeof data !== 'object') {
            console.error('processAIEnhancedData: 输入数据无效');
            return data || {};
        }
        
        // 如果数据已包含AI处理标记，直接返回
        if (data.is_ai_processed) {
            return data;
        }
        
        // 复制原始数据以避免修改
        const processedData = JSON.parse(JSON.stringify(data));
        
        // 确保基本结构存在
        if (!processedData.itinerary_data) {
            processedData.itinerary_data = data.data || {};
        }
        
        const itinerary = processedData.itinerary_data;
        
        // 添加AI增强标记
        processedData.is_ai_processed = true;
        processedData.ai_enhanced_features = [];
        
        // 添加季节性建议到顶层（供handleFormResponse使用）
        processedData.seasonal_recommendations = [];
        
        // 处理AI生成的行程优化建议
        if (data.ai_optimizations) {
            processedData.ai_enhanced_features.push('行程优化');
            
            // 合并AI优化建议到行程数据中
            if (data.ai_optimizations.transport_suggestions) {
                itinerary.transport_suggestions = data.ai_optimizations.transport_suggestions;
            }
            
            if (data.ai_optimizations.timing_adjustments) {
                itinerary.timing_adjustments = data.ai_optimizations.timing_adjustments;
            }
            
            // 处理其他优化建议
            if (data.ai_optimizations.avoid_crowds) {
                itinerary.avoid_crowds = data.ai_optimizations.avoid_crowds;
            }
        }
        
        // 处理AI生成的个性化建议
        if (data.ai_personalization) {
            processedData.ai_enhanced_features.push('个性化建议');
            itinerary.personalized_tips = data.ai_personalization.tips || [];
            
            // 处理特殊饮食建议
            if (data.ai_personalization.dietary_suggestions) {
                itinerary.dietary_suggestions = data.ai_personalization.dietary_suggestions;
            }
        }
        
        // 处理季节性建议 - 同时添加到顶层和行程数据中
        if (data.seasonal_suggestions && Array.isArray(data.seasonal_suggestions)) {
            processedData.ai_enhanced_features.push('季节性建议');
            itinerary.seasonal_tips = data.seasonal_suggestions;
            // 复制到顶层供显示使用
            processedData.seasonal_recommendations = [...data.seasonal_suggestions];
        } else if (data.seasonal_info && data.seasonal_info.recommendations) {
            // 处理可能的替代结构
            processedData.ai_enhanced_features.push('季节性建议');
            itinerary.seasonal_tips = data.seasonal_info.recommendations;
            processedData.seasonal_recommendations = [...data.seasonal_info.recommendations];
        }
        
        // 处理天气相关建议
        if (data.weather_advisory) {
            processedData.ai_enhanced_features.push('天气建议');
            itinerary.weather_advisory = data.weather_advisory;
            
            // 如果有重要的天气提醒，添加到季节性建议中
            if (data.weather_advisory.important_reminder) {
                processedData.seasonal_recommendations.unshift(data.weather_advisory.important_reminder);
            }
        }
        
        // 处理实时信息建议（如交通、活动等）
        if (data.real_time_info) {
            processedData.ai_enhanced_features.push('实时信息');
            itinerary.real_time_info = data.real_time_info;
            
            // 如果有重要的实时提醒，添加到季节性建议中
            if (data.real_time_info.alert) {
                processedData.seasonal_recommendations.unshift(data.real_time_info.alert);
            }
        }
        
        // 处理预算优化建议
        if (data.budget_optimization) {
            processedData.ai_enhanced_features.push('预算优化');
            itinerary.budget_optimization = data.budget_optimization;
        }
        
        // 去重AI增强功能列表
        processedData.ai_enhanced_features = [...new Set(processedData.ai_enhanced_features)];
        
        // 确保input_data存在
        if (!processedData.input_data && data.input_data) {
            processedData.input_data = data.input_data;
        }
        
        console.log('AI增强数据处理完成:', {
            features: processedData.ai_enhanced_features,
            seasonalRecommendations: processedData.seasonal_recommendations.length
        });
        
        return processedData;
    } catch (error) {
        console.error('处理AI增强数据时出错:', error);
        // 出错时返回原始数据，确保流程不会中断
        return data || {};
    }
}

// 处理表单错误
function handleFormError(error) {
    // 隐藏加载状态
    hideLoadingState();
    
    // 确保WebSocket连接已关闭
    if (window.websocket && window.websocket.readyState === WebSocket.OPEN) {
        window.websocket.close();
        window.websocket = null;
    }
    
    // 重置进度条（如果有）
    const progressBar = document.querySelector('.progress-bar-fill');
    if (progressBar) {
        progressBar.style.width = '0%';
    }
    
    // 分析错误类型并显示相应的错误信息
    let errorMessage = '生成行程时发生错误，请检查您的输入或稍后再试';
    let errorLevel = 'default';
    let allowRetry = true;
    
    // 错误分类和处理
    if (error.type === 'network_error' || error.message && error.message.includes('Network')) {
        errorMessage = '网络连接失败，请检查您的网络连接后重试';
        errorLevel = 'warning';
        allowRetry = true;
    } else if (error.type === 'timeout' || error.message && error.message.includes('timeout')) {
        errorMessage = '请求超时，请稍后再试或尝试减少行程天数';
        errorLevel = 'warning';
        allowRetry = true;
    } else if (error.message && error.message.includes('Unexpected token')) {
        errorMessage = '服务器返回数据格式错误，请刷新页面后重试';
        errorLevel = 'critical';
        allowRetry = true;
    } else if (error.type === 'city_not_found' || error.message && error.message.includes('城市')) {
        errorMessage = '未找到指定城市的信息，请检查城市名称是否正确';
        errorLevel = 'info';
        allowRetry = false; // 需要用户修改输入
    } else if (error.type === 'validation_error' || error.message && error.message.includes('验证')) {
        errorMessage = error.message || '输入数据有误，请检查您的输入信息';
        errorLevel = 'info';
        allowRetry = false; // 需要用户修改输入
    } else if (error.message) {
        // 显示来自服务器的具体错误信息
        errorMessage = error.message;
        // 根据错误消息内容确定级别
        if (errorMessage.includes('服务器') || errorMessage.includes('Server')) {
            errorLevel = 'critical';
        }
    } else if (error.status) {
        // 根据HTTP状态码处理
        switch(error.status) {
            case 400:
                errorMessage = '请求参数错误，请检查您的输入';
                errorLevel = 'info';
                allowRetry = false;
                break;
            case 404:
                errorMessage = '请求的资源不存在';
                errorLevel = 'warning';
                break;
            case 500:
                errorMessage = '服务器内部错误，请稍后再试';
                errorLevel = 'critical';
                break;
            default:
                errorMessage = `服务器错误 (${error.status})，请稍后再试`;
                errorLevel = 'critical';
        }
    }
    
    // 显示错误信息
    showErrorMessage(errorMessage, {
        level: errorLevel,
        position: 'top-center',
        allowRetry: allowRetry,
        onRetry: function() {
            // 自动重试表单提交
            submitFormData();
        },
        errorData: error
    });
    
    // 详细记录错误日志便于调试
    console.error('表单提交错误:', error);
    if (error.stack) {
        console.error('错误详情:', error.stack);
    }
    
    // 对于严重错误，可以考虑发送错误报告
    if (errorLevel === 'critical' && navigator.onLine) {
        // 异步发送错误报告到服务器
        try {
            fetch('/api/error-report', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    error: errorMessage,
                    stack: error.stack || '',
                    url: window.location.href,
                    timestamp: new Date().toISOString()
                }),
                // 不影响用户体验，静默失败
                keepalive: true
            }).catch(() => {});
        } catch (e) {
            // 忽略报告发送错误
        }
    }
    
    // 对于用户输入错误，聚焦到问题字段
    if (!allowRetry) {
        const firstInvalidInput = document.querySelector('.error-input, .form-control:invalid');
        if (firstInvalidInput) {
            setTimeout(() => {
                firstInvalidInput.focus();
                firstInvalidInput.scrollIntoView({ behavior: 'smooth', block: 'center' });
            }, 500);
        }
    }
}

// 隐藏加载状态
function hideLoadingState() {
    const formSubmitButton = document.getElementById('submit-button');
    const loadingOverlay = document.querySelector('.loading-overlay');
    
    if (formSubmitButton && formSubmitButton.dataset.originalContent) {
        // 恢复原始按钮内容
        formSubmitButton.disabled = false;
        formSubmitButton.innerHTML = formSubmitButton.dataset.originalContent;
    }
    
    if (loadingOverlay) {
        loadingOverlay.remove();
    }
}

// 显示错误消息（增强版）
function showErrorMessage(message, options = {}) {
    // 清除已存在的错误消息
    const existingMessage = document.querySelector('.error-message-container');
    if (existingMessage) {
        existingMessage.remove();
    }
    
    const errorContainer = document.createElement('div');
    errorContainer.className = 'error-message-container animate__animated animate__shakeX';
    
    // 根据错误级别设置不同的图标和样式
    const errorLevel = options.level || 'default'; // default, warning, critical, info
    let iconClass = 'fa-exclamation-circle';
    let bgClass = '';
    
    switch(errorLevel) {
        case 'warning':
            iconClass = 'fa-exclamation-triangle';
            bgClass = 'error-warning';
            break;
        case 'critical':
            iconClass = 'fa-exclamation-circle';
            bgClass = 'error-critical';
            break;
        case 'info':
            iconClass = 'fa-info-circle';
            bgClass = 'error-info';
            break;
    }
    
    errorContainer.innerHTML = `
        <div class="error-message-content ${bgClass}">
            <i class="fa ${iconClass}"></i>
            <span>${message}</span>
            <button type="button" class="close-error" aria-label="关闭">
                <i class="fa fa-times"></i>
            </button>
        </div>
        ${options.allowRetry ? `
        <div class="error-actions">
            <button type="button" class="retry-error-btn">
                <i class="fa fa-refresh"></i>
                重试
            </button>
        </div>
        ` : ''}
    `;
    
    // 设置自定义位置（如果有）
    if (options.position) {
        errorContainer.style.position = 'fixed';
        switch(options.position) {
            case 'top-left':
                errorContainer.style.top = '20px';
                errorContainer.style.left = '20px';
                break;
            case 'top-right':
                errorContainer.style.top = '20px';
                errorContainer.style.right = '20px';
                break;
            case 'bottom-left':
                errorContainer.style.bottom = '20px';
                errorContainer.style.left = '20px';
                break;
            case 'bottom-right':
                errorContainer.style.bottom = '20px';
                errorContainer.style.right = '20px';
                break;
            case 'top-center':
                errorContainer.style.top = '20px';
                errorContainer.style.left = '50%';
                errorContainer.style.transform = 'translateX(-50%)';
                break;
            default: // bottom-center
                errorContainer.style.bottom = '20px';
                errorContainer.style.left = '50%';
                errorContainer.style.transform = 'translateX(-50%)';
        }
    }
    
    document.body.appendChild(errorContainer);
    
    // 添加关闭事件
    const closeButton = errorContainer.querySelector('.close-error');
    closeButton.addEventListener('click', function() {
        errorContainer.classList.remove('animate__shakeX');
        errorContainer.classList.add('animate__fadeOutDown');
        setTimeout(() => {
            if (errorContainer.parentNode) {
                errorContainer.remove();
            }
        }, 300);
    });
    
    // 添加重试按钮事件
    if (options.allowRetry && options.onRetry) {
        const retryBtn = errorContainer.querySelector('.retry-error-btn');
        if (retryBtn) {
            retryBtn.addEventListener('click', () => {
                // 移除错误消息
                errorContainer.classList.remove('animate__shakeX');
                errorContainer.classList.add('animate__fadeOutDown');
                setTimeout(() => {
                    if (errorContainer.parentNode) {
                        errorContainer.remove();
                    }
                }, 300);
                
                // 执行重试回调
                options.onRetry();
            });
        }
    }
    
    // 自动关闭（如果启用）
    const autoHideTimeout = options.autoHide !== false ? 5000 : null;
    if (autoHideTimeout) {
        setTimeout(() => {
            errorContainer.classList.remove('animate__shakeX');
            errorContainer.classList.add('animate__fadeOutDown');
            setTimeout(() => {
                if (errorContainer.parentNode) {
                    errorContainer.remove();
                }
            }, 300);
        }, autoHideTimeout);
    }
    
    // 记录错误到控制台（开发环境）
    if (options.logToConsole !== false) {
        console.error(`[错误] ${message}`, options.errorData);
    }
    
    // 返回创建的元素
    return errorContainer;
}

// 初始化行程结果页面
function initItineraryResult() {
    // 尝试从sessionStorage获取行程数据并解码Unicode
    try {
        const storedData = sessionStorage.getItem('itinerary_data');
        if (storedData) {
            // 解析并解码数据
            const parsedData = JSON.parse(storedData);
            const decodedData = decodeUnicodeEscapesInObject(parsedData);
            
            // 将解码后的数据重新存储，确保页面使用解码后的数据
            sessionStorage.setItem('itinerary_data', JSON.stringify(decodedData, null, 2));
            console.log('已从sessionStorage解码行程数据');
        }
    } catch (e) {
        console.error('解码sessionStorage中的行程数据时出错:', e);
    }
    
    // 初始化标签页切换
    initTabs();
    
    // 初始化地图
    initMap();
    
    // 初始化导出功能
    initExportFunctions();
    
    // 初始化分享功能
    initShareFunctions();
    
    // 初始化常见问题切换
    initFAQToggle();
}

// 初始化标签页切换
function initTabs() {
    const tabs = document.querySelectorAll('.nav-tab');
    const tabContents = document.querySelectorAll('.tab-content');
    
    tabs.forEach(tab => {
        tab.addEventListener('click', function() {
            const tabTarget = this.dataset.target;
            
            // 移除所有标签的活动状态
            tabs.forEach(t => t.classList.remove('active'));
            
            // 隐藏所有内容
            tabContents.forEach(content => {
                content.classList.add('hidden');
                content.classList.remove('block');
            });
            
            // 添加当前标签的活动状态
            this.classList.add('active');
            
            // 显示对应内容
            const targetContent = document.getElementById(`${tabTarget}-content`);
            if (targetContent) {
                targetContent.classList.remove('hidden');
                targetContent.classList.add('block');
                
                // 如果是地图标签，确保地图正确加载
                if (tabTarget === 'overview') {
                    setTimeout(initMap, 100);
                }
            }
        });
    });
}

// 初始化地图
function initMap() {
    // 检查是否在结果页面且有地图容器
    const mapContainer = document.getElementById('map-container');
    
    if (mapContainer) {
        // 尝试从会话存储获取行程数据
        let itineraryData = null;
        try {
            itineraryData = JSON.parse(sessionStorage.getItem('itinerary_data'));
        } catch (e) {
            console.error('无法获取行程数据:', e);
        }
        
        // 这里会集成高德地图API
        // 由于需要API密钥，这里只提供基础框架
        
        // 模拟地图加载
        if (!mapContainer.classList.contains('map-loaded')) {
            mapContainer.classList.add('map-loaded');
            mapContainer.innerHTML = '<div class="map-placeholder">行程地图将在这里显示</div>';
            
            // 实际实现中，这里会调用高德地图API创建地图实例
            /*
            if (window.AMap) {
                // 初始化地图
                var map = new AMap.Map('map-container', {
                    zoom: 12,
                    center: [100.25, 25.62] // 默认大理市坐标
                });
                
                // 添加标记点和路线
                addMapMarkers(map, itineraryData);
                
                // 添加控件
                map.addControl(new AMap.ToolBar());
                map.addControl(new AMap.Scale());
            }
            */
        }
    }
}

// 添加地图标记点和路线
function addMapMarkers(map, data) {
    if (!data || !data.itinerary) return;
    
    const markers = [];
    const path = [];
    
    // 遍历每天行程
    data.itinerary.daily_itineraries.forEach(day => {
        // 遍历每个景点
        day.schedule.forEach(item => {
            if (item.spot && item.spot.location) {
                const position = [item.spot.location.longitude, item.spot.location.latitude];
                
                // 创建标记点
                const marker = new AMap.Marker({
                    position: position,
                    title: item.spot.name,
                    map: map
                });
                
                // 添加信息窗口
                const infoWindow = new AMap.InfoWindow({
                    content: `<h3>${item.spot.name}</h3><p>游玩时间: ${item.duration_hours}小时</p>`
                });
                
                marker.on('click', function() {
                    infoWindow.open(map, marker.getPosition());
                });
                
                markers.push(marker);
                path.push(position);
            }
        });
    });
    
    // 添加路线
    if (path.length > 1) {
        const polyline = new AMap.Polyline({
            path: path,
            strokeColor: '#165DFF',
            strokeWeight: 5,
            strokeOpacity: 0.8
        });
        
        polyline.setMap(map);
        
        // 设置地图视野，显示所有标记
        map.setFitView();
    }
}

// 初始化导出功能
function initExportFunctions() {
    // 导出为JSON
    document.getElementById('export-json').addEventListener('click', function() {
        exportItinerary('json');
    });
    
    // 导出为文本
    document.getElementById('export-text').addEventListener('click', function() {
        exportItinerary('text');
    });
    
    // 导出为PDF（模拟）
    const exportPdfBtn = document.getElementById('export-pdf');
    if (exportPdfBtn) {
        exportPdfBtn.addEventListener('click', function() {
            alert('PDF导出功能将在未来版本中提供');
        });
    }
}

// 导出行程
function exportItinerary(format) {
    try {
        const itineraryData = JSON.parse(sessionStorage.getItem('itinerary_data'));
        
        if (!itineraryData) {
            throw new Error('没有找到行程数据');
        }
        
        let content, filename, mimeType;
        
        if (format === 'json') {
            content = JSON.stringify(itineraryData, null, 2);
            filename = `travel_itinerary_${getFormattedDate()}.json`;
            mimeType = 'application/json';
        } else if (format === 'text') {
            content = generateTextItinerary(itineraryData);
            filename = `travel_itinerary_${getFormattedDate()}.txt`;
            mimeType = 'text/plain';
        }
        
        // 创建下载链接
        const blob = new Blob([content], { type: mimeType });
        const url = URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = filename;
        
        // 模拟点击下载
        document.body.appendChild(link);
        link.click();
        
        // 清理
        setTimeout(() => {
            document.body.removeChild(link);
            URL.revokeObjectURL(url);
        }, 100);
        
        // 显示成功消息
        showSuccessMessage(`行程已成功导出为${format.toUpperCase()}文件`);
        
    } catch (error) {
        console.error('导出失败:', error);
        showErrorMessage('导出行程失败，请重试');
    }
}

// 生成文本格式行程
function generateTextItinerary(data) {
    let text = `===== 智游中国 - 行程规划 =====\n\n`;
    
    // 行程概览
    text += `【行程概览】\n`;
    text += `目的地: ${data.destination || '未知'}\n`;
    text += `天数: ${data.days || '未知'}天\n`;
    text += `生成时间: ${data.generation_time || new Date().toLocaleString()}\n\n`;
    
    // 季节信息
    if (data.seasonal_info) {
        text += `【最佳季节】\n`;
        text += `${data.seasonal_info.current_season}，${data.seasonal_info.season_suggestion}\n`;
        text += `${data.seasonal_info.city_suggestion}\n\n`;
    }
    
    // 每日行程
    if (data.itinerary && data.itinerary.daily_itineraries) {
        data.itinerary.daily_itineraries.forEach(day => {
            text += `【Day ${day.day}】\n`;
            text += `${day.description}\n\n`;
            
            day.schedule.forEach(item => {
                text += `- ${item.arrival_time}-${item.departure_time}: ${item.spot.name}\n`;
                text += `  类型: ${item.spot.type}\n`;
                text += `  游玩时间: ${item.duration_hours}小时\n`;
                if (item.transportation) {
                    text += `  交通: ${item.transportation}\n`;
                }
                text += `\n`;
            });
            
            if (day.dining_suggestions && day.dining_suggestions.length > 0) {
                text += `【餐饮建议】\n`;
                day.dining_suggestions.forEach(dining => {
                    text += `${dining.time_of_day}:\n`;
                    dining.options.forEach(option => {
                        text += `- ${option.name}: ${option.description}\n`;
                    });
                });
                text += `\n`;
            }
            
            text += `【住宿建议】\n`;
            text += `${day.accommodation_suggestion}\n\n`;
        });
    }
    
    // 旅行贴士
    if (data.itinerary && data.itinerary.travel_tips) {
        text += `【旅行贴士】\n`;
        data.itinerary.travel_tips.forEach(tip => {
            text += `- ${tip}\n`;
        });
        text += `\n`;
    }
    
    text += `==============================`;
    return text;
}

// 获取格式化日期
function getFormattedDate() {
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    const hour = String(now.getHours()).padStart(2, '0');
    const minute = String(now.getMinutes()).padStart(2, '0');
    
    return `${year}${month}${day}_${hour}${minute}`;
}

// 初始化分享功能
function initShareFunctions() {
    const shareButton = document.getElementById('share-itinerary');
    const shareModal = document.getElementById('share-modal');
    const closeShareModal = document.getElementById('close-share-modal');
    
    if (shareButton && shareModal && closeShareModal) {
        // 打开分享模态框
        shareButton.addEventListener('click', function() {
            shareModal.classList.remove('hidden');
            shareModal.classList.add('flex');
            
            // 生成分享链接（模拟）
            generateShareLink();
        });
        
        // 关闭分享模态框
        closeShareModal.addEventListener('click', function() {
            shareModal.classList.add('hidden');
            shareModal.classList.remove('flex');
        });
        
        // 点击模态框外部关闭
        shareModal.addEventListener('click', function(event) {
            if (event.target === shareModal) {
                shareModal.classList.add('hidden');
                shareModal.classList.remove('flex');
            }
        });
    }
}

// 生成分享链接
function generateShareLink() {
    // 在实际实现中，这里会生成一个指向此行程的唯一链接
    const shareLinkInput = document.getElementById('share-link-input');
    
    if (shareLinkInput) {
        // 生成一个模拟的分享链接
        const baseUrl = window.location.origin;
        const shareCode = Math.random().toString(36).substring(2, 10);
        const shareLink = `${baseUrl}/shared/${shareCode}`;
        
        shareLinkInput.value = shareLink;
        
        // 添加复制功能
        const copyButton = document.getElementById('copy-share-link');
        if (copyButton) {
            copyButton.addEventListener('click', function() {
                shareLinkInput.select();
                document.execCommand('copy');
                showSuccessMessage('分享链接已复制到剪贴板');
            });
        }
    }
}

// 初始化常见问题切换
function initFAQToggle() {
    const faqItems = document.querySelectorAll('.faq-item');
    
    faqItems.forEach(item => {
        const question = item.querySelector('.faq-question');
        const answer = item.querySelector('.faq-answer');
        const icon = item.querySelector('.faq-icon');
        
        if (question && answer && icon) {
            question.addEventListener('click', function() {
                answer.classList.toggle('hidden');
                icon.classList.toggle('rotate-180');
            });
        }
    });
}

// 显示成功消息（增强版）
function showSuccessMessage(message, options = {}) {
    // 清除已存在的成功消息
    const existingMessage = document.querySelector('.success-message');
    if (existingMessage) {
        existingMessage.remove();
    }
    
    const successMessage = document.createElement('div');
    successMessage.className = 'success-message animate__animated animate__fadeInUp';
    
    // 根据消息类型设置不同的图标
    const iconClass = options.icon || 'fa-check-circle';
    const bgColor = options.bgColor || '';
    
    successMessage.innerHTML = `
        <div class="message-content ${bgColor}">
            <i class="fa ${iconClass}"></i>
            <span>${message}</span>
            <button class="close-btn" aria-label="关闭">
                <i class="fa fa-times"></i>
            </button>
        </div>
    `;
    
    // 设置自定义位置（如果有）
    if (options.position) {
        successMessage.style.position = 'fixed';
        switch(options.position) {
            case 'top-left':
                successMessage.style.top = '20px';
                successMessage.style.left = '20px';
                break;
            case 'top-right':
                successMessage.style.top = '20px';
                successMessage.style.right = '20px';
                break;
            case 'bottom-left':
                successMessage.style.bottom = '20px';
                successMessage.style.left = '20px';
                break;
            case 'bottom-right':
                successMessage.style.bottom = '20px';
                successMessage.style.right = '20px';
                break;
            case 'top-center':
                successMessage.style.top = '20px';
                successMessage.style.left = '50%';
                successMessage.style.transform = 'translateX(-50%)';
                break;
            default: // bottom-center
                successMessage.style.bottom = '20px';
                successMessage.style.left = '50%';
                successMessage.style.transform = 'translateX(-50%)';
        }
    }
    
    document.body.appendChild(successMessage);
    
    // 添加点击关闭事件
    const closeBtn = successMessage.querySelector('.close-btn');
    if (closeBtn) {
        closeBtn.addEventListener('click', () => {
            successMessage.classList.remove('animate__fadeInUp');
            successMessage.classList.add('animate__fadeOutDown');
            setTimeout(() => {
                if (successMessage.parentNode) {
                    successMessage.parentNode.removeChild(successMessage);
                }
            }, 300);
        });
    }
    
    // 自动关闭（如果启用）
    const autoHideTimeout = options.autoHide !== false ? 3000 : null;
    if (autoHideTimeout) {
        setTimeout(() => {
            successMessage.classList.remove('animate__fadeInUp');
            successMessage.classList.add('animate__fadeOutDown');
            setTimeout(() => {
                if (successMessage.parentNode) {
                    successMessage.parentNode.removeChild(successMessage);
                }
            }, 300);
        }, autoHideTimeout);
    }
    
    // 如果需要，返回创建的元素
    return successMessage;
}

// 隐藏加载状态
function hideLoadingState() {
    const loadingOverlay = document.querySelector('.loading-overlay');
    const formSubmitButton = document.getElementById('submit-button');
    
    if (loadingOverlay) {
        loadingOverlay.remove();
    }
    
    if (formSubmitButton && formSubmitButton.dataset.originalContent) {
        formSubmitButton.disabled = false;
        formSubmitButton.innerHTML = formSubmitButton.dataset.originalContent;
    }
}

// 日期选择器初始化
function initDatePicker() {
    const dateInput = document.getElementById('travel-date');
    
    if (dateInput) {
        // 设置默认日期为明天
        const tomorrow = new Date();
        tomorrow.setDate(tomorrow.getDate() + 1);
        dateInput.value = tomorrow.toISOString().split('T')[0];
        
        // 添加日期格式化功能
        dateInput.addEventListener('change', function() {
            // 可以在这里添加日期验证逻辑
        });
    }
}

// 动画效果辅助函数
function animateValue(obj, start, end, duration) {
    let startTimestamp = null;
    const step = (timestamp) => {
        if (!startTimestamp) startTimestamp = timestamp;
        const progress = Math.min((timestamp - startTimestamp) / duration, 1);
        obj.innerHTML = Math.floor(progress * (end - start) + start);
        if (progress < 1) {
            window.requestAnimationFrame(step);
        }
    };
    window.requestAnimationFrame(step);
}

// 全局变量存储用户偏好
let userPreferences = [];
let currentState = 'waiting_for_city'; // 初始状态：等待城市输入
// 聊天界面使用的偏好选项列表（避免与主要的availablePreferences对象冲突）
let chatPreferenceOptions = ["自然风光", "历史文化", "美食", "亲子", "购物", "休闲度假", "户外探险", "摄影"];

// 初始化对话界面
function initChatInterface() {
    const chatInput = document.getElementById('chat-input');
    const sendButton = document.getElementById('send-message');
    const chatContainer = document.getElementById('chat-container');
    const quickActionButtons = document.querySelectorAll('.quick-action-btn');
    
    // 发送消息事件
    sendButton.addEventListener('click', sendMessage);
    chatInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            sendMessage();
        }
    });
    
    // 快捷操作按钮事件
    quickActionButtons.forEach(button => {
        button.addEventListener('click', function() {
            const action = this.getAttribute('data-action');
            handleQuickAction(action);
        });
    });
    
    // 自动滚动到底部
    scrollToBottom();
}

// 发送用户消息
function sendMessage() {
    const chatInput = document.getElementById('chat-input');
    const message = chatInput.value.trim();
    
    if (message === '') return;
    
    // 添加用户消息到聊天界面
    addMessageToChat('user', message);
    
    // 清空输入框
    chatInput.value = '';
    
    // 处理用户消息
    processUserMessage(message);
}

// 处理用户消息，根据当前状态决定下一步
function processUserMessage(message) {
    switch (currentState) {
        case 'waiting_for_city':
            handleCityInput(message);
            break;
        case 'waiting_for_days':
            handleDaysInput(message);
            break;
        case 'waiting_for_preferences':
            handlePreferencesInput(message);
            break;
        case 'complete':
            // 如果已经收集了所有信息，可以重新开始或修改现有信息
            if (message.includes('修改') || message.includes('重新') || message.includes('换一个')) {
                resetConversation();
            } else {
                addMessageToChat('bot', '我已经为您生成了行程规划。如果您想修改规划，可以点击下方的快捷按钮进行调整。');
            }
            break;
        default:
            addMessageToChat('bot', '抱歉，我不太理解您的意思。请问您想去哪个城市旅行？');
            currentState = 'waiting_for_city';
    }
}

// 处理城市输入
function handleCityInput(message) {
    // 简单的城市名提取逻辑，可以根据需要扩展
    const cityName = message.replace(/[^\u4e00-\u9fa5a-zA-Z\s]/g, '').trim();
    
    if (cityName) {
        // 保存城市信息
        document.getElementById('city').value = cityName;
        
        // 更新状态并询问天数
        currentState = 'waiting_for_days';
        addMessageToChat('bot', `您想去${cityName}旅行，很棒的选择！请问您计划旅行几天？`);
    } else {
        addMessageToChat('bot', '请告诉我您想去的城市名称，以便我为您规划行程。');
    }
}

// 处理天数输入
function handleDaysInput(message) {
    // 提取数字
    const daysMatch = message.match(/\d+/);
    let days = daysMatch ? parseInt(daysMatch[0]) : null;
    
    // 验证天数
    if (days && days > 0 && days <= 30) {
        // 保存天数信息
        document.getElementById('days').value = days;
        
        // 更新状态并询问偏好
        currentState = 'waiting_for_preferences';
        
        // 显示偏好选择UI
        showPreferencesSelection();
    } else {
        addMessageToChat('bot', '请输入有效的天数（1-30天）。');
    }
}

// 处理偏好输入
function handlePreferencesInput(message) {
    // 尝试从文本中匹配偏好
    const matchedPreferences = chatPreferenceOptions.filter(pref => message.includes(pref));
    
    if (matchedPreferences.length > 0) {
        userPreferences = matchedPreferences;
        completeDataCollection();
    } else {
        addMessageToChat('bot', '抱歉，我没有识别到您的偏好。您可以选择：自然风光、历史文化、美食、亲子、购物、休闲度假、户外探险、摄影。');
    }
}

// 显示偏好选择界面
function showPreferencesSelection() {
    // 添加机器人消息
    addMessageToChat('bot', '请选择您的旅行偏好（可多选）：', function() {
        // 创建偏好选择按钮
        const chatContainer = document.getElementById('chat-container');
        const preferencesContainer = document.createElement('div');
        preferencesContainer.className = 'flex flex-wrap gap-2 pl-13';
        
        chatPreferenceOptions.forEach(preference => {
            const button = document.createElement('button');
            button.className = 'px-4 py-2 rounded-full border border-gray-300 text-sm hover:bg-primary hover:text-white hover:border-primary transition-colors';
            button.textContent = preference;
            
            button.addEventListener('click', function() {
                if (!userPreferences.includes(preference)) {
                    userPreferences.push(preference);
                    this.classList.add('bg-primary', 'text-white', 'border-primary');
                } else {
                    userPreferences = userPreferences.filter(item => item !== preference);
                    this.classList.remove('bg-primary', 'text-white', 'border-primary');
                }
            });
            
            preferencesContainer.appendChild(button);
        });
        
        // 添加确认按钮
        const confirmButton = document.createElement('button');
        confirmButton.className = 'w-full mt-3 px-4 py-2 bg-primary text-white rounded-lg';
        confirmButton.textContent = '确认选择';
        confirmButton.addEventListener('click', function() {
            if (userPreferences.length > 0) {
                completeDataCollection();
            } else {
                addMessageToChat('bot', '请至少选择一项旅行偏好。');
            }
        });
        
        preferencesContainer.appendChild(confirmButton);
        chatContainer.appendChild(preferencesContainer);
        scrollToBottom();
    });
}

// 数据收集完成，准备调用API
function completeDataCollection() {
    const city = document.getElementById('city').value;
    const days = document.getElementById('days').value;
    
    // 显示总结消息
    const preferencesText = userPreferences.join('、');
    addMessageToChat('bot', `我已收到您的旅行信息：\n- 目的地：${city}\n- 天数：${days}天\n- 旅行偏好：${preferencesText}\n\n正在为您生成行程规划，请稍候...`);
    
    // 更新状态为完成
    currentState = 'complete';
    
    // 准备数据并调用API
    prepareAndSendData();
}

// 准备数据并发送到后端
function prepareAndSendData() {
    const city = document.getElementById('city').value;
    const days = document.getElementById('days').value;
    
    // 显示加载状态
    showLoading();
    
    // 构建数据对象
    const data = {
        city: city,
        province: document.getElementById('province').value,
        days: parseInt(days),
        preferences: userPreferences,
        travel_date: document.getElementById('travel_date').value || new Date().toISOString().split('T')[0]
    };
    
    // 发送数据到后端API
    fetch('/plan', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('网络响应错误');
        }
        return response.text();
    })
    .then(html => {
        // 隐藏加载状态
        hideLoading();
        
        // 解析返回的HTML，提取行程规划内容
        addMessageToChat('bot', '您的行程规划已生成！');
        
        // 创建结果展示区域
        showItineraryResult(html);
    })
    .catch(error => {
        console.error('生成行程出错:', error);
        hideLoading();
        addMessageToChat('bot', '很抱歉，生成行程规划时出错。请稍后再试。');
    });
}

// 显示行程结果
function showItineraryResult(html) {
    // 创建一个临时元素来解析HTML
    const tempDiv = document.createElement('div');
    tempDiv.innerHTML = html;
    
    // 提取行程规划内容
    const itineraryContent = tempDiv.querySelector('.itinerary-result') || tempDiv;
    
    if (itineraryContent) {
        const chatContainer = document.getElementById('chat-container');
        const resultContainer = document.createElement('div');
        resultContainer.className = 'pl-13 pr-4';
        
        // 添加结果卡片
        const resultCard = document.createElement('div');
        resultCard.className = 'bg-white rounded-lg p-4 shadow-sm border border-gray-100';
        resultCard.innerHTML = itineraryContent.innerHTML;
        
        resultContainer.appendChild(resultCard);
        chatContainer.appendChild(resultContainer);
    } else {
        // 如果无法解析HTML，直接显示
        addMessageToChat('bot', html);
    }
    
    scrollToBottom();
}

// 处理快捷操作
function handleQuickAction(action) {
    switch (action) {
        case 'city':
            currentState = 'waiting_for_city';
            addMessageToChat('bot', '请告诉我您想去的城市名称。');
            break;
        case 'days':
            currentState = 'waiting_for_days';
            addMessageToChat('bot', '请告诉我您计划旅行几天。');
            break;
        case 'preferences':
            currentState = 'waiting_for_preferences';
            showPreferencesSelection();
            break;
        case 'generate':
            // 检查是否已经收集了所有必要信息
            const city = document.getElementById('city').value;
            const days = document.getElementById('days').value;
            
            if (!city) {
                addMessageToChat('bot', '请先告诉我您想去的城市名称。');
                currentState = 'waiting_for_city';
            } else if (!days) {
                addMessageToChat('bot', '请先告诉我您计划旅行几天。');
                currentState = 'waiting_for_days';
            } else if (userPreferences.length === 0) {
                showPreferencesSelection();
                currentState = 'waiting_for_preferences';
            } else {
                // 所有信息都已收集，可以直接生成
                completeDataCollection();
            }
            break;
    }
}

// 添加消息到聊天界面
function addMessageToChat(sender, message, callback) {
    const chatContainer = document.getElementById('chat-container');
    const messageContainer = document.createElement('div');
    
    if (sender === 'user') {
        // 用户消息
        messageContainer.className = 'flex items-start space-x-3 justify-end';
        messageContainer.innerHTML = `
            <div class="bg-primary text-white rounded-lg p-4 shadow-sm max-w-[80%]">
                <p>${escapeHtml(message)}</p>
            </div>
            <div class="w-10 h-10 rounded-full bg-gray-200 flex items-center justify-center flex-shrink-0">
                <i class="fa fa-user text-gray-500"></i>
            </div>
        `;
    } else {
        // 机器人消息
        messageContainer.className = 'flex items-start space-x-3';
        messageContainer.innerHTML = `
            <div class="w-10 h-10 rounded-full bg-primary/10 flex items-center justify-center flex-shrink-0">
                <i class="fa fa-robot text-primary"></i>
            </div>
            <div class="bg-white rounded-lg p-4 shadow-sm max-w-[80%]">
                <p>${escapeHtml(message)}</p>
            </div>
        `;
    }
    
    chatContainer.appendChild(messageContainer);
    scrollToBottom();
    
    // 如果提供了回调函数，在消息添加后执行
    if (typeof callback === 'function') {
        setTimeout(callback, 100);
    }
}

// 显示加载状态
function showLoading() {
    const chatContainer = document.getElementById('chat-container');
    const loadingElement = document.createElement('div');
    loadingElement.id = 'loading-indicator';
    loadingElement.className = 'flex items-start space-x-3';
    loadingElement.innerHTML = `
        <div class="w-10 h-10 rounded-full bg-primary/10 flex items-center justify-center flex-shrink-0">
            <i class="fa fa-robot text-primary"></i>
        </div>
        <div class="bg-white rounded-lg p-4 shadow-sm">
            <div class="flex space-x-2">
                <div class="w-2 h-2 rounded-full bg-primary animate-bounce"></div>
                <div class="w-2 h-2 rounded-full bg-primary animate-bounce" style="animation-delay: 0.2s"></div>
                <div class="w-2 h-2 rounded-full bg-primary animate-bounce" style="animation-delay: 0.4s"></div>
            </div>
        </div>
    `;
    
    chatContainer.appendChild(loadingElement);
    scrollToBottom();
}

// 隐藏加载状态
function hideLoading() {
    const loadingElement = document.getElementById('loading-indicator');
    if (loadingElement) {
        loadingElement.remove();
    }
}

// 滚动到底部
function scrollToBottom() {
    const chatContainer = document.getElementById('chat-container');
    chatContainer.scrollTop = chatContainer.scrollHeight;
}

// 重置对话
function resetConversation() {
    userPreferences = [];
    currentState = 'waiting_for_city';
    
    // 清空表单
    document.getElementById('province').value = '';
    document.getElementById('city').value = '';
    document.getElementById('days').value = '3';
    
    // 清空聊天记录，只保留欢迎消息
    const chatContainer = document.getElementById('chat-container');
    chatContainer.innerHTML = `
        <div class="flex items-start space-x-3">
            <div class="w-10 h-10 rounded-full bg-primary/10 flex items-center justify-center flex-shrink-0">
                <i class="fa fa-robot text-primary"></i>
            </div>
            <div class="bg-white rounded-lg p-4 shadow-sm max-w-[80%]">
                <p class="text-gray-700">您好！我是您的智能旅行规划助手。请问您计划去哪里旅行？请告诉我目的地城市名称。</p>
            </div>
        </div>
    `;
    
    scrollToBottom();
}

// HTML转义函数，防止XSS攻击
function escapeHtml(text) {
    return text
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#039;');
}

// 修改原有的表单提交函数，确保兼容性
function submitFormData(form) {
    // 调用对话式交互的API调用函数
    prepareAndSendData();
    return false; // 阻止默认表单提交
}

// 修改DOMContentLoaded事件，添加对话界面初始化
document.addEventListener('DOMContentLoaded', function() {
    // ... 现有的初始化代码 ...
    
    // 初始化对话界面
    initChatInterface();
});