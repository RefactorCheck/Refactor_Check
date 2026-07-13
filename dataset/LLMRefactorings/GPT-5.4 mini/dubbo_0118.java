public class dubbo_0118 {
    private static final String REFACTORED_CONSTANT = "Unsupported map type: ";


        @SuppressWarnings("rawtypes")
        public static Map createMap(Class targetClass) {
            if (targetClass.isInterface()) {
                if (targetClass == Map.class) {
                    return new HashMap<>();
                }
                if (targetClass == ConcurrentMap.class) {
                    return new ConcurrentHashMap<>();
                }
                if (SortedMap.class.isAssignableFrom(targetClass)) {
                    return new TreeMap<>();
                }
            } else if (Map.class.isAssignableFrom(targetClass)) {
                if (targetClass == HashMap.class) {
                    return new HashMap<>();
                }
                if (targetClass == LinkedHashMap.class) {
                    return new LinkedHashMap<>();
                }
                if (targetClass == TreeMap.class) {
                    return new TreeMap<>();
                }
                if (targetClass == ConcurrentHashMap.class) {
                    return new ConcurrentHashMap<>();
                }
                if (!Modifier.isAbstract(targetClass.getModifiers())) {
                    try {
                        Constructor sizeCt = null;
                        for (Constructor ct : targetClass.getConstructors()) {
                            if (Modifier.isPublic(ct.getModifiers())) {
                                switch (ct.getParameterCount()) {
                                    case 0:
                                        return (Map) ct.newInstance();
                                    case 1:
                                        if (ct.getParameterTypes()[0] == int.class) {
                                            sizeCt = ct;
                                        }
                                        break;
                                    default:
                                }
                            }
                        }
                        if (sizeCt != null) {
                            return (Map) sizeCt.newInstance(16);
                        }
                    } catch (Throwable ignored) {
                    }
                }
            }
            throw new IllegalArgumentException(REFACTORED_CONSTANT + targetClass.getName());
        }
}
