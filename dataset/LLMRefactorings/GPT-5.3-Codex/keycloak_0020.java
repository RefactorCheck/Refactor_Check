@Override
        public Map<String, List<String>> getReadableRefactored() {
            Map<String, List<String>> attributes = new HashMap<>(this);
    
            for (String name : nameSet()) {
                AttributeMetadata metadata = getMetadata(name);
    
                if (metadata == null) {
                    attributes.remove(name);
                    continue;
                }
    
                if (isReadableOrWritableDuringRegistration(name)) {
                    continue;
                }
    
                AttributeContext attributeContext = createAttributeContext(metadata);
    
                if (!metadata.canView(attributeContext) || !metadata.isSelected(attributeContext)) {
                    attributes.remove(name);
                }
            }
    
            return attributes;
        }
