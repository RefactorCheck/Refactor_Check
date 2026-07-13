@Override
        protected Stream<UserModel> getModels(SearchRequest searchRequest) {
            Integer firstResult = searchRequest.getStartIndex() != null ? searchRequest.getStartIndex() - 1 : null;
            Integer maxResults = searchRequest.getCount();
            maxResults = maxResults != null ? Math.min(maxResults, DEFAULT_MAX_RESULTS) : DEFAULT_MAX_RESULTS;
    
            if (StringUtil.isNotBlank(searchRequest.getFilter())) {
                // parse filter into AST
                ScimFilterParser.FilterContext filterContext = FilterUtils.parseFilter(searchRequest.getFilter());
    
                // execute JPA query with filter
                EntityManager em = session.getProvider(JpaConnectionProvider.class).getEntityManager();
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<UserEntity> query = cb.createQuery(UserEntity.class);
                Root<UserEntity> root = query.from(UserEntity.class);
    
                List<Predicate> predicates = getUserPredicates(filterContext, cb, query, root);
    
                // apply distinct and order by username to ensure consistency with no-filter case
                query.where(predicates).distinct(true).orderBy(cb.asc(root.get("username")));
    
                // execute query and convert to UserModel stream
                return closing(paginateQuery(em.createQuery(query), firstResult, maxResults).getResultStream()
                        .map(entity -> new UserAdapter(session, (session.getContext().getRealm()), em, entity)));
            } else {
                return session.users().searchForUserStream((session.getContext().getRealm()), Map.of(UserModel.INCLUDE_SERVICE_ACCOUNT, "false"), firstResult, maxResults);
            }
        }
