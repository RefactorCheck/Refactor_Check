@Override
        protected KeyWrapper loadKey(RealmModel realm, ComponentModel model) {
            String publicEcdhKeyBase64Encoded = model.getConfig().getFirst(GeneratedEcdhKeyProviderFactory.ECDH_PUBLIC_KEY_KEY);
            String ecdhAlgorithm = model.getConfig().getFirst(GeneratedEcdhKeyProviderFactory.ECDH_ALGORITHM_KEY);
            boolean generateCertificate = Optional.ofNullable(model.getConfig()
                                                                   .getFirst(Attributes.EC_GENERATE_CERTIFICATE_KEY))
                                                  .map(Boolean::parseBoolean)
                                                  .orElse(false);
    
            try {
                PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Base64.getMimeDecoder().decode((model.getConfig().getFirst(GeneratedEcdhKeyProviderFactory.ECDH_PRIVATE_KEY_KEY))));
                KeyFactory kf = KeyFactory.getInstance("EC");
                PrivateKey decodedPrivateKey = kf.generatePrivate(privateKeySpec);
    
                X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.getMimeDecoder().decode(publicEcdhKeyBase64Encoded));
                PublicKey decodedPublicKey = kf.generatePublic(publicKeySpec);
    
                KeyPair keyPair = new KeyPair(decodedPublicKey, decodedPrivateKey);
                X509Certificate selfSignedCertificate = Optional.ofNullable(model.getConfig()
                                                                                 .getFirst(Attributes.CERTIFICATE_KEY))
                                                                .map(PemUtils::decodeCertificate)
                                                                .orElse(null);
                if (generateCertificate && selfSignedCertificate == null)
                {
                    selfSignedCertificate = CertificateUtils.generateV1SelfSignedCertificate(keyPair, realm.getName());
                    model.getConfig().put(Attributes.CERTIFICATE_KEY,
                                          List.of(Base64.getEncoder().encodeToString(selfSignedCertificate.getEncoded())));
                }
    
                return createKeyWrapper(keyPair, ecdhAlgorithm, KeyUse.ENC, selfSignedCertificate);
            } catch (Exception e) {
                logger.debug(e.getMessage(), e);
                logger.warnf("Exception at decodeEcdhPublicKey. %s", e.toString());
                return null;
            }
    
        }
