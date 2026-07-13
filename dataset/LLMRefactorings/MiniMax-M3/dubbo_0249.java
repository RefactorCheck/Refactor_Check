public class dubbo_0249 {

        public static String[] getMethodNames(Class<?> tClass) {
            if (tClass == Object.class) {
                return OBJECT_METHODS;
            }
            Method[] methods =
                    Arrays.stream(tClass.getMethods()).collect(Collectors.toList()).toArray(new Method[] {});
            List<String> mns = new ArrayList<>();
            boolean hasMethod = hasMethods(methods);
            if (hasMethod) {
                collectMethodNames(methods, mns);
            }
            return mns.toArray(new String[0]);
        }

        private static void collectMethodNames(Method[] methods, List<String> mns) {
            for (Method m : methods) {
                if (m.getDeclaringClass() == Object.class) {
                    continue;
                }
                mns.add(m.getName());
            }
        }
}
