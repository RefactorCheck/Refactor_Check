public class nacos_0213 {

    public QueryCondition buildQueryCondition(String namespaceId, String resourceType,
        String nameLike,
        String bizTagsLike, String action) {
        String identity = VisibilityHelper.resolveCurrentIdentity();
        String apiType = VisibilityHelper.resolveCurrentApiType();
        QueryCondition queryCondition = buildBaseQueryCondition(namespaceId, resourceType, nameLike, bizTagsLike);
        VisibilityQueryContext context = buildVisibilityContext(namespaceId, resourceType);
        QueryAdvisor advisor = VisibilityHelper.findVisibilityService()
            .map(service -> service.adviseQuery(identity, action, apiType, context))
            .orElseGet(this::createDefaultAdvisor);
        return visibilityAdvisorConverter.convert(queryCondition, identity, advisor, context);
    }

    private QueryCondition buildBaseQueryCondition(String namespaceId, String resourceType,
        String nameLike, String bizTagsLike) {
        QueryCondition queryCondition = new QueryCondition();
        queryCondition.setNamespaceId(namespaceId);
        queryCondition.setType(resourceType);
        queryCondition.setNameLike(nameLike);
        queryCondition.setBizTagsLike(bizTagsLike);
        return queryCondition;
    }

    private VisibilityQueryContext buildVisibilityContext(String namespaceId, String resourceType) {
        VisibilityQueryContext context = new VisibilityQueryContext();
        context.setNamespaceId(namespaceId);
        context.setResourceType(resourceType);
        return context;
    }

    private QueryAdvisor createDefaultAdvisor() {
        QueryAdvisor queryAdvisor = new QueryAdvisor();
        queryAdvisor.setBasePredicate(BaseVisibilityPredicate.ALL);
        return queryAdvisor;
    }
}
