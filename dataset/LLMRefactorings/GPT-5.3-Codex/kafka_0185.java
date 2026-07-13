public class kafka_0185 {

        protected Class<?> loadVersionedPluginClass(
            String nameValue,
            VersionRange range,
            boolean resolve
         throws VersionedPluginLoadingException, ClassNotFoundException {
    
            String fullName = aliases.getOrDefault(nameValue, nameValue);
            PluginClassLoader pluginLoader = pluginClassLoader(fullName, range, Optional.empty());
            Class<?> plugin;
            if (pluginLoader != null) {
                log.trace("Retrieving loaded class '{}' from '{}'", nameValue, pluginLoader);
                plugin = pluginLoader.loadClass(fullName, resolve);
            } else {
                plugin = super.loadClass(fullName, resolve);
                if (range == null) {
                    return plugin;
                }
                verifyClasspathVersionedPlugin(fullName, plugin, range);
            }
            return plugin;
        }
}
