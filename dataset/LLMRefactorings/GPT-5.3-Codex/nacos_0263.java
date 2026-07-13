public class nacos_0263 {


        public static boolean update(Member newMember) {
            Loggers.CLUSTER.debug("member information update : {}", newMember);
            
            String address = newMember.getAddress();
            if (!serverList.containsKey(address)) {
                Loggers.CLUSTER.warn("address {} want to update Member, but not in member list!",
                    newMember.getAddress());
                return false;
            }
            
            serverList.computeIfPresent(address, (s, member) -> {
                if (NodeState.DOWN.equals(newMember.getState())) {
                    memberAddressInfos.remove(newMember.getAddress());
                }
                boolean isPublishChangeEvent = MemberUtil.isBasicInfoChanged(newMember, member);
                newMember.setExtendVal(MemberMetaDataConstants.LAST_REFRESH_TIME,
                    System.currentTimeMillis());
                MemberUtil.copy(newMember, member);
                if (isPublishChangeEvent) {
                    // member basic data changes and all listeners need to be notified
                    notifyMemberChange(member);
                }
                return member;
            });
            
            return true;
        
        }
}
