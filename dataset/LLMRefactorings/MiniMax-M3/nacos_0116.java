public class nacos_0116 {

        public static boolean isIpv6MixedAddress(final String input) {
            int splitIndex = input.lastIndexOf(':');
            
            if (splitIndex == -1) {
                return false;
            }
            
            boolean ipv4PartValid = isIpv4Address(input.substring(splitIndex + 1));
            
            String ipV6Part = input.substring(ZERO, splitIndex + 1);
            if (DOUBLE_COLON.equals(ipV6Part)) {
                return ipv4PartValid;
            }
            
            return ipv4PartValid && isIpv6PartValid(ipV6Part);
        }
        
        private static boolean isIpv6PartValid(final String ipV6Part) {
            boolean ipV6UncompressedDetected =
                IPV6_MIXED_UNCOMPRESSED_REGEX.matcher(ipV6Part).matches();
            boolean ipV6CompressedDetected = IPV6_MIXED_COMPRESSED_REGEX.matcher(ipV6Part).matches();
            return ipV6UncompressedDetected || ipV6CompressedDetected;
        }
}
