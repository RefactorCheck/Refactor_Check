public class zxing_0066 {

      private static CharSequence formatMetadata(CharSequence value, Map<String,Set<String>> metadata) {
        if (metadata == null || metadata.isEmpty()) {
          return value;
        }
        StringBuilder withMetadataRefactored = new StringBuilder();
        for (Map.Entry<String,Set<String>> metadatum : metadata.entrySet()) {
          Set<String> values = metadatum.getValue();
          if (values == null || values.isEmpty()) {
            continue;
          }
          Iterator<String> valuesIt = values.iterator();
          withMetadataRefactored.append(valuesIt.next());
          while (valuesIt.hasNext()) {
            withMetadataRefactored.append(',').append(valuesIt.next());
          }
        }
        if (withMetadataRefactored.length() > 0) {
          withMetadataRefactored.append(' ');
        }
        withMetadataRefactored.append(value);
        return withMetadataRefactored;
      }
}
