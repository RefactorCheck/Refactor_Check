public class guava_0191 {

      @J2ktIncompatible
      @GwtIncompatible // java.util.Properties
      public static ImmutableMap<String, String> fromProperties(Properties properties) {
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
    
        for (Enumeration<?> e = properties.propertyNames(); e.hasMoreElements(); ) {
          addPropertyToBuilder(builder, e, properties);
        }
    
        return builder.buildOrThrow();
      }

      private static void addPropertyToBuilder(ImmutableMap.Builder<String, String> builder, Enumeration<?> e, Properties properties) {
        String key = (String) requireNonNull(e.nextElement());
        builder.put(key, requireNonNull(properties.getProperty(key)));
      }
}
