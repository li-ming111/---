import java.sql.*;

public class insert_user {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/xueya?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "123456";
        
        try {
            // 加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 建立连接
            Connection conn = DriverManager.getConnection(url, user, password);
            // 创建SQL语句
            String sql = "INSERT INTO user (id, username, password, name, gender, age, grade, student_id, id_card, email, phone, school_id, major_id, role, status, create_time, update_time) VALUES (1, '2023020616', '$2a$10$E4jvZJ6B2vQ8Yb4Y8Y8Y8e7e7e7e7e7e7e7e7e7e7e7e7e7e7e7e7e7e7', '测试用户', '男', 20, '2023', '2023020616', '123456789012345678', 'test@example.com', '13800138000', 1, NULL, '1', '1', NOW(), NOW()) ON DUPLICATE KEY UPDATE password = VALUES(password), name = VALUES(name), gender = VALUES(gender), age = VALUES(age), grade = VALUES(grade), student_id = VALUES(student_id), id_card = VALUES(id_card), email = VALUES(email), phone = VALUES(phone), school_id = VALUES(school_id), major_id = VALUES(major_id), role = VALUES(role), status = VALUES(status), update_time = NOW();";
            // 创建执行对象
            Statement stmt = conn.createStatement();
            // 执行SQL语句
            int result = stmt.executeUpdate(sql);
            System.out.println("插入/更新用户数据成功，影响行数: " + result);
            // 关闭连接
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
