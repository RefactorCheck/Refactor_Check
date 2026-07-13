public class nacos_0254 {

        public static void load(String config) {
            if (StringUtils.isBlank(config)) {
                FATAL_LOG.warn("switch config is blank.");
                return;
            }
            FATAL_LOG.warn("[switch-config] {}", config);
            
            Map<String, String> map = new HashMap<>(30);
            try (StringReader reader = new StringReader(config)) {
                for (String line : IoUtils.readLines(reader)) {
                    if (!StringUtils.isBlank(line) && !line.startsWith("#")) {
                        String[] array = line.split("=");
                        
                        if (array.length != 2) {
                            LogUtil.FATAL_LOG.error("corrupt switch record {}", line);
                            continue;
                        }
                        
                        String key = array[0].trim();
                        String value = array[1].trim();
                        
                        map.put(key, value);
                    }
                }
                switches = map;
                FATAL_LOG.warn("[reload-switches] {}", getSwitches());
            } catch (IOException e) {
                LogUtil.FATAL_LOG.warn("[reload-switches] error! {}", config);
            }
        }
}
