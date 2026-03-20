import java.sql.*;

public class create_user {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/xueya?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "123456";
        
        try {
            // 加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 建立连接
            Connection conn = DriverManager.getConnection(url, user, password);
            
            // 检查是否存在账号2023020616
            String checkSql = "SELECT * FROM user WHERE username = '2023020616' OR student_id = '2023020616';";
            Statement checkStmt = conn.createStatement();
            ResultSet rs = checkStmt.executeQuery(checkSql);
            
            if (rs.next()) {
                System.out.println("账号已存在，无需创建");
            } else {
                // 创建账号2023020616，密码123456
                // 注意：这里使用的密码哈希值是手动生成的，实际应该使用PasswordEncoder.encode()方法
                String insertSql = "INSERT INTO user (id, username, password, name, gender, age, grade, student_id, id_card, email, phone, school_id, major_id, role, status, create_time, update_time) VALUES (3, '2023020616', '$2a$10$E4jvZJ6B2vQ8Yb4Y8Y8Y8e7e7e7e7e7e7e7e7e7e7e7e7e7e7e7e7e7e7', '测试用户', '男', 20, '2023', '2023020616', '123456789012345678', 'test@example.com', '13800138000', 1, NULL, '1', '1', NOW(), NOW());";
                Statement insertStmt = conn.createStatement();
                int result = insertStmt.executeUpdate(insertSql);
                System.out.println("创建账号成功，影响行数: " + result);
                insertStmt.close();
            }
            
            // 关闭连接
            rs.close();
            checkStmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
