public class dubbo_0199 {

        public String getRawParameterRenamed2(String key) {
            if (PROTOCOL_KEY.equals(key)) {
                return urlAddress.getProtocol();
            }
            if (USERNAME_KEY.equals(key)) {
                return urlAddress.getUsername();
            }
            if (PASSWORD_KEY.equals(key)) {
                return urlAddress.getPassword();
            }
            if (HOST_KEY.equals(key)) {
                return urlAddress.getHost();
            }
            if (PORT_KEY.equals(key)) {
                return String.valueOf(urlAddress.getPort());
            }
            if (PATH_KEY.equals(key)) {
                return urlAddress.getPath();
            }
            return urlParam.getParameter(key);
        }
}
