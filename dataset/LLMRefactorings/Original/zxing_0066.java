public class zxing_0066 {

      private static CharSequence formatMetadata(CharSequence value, Map<String,Set<String>> metadata) {
        if (metadata == null || metadata.isEmpty()) {
          return value;
        }
        StringBuilder withMetadata = new StringBuilder();
        for (Map.Entry<String,Set<String>> metadatum : metadata.entrySet()) {
          Set<String> values = metadatum.getValue();
          if (values == null || values.isEmpty()) {
            continue;
          }
          Iterator<String> valuesIt = values.iterator();
          withMetadata.append(valuesIt.next());
          while (valuesIt.hasNext()) {
            withMetadata.append(',').append(valuesIt.next());
          }
        }
        if (withMetadata.length() > 0) {
          withMetadata.append(' ');
        }
        withMetadata.append(value);
        return withMetadata;
      }
}
