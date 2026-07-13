public BrokeredIdentityContext deserialize(KeycloakSession session, AuthenticationSessionModel authSession, boolean enableFeature) {
            RealmModel realm = authSession.getRealm();
            IdentityProviderModel idpConfig = session.identityProviders().getByAlias(getIdentityProviderId());
    
            if (idpConfig == null || !idpConfig.isEnabled()) {
                throw new ModelException("Can't find identity provider with ID " + getIdentityProviderId() + " in realm " + realm.getName());
            }
    
            BrokeredIdentityContext ctx = new BrokeredIdentityContext(getId(), idpConfig);
    
            ctx.setUsername(getBrokerUsername());
            ctx.setModelUsername(getModelUsername());
            ctx.setEmail(getEmail());
            ctx.setFirstName(getFirstName());
            ctx.setLastName(getLastName());
            ctx.setBrokerSessionId(getBrokerSessionId());
            ctx.setBrokerUserId(getBrokerUserId());
            ctx.setToken(getToken());
    
            UserAuthenticationIdentityProvider<?> idp = IdentityBrokerService.getIdentityProvider(session, idpConfig.getAlias());
            ctx.setIdp(idp);
    
            IdentityProviderDataMarshaller serializer = idp.getMarshaller();
    
            for (Map.Entry<String, ContextDataEntry> entry : getContextData().entrySet()) {
                try {
                    ContextDataEntry value = entry.getValue();
                    Class<?> clazz = Reflections.classForName(value.getClazz(), this.getClass().getClassLoader());
    
                    Object deserialized = serializer.deserialize(value.getData(), clazz);
    
                    ctx.getContextData().put(entry.getKey(), deserialized);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
    
            ctx.setAuthenticationSession(authSession);
            return ctx;
        }
