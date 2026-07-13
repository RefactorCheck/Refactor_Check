public class keycloak_0295 {

        @Override
        public void authenticate(AuthenticationFlowContext context) {
            // Retrieve configuration
            Map<String, String> config = context.getAuthenticatorConfig().getConfig();
            String attributeName = config.get(SetUserAttributeAuthenticatorFactory.CONF_ATTR_NAME);
            String attributeValue = config.get(SetUserAttributeAuthenticatorFactory.CONF_ATTR_VALUE);

            UserModel user = context.getUser();
            List<String> attributeValues = user.getAttributeStream(attributeName).collect(Collectors.toList());
            if (attributeValues.isEmpty()) {
                user.setSingleAttribute(attributeName, attributeValue);
            }
            else {
                if (!attributeValues.contains(attributeValue)) {
                    attributeValues.add(attributeValue);
                }
                user.setAttribute(attributeName, attributeValues);
            }

            context.success();
        }
}
