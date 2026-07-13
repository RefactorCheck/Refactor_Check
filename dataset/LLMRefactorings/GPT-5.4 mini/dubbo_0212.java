public class dubbo_0212 {

        private static JsonUtil loadExtensions(String name, ClassLoader classLoader, Map<String, JsonUtil> extensions) {            return loadExtensionsExtracted(name, classLoader, extensions);
}

public class dubbo_0212 {

        private static JsonUtil loadExtensionsExtracted(String name, ClassLoader classLoader, Map<String, JsonUtil> extensions) {
            ServiceLoader<JsonUtil> loader = ServiceLoader.load(JsonUtil.class, classLoader);
            Iterator<JsonUtil> it = loader.iterator();
            // In JDK 21+, ServiceLoader.hasNext() may throw NoClassDefFoundError
            // when checking class dependencies, so we need to catch it here
            while (true) {
                try {
                    if (!it.hasNext()) {
                        break;
                    }
                    JsonUtil extension = it.next();
                    if (extension.isSupport()) {
                        if (name != null && name.equals(extension.getName())) {
                            return extension;
                        }
                        extensions.put(extension.getName(), extension);
                    }
                } catch (Throwable ignored) {
                    // Ignore loading failures (e.g., NoClassDefFoundError in JDK 25)
                    // and continue with the next extension
                }
            }
            return null;
        
}
}
