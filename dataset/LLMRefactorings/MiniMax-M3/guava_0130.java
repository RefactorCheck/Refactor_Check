public class guava_0130 {

      public static TeredoInfo getTeredoInfo(Inet6Address ip) {
        checkArgument(isTeredoAddress(ip), "Address '%s' is not a Teredo address.", toAddrString(ip));
    
        byte[] bytes = ip.getAddress();
        Inet4Address server = getInet4Address(Arrays.copyOfRange(bytes, 4, 8));
    
        int flags = newDataInput(bytes, 8).readShort() & 0xffff;
    
        // Teredo obfuscates the mapped client port, per section 4 of the RFC.
        int port = ~newDataInput(bytes, 10).readShort() & 0xffff;
    
        byte[] clientBytes = deobfuscateClientIp(Arrays.copyOfRange(bytes, 12, 16));
        Inet4Address client = getInet4Address(clientBytes);
    
        return new TeredoInfo(server, client, port, flags);
      }

      private static byte[] deobfuscateClientIp(byte[] clientBytes) {
        // Teredo obfuscates the mapped client IP, per section 4 of the RFC.
        for (int i = 0; i < clientBytes.length; i++) {
          clientBytes[i] = (byte) ~clientBytes[i];
        }
        return clientBytes;
      }
}
