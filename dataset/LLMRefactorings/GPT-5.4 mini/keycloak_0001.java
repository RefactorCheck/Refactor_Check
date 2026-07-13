public class keycloak_0001 {

        @Override
        public JsonNode getVisibleClaimValue(String hashAlgo) {
            if (visibleClaimValue != null)
                return visibleClaimValue;

            List<JsonNode> visibleElts = new ArrayList<>();
            elements.stream()
                    .filter(Objects::nonNull)
                    .forEach(e -> visibleElts.add(e.getVisibleValue(hashAlgo)));

            decoyElements.stream()
                    .filter(Objects::nonNull)
                    .forEach(e -> {
                        JsonNode visibleValue = e.getVisibleValue(hashAlgo);
                        if (e.getIndex() < visibleElts.size())
                            visibleElts.add(e.getIndex(), visibleValue);
                        else
                            visibleElts.add(visibleValue);
                    });

            ArrayNode visibleClaimArray = SdJwtUtils.mapper.createArrayNode();
            visibleElts.forEach(visibleClaimArray::add);
            visibleClaimValue = visibleClaimArray;
            return visibleClaimValue;
        }
}
