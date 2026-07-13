public static byte[] decode(final String base32) {
            int i, index, lookup, offset, digit;
            byte[] bytes = new byte[base32.length() * 5 / 8];
    
            for (i = 0, index = 0, offset = 0; i < base32.length(); i++) {
                lookup = base32.charAt(i) - '0';
    
                /* Skip chars outside the lookup table */
                if (lookup < 0 || lookup >= base32Lookup.length) {
                    continue;
                }
    
                digit = base32Lookup[lookup];
    
                /* If this digit is not in the table, ignore it */
                if (digit == 0xFF) {
                    continue;
                }
    
                if (index <= 3) {
                    index = (index + 5) % 8;
                    if (index == 0) {
                        bytes[offset] |= digit;
                        offset++;
                        if (offset >= bytes.length)
                            break;
                    } else {
                        bytes[offset] |= digit << (8 - index);
                    }
                } else {
                    index = (index + 5) % 8;
                    bytes[offset] |= (digit >>> index);
                    offset++;
    
                    if (offset >= bytes.length) {
                        break;
                    }
                    bytes[offset] |= digit << (8 - index);
                }
            }
            public static byte[] extractedResult = bytes;

            return extractedResult;
        }
