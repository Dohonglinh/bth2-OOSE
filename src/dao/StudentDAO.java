package dao;

import model.Major;
import model.Student;
import util.DbUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public boolean insert(Student s) throws Exception {
        String sql = "INSERT INTO students(ma_sv, ho_ten, ngay_sinh, nganh, diem_tb, lop_sinh_hoat) VALUES (?,?,?,?,?,?)";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s.getMaSV());
            ps.setString(2, s.getHoTen());
            ps.setDate(3, Date.valueOf(s.getNgaySinh()));
            ps.setString(4, s.getNganh().name());
            ps.setBigDecimal(5, new BigDecimal(s.getDiemTB()).setScale(2, RoundingMode.HALF_UP));
            ps.setString(6, s.getLopSinhHoat());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean update(Student s) throws Exception {
        String sql = "UPDATE students SET ho_ten=?, ngay_sinh=?, nganh=?, diem_tb=?, lop_sinh_hoat=? WHERE ma_sv=?";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s.getHoTen());
            ps.setDate(2, Date.valueOf(s.getNgaySinh()));
            ps.setString(3, s.getNganh().name());
            ps.setBigDecimal(4, new BigDecimal(s.getDiemTB()).setScale(2, RoundingMode.HALF_UP));
            ps.setString(5, s.getLopSinhHoat());
            ps.setString(6, s.getMaSV());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean delete(String maSV) throws Exception {
        String sql = "DELETE FROM students WHERE ma_sv=?";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, maSV);
            return ps.executeUpdate() == 1;
        }
    }

    public Student findById(String maSV) throws Exception {
        String sql = "SELECT * FROM students WHERE ma_sv=?";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, maSV);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<Student> findAll() throws Exception {
        String sql = "SELECT * FROM students";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Student> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;
        }
    }

    public List<Student> findByClass(String lop) throws Exception {
        String sql = "SELECT * FROM students WHERE lop_sinh_hoat=?";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, lop);
            try (ResultSet rs = ps.executeQuery()) {
                List<Student> list = new ArrayList<>();
                while (rs.next()) list.add(map(rs));
                return list;
            }
        }
    }

    public List<Student> findByMajor(Major m) throws Exception {
        String sql = "SELECT * FROM students WHERE nganh=?";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, m.name());
            try (ResultSet rs = ps.executeQuery()) {
                List<Student> list = new ArrayList<>();
                while (rs.next()) list.add(map(rs));
                return list;
            }
        }
    }

    public List<Student> findBirthMonth(int month) throws Exception {
        String sql = "SELECT * FROM students WHERE MONTH(ngay_sinh)=?";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, month);
            try (ResultSet rs = ps.executeQuery()) {
                List<Student> list = new ArrayList<>();
                while (rs.next()) list.add(map(rs));
                return list;
            }
        }
    }

    public List<Student> findAllOrderByGPA(boolean desc) throws Exception {
        String sql = "SELECT * FROM students ORDER BY diem_tb " + (desc ? "DESC" : "ASC");
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Student> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;
        }
    }

    private Student map(ResultSet rs) throws Exception {
        String ma = rs.getString("ma_sv");
        String hoTen = rs.getString("ho_ten");
        LocalDate d = rs.getDate("ngay_sinh").toLocalDate();
        Major m = Major.valueOf(rs.getString("nganh"));
        double gpa = rs.getBigDecimal("diem_tb").doubleValue();
        String lop = rs.getString("lop_sinh_hoat");
        return new Student(ma, hoTen, d, m, gpa, lop);
    }
}
