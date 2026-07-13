public class dubbo_0273 {

        @SuppressWarnings("unchecked")
        public static AffinityRouterRule parseFromMap(Map<String, Object> map) {
            AffinityRouterRule affinityRouterRule = new AffinityRouterRule();
            affinityRouterRule.parseFromMap0(map);
            Object conditions = map.get(AFFINITY_KEY);
    
            Map<String, String> conditionMap = (Map<String, String>) conditions;
            affinityRouterRule.setAffinityKey(conditionMap.get("key"));
            Object ratio = conditionMap.getOrDefault("ratio", String.valueOf(DefaultAffinityRatio));
            affinityRouterRule.setRatio(Double.valueOf(String.valueOf(ratio)));
    
            if (affinityRouterRule.getRatio() > 100 || affinityRouterRule.getRatio() < 0) {
                logger.error(
                        CLUSTER_FAILED_RULE_PARSING,
                        "Invalid affinity router config.",
                        "",
                        "The ratio value must range from 0 to 100");
                affinityRouterRule.setValid(false);
            }
            return affinityRouterRule;
        }
}
