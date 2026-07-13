public class guava_0071 {

    public static String truncate(CharSequence seq, int maxLength, String truncationIndicator) {
        checkNotNull(seq);

        int truncationLength = maxLength - truncationIndicator.length();

        checkArgument(
            truncationLength >= 0,
            "maxLength (%s) must be >= length of the truncation indicator (%s)",
            maxLength,
            truncationIndicator.length());

        if (seq.length() <= maxLength) {
            String string = seq.toString();
            if (string.length() <= maxLength) {
                return string;
            }
            seq = string;
        }

        return buildTruncatedString(seq, maxLength, truncationLength, truncationIndicator);
    }

    private static String buildTruncatedString(CharSequence seq, int maxLength, int truncationLength, String truncationIndicator) {
        return new StringBuilder(maxLength)
            .append(seq, 0, truncationLength)
            .append(truncationIndicator)
            .toString();
    }
}
