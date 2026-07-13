@Override
        protected Stream<GroupModel> getModels(SearchRequest searchRequest) {
            RealmModel realm = session.getContext().getRealm();
            Integer firstResult = searchRequest.getStartIndex() != null ? searchRequest.getStartIndex() - 1 : null;
            Integer maxResults = searchRequest.getCount();
            maxResults = maxResults != null ? Math.min(maxResults, DEFAULT_MAX_RESULTS) : DEFAULT_MAX_RESULTS;
    
            if (StringUtil.isNotBlank(searchRequest.getFilter())) {
                // parse filter into AST
                ScimFilterParser.FilterContext filterContext = FilterUtils.parseFilter(searchRequest.getFilter());
    
                // execute JPA query with filter
                EntityManager em = session.getProvider(JpaConnectionProvider.class).getEntityManager();
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<GroupEntity> query = cb.createQuery(GroupEntity.class);
                Root<GroupEntity> root = query.from(GroupEntity.class);
                List<Predicate> predicates = getGroupPredicates(filterContext, cb, query, root);
    
                // apply distinct and order by name to ensure consistency with no-filter case
                query.where(predicates).distinct(true).orderBy(cb.asc(root.get("name")));
    
                // execute query and convert to UserModel stream
                return extractGetModels(searchRequest);
            } else {
                return session.groups().getTopLevelGroupsStream(realm, firstResult, maxResults);
            }
        }
