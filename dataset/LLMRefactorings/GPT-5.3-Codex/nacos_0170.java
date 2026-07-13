public class nacos_0170 {


        public static List<GroupKeyState> diffGroupKeysRefactored(Set<String> basedGroupKeys, Set<String> followedGroupKeys) {
            // Calculate the set of group keys to be added and removed
            Set<String> addGroupKeys = new HashSet<>();
            if (CollectionUtils.isNotEmpty(basedGroupKeys)) {
                addGroupKeys.addAll(basedGroupKeys);
            }
            if (CollectionUtils.isNotEmpty(followedGroupKeys)) {
                addGroupKeys.removeAll(followedGroupKeys);
            }
            
            Set<String> removeGroupKeys = new HashSet<>();
            if (CollectionUtils.isNotEmpty(followedGroupKeys)) {
                removeGroupKeys.addAll(followedGroupKeys);
            }
            if (CollectionUtils.isNotEmpty(basedGroupKeys)) {
                removeGroupKeys.removeAll(basedGroupKeys);
            }
            
            // Convert the group keys to be added and removed into corresponding ConfigState objects and merge them into a list
            return Stream
                .concat(addGroupKeys.stream().map(groupKey -> new GroupKeyState(groupKey, true)),
                    removeGroupKeys.stream().map(groupKey -> new GroupKeyState(groupKey, false)))
                .collect(Collectors.toList());
        
        }
}
