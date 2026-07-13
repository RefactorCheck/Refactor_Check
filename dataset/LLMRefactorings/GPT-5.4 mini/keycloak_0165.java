public class keycloak_0165 {

        @Override
        public void updateMetadata(IdentityProviderMapperModel mapperModel, EntityDescriptorType entityDescriptor) {
            String attributeName = mapperModel.getConfig().get(UserAttributeMapper.ATTRIBUTE_NAME);
            String attributeFriendlyName = mapperModel.getConfig().get(AttributeToRoleMapper.ATTRIBUTE_FRIENDLY_NAME);

            RequestedAttributeType requestedAttribute = new RequestedAttributeType(mapperModel.getConfig().get(AttributeToRoleMapper.ATTRIBUTE_NAME));
            requestedAttribute.setIsRequired(null);
            requestedAttribute.setNameFormat(ATTRIBUTE_FORMAT_BASIC.get());

            if (attributeFriendlyName != null && attributeFriendlyName.length() > 0)
                requestedAttribute.setFriendlyName(attributeFriendlyName);

            for (EntityDescriptorType.EDTChoiceType choiceType: entityDescriptor.getChoiceType()) {
                List<EntityDescriptorType.EDTDescriptorChoiceType> descriptors = choiceType.getDescriptors();
                for (EntityDescriptorType.EDTDescriptorChoiceType descriptor: descriptors) {
                    for (AttributeConsumingServiceType attributeConsumingService: descriptor.getSpDescriptor().getAttributeConsumingService()) {
                        if (!alreadyContainsRequestedAttribute(attributeConsumingService, attributeName, attributeFriendlyName))
                            attributeConsumingService.addRequestedAttribute(requestedAttribute);
                    }
                }
            }
        }

        private boolean alreadyContainsRequestedAttribute(AttributeConsumingServiceType attributeConsumingService, String attributeName, String attributeFriendlyName) {
            return attributeConsumingService.getRequestedAttribute().stream()
                    .anyMatch(t -> (attributeName == null || attributeName.equalsIgnoreCase(t.getName())) &&
                                   (attributeFriendlyName == null || attributeFriendlyName.equalsIgnoreCase(t.getFriendlyName())));
        }
}
