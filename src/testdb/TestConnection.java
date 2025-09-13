package testdb;

import java.sql.*;

public class TestConnection {
    public static void main(String[] args) {
        String url = "jdbc:sqlserver://127.0.0.1:1433;"
                   + "databaseName=oop_students;"
                   + "encrypt=false;trustServerCertificate=true;"
                   + "integratedSecurity=true;loginTimeout=30";

        String sql = "SELECT TOP 5 ma_sv, ho_ten, nganh, diem_tb FROM students";

        try (Connection conn = DriverManager.getConnection(url)) {
            System.out.println("✅ Kết nối thành công bằng Windows Authentication");

            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {

                while (rs.next()) {
                    System.out.printf("%s | %s | %s | %.2f%n",
                            rs.getString("ma_sv"),
                            rs.getString("ho_ten"),
                            rs.getString("nganh"),
                            rs.getDouble("diem_tb"));
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi kết nối: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
