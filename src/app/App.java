package app;

import dao.StudentDAO;
import model.Major;
import model.Student;
import util.NameNormalizer;
import util.Validator;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final Scanner sc = new Scanner(System.in);
    private static final StudentDAO dao = new StudentDAO();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== QUAN LY SINH VIEN =====");
            System.out.println("1. Them sinh vien");
            System.out.println("2. Sua sinh vien");
            System.out.println("3. Xoa sinh vien");
            System.out.println("4. In tat ca SV");
            System.out.println("5. In SV theo lop sinh hoat");
            System.out.println("6. In SV theo nganh");
            System.out.println("7. In SV sap xep theo diem TB (tang/giam)");
            System.out.println("8. In SV sinh vao thang X");
            System.out.println("9. Tim SV theo ma");
            System.out.println("0. Thoat");
            System.out.print("Chon: ");
            String opt = sc.nextLine().trim();
            try {
                switch (opt) {
                    case "1" -> addStudent();
                    case "2" -> updateStudent();
                    case "3" -> deleteStudent();
                    case "4" -> printList(dao.findAll());
                    case "5" -> printByClass();
                    case "6" -> printByMajor();
                    case "7" -> printOrderByGPA();
                    case "8" -> printByBirthMonth();
                    case "9" -> findById();
                    case "0" -> { System.out.println("Bye!"); return; }
                    default -> System.out.println("Lua chon khong hop le!");
                }
            } catch (Exception e) {
                System.out.println("Loi: " + e.getMessage());
            }
        }
    }

    private static void addStudent() throws Exception {
        System.out.print("Ma SV (10 chu so): ");
        String ma = sc.nextLine().trim();

        System.out.print("Ho ten: ");
        String hoTen = NameNormalizer.normalize(sc.nextLine());

        LocalDate dob = readDate("Ngay sinh (yyyy-MM-dd): ");

        System.out.print("Nganh (CNTT/KTPM): ");
        Major nganh = Major.fromString(sc.nextLine());

        System.out.print("Diem TB [0..10]: ");
        double gpa = Double.parseDouble(sc.nextLine());

        System.out.print("Lop sinh hoat: ");
        String lop = sc.nextLine().trim();

        if (!Validator.isValidMajor(nganh)) throw new IllegalArgumentException("Nganh khong hop le");
        if (!Validator.isValidMaSV(ma, nganh)) throw new IllegalArgumentException("Ma SV khong hop le/khong dung prefix voi nganh");
        if (!Validator.isValidBirth(dob)) throw new IllegalArgumentException("Ngay sinh/tuoi khong hop le");
        if (!Validator.isValidGPA(gpa)) throw new IllegalArgumentException("Diem TB khong hop le");
        if (hoTen == null || hoTen.isBlank()) throw new IllegalArgumentException("Ho ten khong duoc rong");

        boolean ok = dao.insert(new Student(ma, hoTen, dob, nganh, gpa, lop));
        System.out.println(ok ? "Them thanh cong" : "Them that bai");
    }

    private static void updateStudent() throws Exception {
        System.out.print("Nhap ma SV can sua: ");
        String ma = sc.nextLine().trim();
        Student s = dao.findById(ma);
        if (s == null) { System.out.println("Khong tim thay!"); return; }

        System.out.print("Ho ten moi (bo qua de giu nguyen): ");
        String ht = sc.nextLine();
        if (!ht.isBlank()) s.setHoTen(NameNormalizer.normalize(ht));

        LocalDate dob = readDateAllowEmpty("Ngay sinh moi (yyyy-MM-dd, bo qua = giu nguyen): ");
        if (dob != null) {
            if (!Validator.isValidBirth(dob)) throw new IllegalArgumentException("Ngay sinh/tuoi khong hop le");
            s.setNgaySinh(dob);
        }

        System.out.print("Nganh moi (CNTT/KTPM, bo qua = giu nguyen): ");
        String ng = sc.nextLine().trim();
        if (!ng.isBlank()) {
            Major m = Major.fromString(ng);
            if (!Validator.isValidMaSV(s.getMaSV(), m))
                throw new IllegalArgumentException("Ma SV khong phu hop voi nganh moi");
            s.setNganh(m);
        }

        System.out.print("Diem TB moi (bo qua = giu nguyen): ");
        String g = sc.nextLine().trim();
        if (!g.isBlank()) {
            double d = Double.parseDouble(g);
            if (!Validator.isValidGPA(d)) throw new IllegalArgumentException("Diem TB khong hop le");
            s.setDiemTB(d);
        }

        System.out.print("Lop SH moi (bo qua = giu nguyen): ");
        String lop = sc.nextLine().trim();
        if (!lop.isBlank()) s.setLopSinhHoat(lop);

        System.out.println(dao.update(s) ? "Sua thanh cong" : "Sua that bai");
    }

    private static void deleteStudent() throws Exception {
        System.out.print("Nhap ma SV can xoa: ");
        String ma = sc.nextLine().trim();
        System.out.println(dao.delete(ma) ? "Xoa thanh cong" : "Xoa that bai");
    }

    private static void printByClass() throws Exception {
        System.out.print("Nhap lop sinh hoat: ");
        printList(dao.findByClass(sc.nextLine().trim()));
    }

    private static void printByMajor() throws Exception {
        System.out.print("Nhap nganh (CNTT/KTPM): ");
        printList(dao.findByMajor(Major.fromString(sc.nextLine())));
    }

    private static void printOrderByGPA() throws Exception {
        System.out.print("Sap xep giam dan? (y/n): ");
        printList(dao.findAllOrderByGPA(sc.nextLine().trim().equalsIgnoreCase("y")));
    }

    private static void printByBirthMonth() throws Exception {
        System.out.print("Nhap thang (1-12): ");
        int m = Integer.parseInt(sc.nextLine());
        if (m < 1 || m > 12) throw new IllegalArgumentException("Thang khong hop le");
        printList(dao.findBirthMonth(m));
    }

    private static void findById() throws Exception {
        System.out.print("Nhap ma SV: ");
        var sv = dao.findById(sc.nextLine().trim());
        if (sv == null) System.out.println("Khong tim thay!");
        else System.out.println(header() + "\n" + sv);
    }

    private static void printList(List<Student> list) {
        System.out.println(header());
        for (Student s : list) System.out.println(s);
        System.out.println("Tong: " + list.size());
    }

    private static String header() {
        return "ma_sv     | ho_ten                   | ngay_sinh  | nganh | diem | lop      ";
    }

    private static LocalDate readDate(String msg) {
        while (true) {
            System.out.print(msg);
            String s = sc.nextLine().trim();
            try { return LocalDate.parse(s); }
            catch (DateTimeParseException e) { System.out.println("Dinh dang yyyy-MM-dd. Thu lai!"); }
        }
    }

    private static LocalDate readDateAllowEmpty(String msg) {
        System.out.print(msg);
        String s = sc.nextLine().trim();
        if (s.isBlank()) return null;
        try { return LocalDate.parse(s); }
        catch (DateTimeParseException e) { throw new IllegalArgumentException("Ngay khong hop le"); }
    }
}
