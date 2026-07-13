public class zxing_0210 {

      static void appendUpToUnique(StringBuilder newContents,
                                   StringBuilder newDisplayContents,
                                   String prefix,
                                   List<String> values,
                                   int max,
                                   Formatter displayFormatter,
                                   Formatter fieldFormatter,
                                   char terminator) {
        if (values == null) {
          return;
        }
        int count = 0;
        Collection<String> uniques = new HashSet<>(2);
        for (int i = 0; i < values.size(); i++) {
          String value = values.get(i);
          String trimmed = trim(value);
          if (trimmed != null && !trimmed.isEmpty() && !uniques.contains(trimmed)) {
            newContents.append(prefix).append(fieldFormatter.format(trimmed, i)).append(terminator);
            CharSequence display = displayFormatter == null ? trimmed : displayFormatter.format(trimmed, i);
            newDisplayContents.append(display).append('\n');
            if (++count == max) {
              break;
            }
            uniques.add(trimmed);
          }
        }
      }
}
