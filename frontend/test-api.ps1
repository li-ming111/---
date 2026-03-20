# 测试API端点
Write-Host "Testing API endpoints..."

# 测试 /api/goals
Write-Host "\nTesting /api/goals..."
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8082/api/goals" -Method GET -Headers @{"Authorization"="Bearer 123456"}
    Write-Host "Status Code: $($response.StatusCode)"
    Write-Host "Response: $($response.Content)"
} catch {
    Write-Host "Error: $($_.Exception.Message)"
}

# 测试 /api/career-plans/user/4
Write-Host "\nTesting /api/career-plans/user/4..."
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8082/api/career-plans/user/4" -Method GET -Headers @{"Authorization"="Bearer 123456"}
    Write-Host "Status Code: $($response.StatusCode)"
    Write-Host "Response: $($response.Content)"
} catch {
    Write-Host "Error: $($_.Exception.Message)"
}

# 测试 /api/user-incentives
Write-Host "\nTesting /api/user-incentives..."
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8082/api/user-incentives" -Method GET -Headers @{"Authorization"="Bearer 123456"}
    Write-Host "Status Code: $($response.StatusCode)"
    Write-Host "Response: $($response.Content)"
} catch {
    Write-Host "Error: $($_.Exception.Message)"
}

# 测试 /api/user-points
Write-Host "\nTesting /api/user-points..."
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8082/api/user-points" -Method GET -Headers @{"Authorization"="Bearer 123456"}
    Write-Host "Status Code: $($response.StatusCode)"
    Write-Host "Response: $($response.Content)"
} catch {
    Write-Host "Error: $($_.Exception.Message)"
}
