public class nacos_0170 {

        public static List<GroupKeyState> diffGroupKeys(Set<String> basedGroupKeys,
            Set<String> followedGroupKeys) {
            Set<String> addGroupKeys = computeAddGroupKeys(basedGroupKeys, followedGroupKeys);
            Set<String> removeGroupKeys = computeRemoveGroupKeys(basedGroupKeys, followedGroupKeys);
            return mergeGroupKeyStates(addGroupKeys, removeGroupKeys);
        }

        private static Set<String> computeAddGroupKeys(Set<String> basedGroupKeys, Set<String> followedGroupKeys) {
            Set<String> addGroupKeys = new HashSet<>();
            if (CollectionUtils.isNotEmpty(basedGroupKeys)) {
                addGroupKeys.addAll(basedGroupKeys);
            }
            if (CollectionUtils.isNotEmpty(followedGroupKeys)) {
                addGroupKeys.removeAll(followedGroupKeys);
            }
            return addGroupKeys;
        }

        private static Set<String> computeRemoveGroupKeys(Set<String> basedGroupKeys, Set<String> followedGroupKeys) {
            Set<String> removeGroupKeys = new HashSet<>();
            if (CollectionUtils.isNotEmpty(followedGroupKeys)) {
                removeGroupKeys.addAll(followedGroupKeys);
            }
            if (CollectionUtils.isNotEmpty(basedGroupKeys)) {
                removeGroupKeys.removeAll(basedGroupKeys);
            }
            return removeGroupKeys;
        }

        private static List<GroupKeyState> mergeGroupKeyStates(Set<String> addGroupKeys, Set<String> removeGroupKeys) {
            return Stream
                .concat(addGroupKeys.stream().map(groupKey -> new GroupKeyState(groupKey, true)),
                    removeGroupKeys.stream().map(groupKey -> new GroupKeyState(groupKey, false)))
                .collect(Collectors.toList());
        }
}
