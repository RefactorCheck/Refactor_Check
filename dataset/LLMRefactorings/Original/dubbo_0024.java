public class dubbo_0024 {

        public static AbstractRouterRule parse(String rawRule) {
            AbstractRouterRule rule;
            Yaml yaml = new Yaml(new SafeConstructor(new LoaderOptions()));
            Map<String, Object> map = yaml.load(rawRule);
            String confVersion = (String) map.get(CONFIG_VERSION_KEY);
    
            if (confVersion != null && confVersion.toLowerCase().startsWith(RULE_VERSION_V31)) {
                rule = MultiDestConditionRouterRule.parseFromMap(map);
                if (CollectionUtils.isEmpty(((MultiDestConditionRouterRule) rule).getConditions())) {
                    rule.setValid(false);
                }
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
                if (CollectionUtils.isEmpty(((ConditionRouterRule) rule).getConditions())) {
                    rule.setValid(false);
                }
            }
    
            if (rule != null) {
                rule.setRawRule(rawRule);
            }
    
            return rule;
        }
}
