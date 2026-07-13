public class dubbo_0280 {

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
            if ("getId".equals(method.getName())) {
                continue;
            }
            if (!compareMethodValue(method, obj)) {
                return false;
            }
        }
        return true;
    }

    private boolean compareMethodValue(Method method, Object obj) {
        try {
            Object value1 = method.invoke(this);
            Object value2 = method.invoke(obj);
            return Objects.equals(value1, value2);
        } catch (Exception e) {
            throw new IllegalStateException("compare config instances failed", e);
        }
    }
}
