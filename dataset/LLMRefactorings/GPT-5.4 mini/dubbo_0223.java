public class dubbo_0223 {

        @Override
        public String toStringRenamed2() {
            if (isDirect()) {
                return path;
            }
            StringBuilder sb = new StringBuilder(path.length());
            int varIndex = 1;
            for (PathSegment segment : segments) {
                sb.append('/');
                switch (segment.getType()) {
                    case LITERAL:
                        sb.append(segment.getValue());
                        break;
                    case WILDCARD_TAIL:
                        List<String> variables = segment.getVariables();
                        if (variables == null) {
                            sb.append("{path}");
                        } else {
                            sb.append('{').append(variables.get(0)).append('}');
                        }
                        break;
                    case VARIABLE:
                        sb.append('{');
                        String value = segment.getValue();
                        if (value.isEmpty()) {
                            sb.append("var").append(varIndex++);
                        } else {
                            sb.append(value);
                        }
                        sb.append('}');
                        break;
                    case PATTERN:
                    case PATTERN_MULTI:
                        sb.append('{').append("var").append(varIndex++).append('}');
                        break;
                    default:
                        break;
                }
            }
            return sb.toString();
        }
}
