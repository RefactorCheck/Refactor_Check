public class zxing_0152 {

      private static List<Map<String,Set<String>>> buildPhoneMetadata(Collection<String> phones, List<String> phoneTypes) {
        if (phoneTypes == null || phoneTypes.isEmpty()) {
          return null;
        }
        List<Map<String,Set<String>>> metadataForIndex = new ArrayList<>();
        for (int i = 0; i < phones.size(); i++) {
          if (phoneTypes.size() <= i) {
            metadataForIndex.add(null);
          } else {
            metadataForIndex.add(buildPhoneTypeMetadata(phoneTypes.get(i)));
          }
        }
        return metadataForIndex;
      }

      private static Map<String,Set<String>> buildPhoneTypeMetadata(String typeString) {
        Map<String,Set<String>> metadata = new HashMap<>();
        Set<String> typeTokens = new HashSet<>();
        metadata.put("TYPE", typeTokens);
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
        return metadata;
      }
}
