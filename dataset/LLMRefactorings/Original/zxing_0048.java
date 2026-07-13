public class zxing_0048 {

      private static String[] toTypes(Collection<List<String>> lists) {
        if (lists == null || lists.isEmpty()) {
          return null;
        }
        List<String> result = new ArrayList<>(lists.size());
        for (List<String> list : lists) {
          String value = list.get(0);
          if (value != null && !value.isEmpty()) {
            String type = null;
            for (int i = 1; i < list.size(); i++) {
              String metadatum = list.get(i);
              int equals = metadatum.indexOf('=');
              if (equals < 0) {
                // take the whole thing as a usable label
                type = metadatum;
                break;
              }
              if ("TYPE".equalsIgnoreCase(metadatum.substring(0, equals))) {
                type = metadatum.substring(equals + 1);
                break;
              }
            }
            result.add(type);
          }
        }
        return result.toArray(EMPTY_STR_ARRAY);
      }
}
