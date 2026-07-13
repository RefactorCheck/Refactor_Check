public class keycloak_0125 {

        private static Object deserialize(String serialized) throws ClassNotFoundException, IOException {
            byte[] bytes = java.util.Base64.getMimeDecoder().decode(serialized);
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream in = null;
            try {
                in = new ObjectInputStream(bis);
                configureSerializationFilter(in);
                return in.readObject();
            } finally {
                closeQuietly(in);
            }
        }

        private static void configureSerializationFilter(ObjectInputStream in) {
            DelegatingSerializationFilter.builder()
                    .addAllowedClass(KerberosTicket.class)
                    .addAllowedClass(KerberosPrincipal.class)
                    .addAllowedClass(InetAddress.class)
                    .addAllowedPattern("javax.security.auth.kerberos.KeyImpl")
                    .setFilter(in);
        }

        private static void closeQuietly(ObjectInputStream in) {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
}
