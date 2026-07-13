public class kafka_0185 {

    protected Class<?> loadVersionedPluginClass(
        String name,
        VersionRange range,
        boolean resolve
    ) throws VersionedPluginLoadingException, ClassNotFoundException {

        String fullName = aliases.getOrDefault(name, name);
        PluginClassLoader pluginLoader = pluginClassLoader(fullName, range, Optional.empty());
        Class<?> plugin;
        if (pluginLoader != null) {
            log.trace("Retrieving loaded class '{}' from '{}'", name, pluginLoader);
            plugin = pluginLoader.loadClass(fullName, resolve);
        } else {
            plugin = loadFallbackClass(fullName, range, resolve);
        }
        return plugin;
    }

    private Class<?> loadFallbackClass(String fullName, VersionRange range, boolean resolve)
            throws VersionedPluginLoadingException, ClassNotFoundException {
        Class<?> plugin = super.loadClass(fullName, resolve);
        if (range == null) {
            return plugin;
        }
        verifyClasspathVersionedPlugin(fullName, plugin, range);
        return plugin;
    }
}
