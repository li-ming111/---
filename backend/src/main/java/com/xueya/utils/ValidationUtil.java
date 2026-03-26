package com.xueya.utils;

public class ValidationUtil {
    // 验证身份证号格式（18位）
    public static boolean isValidIdCardFormat(String idCard) {
        if (idCard == null) {
            return false;
        }
        return idCard.matches("^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]$") && idCard.length() == 18;
    }

    // 验证身份证号校验码
    public static boolean validateIdCardChecksum(String idCard) {
        if (idCard == null || idCard.length() != 18) {
            return false;
        }
        
        // 前17位的系数
        int[] weights = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        // 校验码对应值
        char[] checkCodes = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += (idCard.charAt(i) - '0') * weights[i];
        }
        
        int mod = sum % 11;
        char expectedCheckCode = checkCodes[mod];
        char actualCheckCode = Character.toUpperCase(idCard.charAt(17));
        
        return expectedCheckCode == actualCheckCode;
    }

    // 验证手机号格式（11位纯数字）
    public static boolean isValidPhoneFormat(String phone) {
        if (phone == null) {
            return false;
        }
        return phone.matches("^1[3-9]\\d{9}$");
    }

    // 验证邮箱格式
    public static boolean isValidEmailFormat(String email) {
        if (email == null) {
            return false;
        }
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    // 验证密码复杂度
    public static boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        if (!password.matches(".*[a-zA-Z].*")) {
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            return false;
        }
        if (!password.matches(".*[^a-zA-Z0-9].*")) {
            return false;
        }
        return true;
    }

    // 检查密码长度
    public static boolean isPasswordLengthValid(String password) {
        return password != null && password.length() >= 6;
    }

    // 检查密码是否包含字母
    public static boolean isPasswordContainsLetter(String password) {
        return password != null && password.matches(".*[a-zA-Z].*");
    }

    // 检查密码是否包含数字
    public static boolean isPasswordContainsDigit(String password) {
        return password != null && password.matches(".*\\d.*");
    }

    // 检查密码是否包含特殊字符
    public static boolean isPasswordContainsSpecialChar(String password) {
        return password != null && password.matches(".*[^a-zA-Z0-9].*");
    }
}