public class netty_0254 {

    static int compareAddresses(byte[] current, byte[] candidate) {
        if (candidate == null || candidate.length < EUI48_MAC_ADDRESS_LENGTH) {
            return 1;
        }

        if (isOnlyZeroAndOne(candidate)) {
            return 1;
        }

        // Must not be a multicast address
        if ((candidate[0] & 1) != 0) {
            return 1;
        }

        // Prefer globally unique address.
        if ((candidate[0] & 2) == 0) {
            if (current.length != 0 && (current[0] & 2) == 0) {
                return 0;
            } else {
                return -1;
            }
        } else {
            if (current.length != 0 && (current[0] & 2) == 0) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private static boolean isOnlyZeroAndOne(byte[] bytes) {
        for (byte b : bytes) {
            if (b != 0 && b != 1) {
                return false;
            }
        }
        return true;
    }
}
