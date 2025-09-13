package util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbUtil {
    private static String URL;
    private static String USER;
    private static String PASS;

    static {
        try {
            Properties p = new Properties();
            try (InputStream is = DbUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
                if (is == null) throw new RuntimeException("Khong tim thay db.properties tren classpath");
                p.load(is);
            }
            URL  = p.getProperty("db.url");
            USER = p.getProperty("db.user", "");
            PASS = p.getProperty("db.password", "");

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        // Nếu dùng integratedSecurity=true thì USER để trống → dùng overload không có user/pass
        if (USER == null || USER.isBlank()) return DriverManager.getConnection(URL);
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
