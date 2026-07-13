public class zxing_0048 {

      private static String[] toTypes(Collection<List<String>> lists) {
        if (lists == null || lists.isEmpty()) {
          return null;
        }
        List<String> result = new ArrayList<>(lists.size());
        for (List<String> list : lists) {
          String value = list.get(0);
          if (value != null && !value.isEmpty()) {
            String type = findType(list);
            result.add(type);
          }
        }
        return result.toArray(EMPTY_STR_ARRAY);
      }

      private static String findType(List<String> list) {
        for (int i = 1; i < list.size(); i++) {
          String metadatum = list.get(i);
          int equals = metadatum.indexOf('=');
          if (equals < 0) {
            return metadatum;
          }
          if ("TYPE".equalsIgnoreCase(metadatum.substring(0, equals))) {
            return metadatum.substring(equals + 1);
          }
        }
        return null;
      }
}
