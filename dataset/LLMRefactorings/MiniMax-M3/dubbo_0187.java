import java.lang.reflect.Method;

public class dubbo_0187 {

    @Override
    public int compare(Method m1, Method m2) {
        if (m1.equals(m2)) {
            return 0;
        }

        int value = compareNames(m1, m2);
        if (value == 0) {
            value = compareParameterCounts(m1, m2);
            if (value == 0) {
                value = compareParameterTypes(m1, m2);
            }
        }

        return Integer.compare(value, 0);
    }

    private int compareNames(Method m1, Method m2) {
        return m1.getName().compareTo(m2.getName());
    }

    private int compareParameterCounts(Method m1, Method m2) {
        return m1.getParameterTypes().length - m2.getParameterTypes().length;
    }

    private int compareParameterTypes(Method m1, Method m2) {
        Class[] types1 = m1.getParameterTypes();
        Class[] types2 = m2.getParameterTypes();
        int value = 0;
        for (int i = 0; i < types1.length; i++) {
            value = types1[i].getName().compareTo(types2[i].getName());
            if (value != 0) {
                break;
            }
        }
        return value;
    }
}
