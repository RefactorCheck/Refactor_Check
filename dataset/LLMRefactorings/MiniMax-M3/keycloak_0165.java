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
    
            addRequestedAttributeToServices(entityDescriptor, requestedAttribute, attributeName, attributeFriendlyName);
        }

        private void addRequestedAttributeToServices(EntityDescriptorType entityDescriptor, RequestedAttributeType requestedAttribute, String attributeName, String attributeFriendlyName) {
            for (EntityDescriptorType.EDTChoiceType choiceType: entityDescriptor.getChoiceType()) {
                List<EntityDescriptorType.EDTDescriptorChoiceType> descriptors = choiceType.getDescriptors();
                for (EntityDescriptorType.EDTDescriptorChoiceType descriptor: descriptors) {
                    for (AttributeConsumingServiceType attributeConsumingService: descriptor.getSpDescriptor().getAttributeConsumingService())
                    {
                        boolean alreadyPresent = attributeConsumingService.getRequestedAttribute().stream()
                            .anyMatch(t -> (attributeName == null || attributeName.equalsIgnoreCase(t.getName())) &&
                                           (attributeFriendlyName == null || attributeFriendlyName.equalsIgnoreCase(t.getFriendlyName())));
    
                        if (!alreadyPresent)
                            attributeConsumingService.addRequestedAttribute(requestedAttribute);
                    }
                }
            }
        }
}
