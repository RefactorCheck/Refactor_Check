public class keycloak_0058 {

        @Override
        public Long count(SearchRequest searchRequest) {
            RealmModel realm = session.getContext().getRealm();
            if (StringUtil.isNotBlank(searchRequest.getFilter())) {
                // parse filter into AST
                ScimFilterParser.FilterContext filterContext = FilterUtils.parseFilter(searchRequest.getFilter());
    
                // execute JPA count query with filter
                EntityManager em = session.getProvider(JpaConnectionProvider.class).getEntityManager();
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<Long> query = cb.createQuery(Long.class);
                Root<UserEntity> root = query.from(UserEntity.class);
    
                List<Predicate> predicates = this.getUserPredicates(filterContext, cb, query, root);
                query.select(cb.countDistinct(root)).where(predicates);
                return em.createQuery(query).getSingleResult();
            } else {
                return (long) session.users().getUsersCount(realm, false);
            }
        }
}
