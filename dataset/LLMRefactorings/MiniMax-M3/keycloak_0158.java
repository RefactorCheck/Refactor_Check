public class keycloak_0158 {

        @Override
        public void init(Config.Scope config) {
            //PicketLinkCoreSTS sts = PicketLinkCoreSTS.instance();
            //sts.installDefaultConfiguration();
            ProtocolMapperModel model;
            model = createX500AttributeMapper("X500 email", "email",
                    X500SAMLProfileConstants.EMAIL.get(),
                    X500SAMLProfileConstants.EMAIL.getFriendlyName(),
                    "${email}");
            builtins.put("X500 email", model);
            model = createX500AttributeMapper("X500 givenName", "firstName",
                    X500SAMLProfileConstants.GIVEN_NAME.get(),
                    X500SAMLProfileConstants.GIVEN_NAME.getFriendlyName(),
                    "${givenName}");
            builtins.put("X500 givenName", model);
            model = createX500AttributeMapper("X500 surname", "lastName",
                    X500SAMLProfileConstants.SURNAME.get(),
                    X500SAMLProfileConstants.SURNAME.getFriendlyName(),
                    "${familyName}");
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
            this.destinationValidator = DestinationValidator.forProtocolMap(config.getArray("knownProtocols"));
            this.maxInflatingSize = config.getLong("maxInflatingSize", DeflateUtil.DEFAULT_MAX_INFLATING_SIZE);
        }

        private ProtocolMapperModel createX500AttributeMapper(String mapperName, String userProperty,
                String samlAttributeUri, String friendlyName, String valueTemplate) {
            return UserPropertyAttributeStatementMapper.createAttributeMapper(mapperName, userProperty,
                    samlAttributeUri,
                    JBossSAMLURIConstants.ATTRIBUTE_FORMAT_URI.get(),
                    friendlyName, true, valueTemplate);
        }
}
