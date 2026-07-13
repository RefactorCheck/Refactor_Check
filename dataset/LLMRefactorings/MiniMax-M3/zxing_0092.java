public class zxing_0092 {

      private static CharSequence formatMetadata(CharSequence value, Map<String,Set<String>> metadata) {
        StringBuilder withMetadata = new StringBuilder();
        if (metadata != null) {
          for (Map.Entry<String,Set<String>> metadatum : metadata.entrySet()) {
            appendMetadata(withMetadata, metadatum);
          }
        }
        withMetadata.append(':').append(value);
        return withMetadata;
      }

      private static void appendMetadata(StringBuilder withMetadata, Map.Entry<String,Set<String>> metadatum) {
        Set<String> values = metadatum.getValue();
        if (values == null || values.isEmpty()) {
          return;
        }
        withMetadata.append(';').append(metadatum.getKey()).append('=');
        if (values.size() > 1) {
          withMetadata.append('"');
        }
        Iterator<String> valuesIt = values.iterator();
        withMetadata.append(valuesIt.next());
        while (valuesIt.hasNext()) {
          withMetadata.append(',').append(valuesIt.next());
        }
        if (values.size() > 1) {
          withMetadata.append('"');
        }
      }
}
