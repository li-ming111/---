import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneratePassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "123456";
        String encodedPassword = encoder.encode(password);
        System.out.println("Original password: " + password);
        System.out.println("Encoded password: " + encodedPassword);
        System.out.println("Encoded password length: " + encodedPassword.length());
        
        // 验证密码
        boolean matches = encoder.matches(password, encodedPassword);
        System.out.println("Password matches: " + matches);
    }
}
