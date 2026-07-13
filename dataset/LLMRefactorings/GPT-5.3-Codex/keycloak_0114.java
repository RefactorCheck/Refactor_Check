@Override
        public long getMembersCount(OrganizationModel organization) {
            this.group = getOrganizationGroup(organization);

            throwExceptionIfObjectIsNull(organization, "Organization");

            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
            Root<UserGroupMembershipEntity> groupMembership = countQuery.from(UserGroupMembershipEntity.class);
            From<UserGroupMembershipEntity, UserEntity> userJoin = groupMembership.join("user");
    
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(builder.equal(groupMembership.get("groupId"), this.group.getId()));
    
            PartialEvaluationStorageProvider storageProvider = (PartialEvaluationStorageProvider) UserStoragePrivateUtil.userLocalStorage(session);
            predicates.addAll(AdminPermissionsSchema.SCHEMA.applyAuthorizationFilters(session, AdminPermissionsSchema.USERS, storageProvider, getRealm(), builder, countQuery, userJoin));
    
            countQuery.select(builder.count(userJoin));
            countQuery.where(predicates.toArray(new Predicate[0]));
    
            return em.createQuery(countQuery).getSingleResult();
        }
