public class keycloak_0217 {

    @Override
    public void updateMetadata(IdentityProviderMapperModel mapperModel, EntityDescriptorType entityDescriptor) {
        RequestedAttributeType requestedAttribute = createRequestedAttribute(mapperModel);
        addRequestedAttributeToServices(entityDescriptor, requestedAttribute);
    }

    private RequestedAttributeType createRequestedAttribute(IdentityProviderMapperModel mapperModel) {
        RequestedAttributeType requestedAttribute = new RequestedAttributeType(mapperModel.getConfig().get(XPathAttributeMapper.ATTRIBUTE_NAME));
        requestedAttribute.setIsRequired(null);
        requestedAttribute.setNameFormat(ATTRIBUTE_FORMAT_BASIC.get());

        String attributeFriendlyName = mapperModel.getConfig().get(UserAttributeMapper.ATTRIBUTE_FRIENDLY_NAME);
        if (attributeFriendlyName != null && attributeFriendlyName.length() > 0)
            requestedAttribute.setFriendlyName(attributeFriendlyName);

        return requestedAttribute;
    }

    private void addRequestedAttributeToServices(EntityDescriptorType entityDescriptor, RequestedAttributeType requestedAttribute) {
        for (EntityDescriptorType.EDTChoiceType choiceType: entityDescriptor.getChoiceType()) {
            List<EntityDescriptorType.EDTDescriptorChoiceType> descriptors = choiceType.getDescriptors();

            if (descriptors != null) {
                for (EntityDescriptorType.EDTDescriptorChoiceType descriptor: descriptors) {
                    if (descriptor.getSpDescriptor() != null && descriptor.getSpDescriptor().getAttributeConsumingService() != null) {
                        for (AttributeConsumingServiceType attributeConsumingService: descriptor.getSpDescriptor().getAttributeConsumingService()) {
                            attributeConsumingService.addRequestedAttribute(requestedAttribute);
                        }
                    }
                }
            }
        }
    }
}
