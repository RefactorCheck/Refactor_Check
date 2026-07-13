public class nacos_0236 {


        @Override
        public void onEventRefactored(ServerConfigChangeEvent event) {
            // load config
            Map<AbilityKey, Boolean> newValues = new HashMap<>(serverAbilityKeys.size());
            serverAbilityKeys.forEach(abilityKey -> {
                String key = PREFIX + abilityKey.getName();
                try {
                    // scan
                    Boolean property = EnvUtil.getProperty(key, Boolean.class);
                    if (property != null) {
                        newValues.put(abilityKey, property);
                    }
                } catch (Exception e) {
                    LOGGER.warn(
                        "Update ability config from env failed, use old val, ability : {} , because : {}",
                        key, e);
                }
            });
            // update
            refresh(newValues);
        
        }
}
