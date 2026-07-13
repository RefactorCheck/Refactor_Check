public class keycloak_0187 {

    @Override
    protected Stream<GroupModel> getModels(SearchRequest searchRequest) {
        RealmModel realm = session.getContext().getRealm();
        Integer firstResult = searchRequest.getStartIndex() != null ? searchRequest.getStartIndex() - 1 : null;
        Integer maxResults = searchRequest.getCount();
        maxResults = maxResults != null ? Math.min(maxResults, DEFAULT_MAX_RESULTS) : DEFAULT_MAX_RESULTS;

        if (StringUtil.isNotBlank(searchRequest.getFilter())) {
            return getGroupModelsByFilter(searchRequest, realm, firstResult, maxResults);
        } else {
            return session.groups().getTopLevelGroupsStream(realm, firstResult, maxResults);
        }
    }

    private Stream<GroupModel> getGroupModelsByFilter(SearchRequest searchRequest, RealmModel realm, Integer firstResult, Integer maxResults) {
        ScimFilterParser.FilterContext filterContext = FilterUtils.parseFilter(searchRequest.getFilter());

        EntityManager em = session.getProvider(JpaConnectionProvider.class).getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<GroupEntity> query = cb.createQuery(GroupEntity.class);
        Root<GroupEntity> root = query.from(GroupEntity.class);
        List<Predicate> predicates = getGroupPredicates(filterContext, cb, query, root);

        query.where(predicates).distinct(true).orderBy(cb.asc(root.get("name")));

        return closing(paginateQuery(em.createQuery(query), firstResult, maxResults).getResultStream()
                .map(entity -> new GroupAdapter(session, realm, em, entity)));
    }
}
