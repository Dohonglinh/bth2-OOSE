package util;

import model.Major;
import java.time.LocalDate;
import java.time.Period;

public class Validator {
    public static boolean isValidGPA(double gpa) {
        return gpa >= 0.0 && gpa <= 10.0;
    }

    public static boolean isValidBirth(LocalDate d) {
        if (d == null || d.isAfter(LocalDate.now())) return false;
        int age = Period.between(d, LocalDate.now()).getYears();
        return age >= 15 && age <= 110;
    }

    public static boolean isValidMajor(Major m) {
        return m == Major.CNTT || m == Major.KTPM;
    }

    public static boolean isValidMaSV(String maSV, Major major) {
        if (maSV == null || !maSV.matches("\\d{10}")) return false;
        String prefix = (major == Major.CNTT) ? "455105" : "455109";
        return maSV.startsWith(prefix);
    }
}
