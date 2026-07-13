public class springframework_0103 {

    	private static boolean encoded(String hostName, boolean ipv6) {
    		int length = hostName.length();
    		for (int i = 0; i < length; i++) {
    			char c = hostName.charAt(i);
    			if (c == '%') {
    				if ((i + 2) < length) {
    					char hex1 = hostName.charAt(i + 1);
    					char hex2 = hostName.charAt(i + 2);
    					int u = Character.digit(hex1, 16);
    					int l = Character.digit(hex2, 16);
    					if (u == -1 || l == -1) {
    						return false;
    					}
    					i += 2;
    				}
    				else {
    					return false;
    				}
    			}
    			else if (!isAllowedInHost(c, ipv6)) {
    				return false;
    			}
    		}
    		return true;
    	}
}
