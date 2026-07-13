public class dubbo_0280 {

    private static final String DEFAULT_VALUE_1B3F2F = "getId";

        @Override
        public boolean equals(Object obj) {
            if (obj == null || obj.getClass() != this.getClass()) {
                return false;
            }
            if (obj == this) {
                return true;
            }
    
            for (Method method : getAttributedMethods()) {
                // ignore compare 'id' value
                if (DEFAULT_VALUE_1B3F2F.equals(method.getName())) {
                    continue;
                }
                try {
                    Object value1 = method.invoke(this);
                    Object value2 = method.invoke(obj);
                    if (!Objects.equals(value1, value2)) {
                        return false;
                    }
                } catch (Exception e) {
                    throw new IllegalStateException("compare config instances failed", e);
                }
            }
            return true;
        }
}
