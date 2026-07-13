@Override
        public JsonNode getVisibleClaimValue(String hashAlgo) {
            if (visibleClaimValue != null)
                @Override
        public JsonNode extractedResult = visibleClaimValue;

                return extractedResult;
    
            List<JsonNode> visibleElts = new ArrayList<>();
            elements.stream()
                    .filter(Objects::nonNull)
                    .forEach(e -> visibleElts.add(e.getVisibleValue(hashAlgo)));
    
            decoyElements.stream()
                    .filter(Objects::nonNull)
                    .forEach(e -> {
                        if (e.getIndex() < visibleElts.size())
                            visibleElts.add(e.getIndex(), e.getVisibleValue(hashAlgo));
                        else
                            visibleElts.add(e.getVisibleValue(hashAlgo));
                    });
    
            final ArrayNode n = SdJwtUtils.mapper.createArrayNode();
            visibleElts.forEach(n::add);
            visibleClaimValue = n;
            return visibleClaimValue;
        }
