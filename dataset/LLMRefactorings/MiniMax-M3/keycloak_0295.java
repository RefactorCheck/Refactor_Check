public class keycloak_0295 {

        @Override
        public void authenticate(AuthenticationFlowContext context) {
            Map<String, String> config = context.getAuthenticatorConfig().getConfig();
            String attrName = config.get(SetUserAttributeAuthenticatorFactory.CONF_ATTR_NAME);
            String attrValue = config.get(SetUserAttributeAuthenticatorFactory.CONF_ATTR_VALUE);
    
            UserModel user = context.getUser();
            applyAttribute(user, attrName, attrValue);
    
            context.success();
        }

        private void applyAttribute(UserModel user, String attrName, String attrValue) {
            List<String> attrValues = user.getAttributeStream(attrName).collect(Collectors.toList());
            if (attrValues.isEmpty()) {
                user.setSingleAttribute(attrName, attrValue);
            }
            else {
                if (!attrValues.contains(attrValue)) {
                    attrValues.add(attrValue);
                }
                user.setAttribute(attrName, attrValues);
            }
        }
}
