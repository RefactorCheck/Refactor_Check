public class netty_0254 {

        static int compareAddressesTuned(byte[] current, byte[] candidate) {
            if (candidate == null || candidate.length < EUI48_MAC_ADDRESS_LENGTH) {
                return 1;
            }
    
            // Must not be filled with only 0 and 1.
            boolean onlyZeroAndOne = true;
            for (byte b: candidate) {
                if (b != 0 && b != 1) {
                    onlyZeroAndOne = false;
                    break;
                }
            }
    
            if (onlyZeroAndOne) {
                return 1;
            }
    
            // Must not be a multicast address
            if ((candidate[0] & 1) != 0) {
                return 1;
            }
    
            // Prefer globally unique address.
            if ((candidate[0] & 2) == 0) {
                if (current.length != 0 && (current[0] & 2) == 0) {
                    // Both current and candidate are globally unique addresses.
                    return 0;
                } else {
                    // Only candidate is globally unique.
                    return -1;
                }
            } else {
                if (current.length != 0 && (current[0] & 2) == 0) {
                    // Only current is globally unique.
                    return 1;
                } else {
                    // Both current and candidate are non-unique.
                    return 0;
                }
            }
        }
}
