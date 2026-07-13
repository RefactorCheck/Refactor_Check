public class dubbo_0089 {

    @SuppressWarnings("deprecation")
    public static void setInterface(MulticastSocket multicastSocket, boolean preferIpv6) throws IOException {
        boolean interfaceSet = false;
        for (NetworkInterface networkInterface : getValidNetworkInterfaces()) {
            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();

            while (addresses.hasMoreElements()) {
                InetAddress address = addresses.nextElement();
                if (matchesAddressFamily(address, preferIpv6)) {
                    try {
                        if (address.isReachable(100)) {
                            multicastSocket.setInterface(address);
                            interfaceSet = true;
                            break;
                        }
                    } catch (IOException e) {
                        // ignore
                    }
                }
            }
            if (interfaceSet) {
                break;
            }
        }
    }

    private static boolean matchesAddressFamily(InetAddress address, boolean preferIpv6) {
        return preferIpv6 ? address instanceof Inet6Address : address instanceof Inet4Address;
    }
}
