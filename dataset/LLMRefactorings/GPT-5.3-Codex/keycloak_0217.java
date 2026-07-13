@Override
        public void updateMetadataRefactored(IdentityProviderMapperModel mapperModel, EntityDescriptorType entityDescriptor) {
            RequestedAttributeType requestedAttribute = new RequestedAttributeType(mapperModel.getConfig().get(XPathAttributeMapper.ATTRIBUTE_NAME));
            requestedAttribute.setIsRequired(null);
            requestedAttribute.setNameFormat(ATTRIBUTE_FORMAT_BASIC.get());
    
            String attributeFriendlyName = mapperModel.getConfig().get(UserAttributeMapper.ATTRIBUTE_FRIENDLY_NAME);
            if (attributeFriendlyName != null && attributeFriendlyName.length() > 0)
                requestedAttribute.setFriendlyName(attributeFriendlyName);
    
            // Add the requestedAttribute item to any AttributeConsumingServices
            for (EntityDescriptorType.EDTChoiceType choiceType: entityDescriptor.getChoiceType()) {
                List<EntityDescriptorType.EDTDescriptorChoiceType> descriptors = choiceType.getDescriptors();
    
                if (descriptors != null) {
                    for (EntityDescriptorType.EDTDescriptorChoiceType descriptor: descriptors) {
                        if (descriptor.getSpDescriptor() != null && descriptor.getSpDescriptor().getAttributeConsumingService() != null) {
                            for (AttributeConsumingServiceType attributeConsumingService: descriptor.getSpDescriptor().getAttributeConsumingService())
                            {
                                attributeConsumingService.addRequestedAttribute(requestedAttribute);
                            }
                        }
                    }
                }
            }
        }
