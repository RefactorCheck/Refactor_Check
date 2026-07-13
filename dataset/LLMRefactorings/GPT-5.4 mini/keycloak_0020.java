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

                boolean readableDuringRegistration = isReadableOrWritableDuringRegistration(name);
                if (readableDuringRegistration) {
                    continue;
                }

                AttributeContext attributeContext = createAttributeContext(metadata);
                boolean canView = metadata.canView(attributeContext);
                boolean selected = metadata.isSelected(attributeContext);

                if (!canView || !selected) {
                    attributes.remove(name);
                }
            }

            return attributes;
        }
}
