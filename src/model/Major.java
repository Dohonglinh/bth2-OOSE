
package model;

public enum Major {
    CNTT, KTPM;

    public static Major fromString(String s) {
        if (s == null) throw new IllegalArgumentException("Nganh rong");
        s = s.trim().toUpperCase();
        if (s.equals("CNTT")) return CNTT;
        if (s.equals("KTPM")) return KTPM;
        throw new IllegalArgumentException("Nganh chi co the CNTT hoac KTPM");
    }
}
