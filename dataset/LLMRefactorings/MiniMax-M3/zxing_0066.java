public class zxing_0066 {

      private static CharSequence formatMetadata(CharSequence value, Map<String,Set<String>> metadata) {
        if (metadata == null || metadata.isEmpty()) {
          return value;
        }
        StringBuilder withMetadata = new StringBuilder();
        for (Map.Entry<String,Set<String>> metadatum : metadata.entrySet()) {
          appendJoinedValues(withMetadata, metadatum.getValue());
        }
        if (withMetadata.length() > 0) {
          withMetadata.append(' ');
        }
        withMetadata.append(value);
        return withMetadata;
      }

      private static void appendJoinedValues(StringBuilder sb, Set<String> values) {
        if (values == null || values.isEmpty()) {
          return;
        }
        Iterator<String> valuesIt = values.iterator();
        sb.append(valuesIt.next());
        while (valuesIt.hasNext()) {
          sb.append(',').append(valuesIt.next());
        }
      }
}
