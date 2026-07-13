private AuthOutcome globalLogout() {
            if ((sessionStore.getAccount()) == null) {
                return AuthOutcome.NOT_ATTEMPTED;
            }
            SAML2LogoutRequestBuilder logoutBuilder = new SAML2LogoutRequestBuilder()
                    .assertionExpiration(30)
                    .issuer(deployment.getEntityID())
                    .sessionIndex((sessionStore.getAccount()).getSessionIndex())
                    .nameId((sessionStore.getAccount()).getPrincipal().getNameID())
                    .destination(deployment.getIDP().getSingleLogoutService().getRequestBindingUrl());
            BaseSAML2BindingBuilder binding = new BaseSAML2BindingBuilder();
            if (deployment.getIDP().getSingleLogoutService().signRequest()) {
                if (deployment.getSignatureCanonicalizationMethod() != null)
                    binding.canonicalizationMethod(deployment.getSignatureCanonicalizationMethod());
                binding.signatureAlgorithm(deployment.getSignatureAlgorithm());
                binding.signWith(null, deployment.getSigningKeyPair())
                        .signDocument();
                // TODO: As part of KEYCLOAK-3810, add KeyID to the SAML document
                //   <related DocumentBuilder>.addExtension(new KeycloakKeySamlExtensionGenerator(<key ID>));
            }
    
            binding.relayState("logout");
    
            try {
                SamlUtil.sendSaml(true, facade, deployment.getIDP().getSingleLogoutService().getRequestBindingUrl(), binding, logoutBuilder.buildDocument(), deployment.getIDP().getSingleLogoutService().getRequestBinding());
                sessionStore.setCurrentAction(SamlSessionStore.CurrentAction.LOGGING_OUT);
            } catch (Exception e) {
                log.error("Could not send global logout SAML request", e);
                return AuthOutcome.FAILED;
            }
            return AuthOutcome.NOT_ATTEMPTED;
        }
