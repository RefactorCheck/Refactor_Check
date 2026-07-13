public class netty_0236 {

    private static String combineWith(String first, String second) {
        return first.toUpperCase(Locale.ROOT) + "with" + second.toUpperCase(Locale.ROOT);
    }

    static String toJavaName(String opensslName) {
        if (opensslName == null) {
            return null;
        }
        Matcher matcher = PATTERN.matcher(opensslName);
        if (matcher.matches()) {
            String group1 = matcher.group(1);
            if (group1 != null) {
                return combineWith(group1, matcher.group(2));
            }
            if (matcher.group(3) != null) {
                return combineWith(matcher.group(4), matcher.group(3));
            }

            if (matcher.group(5) != null) {
                return combineWith(matcher.group(6), matcher.group(5));
            }
        }
        return null;
    }
}
