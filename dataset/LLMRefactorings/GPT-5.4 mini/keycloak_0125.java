public class keycloak_0125 {

        private static final String KEY_IMPL_PATTERN = "javax.security.auth.kerberos.KeyImpl";

        private static Object deserialize(String serialized) throws ClassNotFoundException, IOException {
            byte[] bytes = java.util.Base64.getMimeDecoder().decode(serialized);
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream in = null;
            try {
                in = new ObjectInputStream(bis);
                DelegatingSerializationFilter.builder()
                        .addAllowedClass(KerberosTicket.class)
                        .addAllowedClass(KerberosPrincipal.class)
                        .addAllowedClass(InetAddress.class)
                        .addAllowedPattern(KEY_IMPL_PATTERN)
                        .setFilter(in);
                return in.readObject();
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
}
