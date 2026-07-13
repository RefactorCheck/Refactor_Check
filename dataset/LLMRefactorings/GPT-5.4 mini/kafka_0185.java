public class kafka_0185 {

        protected Class<?> loadVersionedPluginClass(
            String name,
            VersionRange range,
            boolean resolve
        ) throws VersionedPluginLoadingException, ClassNotFoundException {

            String pluginName = aliases.getOrDefault(name, name);
            PluginClassLoader pluginLoader = pluginClassLoader(pluginName, range, Optional.empty());
            Class<?> plugin;
            if (pluginLoader != null) {
                log.trace("Retrieving loaded class '{}' from '{}'", name, pluginLoader);
                plugin = pluginLoader.loadClass(pluginName, resolve);
            } else {
                plugin = super.loadClass(pluginName, resolve);
                if (range == null) {
                    return plugin;
                }
                verifyClasspathVersionedPlugin(pluginName, plugin, range);
            }
            return plugin;
        }
}
