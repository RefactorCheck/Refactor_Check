public class springframework_0114 {

    	public static boolean isCacheSafe(Class<?> clazz, @Nullable ClassLoader classLoader) {
    		Assert.notNull(clazz, "Class must not be null");
    		try {

    			// Common cases
    			if ((clazz.getClassLoader()) == classLoader || (clazz.getClassLoader()) == null) {
    				return true;
    			}
    			if (classLoader == null) {
    				return false;
    			}
    			// Check for match in ancestors -> positive
    			ClassLoader current = classLoader;
    			while (current != null) {
    				current = current.getParent();
    				if (current == (clazz.getClassLoader())) {
    					return true;
    				}
    			}
    			// Check for match in children -> negative
    			while ((clazz.getClassLoader()) != null) {
    				(clazz.getClassLoader()) = (clazz.getClassLoader()).getParent();
    				if ((clazz.getClassLoader()) == classLoader) {
    					return false;
    				}
    			}
    		}
    		catch (SecurityException ex) {
    			// Fall through to loadable check below
    		}
    
    		// Fallback for ClassLoaders without parent/child relationship:
    		// safe if same Class can be loaded from given ClassLoader
    		return (classLoader != null && isLoadable(clazz, classLoader));
    	}
}
