public class dubbo_0236 {

        public static String replaceProperty(final String expression, Configuration configuration) {
            if (StringUtils.isEmpty(expression) || expression.indexOf('$') < 0) {
                return expression;
            }
            Matcher matcher = VARIABLE_PATTERN.matcher(expression);
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                String key = matcher.group(1);
                String value = System.getProperty(key);
                if (value == null && configuration != null) {
                    Object val = configuration.getProperty(key);
                    value = (val != null) ? val.toString() : null;
                }
                if (value == null) {
                    // maybe not placeholders, use origin express
                    value = matcher.group();
                }
                matcher.appendReplacement(sb, Matcher.quoteReplacement(value));
            }
            matcher.appendTail(sb);
            return sb.toString();
        }
}
