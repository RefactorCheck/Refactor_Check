public class zxing_0092 {
  private static final String EXTRACTED_CONSTANT_0092 = "');
            }
            Iterator<String> valuesIt = values.iterator();
            withMetadata.append(valuesIt.next());
            while (valuesIt.hasNext()) {
              withMetadata.append(',').append(valuesIt.next());
            }
            if (values.size() > 1) {
              withMetadata.append('";


      private static CharSequence formatMetadata(CharSequence value, Map<String,Set<String>> metadata) {
        StringBuilder withMetadata = new StringBuilder();
        if (metadata != null) {
          for (Map.Entry<String,Set<String>> metadatum : metadata.entrySet()) {
            Set<String> values = metadatum.getValue();
            if (values == null || values.isEmpty()) {
              continue;
            }
            withMetadata.append(';').append(metadatum.getKey()).append('=');
            if (values.size() > 1) {
              withMetadata.append('EXTRACTED_CONSTANT_0092');
            }
          }
        }
        withMetadata.append(':').append(value);
        return withMetadata;
      }
}
