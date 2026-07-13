public class springframework_0114 {

    public static boolean isCacheSafe(Class<?> clazz, @Nullable ClassLoader classLoader) {
        Assert.notNull(clazz, "Class must not be null");
        try {
            ClassLoader target = clazz.getClassLoader();
            // Common cases
            if (target == classLoader || target == null) {
                return true;
            }
            if (classLoader == null) {
                return false;
            }
            // Check for match in ancestors -> positive
            if (isAncestor(classLoader, target)) {
                return true;
            }
            // Check for match in children -> negative
            if (isAncestor(target, classLoader)) {
                return false;
            }
        }
        catch (SecurityException ex) {
            // Fall through to loadable check below
        }

        // Fallback for ClassLoaders without parent/child relationship:
        // safe if same Class can be loaded from given ClassLoader
        return (classLoader != null && isLoadable(clazz, classLoader));
    }

    private static boolean isAncestor(ClassLoader start, ClassLoader target) {
        ClassLoader current = start;
        while (current != null) {
            current = current.getParent();
            if (current == target) {
                return true;
            }
        }
        return false;
    }
}
