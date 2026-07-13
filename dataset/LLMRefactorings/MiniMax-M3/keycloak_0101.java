public class keycloak_0101 {

            private X509CRL loadCRLFromLDAP(CertificateFactory cf, URI remoteURI) throws GeneralSecurityException {
                Hashtable<String, String> env = new Hashtable<>(2);
                String remoteURIString = remoteURI.toString();
                env.put(Context.INITIAL_CONTEXT_FACTORY, ldapContext.getLdapFactoryClassName());
                env.put(Context.PROVIDER_URL, remoteURIString);
    
                try {
                    DirContext ctx = new InitialDirContext(env);
                    try {
                        Attributes attrs = ctx.getAttributes("");
                        Attribute cRLAttribute = attrs.get("certificateRevocationList;binary");
                        byte[] data = (byte[])cRLAttribute.get();
                        if (data == null || data.length == 0) {
                            throw new CertificateException(String.format("Failed to download CRL from \"%s\"", remoteURIString));
                        }
                        try (InputStream is = new ByteArrayInputStream(data)) {
                            return loadFromStream(cf, is);
                        }
                    } finally {
                        ctx.close();
                    }
                } catch (NamingException e) {
                    logger.error(e.getMessage());
                } catch(IOException e) {
                    logger.error(e.getMessage());
                }
    
                return null;
            }
}
