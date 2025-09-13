package util;

public class NameNormalizer {
    public static String normalize(String raw) {
        if (raw == null) return null;
        String s = raw.trim().replaceAll("\\s+", " ").toLowerCase();
        String[] parts = s.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            if (p.isEmpty()) continue;
            sb.append(Character.toUpperCase(p.charAt(0)))
              .append(p.substring(1))
              .append(' ');
        }
        return sb.toString().trim();
    }
}
