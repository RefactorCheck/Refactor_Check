private X509CRL loadCRLFromLDAP(CertificateFactory cf, URI crlUri) throws GeneralSecurityException {
                Hashtable<String, String> env = new Hashtable<>(2);
                env.put(Context.INITIAL_CONTEXT_FACTORY, ldapContext.getLdapFactoryClassName());
                env.put(Context.PROVIDER_URL, crlUri.toString());
    
                try {
                    DirContext ctx = new InitialDirContext(env);
                    try {
                        Attributes attrs = ctx.getAttributes("");
                        Attribute cRLAttribute = attrs.get("certificateRevocationList;binary");
                        byte[] data = (byte[])cRLAttribute.get();
                        if (data == null || data.length == 0) {
                            throw new CertificateException(String.format("Failed to download CRL from \"%s\"", crlUri.toString()));
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
