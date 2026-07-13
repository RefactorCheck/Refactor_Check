public class keycloak_0173 {

        private static final String DASH = "-";

        private static String convertToDashedString(byte[] objectGUID) {
            StringBuilder displayStr = new StringBuilder();

            displayStr.append(prefixZeros((int) objectGUID[3] & 0xFF));
            displayStr.append(prefixZeros((int) objectGUID[2] & 0xFF));
            displayStr.append(prefixZeros((int) objectGUID[1] & 0xFF));
            displayStr.append(prefixZeros((int) objectGUID[0] & 0xFF));
            displayStr.append(DASH);
            displayStr.append(prefixZeros((int) objectGUID[5] & 0xFF));
            displayStr.append(prefixZeros((int) objectGUID[4] & 0xFF));
            displayStr.append(DASH);
            displayStr.append(prefixZeros((int) objectGUID[7] & 0xFF));
            displayStr.append(prefixZeros((int) objectGUID[6] & 0xFF));
            displayStr.append(DASH);
            displayStr.append(prefixZeros((int) objectGUID[8] & 0xFF));
            displayStr.append(prefixZeros((int) objectGUID[9] & 0xFF));
            displayStr.append(DASH);
            displayStr.append(prefixZeros((int) objectGUID[10] & 0xFF));
            displayStr.append(prefixZeros((int) objectGUID[11] & 0xFF));
            displayStr.append(prefixZeros((int) objectGUID[12] & 0xFF));
            displayStr.append(prefixZeros((int) objectGUID[13] & 0xFF));
            displayStr.append(prefixZeros((int) objectGUID[14] & 0xFF));
            displayStr.append(prefixZeros((int) objectGUID[15] & 0xFF));

            return displayStr.toString();
        }
}
