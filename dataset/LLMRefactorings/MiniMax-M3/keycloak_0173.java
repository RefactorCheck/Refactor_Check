public class keycloak_0173 {

    private static String convertToDashedString(byte[] objectGUID) {
        StringBuilder displayStr = new StringBuilder();

        appendBytes(displayStr, objectGUID, 3, 2, 1, 0).append("-");
        appendBytes(displayStr, objectGUID, 5, 4).append("-");
        appendBytes(displayStr, objectGUID, 7, 6).append("-");
        appendBytes(displayStr, objectGUID, 8, 9).append("-");
        appendBytes(displayStr, objectGUID, 10, 11, 12, 13, 14, 15);

        return displayStr.toString();
    }

    private static StringBuilder appendBytes(StringBuilder sb, byte[] bytes, int... indices) {
        for (int index : indices) {
            sb.append(prefixZeros((int) bytes[index] & 0xFF));
        }
        return sb;
    }
}
