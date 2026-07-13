public class netty_0138 {

        private Attribute getContentDispositionAttribute(String... values) {
            String name = cleanString(values[0]);
            String value = values[1];
    
            if (HttpHeaderValues.FILENAME.contentEquals(name)) {
                value = stripQuotes(value);
            } else if (FILENAME_ENCODED.equals(name)) {
                try {
                    name = HttpHeaderValues.FILENAME.toString();
                    String[] split = cleanString(value).split("'", 3);
                    value = QueryStringDecoder.decodeComponent(split[2], Charset.forName(split[0]));
                } catch (ArrayIndexOutOfBoundsException | UnsupportedCharsetException e) {
                     throw new ErrorDataDecoderException(e);
                }
            } else {
                value = cleanString(value);
            }
            return factory.createAttribute(request, name, value);
        }

        private static String stripQuotes(String value) {
            int last = value.length() - 1;
            if (last > 0 &&
              value.charAt(0) == HttpConstants.DOUBLE_QUOTE &&
              value.charAt(last) == HttpConstants.DOUBLE_QUOTE) {
                return value.substring(1, last);
            }
            return value;
        }
}
