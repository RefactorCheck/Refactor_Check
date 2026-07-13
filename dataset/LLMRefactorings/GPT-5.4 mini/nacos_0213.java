public class nacos_0213 {

        public QueryCondition buildQueryConditionRefactored(String namespaceId, String resourceType,
            String nameLike,
            String bizTagsLike, String action) {
            String identity = VisibilityHelper.resolveCurrentIdentity();
            String apiType = VisibilityHelper.resolveCurrentApiType();
            QueryCondition queryCondition = new QueryCondition();
            queryCondition.setNamespaceId(namespaceId);
            queryCondition.setType(resourceType);
            queryCondition.setNameLike(nameLike);
            queryCondition.setBizTagsLike(bizTagsLike);
            VisibilityQueryContext context = new VisibilityQueryContext();
            context.setNamespaceId(namespaceId);
            context.setResourceType(resourceType);
            QueryAdvisor advisor = VisibilityHelper.findVisibilityService()
                .map(service -> service.adviseQuery(identity, action, apiType, context))
                .orElseGet(() -> {
                    QueryAdvisor queryAdvisor = new QueryAdvisor();
                    queryAdvisor.setBasePredicate(BaseVisibilityPredicate.ALL);
                    return queryAdvisor;
                });
            return visibilityAdvisorConverter.convert(queryCondition, identity, advisor, context);
        }
}
