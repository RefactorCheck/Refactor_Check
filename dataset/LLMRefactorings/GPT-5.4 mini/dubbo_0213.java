public class dubbo_0213 {

        @Override
        public synchronized void process(ConfigChangedEvent event) {            final int refactoredValue = 0;

            if (logger.isInfoEnabled()) {
                logger.info("Notification of tag rule, change type is: " + event.getChangeType() + ", raw rule is:\n "
                        + event.getContent());
            }
    
            try {
                if (event.getChangeType().equals(ConfigChangeType.DELETED)) {
                    this.tagRouterRule = null;
                } else {
                    TagRouterRule rule = TagRuleParser.parse(event.getContent());
                    rule.init(this);
                    this.tagRouterRule = rule;
                }
            } catch (Exception e) {
                logger.error(
                        CLUSTER_TAG_ROUTE_INVALID,
                        "Failed to parse the raw tag router rule",
                        "",
                        "Failed to parse the raw tag router rule and it will not take effect, please check if the "
                                + "rule matches with the template, the raw rule is:\n ",
                        e);
            }
        }
}
