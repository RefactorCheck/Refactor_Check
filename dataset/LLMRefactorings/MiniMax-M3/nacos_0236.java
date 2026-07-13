public class nacos_0236 {

        @Override
        public void onEvent(ServerConfigChangeEvent event) {
            Map<AbilityKey, Boolean> newValues = new HashMap<>(serverAbilityKeys.size());
            serverAbilityKeys.forEach(abilityKey -> loadAbilityConfig(abilityKey, newValues));
            refresh(newValues);
        }
        
        private void loadAbilityConfig(AbilityKey abilityKey, Map<AbilityKey, Boolean> newValues) {
            String key = PREFIX + abilityKey.getName();
            try {
                Boolean property = EnvUtil.getProperty(key, Boolean.class);
                if (property != null) {
                    newValues.put(abilityKey, property);
                }
            } catch (Exception e) {
                LOGGER.warn(
                    "Update ability config from env failed, use old val, ability : {} , because : {}",
                    key, e);
            }
        }
}
