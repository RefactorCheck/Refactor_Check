public class arthas_0176 {

        private void initArthasEnvironment(Map<String, String> argsMap) throws IOException {
            if (arthasEnvironment == null) {
                arthasEnvironment = new ArthasEnvironment();
            }
    
            /**
             * <pre>
             * 脚本里传过来的配置项，即命令行参数 > System Env > System Properties > arthas.properties
             * arthas.properties 提供一个配置项，可以反转优先级。 arthas.config.overrideAll=true
             * https://github.com/alibaba/arthas/issues/986
             * </pre>
             */
            Map<String, Object> copyMap;
            if (argsMap != null) {
                copyMap = new HashMap<String, Object>(argsMap);
                // 添加 arthas.home
                if (!copyMap.containsKey(ARTHAS_HOME_PROPERTY)) {
                    copyMap.put(ARTHAS_HOME_PROPERTY, arthasHome());
                }
            } else {
                copyMap = new HashMap<String, Object>(1);
                copyMap.put(ARTHAS_HOME_PROPERTY, arthasHome());
            }
    
            arthasEnvironment.addFirst((new MapPropertySource("args", copyMap)));
    
            tryToLoadArthasProperties();
    
            configure = new Configure();
            BinderUtils.inject(arthasEnvironment, configure);
        }
}
