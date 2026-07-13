public class dubbo_0024 {

        public static AbstractRouterRule parse(String rawRule) {
            AbstractRouterRule rule;
            Yaml yaml = new Yaml(new SafeConstructor(new LoaderOptions()));
            Map<String, Object> map = yaml.load(rawRule);
            String confVersion = (String) map.get(CONFIG_VERSION_KEY);
    
            if (confVersion != null && confVersion.toLowerCase().startsWith(RULE_VERSION_V31)) {
                rule = MultiDestConditionRouterRule.parseFromMap(map);
                markInvalidIfNoConditions(rule, ((MultiDestConditionRouterRule) rule).getConditions());
            } else if (confVersion != null && confVersion.compareToIgnoreCase(RULE_VERSION_V31) > 0) {
                logger.warn(
                        CLUSTER_FAILED_RULE_PARSING,
                        "Invalid condition config version number.",
                        "",
                        "Ignore this configuration. Only " + RULE_VERSION_V31 + " and below are supported in this release");
                rule = null;
            } else {
                // for under v3.1
                rule = ConditionRouterRule.parseFromMap(map);
                markInvalidIfNoConditions(rule, ((ConditionRouterRule) rule).getConditions());
            }
    
            if (rule != null) {
                rule.setRawRule(rawRule);
            }
    
            return rule;
        }

        private static void markInvalidIfNoConditions(AbstractRouterRule rule, Collection<?> conditions) {
            if (CollectionUtils.isEmpty(conditions)) {
                rule.setValid(false);
            }
        }
}
