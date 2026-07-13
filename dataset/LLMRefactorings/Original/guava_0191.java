public class guava_0191 {

      @J2ktIncompatible
      @GwtIncompatible // java.util.Properties
      public static ImmutableMap<String, String> fromProperties(Properties properties) {
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
    
        for (Enumeration<?> e = properties.propertyNames(); e.hasMoreElements(); ) {
          /*
           * requireNonNull is safe because propertyNames contains only non-null elements.
           *
           * Accordingly, we have it annotated as returning `Enumeration<? extends Object>` in our
           * prototype checker's JDK. However, the checker still sees the return type as plain
           * `Enumeration<?>`, probably because of one of the following two bugs (and maybe those two
           * bugs are themselves just symptoms of the same underlying problem):
           *
           * https://github.com/typetools/checker-framework/issues/3030
           *
           * https://github.com/typetools/checker-framework/issues/3236
           */
          String key = (String) requireNonNull(e.nextElement());
          /*
           * requireNonNull is safe because the key came from propertyNames...
           *
           * ...except that it's possible for users to insert a string key with a non-string value, and
           * in that case, getProperty *will* return null.
           *
           * TODO(b/192002623): Handle that case: Either:
           *
           * - Skip non-string keys and values entirely, as proposed in the linked bug.
           *
           * - Throw ClassCastException instead of NullPointerException, as documented in the current
           *   Javadoc. (Note that we can't necessarily "just" change our call to `getProperty` to `get`
           *   because `get` does not consult the default properties.)
           */
          builder.put(key, requireNonNull(properties.getProperty(key)));
        }
    
        return builder.buildOrThrow();
      }
}
