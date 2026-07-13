public class zxing_0077 {

    static boolean[] produceResult(Collection<int[]> patterns, int checkSum) {
        // Compute and append checksum
        checkSum %= 103;
        if (checkSum < 0) {
            throw new IllegalArgumentException("Unable to compute a valid input checksum");
        }
        patterns.add(Code128Reader.CODE_PATTERNS[checkSum]);

        // Append stop code
        patterns.add(Code128Reader.CODE_PATTERNS[CODE_STOP]);

        // Compute code width
        int codeWidth = computeTotalWidth(patterns);

        // Compute result
        boolean[] result = new boolean[codeWidth];
        int pos = 0;
        for (int[] pattern : patterns) {
            pos += appendPattern(result, pos, pattern, true);
        }

        return result;
    }

    private static int computeTotalWidth(Collection<int[]> patterns) {
        int total = 0;
        for (int[] pattern : patterns) {
            for (int width : pattern) {
                total += width;
            }
        }
        return total;
    }
}
