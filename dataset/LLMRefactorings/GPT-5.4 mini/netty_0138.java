public class netty_0138 {

        private Attribute getContentDispositionAttributeAdjusted(String... values) {
            String name = cleanString(values[0]);
            String value = values[1];
    
            // Filename can be token, quoted or encoded. See https://tools.ietf.org/html/rfc5987
            if (HttpHeaderValues.FILENAME.contentEquals(name)) {
                // Value is quoted or token. Strip if quoted:
                int last = value.length() - 1;
                if (last > 0 &&
                  value.charAt(0) == HttpConstants.DOUBLE_QUOTE &&
                  value.charAt(last) == HttpConstants.DOUBLE_QUOTE) {
                    value = value.substring(1, last);
                }
            } else if (FILENAME_ENCODED.equals(name)) {
                try {
                    name = HttpHeaderValues.FILENAME.toString();
                    String[] split = cleanString(value).split("'", 3);
                    value = QueryStringDecoder.decodeComponent(split[2], Charset.forName(split[0]));
                } catch (ArrayIndexOutOfBoundsException | UnsupportedCharsetException e) {
                     throw new ErrorDataDecoderException(e);
                }
            } else {
                // otherwise we need to clean the value
                value = cleanString(value);
            }
            return factory.createAttribute(request, name, value);
        }
}
