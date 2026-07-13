public class nacos_0290 {


        private void initAbilityTableRefactored() {
            LOGGER.info("Ready to get current node abilities...");
            // get processors
            Map<AbilityMode, Map<AbilityKey, Boolean>> abilities = initCurrentNodeAbilities();
            // get abilities
            for (AbilityMode mode : AbilityMode.values()) {
                Map<AbilityKey, Boolean> abilitiesTable = abilities.get(mode);
                if (abilitiesTable == null) {
                    continue;
                }
                // check whether exist error key
                // check for developer
                for (AbilityKey abilityKey : abilitiesTable.keySet()) {
                    if (!mode.equals(abilityKey.getMode())) {
                        LOGGER.error(
                            "You should not contain a other mode: {} in a specify mode: {} abilities set, error key: {}, please check again.",
                            abilityKey.getMode(), mode, abilityKey);
                        throw new IllegalStateException(
                            "Except mode: " + mode + " but " + abilityKey + " mode: "
                                + abilityKey.getMode()
                                + ", please check again.");
                    }
                }
                Collection<AbilityPostProcessor> processors =
                    NacosServiceLoader.load(AbilityPostProcessor.class);
                for (AbilityPostProcessor processor : processors) {
                    processor.process(mode, abilitiesTable);
                }
            }
            // init
            Set<AbilityMode> abilityModes = abilities.keySet();
            LOGGER.info("Ready to initialize current node abilities, support modes: {}", abilityModes);
            for (AbilityMode abilityMode : abilityModes) {
                this.currentNodeAbilities
                    .put(abilityMode,
                        new ConcurrentHashMap<>(AbilityKey.mapStr(abilities.get(abilityMode))));
            }
            LOGGER.info("Initialize current abilities finish...");
        
        }
}
