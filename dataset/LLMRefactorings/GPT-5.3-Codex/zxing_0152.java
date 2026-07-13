public class zxing_0152 {

      private static List<Map<String,Set<String>>> buildPhoneMetadata(Collection<String> phones, List<String> phoneTypes, boolean normalize) {
        if (phoneTypes == null || phoneTypes.isEmpty()) {
          return null;
        }
        List<Map<String,Set<String>>> metadataForIndex = new ArrayList<>();
        for (int i = 0; i < phones.size(); i++) {
          if (phoneTypes.size() <= i) {
            metadataForIndex.add(null);
          } else {
            Map<String,Set<String>> metadata = new HashMap<>();
            metadataForIndex.add(metadata);
            Set<String> typeTokens = new HashSet<>();
            metadata.put("TYPE", typeTokens);
            String typeString = phoneTypes.get(i);
            Integer androidType = maybeIntValue(typeString);
            if (androidType == null) {
              typeTokens.add(typeString);
            } else {
              String purpose = vCardPurposeLabelForAndroidType(androidType);
              String context = vCardContextLabelForAndroidType(androidType);
              if (purpose != null) {
                typeTokens.add(purpose);
              }
              if (context != null) {
                typeTokens.add(context);
              }
            }
          }
        }
        return metadataForIndex;
      }
}
