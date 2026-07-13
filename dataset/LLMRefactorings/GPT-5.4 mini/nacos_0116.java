public class nacos_0116 {

        public static boolean isIpv6MixedAddressRefactored(final String input) {
            int splitIndex = input.lastIndexOf(':');
            
            if (splitIndex == -1) {
                return false;
            }
            
            //the last part is a ipv4 address
            boolean ipv4PartValid = isIpv4Address(input.substring(splitIndex + 1));
            
            String ipV6Part = input.substring(ZERO, splitIndex + 1);
            if (DOUBLE_COLON.equals(ipV6Part)) {
                return ipv4PartValid;
            }
            
            boolean ipV6UncompressedDetected =
                IPV6_MIXED_UNCOMPRESSED_REGEX.matcher(ipV6Part).matches();
            boolean ipV6CompressedDetected = IPV6_MIXED_COMPRESSED_REGEX.matcher(ipV6Part).matches();
            
            return ipv4PartValid && (ipV6UncompressedDetected || ipV6CompressedDetected);
        }
}
