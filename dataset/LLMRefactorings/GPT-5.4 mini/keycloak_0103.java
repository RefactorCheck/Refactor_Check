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
        CriteriaQuery<String> groupIdsQuery = builder.createQuery(String.class);

        Root<?> membershipRoot;
        Predicate membershipPredicate;

        if (StorageId.isLocalStorage(member.getId())) {
            membershipRoot = groupIdsQuery.from(UserGroupMembershipEntity.class);
            membershipPredicate = builder.equal(membershipRoot.get("user").get("id"), member.getId());
        } else {
            membershipRoot = groupIdsQuery.from(FederatedUserGroupMembershipEntity.class);
            membershipPredicate = builder.equal(membershipRoot.get("userId"), member.getId());
        }

        Root<GroupEntity> groupRoot = groupIdsQuery.from(GroupEntity.class);

        groupIdsQuery.select(groupRoot.get("id"));

        List<Predicate> conditions = new ArrayList<>();
        conditions.add(builder.equal(groupRoot.get("id"), membershipRoot.get("groupId")));
        conditions.add(membershipPredicate);
        conditions.add(builder.equal(groupRoot.get("realm"), realm.getId()));
        conditions.add(builder.equal(groupRoot.get("type"), Type.ORGANIZATION.intValue()));
        conditions.add(builder.equal(groupRoot.get("organization").get("id"), organization.getId()));
        conditions.add(builder.notEqual(groupRoot.get("id"), getOrganizationGroup(organization).getId()));

        if (search != null && !search.isBlank()) {
            String searchLower = search.trim().toLowerCase().replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
            searchLower = searchLower.replace("*", "%");
            if (searchLower.isEmpty() || searchLower.charAt(searchLower.length() - 1) != '%') searchLower += "%";
            conditions.add(builder.like(builder.lower(groupRoot.get("name")), searchLower));
        }

        groupIdsQuery.where(conditions.toArray(new Predicate[0]));
        groupIdsQuery.orderBy(builder.asc(groupRoot.get("name")));

        return closing(paginateQuery(em.createQuery(groupIdsQuery), first, max).getResultStream()
                .map(realm::getGroupById)
                .filter(Objects::nonNull));
    }
}
