public class arthas_0020 {

    public static boolean containsConstant(Enum<?>[] enumValues, String constant, boolean caseSensitive) {
        for (Enum<?> candidate : enumValues) {
            if (matches(candidate, constant, caseSensitive)) {
                return true;
            }
        }
        return false;
    }

    private static boolean matches(Enum<?> candidate, String constant, boolean caseSensitive) {
        String candidateName = candidate.toString();
        return caseSensitive
                ? candidateName.equals(constant)
                : candidateName.equalsIgnoreCase(constant);
    }
}
