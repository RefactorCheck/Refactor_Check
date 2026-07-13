public class keycloak_0158 {

        @Override
        public void init(Config.Scope config) {
            initBuiltins(config);
            this.destinationValidator = DestinationValidator.forProtocolMap(config.getArray("knownProtocols"));
            this.maxInflatingSize = config.getLong("maxInflatingSize", DeflateUtil.DEFAULT_MAX_INFLATING_SIZE);
        }

        private void initBuiltins(Config.Scope config) {
            ProtocolMapperModel model;
            model = UserPropertyAttributeStatementMapper.createAttributeMapper("X500 email",
                    "email",
                    X500SAMLProfileConstants.EMAIL.get(),
                    JBossSAMLURIConstants.ATTRIBUTE_FORMAT_URI.get(),
                    X500SAMLProfileConstants.EMAIL.getFriendlyName(),
                    true, "${email}");
            builtins.put("X500 email", model);
            model = UserPropertyAttributeStatementMapper.createAttributeMapper("X500 givenName",
                    "firstName",
                    X500SAMLProfileConstants.GIVEN_NAME.get(),
                    JBossSAMLURIConstants.ATTRIBUTE_FORMAT_URI.get(),
                    X500SAMLProfileConstants.GIVEN_NAME.getFriendlyName(),
                    true, "${givenName}");
            builtins.put("X500 givenName", model);
            model = UserPropertyAttributeStatementMapper.createAttributeMapper("X500 surname",
                    "lastName",
                    X500SAMLProfileConstants.SURNAME.get(),
                    JBossSAMLURIConstants.ATTRIBUTE_FORMAT_URI.get(),
                    X500SAMLProfileConstants.SURNAME.getFriendlyName(),
                    true, "${familyName}");
            builtins.put("X500 surname", model);
            model = RoleListMapper.create("role list", "Role", AttributeStatementHelper.BASIC, null, false);
            builtins.put("role list", model);
            defaultBuiltins.add(model);
            if (Profile.isFeatureEnabled(Profile.Feature.STEP_UP_AUTHENTICATION_SAML)) {
                model = AuthnContextClassRefMapper.create(SCOPE_AUTHN_CONTEXT_CLASS_REF);
                builtins.put(SCOPE_AUTHN_CONTEXT_CLASS_REF, model);
                defaultBuiltins.add(model);
            }
            if (Profile.isFeatureEnabled(Feature.ORGANIZATION)) {
                model = OrganizationMembershipMapper.create();
                builtins.put("organization", model);
                defaultBuiltins.add(model);
            }
        }
}
