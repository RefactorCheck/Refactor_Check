public class guava_0259 {

      public static String lenientFormat(
          @Nullable String template, @Nullable Object @Nullable ... args) {
        template = String.valueOf(template); // null -> "null"
    
        if (args == null) {
          args = new Object[] {"(Object[])null"};
        }
    
        // start substituting the arguments into the '%s' placeholders
        StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
        int templateStart = 0;
        int i = 0;
        while (i < args.length) {
          int placeholderStart = template.indexOf("%s", templateStart);
          if (placeholderStart == -1) {
            break;
          }
          builder.append(template, templateStart, placeholderStart);
          builder.append(lenientToString(args[i++]));
          templateStart = placeholderStart + 2;
        }
        builder.append(template, templateStart, template.length());
    
        // if we run out of placeholders, append the extra args in square brackets
        if (i < args.length) {
          String prefix = " [";
          for (; i < args.length; i++) {
            builder.append(prefix);
            builder.append(lenientToString(args[i]));
            prefix = ", ";
          }
          builder.append(']');
        }
    
        return builder.toString();
      }
}
