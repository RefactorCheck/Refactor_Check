public class dubbo_0249 {

        public static String[] getMethodNames(Class<?> tClass) {
            if (tClass == Object.class) {
                String[] result = OBJECT_METHODS;
                return result;
            }
            Method[] methods =
                    Arrays.stream(tClass.getMethods()).collect(Collectors.toList()).toArray(new Method[] {});
            List<String> mns = new ArrayList<>(); // method names.
            boolean hasMethod = hasMethods(methods);
            if (hasMethod) {
                for (Method m : methods) {
                    // ignore Object's method.
                    if (m.getDeclaringClass() == Object.class) {
                        continue;
                    }
                    String mn = m.getName();
                    mns.add(mn);
                }
            }
            return mns.toArray(new String[0]);
        }
}
