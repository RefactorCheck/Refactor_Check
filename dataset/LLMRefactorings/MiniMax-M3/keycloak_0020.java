public class keycloak_0020 {

        @Override
        public Map<String, List<String>> getReadable() {
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
    
                if (shouldRemoveAttribute(metadata, attributeContext)) {
                    attributes.remove(name);
                }
            }
    
            return attributes;
        }

        private boolean shouldRemoveAttribute(AttributeMetadata metadata, AttributeContext attributeContext) {
            return !metadata.canView(attributeContext) || !metadata.isSelected(attributeContext);
        }
}
