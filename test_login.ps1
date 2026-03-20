$uri = "http://localhost:8082/api/auth/login"
$headers = @{"Content-Type"="application/json"}
$body = '{"schoolCode":"HIIE","username":"student","password":"student123"}'

try {
    $response = Invoke-RestMethod -Uri $uri -Method POST -Headers $headers -Body $body
    Write-Host "Login Success!"
    Write-Host "Response: $($response | ConvertTo-Json)"
} catch {
    Write-Host "Login Failed: $($_.Exception.Message)"
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "Response Body: $responseBody"
    }
}
