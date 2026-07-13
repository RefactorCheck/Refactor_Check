public class keycloak_0114 {

        @Override
        public long getMembersCount(OrganizationModel organization) {
            throwExceptionIfObjectIsNull(organization, "Organization");
            GroupModel group = getOrganizationGroup(organization);
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
            Root<UserGroupMembershipEntity> groupMembership = countQuery.from(UserGroupMembershipEntity.class);
            From<UserGroupMembershipEntity, UserEntity> userJoin = groupMembership.join("user");

            List<Predicate> predicates = new ArrayList<Predicate>();
            Predicate groupPredicate = builder.equal(groupMembership.get("groupId"), group.getId());
            predicates.add(groupPredicate);

            PartialEvaluationStorageProvider storageProvider = (PartialEvaluationStorageProvider) UserStoragePrivateUtil.userLocalStorage(session);
            predicates.addAll(AdminPermissionsSchema.SCHEMA.applyAuthorizationFilters(session, AdminPermissionsSchema.USERS, storageProvider, getRealm(), builder, countQuery, userJoin));

            countQuery.select(builder.count(userJoin));
            countQuery.where(predicates.toArray(new Predicate[0]));

            return em.createQuery(countQuery).getSingleResult();
        }
}
