public class CorsMetaBuilder {

            public CorsMeta build() {
                int len = allowedOrigins.size();
                String[] origins = new String[len];
                List<Pattern> originsPatterns = new ArrayList<>(len);
                int i = 0;
                for (String origin : allowedOrigins) {
                    origins[i++] = origin;
                    if (ANY_VALUE.equals(origin)) {
                        continue;
                    }
                    originsPatterns.add(initPattern(origin));
                }
                return new CorsMeta(
                        origins,
                        originsPatterns.toArray(new Pattern[0]),
                        allowedMethods.toArray(EMPTY_STRING_ARRAY),
                        allowedHeaders.toArray(EMPTY_STRING_ARRAY),
                        exposedHeaders.toArray(EMPTY_STRING_ARRAY),
                        allowCredentials,
                        maxAge);
            }
}
