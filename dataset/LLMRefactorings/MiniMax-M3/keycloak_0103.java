public class keycloak_0103 {

        @Override
        public Stream<GroupModel> getOrganizationGroupsByMember(OrganizationModel organization, UserModel member, String search, Integer first, Integer max) {
            throwExceptionIfObjectIsNull(organization, "Organization");
            throwExceptionIfObjectIsNull(member, "Member");
    
            if (!isMember(organization, member)) {
                return Stream.of();
            }
    
            RealmModel realm = getRealm();
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<String> queryBuilder = builder.createQuery(String.class);
    
            Root<?> memberRoot;
            Predicate userPredicate;
    
            if (StorageId.isLocalStorage(member.getId())) {
                memberRoot = queryBuilder.from(UserGroupMembershipEntity.class);
                userPredicate = builder.equal(memberRoot.get("user").get("id"), member.getId());
            } else {
                memberRoot = queryBuilder.from(FederatedUserGroupMembershipEntity.class);
                userPredicate = builder.equal(memberRoot.get("userId"), member.getId());
            }
    
            Root<GroupEntity> groupRoot = queryBuilder.from(GroupEntity.class);
    
            queryBuilder.select(groupRoot.get("id"));
    
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(groupRoot.get("id"), memberRoot.get("groupId")));
            predicates.add(userPredicate);
            predicates.add(builder.equal(groupRoot.get("realm"), realm.getId()));
            predicates.add(builder.equal(groupRoot.get("type"), Type.ORGANIZATION.intValue()));
            predicates.add(builder.equal(groupRoot.get("organization").get("id"), organization.getId()));
            predicates.add(builder.notEqual(groupRoot.get("id"), getOrganizationGroup(organization).getId()));
    
            if (search != null && !search.isBlank()) {
                String searchLower = buildSearchPattern(search);
                predicates.add(builder.like(builder.lower(groupRoot.get("name")), searchLower));
            }
    
            queryBuilder.where(predicates.toArray(new Predicate[0]));
            queryBuilder.orderBy(builder.asc(groupRoot.get("name")));
    
            return closing(paginateQuery(em.createQuery(queryBuilder), first, max).getResultStream()
                    .map(realm::getGroupById)
                    .filter(Objects::nonNull));
        }
    
        private String buildSearchPattern(String search) {
            String searchLower = search.trim().toLowerCase().replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
            searchLower = searchLower.replace("*", "%");
            if (searchLower.isEmpty() || searchLower.charAt(searchLower.length() - 1) != '%') searchLower += "%";
            return searchLower;
        }
}
